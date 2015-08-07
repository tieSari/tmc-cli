#bin/bash


Deps=('nohup' 'pgrep' 'netcat' 'ps' 'sed' 'trap' 'kill' 'echo' 'curl');

command_not_exists(){
	!(hash "$1" 2>/dev/null);
}

for i in "${Deps[@]}"
do
	:
	if command_not_exists $i ; then
		printf "%s is required but not installed in your computer. \n" $i;
	fi
done

curl -O http://ptoivanen.users.paivola.fi/tmc-cli-release.zip
unzip tmc-cli-release.zip
cd tmc-cli-release

#cd $( dirname "${BASH_SOURCE[0]}" )
TMCDIR=$(pwd)

echo -e '\n### Added by TestMyCode Commandline client' >> $HOME/.bashrc
echo "export PATH=\"$TMCDIR:\$PATH\"" >> $HOME/.bashrc
source $HOME/.bashrc

echo Install finished. Type "tmc" to get started.
