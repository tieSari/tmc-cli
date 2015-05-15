#!/usr/bin/env bash

# echo $@
# echo $1

#function command_help () {
#    declare -A array
#    array[help]=description
#
#    echo "Listing commands..."
#    echo "-----"
#
#    for i in "${!array[@]}"
#    do
#        echo "command: $i"
#        echo "description: ${array[$i]}"
#    done
#
#    return 0;
#}

function command_login () {
    read -p "Username: " username
    read -s  -p "Password: " password
    echo ""
    login $username $password
    return 0;
}

function command_default () {
    send_command $@
    return 0;
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
    OUTPUT=$(echo $@ | nc localhost 1234)
    echo $OUTPUT
    return 0;

}

#echo "Servu paalle"
bash startup.sh

case "$1" in
#    "help") command_help;;
    "login") command_login;;
    *) command_default $@;;
esac