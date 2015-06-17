package hy.tmc.cli.backend;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import fi.helsinki.cs.tmc.langs.RunResult;
import fi.helsinki.cs.tmc.langs.TestResult;
import hy.tmc.cli.domain.submission.SubmissionResult;
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
import java.util.ArrayList;
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
    public void setUp() {
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
        when(fakeCommandMap.get("login").call()).thenReturn(true);
        assertEquals(Boolean.class, tmcCore.login("nimi", "passu").get().getClass());
    }
    
    @Test(expected = ProtocolException.class)
    public void loginWithoutNumberFails() throws ProtocolException, InterruptedException, ExecutionException, Exception {
        when(fakeCommandMap.get("login").call()).thenThrow(new ProtocolException("feilaus"));
        tmcCore.login("", "").get();
    }

    @Test
    public void logout() throws ProtocolException, InterruptedException, ExecutionException, Exception {
        when(fakeCommandMap.get("logout").call()).thenReturn(true);
        assertEquals(Boolean.class, tmcCore.logout().get().getClass());
    }
//    
//    @Test
//    public void logoutFailsWithInvalidUrl() throws ProtocolException, InterruptedException, ExecutionException, Exception {
//        when(fakeCommandMap.get("logout").call()).thenReturn(true);
//        assertEquals(Boolean.class, tmcCore.logout().get().getClass());
//    }


    @Test
    public void selectServer() throws ProtocolException, InterruptedException, ExecutionException, Exception {
        when(fakeCommandMap.get("setServer").call()).thenReturn(true);
        assertEquals(Boolean.class, tmcCore.selectServer("uusServu").get().getClass());
    }

    @Test
    public void downloadExercises() throws ProtocolException, InterruptedException, ExecutionException, Exception {
        when(fakeCommandMap.get("downloadExercises").call()).thenReturn("45633 exercises downloaded");
        assertEquals(String.class, tmcCore.downloadExercises("polku/tiedoston", "77").get().getClass());
    }

    @Test
    public void help() throws ProtocolException, InterruptedException, ExecutionException, Exception {
        when(fakeCommandMap.get("help").call()).thenReturn("ping pong padam");
        assertEquals(String.class, tmcCore.help().get().getClass());
    }

    @Test
    public void listCourses() throws ProtocolException, InterruptedException, ExecutionException, Exception {
        when(fakeCommandMap.get("listCourses").call()).thenReturn(new ArrayList<>());
        assertEquals(ArrayList.class, tmcCore.listCourses().get().getClass());
    }

    @Test
    public void listExercises() throws ProtocolException, InterruptedException, ExecutionException, Exception {
        when(fakeCommandMap.get("listExercises").call()).thenReturn(new ArrayList<>());
        assertEquals(ArrayList.class, tmcCore.listExercises("polku").get().getClass());
    }
    
    @Test
    public void test() throws ProtocolException, InterruptedException, ExecutionException, Exception {
        when(fakeCommandMap.get("runTests").call()).thenReturn(new RunResult(
                RunResult.Status.PASSED,
                ImmutableList.copyOf(new ArrayList<TestResult>()),
                ImmutableSortedMap.copyOf(new HashMap<String, byte[]>())
        ));
        assertEquals(RunResult.class, tmcCore.test("askjdhasdhasjd").get().getClass());
    }
    
    @Test
    public void submit() throws Exception {
        when(fakeCommandMap.get("submit").call()).thenReturn(new SubmissionResult());
        assertEquals(SubmissionResult.class, tmcCore.submit("merkkijono").get().getClass());
    }
}
