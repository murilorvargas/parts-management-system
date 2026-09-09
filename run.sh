#!/bin/bash
set -e

docker network inspect parts-management-net >/dev/null 2>&1 || docker network create parts-management-net

for svc in discovery-server config-server part-service client-service representative-service gateway-service; do
  [ -f "$svc/infrastructure/.env" ] || cp "$svc/infrastructure/.env.example" "$svc/infrastructure/.env"
done

wait_for() {
  local url=$1
  local name=$2
  echo "Aguardando $name ficar pronto..."
  for i in $(seq 1 60); do
    if curl -sf "$url" >/dev/null 2>&1; then
      echo "$name pronto."
      return 0
    fi
    sleep 2
  done
  echo "Timeout aguardando $name" >&2
  exit 1
}

echo "Subindo discovery-server (Service Discovery)..."
(cd discovery-server/infrastructure && docker compose -p discovery-server up -d --build)
wait_for "http://localhost:8761/actuator/health" "discovery-server"

echo "Subindo config-server (Configuracao Centralizada)..."
(cd config-server/infrastructure && docker compose -p config-server up -d --build)
wait_for "http://localhost:8888/actuator/health" "config-server"

echo "Subindo part-service..."
(cd part-service/infrastructure && docker compose -p part-service up -d --build)

echo "Subindo client-service..."
(cd client-service/infrastructure && docker compose -p client-service up -d --build)

echo "Subindo representative-service..."
(cd representative-service/infrastructure && docker compose -p representative-service up -d --build)

echo "Subindo gateway-service (Gateway)..."
(cd gateway-service/infrastructure && docker compose -p gateway-service up -d --build)

echo ""
echo "Tudo no ar. Teste apenas via Gateway:"
echo "  gateway-service       -> http://localhost:8080"
echo "  discovery-server      -> http://localhost:8761 (dashboard Eureka)"
echo "  config-server         -> http://localhost:8888"
echo ""
echo "Endpoints (via Gateway):"
echo "  http://localhost:8080/api/parts"
echo "  http://localhost:8080/api/clients"
echo "  http://localhost:8080/api/representatives"
