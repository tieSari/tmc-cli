#!/bin/bash

PORT=$1
echo "Using port $PORT"
RESPONSE=eval nc -z -w5 localhost $PORT; RESPONSE=$?

if [ $RESPONSE = 1 ]; then
  eval nohup java -jar tmc-client.jar &> /dev/null &
  echo "Server started"
else
  echo "Server is already running. Not doing anything."
fi
