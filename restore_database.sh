#!/usr/bin/env bash
# Requirements: grep, gzip, docker-compose

SCRIPT_DIR=$(cd "$(dirname $0)" && pwd)
DOCKER_COMPOSE_FILE="$SCRIPT_DIR/docker-compose.yaml"

if [ ! -f "$DOCKER_COMPOSE_FILE" ]; then
  echo "File not found: $DOCKER_COMPOSE_FILE"
  exit 1
fi

if [ -z "$1" ]; then
  echo "Usage: $0 <gzip SQL backup file>"
  exit 1
fi

BACKUP_FILE=$(realpath "$1")

if [ ! -f "$BACKUP_FILE" ]; then
  echo "Backup file not found: $BACKUP_FILE"
    exit 1
fi

zcat "$BACKUP_FILE" | docker-compose exec -T db psql -U postgres
