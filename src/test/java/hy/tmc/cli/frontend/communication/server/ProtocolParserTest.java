
package hy.tmc.cli.frontend.communication.server;

import hy.tmc.cli.frontend.communication.commands.Command;
import hy.tmc.cli.testhelpers.FrontendStub;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class ProtocolParserTest {

    private FrontendStub server;
    private ProtocolParser parser;
    
    @Before
    public void setup() {
        this.server = new FrontendStub();
        this.parser = new ProtocolParser(server);
    }
    
    @Test
    public void testGiveDataWithVimflag() throws ProtocolException{
        Command command = parser.getCommand("submit path /asd/lmao --vim");
        Assert.assertNotNull(command.getData().get("--vim"));
    }
    
    
}
