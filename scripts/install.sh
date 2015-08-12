<<<<<<< HEAD
#bin/bash

=======
#!/bin/bash
>>>>>>> 73ae0702f31fe88266b819d333543d1a2a6d128c

Deps=('nohup' 'pgrep' 'netcat' 'ps' 'sed' 'trap' 'kill' 'echo' 'curl' 'unzip');

command_not_exists(){
<<<<<<< HEAD
	!(hash "$1" 2>/dev/null);
=======
	! (hash "$1" 2>/dev/null);
>>>>>>> 73ae0702f31fe88266b819d333543d1a2a6d128c
}

for i in "${Deps[@]}"
do
	:
<<<<<<< HEAD
	if command_not_exists $i ; then
		printf "%s is required but not installed in your computer. \n" $i;
=======
	if command_not_exists "$i" ; then
		printf "%s is required but not installed in your computer. \n" "$i";
>>>>>>> 73ae0702f31fe88266b819d333543d1a2a6d128c
	fi
done

curl -O http://ptoivanen.users.paivola.fi/tmc-cli-release.zip
unzip tmc-cli-release.zip
<<<<<<< HEAD
rm -rf tmc-cli-release.zip
cd tmc-cli-release

#cd $( dirname "${BASH_SOURCE[0]}" )
TMCDIR=$(pwd)

echo -e '\n### Added by TestMyCode Commandline client' >> $HOME/.bashrc
echo "export PATH=\"$TMCDIR:\$PATH\"" >> $HOME/.bashrc
source $HOME/.bashrc
=======
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
>>>>>>> 73ae0702f31fe88266b819d333543d1a2a6d128c

echo Install finished. Type "tmc" to get started. Usage instructions and support information available in http://rage.github.io/tmc-cli/
