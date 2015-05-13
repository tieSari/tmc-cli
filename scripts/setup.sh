#!/bin/bash

#Requires sudo
if [ `whoami` = "root" ]; then
  echo "OK"
else
  >&2 echo "Not root. Aborting"
  exit 0
fi
