#!/usr/bin/env bash
# =============================================================================
# bootstrap-gradle-wrapper.sh
#
# Generates gradle/wrapper/gradle-wrapper.jar using the system-installed Gradle
# binary. Intended for CI environments (GitHub Actions ubuntu-latest has Gradle
# pre-installed) and developer machines.
#
# Usage (run from the SmartTouch project root):
#   chmod +x scripts/bootstrap-gradle-wrapper.sh
#   ./scripts/bootstrap-gradle-wrapper.sh
# =============================================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
JAR="${PROJECT_ROOT}/gradle/wrapper/gradle-wrapper.jar"
GRADLE_VERSION="8.11.1"

cd "$PROJECT_ROOT"

if [ -f "$JAR" ]; then
  echo "✅  gradle-wrapper.jar already present — nothing to do."
  exit 0
fi

echo "⚙️   gradle-wrapper.jar not found. Generating via system Gradle…"

if ! command -v gradle &>/dev/null; then
  echo "❌  'gradle' not found on PATH."
  echo "    Install Gradle $GRADLE_VERSION or run: sdk install gradle $GRADLE_VERSION"
  exit 1
fi

gradle wrapper \
  --gradle-version "$GRADLE_VERSION" \
  --distribution-type bin \
  --no-daemon

chmod +x gradlew gradlew.bat 2>/dev/null || true

echo "✅  gradle-wrapper.jar generated successfully."
echo "    Commit gradle/wrapper/gradle-wrapper.jar to your repository to skip"
echo "    this step on future checkouts."
