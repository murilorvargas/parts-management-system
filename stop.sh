#!/bin/bash
(cd observability && docker compose -p observability down)
(cd gateway-service/infrastructure && docker compose -p gateway-service down)
(cd representative-service/infrastructure && docker compose -p representative-service down)
(cd client-service/infrastructure && docker compose -p client-service down)
(cd part-service/infrastructure && docker compose -p part-service down)
(cd config-server/infrastructure && docker compose -p config-server down)
(cd discovery-server/infrastructure && docker compose -p discovery-server down)

docker network rm parts-management-net 2>/dev/null || true
