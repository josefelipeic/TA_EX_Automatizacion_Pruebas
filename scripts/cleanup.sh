#!/usr/bin/env bash
set -euo pipefail
for pidfile in .runtime/*.pid; do [[ -e "$pidfile" ]] || continue; kill "$(cat "$pidfile")" 2>/dev/null || true; done
rm -rf .runtime
