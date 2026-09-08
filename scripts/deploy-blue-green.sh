#!/usr/bin/env bash
set -euo pipefail
JAR="${1:-target/automatizacion-pruebas-ta.jar}"
RUNTIME="${RUNTIME_DIR:-.runtime}"
mkdir -p "$RUNTIME"
active="blue"; [[ -f "$RUNTIME/active-slot" ]] && active="$(cat "$RUNTIME/active-slot")"
if [[ "$active" == "blue" ]]; then candidate="green"; port=8082; else candidate="blue"; port=8081; fi
pidfile="$RUNTIME/$candidate.pid"
[[ -f "$pidfile" ]] && kill "$(cat "$pidfile")" 2>/dev/null || true
APP_PORT="$port" nohup java -jar "$JAR" >"$RUNTIME/$candidate.log" 2>&1 &
echo $! > "$pidfile"
if ./scripts/acceptance-test.sh "http://localhost:$port"; then
  printf '%s' "$candidate" > "$RUNTIME/active-slot"; printf '%s' "$port" > "$RUNTIME/active-port"
  echo "DEPLOY_OK slot=$candidate port=$port"
else
  kill "$(cat "$pidfile")" 2>/dev/null || true
  echo "DEPLOY_FAILED: se conserva slot=$active" >&2; exit 1
fi
