#!/bin/bash

#This script starts the server if it is not running.
#TODO If the port is occupied or another error occurs, it fails.
#Needs tmc-client.jar in classpath or same directory
#SERVER NEEDS TO BE NOT RUNNING

function start_server () {
    CLIENTPATH=$DIR
    cd $CLIENTPATH
    nohup java -jar tmc-client.jar 2> $LOGPATH > /dev/null &
    PID=$!
    echo $PID > $CONFIGPATH
}

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

CONFIGPATH=$DIR
CONFIGPATH+="/config"
LOGPATH=$DIR
LOGPATH+="/log.txt"

if [ ! -f "$CONFIGPATH" ];
then
  start_server
  exit 0
fi

PID=`cat $CONFIGPATH`

if [[ -n "$PID" ]]
then
  if ps -p $PID > /dev/null
  then
    exit 0
  fi
fi

if [ pgrep `cat $CONFIGPATH` &> /dev/null ]; then
  start_server
fi
