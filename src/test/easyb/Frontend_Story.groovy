package easyb
/**

 * Story for frontend

 */

before "", {
    helper = new Helper()
}

scenario "user wants to see available commands", {

    given "command help to cli",{

        output = helper.startCommand("help")

    }

    then "cli returns list of commands", {

        expectedOutput = "Listing commands...\n" +
                "-----\n" +
                "command: help\n" +
                "description: description\n"

        output.shouldBe expectedOutput

    }

}
