package hy.tmc.cli;

import fi.helsinki.cs.tmc.langs.utils.ProcessResult;
import fi.helsinki.cs.tmc.langs.utils.ProcessRunner;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.TestServer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

public class ScriptTest {

    private final TestServer servu;
    private Thread serverThread;
    private String runningPath = Paths.get("scripts").toAbsolutePath().toString();

    public ScriptTest() throws IOException {
        servu = new TestServer();
    }

    @BeforeClass
    public static void setupClass() throws IOException, Exception {
        Path pidfile = Paths.get("scripts", "pidfile.pid");
        Files.write(pidfile, "1".getBytes());
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        Files.delete(Paths.get("scripts", "pidfile.pid"));
    }

    @After
    public void tearDown() throws IOException {
        servu.close();
        if (serverThread != null){
            serverThread.interrupt();
        }
        Files.deleteIfExists(Paths.get(new ConfigHandler().getConfigFilePath()));
    }

    private void startServer() {
        serverThread = new Thread(servu);
        serverThread.start();
    }

    @Test
    public void testDefaultHelp() throws Exception {
        servu.mock("help", "a helpful message");
        startServer();
        ProcessResult res = runCommand("");
        assertEquals("Usage: tmc <command> [...]\n"
                + "Command-line client for Test My Code.\n"
                + "\n"
                + "a helpful message", res.output);
    }

    @Test
    public void testHelp() throws Exception {
        servu.mock("help", "a helpful message");
        startServer();
        ProcessResult res = runCommand("help");
        assertEquals("a helpful message", res.output);
    }
    
    @Test
    public void testHelpWithArgs() throws Exception {
        servu.mock("help command duck", "will display duck");
        startServer();
        ProcessResult res = runCommand("help duck");
        assertEquals("will display duck", res.output);
    }

    @Test
    public void testShowSettings() throws Exception {
        servu.mock("showSettings", "settings: uname=BossmanSamu");
        startServer();
        ProcessResult res = runCommand("showSettings");
        assertEquals("settings: uname=BossmanSamu", res.output);
    }
    
    @Test
    public void testUpdate() throws Exception {
        servu.mock("update path " + runningPath, "update done");
        startServer();
        ProcessResult res = runCommand("update");
        assertEquals("update done", res.output);
    }
    
    @Test
    public void testLogout() throws Exception {
        servu.mock("logout", "done");
        startServer();
        ProcessResult res = runCommand("logout");
        assertEquals("done", res.output);
    }
    
    @Test
    public void testTest() throws Exception {
        servu.mock("runTests path " + runningPath, "tests done");
        startServer();
        ProcessResult res = runCommand("test");
        assertEquals("tests done", res.output);
    }
    
    @Test
    public void testPaste() throws Exception {
        servu.mock("paste path " + runningPath, "paste done");
        startServer();
        ProcessResult res = runCommand("paste");
        assertEquals("paste done", res.output);
    }
    
    @Test
    public void testSubmit() throws Exception {
        servu.mock("submit path " + runningPath, "_submit done");
        startServer();
        ProcessResult res = runCommand("submit");
        assertEquals("Submitting exercise...\n_submit done", res.output);
    }
    
    @Test
    public void testDownloadWithId() throws Exception {
        String expectedMessage = "downloadExercises path " + runningPath + " courseID 7";
        servu.mock(expectedMessage, "dl finished");
        startServer();
        ProcessResult res = runCommand("download 7");
        assertEquals("dl finished", res.output);
    }
    
    @Test
    public void testDownloadWithName() throws Exception {
        String expectedMessage = "downloadExercises path " + runningPath + " courseName ohpe";
        servu.mock(expectedMessage, "dl finished");
        startServer();
        ProcessResult res = runCommand("download ohpe");
        assertEquals("dl finished", res.output);
    }
    
    @Test
    public void testDownloadWithoutArgs() throws Exception {
        servu.mock("help command download", "this is how to use dl:");
        startServer();
        ProcessResult res = runCommand("download");
        assertEquals("this is how to use dl:", res.output);
    }
    
    @Test
    public void testListCourses() throws Exception {
        servu.mock("listCourses", "<(^)");
        startServer();
        ProcessResult res = runCommand("list courses");
        assertEquals("<(^)", res.output);
    }
    
    @Test
    public void testListExercises() throws Exception {
        servu.mock("listExercises path " + runningPath, "<(^)");
        startServer();
        ProcessResult res = runCommand("list exercises");
        assertEquals("<(^)", res.output);
    }
    
    @Test
    public void testListWithBadArgs() throws Exception {
        startServer();
        ProcessResult res = runCommand("list ducks");
        assertEquals("don\'t know how to list ducks\n", res.output);
    }
    
    @Test
    public void testSetServer() throws Exception {
        servu.mock("setServer tmc-server xkcd.com", "okay");
        startServer();
        ProcessResult res = runCommand("set server xkcd.com");
        assertEquals("okay", res.output);
    }
    
    @Test
    public void testSetCourse() throws Exception {
        servu.mock("setCourse course ohpe", "course set to ohpe");
        startServer();
        ProcessResult res = runCommand("set course ohpe");
        assertEquals("course set to ohpe", res.output);
    }

    @Test
    public void testSetRandomThing() throws Exception {
        startServer();
        ProcessResult res = runCommand("set pizza 5");
        assertEquals("don\'t know how to set pizza\n", res.output);
    }
    
    @Test
    public void testWontSetToEmptyValue() throws Exception {
        startServer();
        ProcessResult res = runCommand("set course");
        assertEquals("Not setting course to an empty value\n", res.output);
    }
    
    @Test
    public void testRandomCommands() throws Exception {
        servu.mock("conquer world", "nah");
        startServer();
        ProcessResult res = runCommand("conquer world");
        assertEquals("nah", res.output);
    }

    /**
     * runCommand("a b c") will be the same as running "tmc a b c" on the command line
     */
    private static ProcessResult runCommand(String command) throws Exception {
        command = "./tmc " + command;
        String[] args = command.split(" ");

        ProcessRunner runner = new ProcessRunner(args, Paths.get("scripts"));
        return runner.call();
    }

}
