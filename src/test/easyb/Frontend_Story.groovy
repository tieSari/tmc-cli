
import hy.tmc.cli.testhelpers.Helper;

/**

 * Story for frontend

 */

description 'User can list commands'

scenario "user wants to see available commands", {

    given "command help to cli",{
        helper = new Helper()
        output = helper.startCommand("help", "scripts/frontend.sh")
    }

    then "cli returns list of commands", {

        ensure(output) {
            contains("help");
        }
    }

}
