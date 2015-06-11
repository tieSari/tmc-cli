#!/usr/bin/env bash

#Submit command
function command_submit () {
  #submit [<exercise name>]
  echo "Submitting exercise..."
  if [ $# -eq 0 ]
   then
    #echo "submit path `pwd`"
    send_command_wait_output "submit path `pwd`"
   else
     #echo "submit path `pwd` $1"
     send_command_wait_output "submit path `pwd` $1"
fi

  if [[ $OUTPUT =~ All\ tests\ passed.* ]]
  then
    TIMESTAMP=`date +%s`
    FEEDBACK="/tmp/feedback-$TIMESTAMP"
    echo "" >> $FEEDBACK
    echo "" >> $FEEDBACK
    echo "#############" >> $FEEDBACK
    echo "" >> $FEEDBACK
    echo "Please enter feedback above the bar." >> $FEEDBACK
    echo "" >> $FEEDBACK
    echo "$OUTPUT" >> $FEEDBACK
    nano $FEEDBACK

    PARSEDOUTPUT=`sed -n '/#############/q;p' $FEEDBACK`
    # TODO: send output to server
  else
    echo "$OUTPUT"
  fi
}

function command_login () {
    read -p "Username: " username
    read -s  -p "Password: " password
    echo ""
    login $username $password
    return 0;
}

function command_download () {
  send_command "downloadExercises pwd `pwd` courseID $1"
}

function command_paste () {
  send_command "paste path `pwd`"
}

function command_test () {
  #send_command "runTests filepath `pwd`"
  if [ $# -eq 0 ]
  then
    send_command "runTests filepath `pwd`"
  else
    send_command "runTests filepath `pwd` $1"
  fi
}

function command_default () {
    send_command $@
    return 0;
}

function command_listExercises () {
  send_command "listExercises path `pwd`"
}

# Backend login
function login () {
    # login username <kayttajatunnus> password <pw>

    loginstring="login username "
    loginstring+=$1
    loginstring+=" password "
    loginstring+=$2

    send_command $loginstring

    return 0;

}

# Backend cmd send
function send_command () {
#    OUTPUT=$(echo $@ | nc localhost 1234)
#    echo $OUTPUT
    DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

    CONFIGPATH="$DIR/config.properties"
    CONFIGPORT=`cat $CONFIGPATH | grep "serverPort" | sed s/serverPort=//g`
    echo $@ | nc localhost $CONFIGPORT

    return 0;

}

function send_command_wait_output () {
#    OUTPUT=$(echo $@ | nc localhost 1234)
#    echo $OUTPUT
    DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

    CONFIGPATH="$DIR/config.properties"
    CONFIGPORT=`cat $CONFIGPATH | grep "serverPort" | sed s/serverPort=//g`
    OUTPUT=`echo $@ | nc localhost $CONFIGPORT`

    return 0;

}

control_c()
# run if user hits control-c
{
  echo -en "\Cancelling\n"
  exit $?
}


# catch crtl_c and run function control_c if user hits ctrl-c
trap control_c SIGINT

#echo "Servu paalle"

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
STARTUP=$DIR
STARTUP+="/startup.sh"
bash $STARTUP

case "$1" in
#    "help") command_help;;
    "login") command_login;;
    "submit") command_submit $2;;
    "download") command_download $2;;
    "test") command_test $2;;
    "paste") command_paste $2;;
    "listExercises") command_listExercises;;
    *) command_default $@;;
esac
