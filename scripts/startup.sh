#!/bin/bash

#This script starts the server if it is not running.
#TODO If the port is occupied or another error occurs, it fails.
#Needs tmc-client.jar in classpath or same directory
#SERVER NEEDS TO BE NOT RUNNING

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

CONFIGPATH=$DIR
CONFIGPATH+="/config"
LOGPATH=$DIR
LOGPATH+="/log.txt"

CLIENTPATH=$DIR
if [ pgrep `cat $CONFIGPATH` &> /dev/null ]; then
<<<<<<< HEAD
  eval "(cd $CLIENTPATH && nohup java -jar tmc-client.jar &> /dev/null &) &"
=======
  eval "(cd $CLIENTPATH && nohup java -jar tmc-client.jar 2> $LOGPATH > /dev/null) &"
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
  PID=$!
  echo $PID > $CONFIGPATH
  echo "Server started"
  sleep 1
<<<<<<< HEAD
else
  echo "Server is already running."
fi
=======
fi
>>>>>>> 7061d626a3951db33faf53d915810654bf6c1720
