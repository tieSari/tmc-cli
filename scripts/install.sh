#!/bin/bash

Deps=('nohup' 'pgrep' 'netcat' 'ps' 'sed' 'trap' 'kill' 'echo' 'curl' 'unzip');

command_not_exists(){
	! (hash "$1" 2>/dev/null);
}

for i in "${Deps[@]}"
do
	:
	if command_not_exists "$i" ; then
		printf "%s is required but not installed in your computer. \n" "$i";
	fi
done

curl -O http://ptoivanen.users.paivola.fi/tmc-cli-release.zip
unzip tmc-cli-release.zip
rm tmc-cli-release.zip
cd tmc-cli-release

if [ "$(uname)" == "Darwin" ]; then
	SHELL_CONFIG=$HOME/.bash_profile
else
	SHELL_CONFIG=$HOME/.bashrc
fi

TMCDIR=$(pwd)

echo -e '\n### Added by TestMyCode Commandline client' >> "$SHELL_CONFIG"
echo "export PATH=\"$TMCDIR:\$PATH\"" >> "$SHELL_CONFIG"
source "$SHELL_CONFIG"

echo Install finished. Type "tmc" to get started. Usage instructions and support information available in http://rage.github.io/tmc-cli/
