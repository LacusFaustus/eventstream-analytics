-- Создаем базу данных
CREATE DATABASE IF NOT EXISTS analytics;

USE analytics;

-- ============================================================
-- 1. ТАБЛИЦА СЫРЫХ СОБЫТИЙ
-- ============================================================
-- Хранит все сырые события из Kafka
-- Партиционирование по месяцам для оптимизации
-- ============================================================

CREATE TABLE IF NOT EXISTS events_raw
(
    event_id String,
    user_id UInt64,
    event_type String,
    category String,
    city String,
    device String,
    os String,
    browser String,
    session_id String,
    timestamp DateTime64(3),
    lat Float64,
    lon Float64,
    metadata Map(String, String),
    ingestion_time DateTime DEFAULT now()
    )
    ENGINE = MergeTree()
    PARTITION BY toYYYYMM(timestamp)
    ORDER BY (timestamp, event_type, category)
    TTL timestamp + INTERVAL 30 DAY
    SETTINGS index_granularity = 8192;

-- ============================================================
-- 2. АГРЕГИРОВАННЫЕ МЕТРИКИ (MATERIALIZED VIEW)
-- ============================================================
-- Автоматически обновляется при добавлении новых событий
-- ============================================================

CREATE MATERIALIZED VIEW IF NOT EXISTS events_metrics_mv
ENGINE = AggregatingMergeTree()
PARTITION BY toYYYYMM(window_start)
ORDER BY (event_id, window_start)
AS
SELECT
    event_id,
    toStartOfMinute(timestamp) as window_start,
    countState() as total_events_state,
    sumState(if(event_type = 'VIEW', 1, 0)) as views_state,
    sumState(if(event_type = 'LIKE', 1, 0)) as likes_state,
    sumState(if(event_type = 'REGISTER', 1, 0)) as registrations_state,
    sumState(if(event_type = 'COMMENT', 1, 0)) as comments_state,
    sumState(if(event_type = 'SHARE', 1, 0)) as shares_state,
    sumState(if(event_type = 'PURCHASE', 1, 0)) as purchases_state,
    uniqState(user_id) as unique_users_state,
    uniqState(session_id) as unique_sessions_state,
    avgState(duration) as avg_duration_state
FROM events_raw
GROUP BY event_id, window_start;

-- ============================================================
-- 3. ЕЖЕДНЕВНЫЕ МЕТРИКИ
-- ============================================================
-- DAU, MAU, Retention, Conversion Rate
-- ============================================================

CREATE TABLE IF NOT EXISTS daily_metrics
(
    date Date,
    active_users UInt64,
    new_users UInt64,
    returning_users UInt64,
    total_events UInt64,
    avg_session_duration UInt64,
    retention_rate Float64,
    conversion_rate Float64,
    events_per_user Float64,
    updated_at DateTime DEFAULT now()
    )
    ENGINE = SummingMergeTree()
    PARTITION BY toYYYYMM(date)
    ORDER BY date;

-- ============================================================
-- 4. ПОПУЛЯРНЫЕ СОБЫТИЯ
-- ============================================================
-- Рейтинг событий по популярности
-- ============================================================

CREATE TABLE IF NOT EXISTS popular_events
(
    event_id UInt64,
    date Date,
    total_views UInt64,
    total_likes UInt64,
    total_registrations UInt64,
    total_comments UInt64,
    engagement_score Float64,
    popularity_rank UInt32,
    category String,
    city String
)
    ENGINE = ReplacingMergeTree()
    PARTITION BY toYYYYMM(date)
    ORDER BY (event_id, date);

-- ============================================================
-- 5. ГЕО-РАСПРЕДЕЛЕНИЕ
-- ============================================================
-- Активность по городам и странам
-- ============================================================

CREATE TABLE IF NOT EXISTS geo_distribution
(
    date Date,
    city String,
    country String DEFAULT 'Russia',
    users_count UInt64,
    events_count UInt64,
    avg_engagement Float64,
    top_category String
)
    ENGINE = SummingMergeTree()
    PARTITION BY toYYYYMM(date)
    ORDER BY (city, date);

-- ============================================================
-- 6. СТАТИСТИКА ПОЛЬЗОВАТЕЛЕЙ
-- ============================================================
-- Поведенческие метрики по пользователям
-- ============================================================

CREATE TABLE IF NOT EXISTS user_activity
(
    user_id UInt64,
    date Date,
    events_count UInt64,
    unique_categories UInt64,
    total_duration UInt64,
    active_minutes UInt16,
    engagement_score Float64,
    favorite_category String,
    device_preferences String
)
    ENGINE = SummingMergeTree()
    PARTITION BY toYYYYMM(date)
    ORDER BY (user_id, date);

-- ============================================================
-- 7. ВОРОНКА КОНВЕРСИИ
-- ============================================================
-- Шаги: VIEW → REGISTER → LIKE → COMMENT → PURCHASE
-- ============================================================

CREATE TABLE IF NOT EXISTS funnel_metrics
(
    date Date,
    step String,
    user_count UInt64,
    conversion_rate Float64,
    event_id UInt64
)
    ENGINE = SummingMergeTree()
    PARTITION BY toYYYYMM(date)
    ORDER BY (date, step);

-- ============================================================
-- ИНДЕКСЫ ДЛЯ ОПТИМИЗАЦИИ
-- ============================================================

ALTER TABLE events_raw ADD INDEX idx_event_type event_type TYPE bloom_filter GRANULARITY 1;
ALTER TABLE events_raw ADD INDEX idx_user_id user_id TYPE bloom_filter GRANULARITY 1;
ALTER TABLE events_raw ADD INDEX idx_category category TYPE bloom_filter GRANULARITY 1;

-- ============================================================
-- ВЫЧИСЛЯЕМЫЕ СТОЛБЦЫ
-- ============================================================

ALTER TABLE events_raw ADD COLUMN IF NOT EXISTS date_hour DateTime DEFAULT toStartOfHour(timestamp);
ALTER TABLE events_raw ADD COLUMN IF NOT EXISTS date_day Date DEFAULT toDate(timestamp);

-- ============================================================
-- ТЕСТОВЫЕ ДАННЫЕ
-- ============================================================

INSERT INTO events_raw (event_id, user_id, event_type, category, city, device, os, browser, session_id, timestamp, lat, lon)
SELECT
    generateUUIDv4() as event_id,
    rand() % 1000000 + 1 as user_id,
    ['VIEW', 'LIKE', 'REGISTER', 'COMMENT', 'SHARE'][rand() % 5 + 1] as event_type,
    ['Concert', 'Festival', 'Exhibition', 'Workshop', 'Conference'][rand() % 5 + 1] as category,
    ['Moscow', 'Saint Petersburg', 'Novosibirsk', 'Yekaterinburg', 'Kazan'][rand() % 5 + 1] as city,
    ['MOBILE', 'DESKTOP', 'TABLET'][rand() % 3 + 1] as device,
    ['iOS', 'Android', 'Windows', 'macOS'][rand() % 4 + 1] as os,
    ['Chrome', 'Safari', 'Firefox'][rand() % 3 + 1] as browser,
    generateUUIDv4() as session_id,
    now() - rand() % 86400000 as timestamp,
    rand() * 180 - 90 as lat,
    rand() * 360 - 180 as lon
FROM numbers(10000);

SELECT '✅ ClickHouse initialized successfully!' as message;