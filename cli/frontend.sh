#!/usr/bin/env bash

# echo $@
# echo $1

function help () {
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
case "$1" in
    "help") help;;
    #*) break;;
esac