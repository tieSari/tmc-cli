#!/bin/bash

#This script starts the server if it is not running.
#TODO If the port is occupied or another error occurs, it fails.
#Needs tmc-client.jar in classpath or same directory
#SERVER NEEDS TO BE NOT RUNNING

function start_server () {
    CLIENTPATH=$DIR
    cd "$CLIENTPATH"
    nohup java -jar tmc-client.jar 2> "$LOGPATH" > /dev/null &
    PID=$!
    echo -n $PID > "$PIDFILE"
    sleep 0.5
}

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

if [ -n "${XDG_RUNTIME_DIR+x}" ]; then
  CONFIG_DIR=$XDG_RUNTIME_DIR/tmc
elif [ -n "${XDG_CONFIG_HOME+x}" ]; then
  CONFIG_DIR=$XDG_CONFIG_HOME/tmc
else
  CONFIG_DIR=$HOME/.config/tmc
fi
mkdir -p "$CONFIG_DIR"

PIDFILE="$CONFIG_DIR/tmc-cli.pid"
LOGPATH=$DIR
LOGPATH+="/log.txt"

if [ ! -f "$PID" ]; then
  start_server
  exit 0
fi

PID=$(cat "$PIDFILE")
if [ -n "${PID+x}" ]; then
  if ps -p "$PID" > /dev/null; then
    exit 0;
  else
    start_server
  fi
fi

PID=$(pgrep "$(cat "$PIDFILE")")

if [ -z "$PID" ]; then
  start_server
fi
