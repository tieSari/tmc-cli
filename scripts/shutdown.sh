#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
PIDFILE="$DIR/pidfile.pid"
PID="$(cat "$PIDFILE")"

kill "$PID"
sleep 2
kill -9 "$PID" &> /dev/null

rm "$PIDFILE"
