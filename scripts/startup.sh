#!/bin/bash

#This script starts the server if it is not running.
#TODO If the port is occupied or another error occurs, it fails.
#Needs tmc-client.jar in classpath or same directory
#SERVER NEEDS TO BE NOT RUNNING

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

CONFIGPATH=$DIR
CONFIGPATH+="/config"

CLIENTPATH=$DIR
#
if [ pgrep `cat $CONFIGPATH` &> /dev/null ]; then
  eval "(cd $CLIENTPATH && nohup java -jar tmc-client.jar &> /dev/null) &"
  PID=$!
  echo $PID > $CONFIGPATH
  echo "Server started"
  sleep 1
fi
