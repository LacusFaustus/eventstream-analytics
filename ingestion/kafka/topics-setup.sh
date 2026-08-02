#!/bin/bash

echo "📤 Creating Kafka topics..."

kafka-topics --bootstrap-server localhost:9092 \
  --create --if-not-exists \
  --topic events.raw.v1 \
  --partitions 12 \
  --replication-factor 1

kafka-topics --bootstrap-server localhost:9092 \
  --create --if-not-exists \
  --topic events.aggregated.v1 \
  --partitions 6 \
  --replication-factor 1

kafka-topics --bootstrap-server localhost:9092 \
  --create --if-not-exists \
  --topic cdc.postgres.events \
  --partitions 6 \
  --replication-factor 1

echo "✅ Topics created successfully!"

kafka-topics --bootstrap-server localhost:9092 --list