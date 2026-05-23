#!/bin/sh
# Simple Maven launcher for this separated component.
# Requires Maven installed or available from your IDE.
if command -v mvn >/dev/null 2>&1; then
  exec mvn "$@"
fi
echo "Maven is not installed or not on PATH. Install Maven, or open this project in IntelliJ IDEA and run it from there."
exit 127
