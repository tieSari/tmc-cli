#!/bin/bash

#This script starts the server if it is not running.
#TODO If the port is occupied or another error occurs, it fails.
#Needs tmc-client.jar in classpath or same directory
#SERVER NEEDS TO BE NOT RUNNING

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

CONFIGPATH=$DIR
CONFIGPATH+="/config"

CLIENTPATH=$DIR
CLIENTPATH+="tmc-client.jar"

if [ pgrep `cat config` &> /dev/null ]; then
  eval "(nohup java -jar $CLIENTPATH &> /dev/null) &"
  PID=$!
  echo $PID > $CONFIGPATH
  echo "Server started"
  sleep 1
else
  echo "Server is already running."
fi
