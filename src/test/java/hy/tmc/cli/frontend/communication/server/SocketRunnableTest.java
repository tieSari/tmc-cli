package hy.tmc.cli.frontend.communication.server;

import hy.tmc.cli.backend.TmcCore;
import hy.tmc.cli.testhelpers.TestClient;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class SocketRunnableTest {

    ServerSocket serverSocket;
    TmcCore tmcCore;
    SocketRunnable socketRunnable;
    Socket socket;
    TestClient testClient;

    @Before
    public void setUp() throws IOException {
        tmcCore = mock(TmcCore.class);
        serverSocket = new ServerSocket(0);
        testClient = new TestClient(serverSocket.getLocalPort());
        socket = serverSocket.accept();
        socketRunnable = new SocketRunnable(socket, tmcCore);
    }

    @After
    public void tearDown() throws IOException {
        serverSocket.close();
    }

    @Test
    public void whenCommandHelpIsSentThroughSocketTmcCoreIsInvokedWithCorrectCommandName() throws IOException, ProtocolException {
        testClient.sendMessage("help");

        socketRunnable.run();

        verify(tmcCore).runCommand(eq("help"));
    }

    @Test
    public void whenCommandHelpIsSentThroughSocketTmcCoreIsNotInvokedWithFalseName() throws ProtocolException, IOException {
        testClient.sendMessage("help");

        socketRunnable.run();

        verify(tmcCore, never()).runCommand(eq("ping"));
    }

    @Test
    public void whenThreeCommandsAreSentTmcCoreIsInvokedThreeTimes() throws IOException, ProtocolException, InterruptedException {
        for (int i = 0; i < 3; i++) {
            testClient.sendMessage("ping");
            socketRunnable.run();
        }
        verify(tmcCore, times(3)).runCommand(eq("ping"));
    }

    @Test
    public void whenGivenInvalidCommandSocketIsClosed() throws ProtocolException, IOException {
        assertFalse(socket.isClosed());
        when(tmcCore.runCommand(eq("invalid"))).thenThrow(ProtocolException.class);
        testClient.sendMessage("invalid");
        socketRunnable.run();
        assertTrue(socket.isClosed());
    }
    
    @Test
    public void whenSocketIsClosedRunCommmandIsNotInvoked() throws IOException, ProtocolException {
        socket = new Socket();
        socket.close();
        socketRunnable = new SocketRunnable(socket, tmcCore);
        
        testClient.sendMessage("help");
        socketRunnable.run();
        verify(tmcCore, never()).runCommand(anyString());
    }
}
