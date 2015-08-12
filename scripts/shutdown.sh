#!/bin/bash

if [ -n "${XDG_RUNTIME_DIR+x}" ]; then
  CONFIG_DIR=$XDG_RUNTIME_DIR/tmc
elif [ -n "${XDG_CONFIG_HOME+x}" ]; then
  CONFIG_DIR=$XDG_CONFIG_HOME/tmc
else
  CONFIG_DIR=$HOME/.config/tmc
fi

PIDFILE="$CONFIG_DIR/tmc-cli.pid"
PID="$(cat "$PIDFILE")"

kill "$PID"
for _ in {1..30}
do
  sleep 1
  if [[ ! -n $(ps -p "$PID" -o pid=) ]]
  then
    break
  fi
done
kill -9 "$PID" &> /dev/null

rm "$PIDFILE"
