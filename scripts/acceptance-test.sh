#!/usr/bin/env bash
set -euo pipefail
BASE_URL="${1:-http://localhost:8080}"
retry() { for _ in {1..20}; do if curl -fsS "$1"; then return 0; fi; sleep 1; done; return 1; }
health="$(retry "$BASE_URL/health")"
[[ "$health" == "UP" ]] || { echo "Health check invalido: $health"; exit 1; }
greeting="$(curl -fsS "$BASE_URL/api/greeting?name=Jose%20Felipe")"
[[ "$greeting" == "Hola, Jose Felipe" ]] || { echo "Respuesta invalida: $greeting"; exit 1; }
echo "Acceptance tests OK en $BASE_URL"
