#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
PIDFILE="$DIR/pidfile.pid"
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
