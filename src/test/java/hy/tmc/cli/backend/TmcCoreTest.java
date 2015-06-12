package hy.tmc.cli.backend;

import static org.junit.Assert.assertTrue;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.commands.CommandFactory;
import hy.tmc.cli.testhelpers.FrontendStub;
import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommandFactory.class)
public class TmcCoreTest {

    private TmcCore tmcCore;
    private FrontendListener listener;

    @Before
    public void setUp() throws Exception {

        PowerMockito.mockStatic(CommandFactory.class);
        HashMap<String, Command> fakeCommandMap = new HashMap<>();
        fakeCommandMap.put("login", new CommandStub(listener));
        fakeCommandMap.put("logout", new CommandStub(listener));
        fakeCommandMap.put("help", new CommandStub(listener));
        fakeCommandMap.put("ping", new CommandStub(listener));
        fakeCommandMap.put("listCourses", new CommandStub(listener));
        fakeCommandMap.put("listExercises", new CommandStub(listener));
        fakeCommandMap.put("setServer", new CommandStub(listener));
        fakeCommandMap.put("submit", new CommandStub(listener));
        fakeCommandMap.put("runTests", new CommandStub(listener));
        fakeCommandMap.put("downloadExercises", new CommandStub(listener));

        PowerMockito
                .when(CommandFactory.createCommandMap(Mockito.any(FrontendListener.class)))
                .thenReturn(fakeCommandMap);

        listener = new FrontendStub();
        tmcCore = new TmcCore(listener);

    }

    @Test
    public void testLogin() {
        assertTrue(tmcCore.login("mock", "isbest"));

    }

    @Test
    public void testLogout() {
        assertTrue(tmcCore.logout());
    }

    @Test
    public void testSelectServer() {
        assertTrue(tmcCore.selectServer("myServer"));
    }

    @Test
    public void testDownloadExercises() {
        assertTrue(tmcCore.downloadExercises("asd", "5"));
    }

    @Test
    public void testHelp() {
        assertTrue(tmcCore.help());
    }

    @Test
    public void testListCourses() {
        assertTrue(tmcCore.listCourses());
    }

    @Test
    public void testListExercises() {
        assertTrue(tmcCore.listExercises());
    }

}