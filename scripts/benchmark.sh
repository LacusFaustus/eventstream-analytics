#!/bin/bash

echo "📊 Running benchmark..."

# Параметры
EVENTS=${1:-1000000}
CONCURRENCY=${2:-10}

# Запуск генератора
echo "Generating $EVENTS events with concurrency $CONCURRENCY..."

# Тест производительности API
echo "Testing API performance..."
ab -n 1000 -c 100 http://localhost:8088/api/analytics/dashboard

# Тест Kafka
echo "Testing Kafka throughput..."
kafka-producer-perf-test \
  --topic events.raw.v1 \
  --num-records $EVENTS \
  --throughput -1 \
  --record-size 1024

echo "✅ Benchmark completed!"