import hy.tmc.cli.testhelpers.Helper;
/**

 * Story for login-function.

 */
description 'Authentication works.'

scenario "User can log in.", {

    given "command login",{
        helper = new Helper()
        output = helper.printOutput("login", "scripts/frontend.sh")
    }



    when "an initial deposit is made", {
        
        System.out.println("output: ");
        System.out.println(output);

    }



    then "the balance should be equal to the amount deposited", {

        

    }

}