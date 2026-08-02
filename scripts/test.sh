#!/bin/bash

set -e

echo "🧪 Running tests..."

# Unit tests
echo "📝 Running unit tests..."
cd serving/analytics-api
mvn test

# Integration tests
echo "🔗 Running integration tests..."
cd ../..
docker-compose up -d test-db test-kafka
mvn verify -P integration-tests

echo "✅ All tests passed!"