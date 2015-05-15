#!/usr/bin/env bash

# echo $@
# echo $1

function command_help () {
    declare -A array
    array[help]=description

    echo "Listing commands..."
    echo "-----"

    for i in "${!array[@]}"
    do
        echo "command: $i"
        echo "description: ${array[$i]}"
    done

    return 0;
}

function command_login () {
    read -p "Username: " username
    read -s  -p "Password: " password
    echo ""
    return 0;
}

function command_default () {
    command_help
    return 0;
}

# Backend login
function login () {
    return 0;

}

# Backend cmd send
function send_command () {
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
bash startup.sh

case "$1" in
    "help") command_help;;
    "login") command_login;;
    *) command_default;;
esac
