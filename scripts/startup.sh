#!/bin/bash

#This script starts the server if it is not running.
#TODO If the port is occupied or another error occurs, it fails.
#Needs tmc-client.jar in classpath or same directory

PORT=$1
if [ -z $PORT ]; then
  echo "Bad parameters"
  exit 0
else
  echo "Using port $PORT"
fi
RESPONSE=eval nc -z -w5 localhost $PORT; RESPONSE=$?

if [ $RESPONSE = 1 ]; then
  eval "(nohup java -jar tmc-client.jar &> /dev/null) &"
  PID=$!
  echo $PID
  echo "Server started"
else
  echo "Server is already running. Not doing anything."
fi
