#!/bin/bash

#This script starts the server if it is not running.
#TODO If the port is occupied or another error occurs, it fails.
#Needs tmc-client.jar in classpath or same directory
#SERVER NEEDS TO BE NOT RUNNING

if [ pgrep `cat config` &> /dev/null ]; then
  eval "(nohup java -jar tmc-client.jar &> /dev/null) &"
  PID=$!
  echo $PID > config
  echo "Server started"
else
<<<<<<< HEAD
  echo "Server is already running. Not doing anything."
=======
  echo "Server is already running."
>>>>>>> origin/clihelp
fi
