package hy.tmc.cli.backend;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.commands.CommandFactory;
import org.junit.Before;
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
        fakeCommandMap.put("login", new CommandStub());
        fakeCommandMap.put("logout", new CommandStub());
        fakeCommandMap.put("help", new CommandStub());
        fakeCommandMap.put("ping", new CommandStub());
        fakeCommandMap.put("listCourses", new CommandStub());
        fakeCommandMap.put("listExercises", new CommandStub());
        fakeCommandMap.put("setServer", new CommandStub());
        fakeCommandMap.put("submit", new CommandStub());
        fakeCommandMap.put("runTests", new CommandStub());
        fakeCommandMap.put("downloadExercises", new CommandStub());

        PowerMockito
                .when(CommandFactory.createCommandMap())
                .thenReturn(fakeCommandMap);

        tmcCore = new TmcCore();
    }

//    @Test
//    public void testLogin() {
//        assertTrue(tmcCore.login("mock", "isbest"));
//
//    }
//
//    @Test
//    public void testLogout() {
//        assertTrue(tmcCore.logout());
//    }
//
//    @Test
//    public void testSelectServer() {
//        assertTrue(tmcCore.selectServer("myServer"));
//    }
//
//    @Test
//    public void testDownloadExercises() {
//        assertTrue(tmcCore.downloadExercises("asd", "5"));
//    }
//
//    @Test
//    public void testHelp() {
//        assertTrue(tmcCore.help());
//    }
//
//    @Test
//    public void testListCourses() {
//        assertTrue(tmcCore.listCourses());
//    }
//
//    @Test
//    public void testListExercises() {
//        assertTrue(tmcCore.listExercises());
//    }

}