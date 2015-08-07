package hy.tmc.cli.commands;

import hy.tmc.cli.TmcCli;
import hy.tmc.cli.frontend.communication.commands.Help;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class HelpTest {

    private Help help;
    private TmcCli cli;

    @Before
    public void setUp() {
        cli = mock(TmcCli.class);
    }

    @Test
    public void testMessageForHelp() throws Exception {
        help = new Help(cli, "help");
        assertEquals(expectedMessage("help"), help.call());
    }

    @Test
    public void testMessageForSubmit() throws Exception {
        help = new Help(cli, "submit");
        assertEquals(expectedMessage("submit"), help.call());
    }

    @Test
    public void testMessageForTest() throws Exception {
        help = new Help(cli, "test");
        assertEquals(expectedMessage("test"), help.call());
    }

    @Test
    public void testMessageForPaste() throws Exception {
        help = new Help(cli, "paste");
        assertEquals(expectedMessage("paste"), help.call());
    }

    @Test
    public void testMessageForDownload() throws Exception {
        help = new Help(cli, "download");
        assertEquals(expectedMessage("download"), help.call());
    }

    @Test
    public void testMessageForList() throws Exception {
        help = new Help(cli, "list");
        assertEquals(expectedMessage("list"), help.call());
    }

    @Test
    public void testMessageForSet() throws Exception {
        help = new Help(cli, "set");
        assertEquals(expectedMessage("set"), help.call());
    }

    @Test
    public void testMessageForLogin() throws Exception {
        help = new Help(cli, "login");
        assertEquals(expectedMessage("login"), help.call());
    }

    @Test
    public void testMessageForLogout() throws Exception {
        help = new Help(cli, "logout");
        assertEquals(expectedMessage("logout"), help.call());
    }
    
    @Test
    public void testMessageForUnknownCommand() throws Exception {
        help = new Help(cli, "ankka");
        assertEquals(expectedMessage("ankka"), help.call());
    }

    private String expectedMessage(String command) {
        switch (command) {
            case "help":
                return "'help' will list all commands, help <command-name> will "
                        + "display a help message for that command";
            case "test":
                return "Runs the local tests for an exercise. You need to be in "
                        + "that exercise directory, or any subdirectory";
            case "submit":
                return "Submits an exercise to the tmc-server. You need to be in"
                        + " that exercise directory, or any subdirectory";
            case "paste":
                return "Creates a tmc-paste of an exercise. You need to be in"
                        + " that exercise directory, or any subdirectory";
            case "download":
                return "'Download <course ID>' downloads a course with the id from "
                        + "the tmc-server. Use 'list courses' to see all courses and their ids. "
                        + "The courses exercises will be downloaded under the current directory";
            case "list":
                return "'list courses' will list all courses on the tmc-server specified."
                        + "'list exercises' will list all exercises of the current course. "
                        + "You must be in the course directory, or any subdirectory.";
            case "set":
                return "set server <tmc-server address> will change your tmc-server to the one given";
            case "login":
                return "Log in to tmc. Username and password will be asked.";
            case "logout":
                return "Log out of tmc. Clears your userdata from program memory.";
            default:
                return "I cannot help you with " + command + " :(";
        }
    }
}
