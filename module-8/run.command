#!/bin/zsh
# Sam Dirr - CSD 420 Assignment 8.2
cd "$(dirname "$0")" || exit 1
JAVA_DIR="${ASSIGNMENT_JAVA_HOME:-/Applications/IntelliJ IDEA.app/Contents/jbr/Contents/Home}"
FX_DIR="${ASSIGNMENT_FX_LIB:-/Users/sam/Documents/JavaFX/javafx-sdk-21.0.12/lib}"
BUILD_DIR="$(mktemp -d "${TMPDIR:-/tmp}/csd420-module8.XXXXXX")" || exit 1
trap 'rm -rf "$BUILD_DIR"' EXIT
"$JAVA_DIR/bin/javac" --module-path "$FX_DIR" --add-modules javafx.controls -d "$BUILD_DIR" SamThreeThreads.java SamCharacterGenerator.java SamThreeThreadsTest.java || exit 1
MAIN_CLASS=SamThreeThreads
if [[ "${1:-}" == test ]]; then
    MAIN_CLASS=SamThreeThreadsTest
fi
"$JAVA_DIR/bin/java" --module-path "$FX_DIR" --add-modules javafx.controls -cp "$BUILD_DIR" "$MAIN_CLASS"
RESULT=$?
echo "Exit status: $RESULT"
if [[ -t 0 ]]; then
    read "?Press Return to close."
fi
exit "$RESULT"
