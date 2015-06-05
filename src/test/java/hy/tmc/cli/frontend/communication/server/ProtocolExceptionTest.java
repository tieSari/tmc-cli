package hy.tmc.cli.frontend.communication.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProtocolExceptionTest {

    @Test
    public void afterThrowingMessageCanBeRetrieved() {
        new ProtocolException();
        String msg = "throwing...";
        String got = "";
        try {
            throw new ProtocolException(msg);
        }
        catch (ProtocolException ex) {
            got = ex.getMessage();
        }
        assertEquals(msg, got);
    }

    @Test
    public void afterThrowingClauseItCanBeRetrieved() {
        String msg = "asd";
        String tMsg = "Throw me";
        Throwable thr = new Throwable(tMsg);
        String got = "";
        
        try {
            throw new ProtocolException(msg, thr);
        }
        catch (ProtocolException ex) {
            got = ex.getCause().getMessage();
        }
        assertEquals(tMsg, got);
    }

}
