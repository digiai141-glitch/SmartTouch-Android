#!/usr/bin/env bash
# =============================================================================
# bootstrap-gradle-wrapper.sh
#
# Downloads gradle-wrapper.jar from the official Gradle GitHub repository.
# Run this once after cloning if the JAR is missing (e.g. on a fresh CI agent).
#
# Usage:
#   chmod +x scripts/bootstrap-gradle-wrapper.sh
#   ./scripts/bootstrap-gradle-wrapper.sh
# =============================================================================

set -euo pipefail

GRADLE_VERSION="8.11.1"
JAR_DIR="$(dirname "$0")/../gradle/wrapper"
JAR_PATH="$JAR_DIR/gradle-wrapper.jar"

if [ -f "$JAR_PATH" ]; then
  echo "✅  gradle-wrapper.jar already present at $JAR_PATH"
  exit 0
fi

echo "⬇️   Downloading gradle-wrapper.jar for Gradle $GRADLE_VERSION …"

mkdir -p "$JAR_DIR"

# Official source: Gradle GitHub releases
URL="https://raw.githubusercontent.com/gradle/gradle/v${GRADLE_VERSION}/gradle/wrapper/gradle-wrapper.jar"

if command -v curl &>/dev/null; then
  curl -fsSL "$URL" -o "$JAR_PATH"
elif command -v wget &>/dev/null; then
  wget -q "$URL" -O "$JAR_PATH"
else
  echo "❌  Neither curl nor wget found. Please install one and retry."
  exit 1
fi

echo "✅  gradle-wrapper.jar downloaded to $JAR_PATH"
