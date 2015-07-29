package hy.tmc.cli.listeners;

import hy.tmc.core.commands.Paste;
import java.net.URI;
import java.net.URISyntaxException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class PasteListenerTest {
    
    private PasteListener pl;

    @Before
    public void setUp() {
        pl = new PasteListener(null, null, null);
    }
    
    @Test
    public void testParseData() throws URISyntaxException {
        URI uri = new URI("www.xkcd.com");
        
        String expected = "Paste submitted. Here it is: \n  " + uri.toString();
        assertEquals(expected, pl.parseData(uri).get());
    }
    
}
