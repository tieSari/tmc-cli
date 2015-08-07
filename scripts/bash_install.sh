#bin/bash

deps[0] = 'nohup'
deps[1] = 'pgrep'
deps[2] = 'netcat'
deps[3] = 'ps'
deps[4] = 'sed'
deps[5] = 'trap'
deps[6] = 'kill'
deps[7] = 'echo'

cd $( dirname "${BASH_SOURCE[0]}" )
TMCDIR=$(pwd)

echo -e '\n### Added by TestMyCode Commandline client' >> $HOME/.bashrc
echo "export PATH=\"$TMCDIR:\$PATH\"" >> $HOME/.bashrc
source $HOME/.bashrc

echo Install finished. Type "tmc" to get started.
