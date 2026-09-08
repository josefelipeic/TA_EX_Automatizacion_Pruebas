#!/usr/bin/env bash
set -euo pipefail
RUNTIME="${RUNTIME_DIR:-.runtime}"
[[ -f "$RUNTIME/active-slot" ]] || { echo "No existe despliegue activo" >&2; exit 1; }
current="$(cat "$RUNTIME/active-slot")"
if [[ "$current" == "blue" ]]; then target="green"; port=8082; else target="blue"; port=8081; fi
[[ -f "$RUNTIME/$target.pid" ]] && kill -0 "$(cat "$RUNTIME/$target.pid")" 2>/dev/null || { echo "Slot previo no disponible" >&2; exit 1; }
./scripts/acceptance-test.sh "http://localhost:$port"
printf '%s' "$target" > "$RUNTIME/active-slot"; printf '%s' "$port" > "$RUNTIME/active-port"
echo "ROLLBACK_OK slot=$target port=$port"
