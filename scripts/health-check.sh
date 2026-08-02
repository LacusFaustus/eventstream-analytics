#!/bin/bash

set -e

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

check_service() {
    local name=$1
    local url=$2
    local response=$(curl -s -o /dev/null -w "%{http_code}" "$url")

    if [ "$response" = "200" ] || [ "$response" = "204" ]; then
        echo -e "${GREEN}✅ $name is healthy${NC}"
        return 0
    else
        echo -e "${RED}❌ $name is down (HTTP $response)${NC}"
        return 1
    fi
}

echo "===================================="
echo "🏥 Health Check"
echo "===================================="

check_service "Zookeeper" "http://localhost:2181/commands/stat"
check_service "Kafka" "http://localhost:9092"
check_service "ClickHouse" "http://localhost:8123/ping"
check_service "Redis" "http://localhost:6379"
check_service "Analytics API" "http://localhost:8088/actuator/health"
check_service "Grafana" "http://localhost:3000/api/health"
check_service "Prometheus" "http://localhost:9090/-/healthy"

echo "===================================="
echo "✅ Health check complete!"