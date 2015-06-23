#!/usr/bin/env bash

#Submit command
function command_submit () {
  #submit [<exercise name>]
  echo "Submitting exercise..."
  if [ $# -eq 0 ]
    then
    send_command_wait_output "submit path `pwd`"
  else
    send_command_wait_output "submit path `pwd` $1"
  fi

  echo "$OUTPUT"

  if [[ $OUTPUT =~ All\ tests\ passed.*Please\ give\ feedback\:.* ]]
  then
    OUTPUT=""
    feedback
  fi
}

function feedback () {
  if [[ $OUTPUT =~ .*text ]]
    then
    text_feedback
  else
    int_feedback
  fi
}

function int_feedback () {
  echo "$OUTPUT"
  read -p "> " answer
  send_command_wait_output "answerQuestion answer $answer kind integer"
  if [[ $OUTPUT =~ end ]]
    then
    echo "Thank you for your answers!"
  else
    feedback
  fi
}

function text_feedback () {
  TIMESTAMP=`date +%s`
  FEEDBACK="/tmp/feedback-$TIMESTAMP"
  echo "" >> $FEEDBACK
  echo "" >> $FEEDBACK
  echo "#############" >> $FEEDBACK
  echo "" >> $FEEDBACK
  echo "Please enter feedback above the bar." >> $FEEDBACK
  echo "" >> $FEEDBACK
  echo "$OUTPUT" >> $FEEDBACK

  if [[ -z "$EDITOR" ]]
  then
    $EDITOR $FEEDBACK
  else
    nano $FEEDBACK
  fi

  PARSEDOUTPUT=`sed -n '/#############/q;p' $FEEDBACK`
  send_command_wait_output "answerQuestion kind text answer { ${PARSEDOUTPUT//$'\n'/\<newline\>} }"
  if [[ $OUTPUT =~ end ]]
    then
    echo "Thank you for your answers!"
  else
    feedback $OUTPUT
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
  send_command "downloadExercises path `pwd` courseID $1"
}

function command_paste () {
  send_command "paste path `pwd`"
}

function command_test () {
  if [ $# -eq 0 ]
    then
      send_command "runTests path `pwd`"
    else
      send_command "runTests path `pwd` $1"
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
    DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

    CONFIGPATH="$DIR/config.properties"
    CONFIGPORT=`cat $CONFIGPATH | grep "serverPort" | sed s/serverPort=//g`
    echo $@ - | nc localhost $CONFIGPORT

    return 0;

}

function send_command_wait_output () {
    DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

    CONFIGPATH="$DIR/config.properties"
    CONFIGPORT=`cat $CONFIGPATH | grep "serverPort" | sed s/serverPort=//g`
    OUTPUT=`echo $@ | nc localhost $CONFIGPORT`

    return 0;

}

control_c()
# run if user hits control-c
{
  echo "Cancelling"
  send_command "stopProcess"
  exit $?
}

# catch ^C and run function control_c if user hits ^C
trap control_c SIGINT

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
STARTUP=$DIR
STARTUP+="/startup.sh"
bash $STARTUP

case "$1" in
    "login") command_login;;
    "submit") command_submit $2;;
    "download") command_download $2;;
    "test") command_test $2;;
    "paste") command_paste $2;;
    "listExercises") command_listExercises;;
    *) command_default $@;;
esac
