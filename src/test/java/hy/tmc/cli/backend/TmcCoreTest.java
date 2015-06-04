package hy.tmc.cli.backend;

import hy.tmc.cli.backend.communication.UrlCommunicator;
import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.commands.CommandFactory;
import hy.tmc.cli.testhelpers.FrontendStub;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.anyString;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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

    /**
     * Test of login method, of class TmcCore.
     */
    @Test
    public void testLogin() {
        assertTrue(tmcCore.login("mock", "isbest"));

    }

    /**
     * Test of logout method, of class TmcCore.
     */
    @Test
    public void testLogout() {
        assertTrue(tmcCore.logout());
    }

    /**
     * Test of selectServer method, of class TmcCore.
     */
    @Test
    public void testSelectServer() {
        assertTrue(tmcCore.selectServer("myServer"));
    }

    /**
     * Test of downloadExercises method, of class TmcCore.
     */
    @Test
    public void testDownloadExercises() {
        assertTrue(tmcCore.downloadExercises("asd", "5"));
    }

    /**
     * Test of help method, of class TmcCore.
     */
    @Test
    public void testHelp() {
        assertTrue(tmcCore.help());
    }

    /**
     * Test of listCourses method, of class TmcCore.
     */
    @Test
    public void testListCourses() {
        assertTrue(tmcCore.listCourses());
    }

    /**
     * Test of listExercises method, of class TmcCore.
     */
    @Test
    public void testListExercises() {
        assertTrue(tmcCore.listExercises());
    }

}
