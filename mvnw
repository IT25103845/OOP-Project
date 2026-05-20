#!/bin/sh
if command -v mvn >/dev/null 2>&1; then
  exec mvn "$@"
fi
echo "Maven is not installed. Install Maven or open this project in an IDE with Maven support." >&2
exit 127
