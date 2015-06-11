package hy.tmc.cli.frontend.communication.server;

import hy.tmc.cli.configuration.ConfigHandler;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;


public class ServerTest {
    
    @Test
    public void testGetCurrentPort() {
        Server servu = createServer();
        int result = servu.getCurrentPort();
        assertEquals(new ConfigHandler().readPort(), result);
    }
    
    private Server createServer() {
        try {
            return new Server();
        }
        catch (IOException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }
}
