package hy.tmc.cli.backend;

import hy.tmc.cli.frontend.FrontendListener;
import hy.tmc.cli.frontend.communication.commands.Authenticate;
import hy.tmc.cli.frontend.communication.commands.ChooseServer;
import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.frontend.communication.commands.CommandFactory;
import hy.tmc.cli.frontend.communication.commands.DownloadExercises;
import hy.tmc.cli.frontend.communication.commands.Help;
import hy.tmc.cli.frontend.communication.commands.ListCourses;
import hy.tmc.cli.frontend.communication.commands.ListExercises;
import hy.tmc.cli.frontend.communication.commands.Logout;
import hy.tmc.cli.frontend.communication.commands.Paste;
import hy.tmc.cli.frontend.communication.commands.ReplyToPing;
import hy.tmc.cli.frontend.communication.commands.RunTests;
import hy.tmc.cli.frontend.communication.commands.Submit;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommandFactory.class)
public class TmcCoreTest {

    private TmcCore tmcCore;
    private FrontendListener listener;
    private Map<String, Command> fakeCommandMap;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(CommandFactory.class);
        fakeCommandMap = new HashMap<>();
        fakeCommandMap.put("login", mock(Authenticate.class));
        fakeCommandMap.put("logout", mock(Logout.class));
        fakeCommandMap.put("help", mock(Help.class));
        fakeCommandMap.put("ping", mock(ReplyToPing.class));
        fakeCommandMap.put("listCourses", mock(ListCourses.class));
        fakeCommandMap.put("listExercises", mock(ListExercises.class));
        fakeCommandMap.put("setServer", mock(ChooseServer.class));
        fakeCommandMap.put("submit", mock(Submit.class));
        fakeCommandMap.put("runTests", mock(RunTests.class));
        fakeCommandMap.put("downloadExercises", mock(DownloadExercises.class));
        fakeCommandMap.put("paste", mock(Paste.class));

        PowerMockito
                .when(CommandFactory.createCommandMap())
                .thenReturn(fakeCommandMap);

        tmcCore = new TmcCore();
    }

    @Test
    public void login() throws ProtocolException, InterruptedException, ExecutionException, Exception {
        when(fakeCommandMap.get("login").call()).thenReturn("stringi");
        assertEquals(String.class, tmcCore.login("nimi", "passu").get().getClass());
    }
    
    @Test(expected = ProtocolException.class)
    public void loginWithoutNumberFails() throws ProtocolException, InterruptedException, ExecutionException, Exception {
        when(fakeCommandMap.get("login").call()).thenThrow(ProtocolException.class);
        tmcCore.login("", "").get();
    }

//    @Test
//    public void logout() throws ProtocolException, InterruptedException, ExecutionException {
//        assertEquals(String.class, tmcCore.logout().get().getClass());
//    }
//
//    @Test
//    public void selectServer() throws ProtocolException, InterruptedException, ExecutionException {
//        assertEquals(String.class, tmcCore.selectServer("uusServu").get().getClass());
//    }
//
//    @Test
//    public void downloadExercises() throws ProtocolException, InterruptedException, ExecutionException {
//        assertEquals(String.class, tmcCore.downloadExercises("polku/tiedoston", "77").get().getClass());
//    }
//
//    @Test
//    public void help() throws ProtocolException, InterruptedException, ExecutionException {
//        assertEquals(String.class, tmcCore.help().get().getClass());
//    }
//
//    @Test
//    public void listCourses() throws ProtocolException, InterruptedException, ExecutionException {
//        assertEquals(String.class, tmcCore.listCourses().get().getClass());
//    }
//
//    @Test
//    public void listExercises() throws ProtocolException, InterruptedException, ExecutionException {
//        assertEquals(String.class, tmcCore.listExercises().get().getClass());
//    }

}
