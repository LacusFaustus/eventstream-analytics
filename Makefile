.PHONY: help build up down logs clean test dev

help:
	@echo ''
	@echo 'EventStream Analytics Platform'
	@echo '============================================'
	@echo ''
	@echo 'Development:'
	@echo '  make dev        🚀 Full development setup'
	@echo '  make build      📦 Build all services'
	@echo '  make test       🧪 Run all tests'
	@echo '  make clean      🧹 Clean all artifacts'
	@echo ''
	@echo 'Docker:'
	@echo '  make up         🐳 Start all services'
	@echo '  make down       🛑 Stop all services'
	@echo '  make logs       📋 Show logs'
	@echo '  make ps         📊 Show status'

dev: build up
	@echo '✅ All services started!'
	@echo ''
	@echo '📍 Analytics API: http://localhost:8088'
	@echo '📍 Swagger UI:   http://localhost:8088/swagger-ui.html'
	@echo '📍 Grafana:      http://localhost:3000 (admin/admin)'
	@echo '📍 Prometheus:   http://localhost:9090'

build:
	@echo '📦 Building all services...'
	mvn clean install -DskipTests -U
	@echo '✅ Build completed!'

test:
	@echo '🧪 Running tests...'
	mvn test
	@echo '✅ Tests completed!'

clean:
	@echo '🧹 Cleaning artifacts...'
	mvn clean
	docker-compose down -v 2>/dev/null || true
	@echo '✅ Cleaned!'

up:
	@echo '🐳 Starting Docker Compose...'
	docker-compose up -d
	@echo '✅ Services started!'

down:
	@echo '🛑 Stopping Docker Compose...'
	docker-compose down
	@echo '✅ Services stopped!'

logs:
	docker-compose logs -f

ps:
	docker-compose ps