#!/bin/bash

set -e

echo "🚀 Starting deployment..."

# Build services
echo "📦 Building services..."
docker-compose build

# Run tests
echo "🧪 Running tests..."
docker-compose run --rm analytics-api mvn test

# Deploy
echo "🚀 Deploying services..."
docker-compose up -d

# Health check
echo "🏥 Checking health..."
sleep 10
curl -f http://localhost:8088/actuator/health || exit 1

echo "✅ Deployment completed successfully!"