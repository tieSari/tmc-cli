package hy.tmc.cli.listeners;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;

import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.DataOutputStream;
import java.net.Socket;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ResultListenerTest {

    private ListenableFuture future;
    private DataOutputStream output;
    private Socket socket;

    @Before
    public void setUp() throws Exception {
        future = Mockito.mock(ListenableFuture.class);
        output = Mockito.mock(DataOutputStream.class);
        socket = Mockito.mock(Socket.class);
    }

    @Test
    public void exceptionInThreadWritesCauseToOutput() throws Exception {
        String cause = "Kesken meni!";
        InterruptedException fakeException = new InterruptedException();
        fakeException.initCause(new TmcCoreException(cause));
        when(future.get()).thenThrow(fakeException);
        ResultListenerImpl resultListener = new ResultListenerImpl(future, output, socket, null);
        resultListener.run();
        verify(output).write(eq((cause + "\n").getBytes()));
    }

    @Test
    public void ifParseDataIsPresentItIsWritten() throws Exception {
        String objectToParse = "blabla";
        String parsedObject = "Hello!";
        when(future.get()).thenReturn(objectToParse);
        ResultListenerImpl resultListener =
            new ResultListenerImpl(future, output, socket, parsedObject);
        resultListener.run();
        verify(output).write(eq((parsedObject + "\n").getBytes()));
    }

    @Test
    public void noDataIsWritten() throws Exception {
        String objectToParse = "blabla";
        String parsedObject = null;
        when(future.get()).thenReturn(objectToParse);
        ResultListenerImpl resultListener =
            new ResultListenerImpl(future, output, socket, parsedObject);
        resultListener.run();
        verify(output, times(0)).write(any(byte.class));
    }
}


class ResultListenerImpl extends ResultListener {

    private String data;

    public ResultListenerImpl(ListenableFuture commandResult, DataOutputStream output,
        Socket socket, String data) {
        super(commandResult, output, socket);
        this.data = data;
    }

    @Override protected Optional<String> parseData(Object result) {
        if (data == null) {
            return Optional.absent();
        } else {
            return Optional.of(data);
        }
    }

    @Override protected void extraActions(Object result) {

    }
}
