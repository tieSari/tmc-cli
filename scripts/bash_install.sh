#bin/bash


cd $( dirname "${BASH_SOURCE[0]}" )
TMCDIR=$(pwd)

echo -e '\n### Added by TestMyCode Commandline client' >> $HOME/.bashrc
echo "export PATH=\"$TMCDIR:\$PATH\"" >> $HOME/.bashrc
source $HOME/.bashrc

echo Install finished. Type "tmc" to get started.
