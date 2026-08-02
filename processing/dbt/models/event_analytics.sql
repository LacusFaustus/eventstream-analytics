WITH events AS (
    SELECT
        event_id,
        user_id,
        event_type,
        category,
        city,
        device,
    timestamp,
    DATE(timestamp) as event_date,
    EXTRACT(HOUR FROM timestamp) as event_hour
FROM {{ ref('stg_events') }}
    ),

    daily_metrics AS (
SELECT
    event_date,
    COUNT(DISTINCT user_id) as daily_active_users,
    COUNT(*) as total_events,
    COUNT(DISTINCT category) as unique_categories,
    COUNT(DISTINCT city) as unique_cities
FROM events
GROUP BY event_date
    ),

    hourly_metrics AS (
SELECT
    event_date,
    event_hour,
    COUNT(*) as events_per_hour,
    COUNT(DISTINCT user_id) as users_per_hour
FROM events
GROUP BY event_date, event_hour
    )

SELECT
    dm.*,
    hm.events_per_hour,
    hm.users_per_hour,
    ROUND(dm.total_events::DECIMAL / NULLIF(dm.daily_active_users, 0), 2) as events_per_user
FROM daily_metrics dm
         LEFT JOIN hourly_metrics hm ON dm.event_date = hm.event_date AND hm.event_hour = 0