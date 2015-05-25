package hy.tmc.cli.frontend_communication.Server;

import hy.tmc.cli.frontend.communication.server.ProtocolException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class ProtocolExceptionTest {

    private ProtocolException protoExc;

    @Before
    public void setUp() {
        protoExc = new ProtocolException("test", new Throwable("asd"));
    }
    
    @Test
    public void constructorsWork(){
        ProtocolException a = new ProtocolException();
        ProtocolException b = new ProtocolException(a);
    }

    @Test(expected = Exception.class)
    public void canBeThrownAndCatched() throws ProtocolException {
        throw protoExc;
    }

    @Test
    public void afterThrowingAndCatchingMessageCanBeRead() {
        try {
            throw protoExc;
        }
        catch (ProtocolException ex) {
            assertEquals("test", ex.getMessage());
        }
    }

    @Test
    public void afterThrowingAndCatchingClauseCanBeRead() {
        try {
            throw protoExc;
        }
        catch (ProtocolException ex) {
            assertEquals("asd", ex.getCause().getMessage());
        }
    }

}
