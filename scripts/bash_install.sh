#bin/bash

wget https://github.com/rage/tmc-cli/archive/v0.6.zip

unzip v0.6.zip

mv tmc-cli-0.6 tmc-cli

cd tmc-cli
mvn clean package

cd scripts
TMCDIR=$(pwd)

echo -e '\n### Added by TestMyCode Commandline client' >> $HOME/.bashrc
echo "export PATH=\"$TMCDIR:\$PATH\"" >> $HOME/.bashrc
source $HOME/.bashrc

echo Install finished. Type "tmc" to get started.
