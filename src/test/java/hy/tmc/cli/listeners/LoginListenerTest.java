package hy.tmc.cli.listeners;

import com.google.common.util.concurrent.ListenableFuture;

import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;

import org.hamcrest.CoreMatchers;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

import java.io.DataOutputStream;
import java.net.Socket;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LoginListenerTest {

    private ListenableFuture future;
    private DataOutputStream output;
    private Socket socket;
    private TmcCli cli;
    private CliSettings settings;
    private LoginListener listener;

    @Before
    public void setUp() throws Exception {
        future = Mockito.mock(ListenableFuture.class);
        output = Mockito.mock(DataOutputStream.class);
        socket = Mockito.mock(Socket.class);
        cli = Mockito.mock(TmcCli.class);
        settings = new CliSettings();
        this.listener = new LoginListener(future, output, socket, cli, settings);
    }

    @Test
    public void parseDataSuccess() {
        assertThat(this.listener.parseData(true).get(),
            CoreMatchers.containsString("Auth successful"));
    }

    @Test
    public void parseDataFail() {
        assertThat(this.listener.parseData(false).get(),
            CoreMatchers.containsString("Auth unsuccessful"));
    }

    @Test
    public void saveCredentials() {
        String username = "asjl";
        String password = "asdjkl";
        settings.setUserData(username, password);
        this.listener.extraActions(true);
        verify(cli).login(eq(username), eq(password));
    }

    @Test
    public void dontSaveCredentials() {
        String username = "asjl";
        String password = "asdjkl";
        settings.setUserData(username, password);
        this.listener.extraActions(false);
        verify(cli, times(0)).login(anyString(), anyString());
    }
}
