
package hy.tmc.cli.frontend_communication.Commands;

import hy.tmc.cli.stubs.FrontendStub;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;

public class HelpTest {

    private Help h;
    private ByteArrayOutputStream outContent;
    
    
    @Before
    public void setUp() {
        h = new Help(new FrontendStub(), null);
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void functionalityPrintsSomeContent() {
        h.functionality();
        h.setParameter(null, null);
        h.checkData();
        assertEquals(false, outContent.toString().isEmpty());
    }
    
    @After
    public void cleanUpStreams(){
        System.setOut(null);
    }
}