# 📊 EventStream Analytics Platform

Платформа для аналитики событий в реальном времени. Обрабатывает потоки данных и показывает бизнес-метрики с задержкой менее 1 секунды.

---

## 🚀 Быстрый старт

### Требования
- Java 21
- Docker & Docker Compose
- Maven

### Запуск

```bash
# Клонируем репозиторий
git clone https://github.com/LacusFaustus/eventstream-analytics.git
cd eventstream-analytics

# Собираем и запускаем
make build
make up
Доступные сервисы
Сервис	URL	Логин/пароль
API	http://localhost:8088	-
Swagger UI	http://localhost:8088/swagger-ui.html	-
Графана	http://localhost:3000	admin / admin
Kafka UI	http://localhost:8085	-

📋 API Примеры
Получить дашборд
http
GET /api/analytics/dashboard?start=2026-01-01 00:00:00&end=2026-12-31 23:59:59
Ответ:

json
{
  "dailyActiveUsers": 42500,
  "monthlyActiveUsers": 1250000,
  "totalEvents": 150000,
  "retentionRate": 0.45,
  "conversionRate": 0.12
}
Получить метрики в реальном времени
http
GET /api/analytics/realtime
Ответ:

json
{
  "activeUsers": 4250,
  "eventsPerSecond": 1250,
  "systemLoad": 0.65
}
🏗 Архитектура

Источники → Kafka → Flink → ClickHouse → API → Графана
Компоненты:

Kafka — приём событий

Flink — обработка в реальном времени

ClickHouse — хранилище для аналитики

Redis — кэш для быстрых ответов

API — REST сервис на Spring Boot

📁 Структура проекта

eventstream-analytics/
├── ingestion/          # Приём данных (Kafka)
├── processing/         # Обработка (Flink)
├── storage/            # Хранилища (ClickHouse, Redis)
├── serving/            # API и дашборды
├── orchestration/      # ETL (Airflow)
├── monitoring/         # Мониторинг (Prometheus, Grafana)
└── infrastructure/     # Kubernetes, Terraform
🧪 Тесты

# Unit тесты
make test

# Нагрузочное тестирование
./scripts/benchmark.sh --events 1000000
🤝 Вклад в проект
Форкните репозиторий

Создайте ветку: git checkout -b feature/your-feature

Сделайте коммит: git commit -m 'Add feature'

Отправьте пулл-реквест

📄 Лицензия
MIT License

📧 Контакты
Автор: Евгений Сметанин

Email: lacusfaustus@gmail.com

Telegram: @lacusfaustus

GitHub: LacusFaustus