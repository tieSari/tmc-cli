#!/bin/bash

testHelp()
{
  shouldBe=""
  shouldBe='Commands:\n    help [command name] \tShow help for a command\n    test \t\t\tRun local tests\n    submit \t\t\tSubmit an exercise\n    paste \t\t\tCreate a tmc-paste\n    list exercises \t\tList all exercises\n    list courses \t\tList all courses\n    download <course ID> \tDownload a course\n    set server <address> \tChange your tmc-server\n    login \t\t\tLog in to tmc\n    logout \t\t\tLog out of tmc\n    update \t\t\tGet updates to your current course\n    showSettings \t\tShow settings\n    set course <coursename> \tSet current course'

  help=`tmc help`
  assertEquals $shouldBe $help
}

# load shunit2
. ./shunit2
