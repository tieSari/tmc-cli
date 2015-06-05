# Installation Instructions

**Warning,** heavily in development. Features might be unusable or broken.
### Use these instructions to get the TMC commandline client up and running. 

If maven is not installed, you won't be able to build the jar. Install it:
```bash
    $ apt-get install maven
```
Start by cloning the repo to ~/.tmc-cli:

    $ git clone https://github.com/tmc-cli/tmc-cli  ~/.tmc-cli

Now switch to the new project directory
```bash
    $ cd
    $ cd .tmc-cli
```
And create the java executable

    $ mvn package

**Optionally** add an alias to .bash_aliases (if using bash) to make the client easily runnable from anywhere.
Open up .bash_aliases:
```bash
    $ nano .bash_aliases
```
And add the following line if you installed directly under ~/.tmc-cli:
```bash
    alias tmc='$HOME/.tmc-cli/scripts/frontend.sh'
```

Otherwise, replace the path with the path you cloned the repo in.

You can use the program by calling the shell script in the scripts directory directly, or by using the alias.

Type the following to get started:

    $ tmc help
