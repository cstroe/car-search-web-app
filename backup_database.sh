#!/usr/bin/env bash
# Requirements: grep, gzip, docker-compose

SCRIPT_DIR=$(cd "$(dirname $0)" && pwd)
DOCKER_COMPOSE_FILE="$SCRIPT_DIR/docker-compose.yaml"

if [ ! -f "$DOCKER_COMPOSE_FILE" ]; then
  echo "File not found: $DOCKER_COMPOSE_FILE"
  exit 1
fi

FILENAME=$(date '+%Y-%m-%d--%H-%M-%S')
FILE="$SCRIPT_DIR/$FILENAME.sql"

if [ -f "$FILE" ]; then
  echo "Backup file already exists: $FILE"
  echo "Stopping."
  exit 1
fi

if [ -f "$FILE.gz" ]; then
  echo "File already exists: $FILE.gz"
  echo "Stopping."
  exit 1
fi

docker-compose exec -T db pg_dump -U postgres --quote-all-identifiers > "$FILE"

gzip -v "$FILE"
