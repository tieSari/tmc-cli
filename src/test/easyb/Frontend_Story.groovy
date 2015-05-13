
import hy.tmc.cli.testhelpers.Helper;

/**

 * Story for frontend

 */

description 'User can list commands'

scenario "user wants to see available commands", {

    given "command help to cli",{
        helper = new Helper()
        output = helper.startCommand("help", null)
    }

    then "cli returns list of commands", {

        expectedOutput = "Listing commands...\n" +
                "-----\n" +
                "command: help\n" +
                "description: description\n"

        output.shouldBe expectedOutput

    }

}
