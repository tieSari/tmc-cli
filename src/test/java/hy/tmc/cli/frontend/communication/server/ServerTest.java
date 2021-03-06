package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.ListeningExecutorService;

import fi.helsinki.cs.tmc.core.TmcCore;

import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.testhelpers.TestClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ServerTest {

    Server server;
    TmcCore tmcCore;
    ListeningExecutorService socketThreadPool;
    TestClient client;
    Thread serverThread;

    @Before
    public void setup() throws IOException, TmcCoreException {
        tmcCore = mock(TmcCore.class);

        socketThreadPool = Mockito.mock(ListeningExecutorService.class);
        server = new Server(new TmcCli(tmcCore, null), socketThreadPool);

        serverThread = new Thread(server);
        serverThread.start();
        client = new TestClient(server.getCurrentPort());
    }

    private void sendMessageToServer(TestClient client, String message) {
        try {
            client.sendMessage(message);
            Thread.sleep(300);
        } catch (IOException | InterruptedException e) {
            fail("Failed to send message to server or sleep interrupted");
        }
    }

    @After 
    public void after() throws IOException {
        server.close();
        serverThread.interrupt();
    }

    @Test
    public void poolIsGivenTasksEvenIfCommandIsBad() throws ProtocolException {
        sendMessageToServer(client, "samu");
        verify(socketThreadPool).submit(Mockito.any(SocketRunnable.class));
    }

    @Test
    public void socketPoolIsGivenATaskWhenACommandIsSent() {
        sendMessageToServer(client, "help");
        verify(socketThreadPool).submit(Mockito.any(SocketRunnable.class));
    }

    @Test
    public void twoClientsSendMessagesAndBothAreIntercepted() throws IOException {
        TestClient clientB = new TestClient(server.getCurrentPort());
        sendMessageToServer(client, "help");
        sendMessageToServer(clientB, "help");
        verify(socketThreadPool, times(2)).submit(any(SocketRunnable.class));
    }

    @Test
    public void twoClientsSendMessagesAndOneIsBadAndBothAreIntercepted() throws IOException {
        TestClient clientB = new TestClient(server.getCurrentPort());
        sendMessageToServer(client, "help");
        sendMessageToServer(clientB, "lol");
        verify(socketThreadPool, times(2)).submit(any(SocketRunnable.class));
    }

    @Test
    public void testGetCurrentPort() throws IOException {
        int result = server.getCurrentPort();
        assertEquals(new ConfigHandler().readPort(), result);
    }

}
