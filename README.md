# tmc-cli

[![Build Status](https://travis-ci.org/tmc-cli/tmc-cli.svg?branch=master)](https://travis-ci.org/tmc-cli/tmc-cli)
[![Coverage Status](https://coveralls.io/repos/tmc-cli/tmc-cli/badge.svg)](https://coveralls.io/r/tmc-cli/tmc-cli)

* Command Line client for Test My Code
* Ohjelmistotuotantoprojekti 2015 Kesä

The goal for this project is to build a command-line client for [TestMyCode](https://github.com/testmycode/tmc-server)

## As of 3.6.2015
Current core functionality includes:
* Downloading exercises from TMC-server
* Submitting exerises
* Running local tests

[Installation instructions](Installation.md)


## Usage
### List of commands:
- login
  - Takes a username and a password and remembers the information. 
- test
  - Runs TMC tests on the exercise in the current directory
  - Returns info about the tests (failed/passed etc)
  - If no exercise is found, looks for it above
  - If it's still not found, throws an error
- submit
  - Submits an exercise in the current directory to the TMC server
  - If no exercise is found, looks for it above
  - If it's still not found, throws an error
- listCourses
  - Lists courses that exist on the specified TMC server and their course IDs
- download
  - Takes a course ID and downloads it to the current directory.
- help
  - Gives a list of commands
- ping
  - returns "pong" if the program is running
- setServer
  - Sets the server where to return exercises to and get exercises from 
- listExercises
  - When a user is in a course directory, lists the exercises of that course
  - If the user has completed a certain exercise, it is shown that way
