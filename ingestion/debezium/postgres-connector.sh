#!/bin/bash

echo "🚀 Registering PostgreSQL connector..."

curl -X POST -H "Content-Type: application/json" \
  --data @connector-config.json \
  http://connect:8083/connectors

echo "✅ Connector registered successfully!"