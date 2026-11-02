#!/usr/bin/env bash
set -euo pipefail
script_dir="$(cd "$(dirname "$0")" && pwd)"
jmeter_bin="${JMETER_BIN:-jmeter}"
if ! command -v "$jmeter_bin" >/dev/null 2>&1; then
  echo "JMeter not found. Set JMETER_BIN to the distribution's bin/jmeter path." >&2
  exit 2
fi
mkdir -p "$script_dir/results"
run_dir="$(mktemp -d "$script_dir/results/run-$(date +%Y%m%d-%H%M%S)-XXXXXX")"
"$jmeter_bin" -n -t "$script_dir/n11-search.jmx" -l "$run_dir/results.jtl" -j "$run_dir/jmeter.log" -e -o "$run_dir/report"
echo "Report: $run_dir/report/index.html"
awk -f "$script_dir/check-results.awk" "$run_dir/results.jtl"
