package hy.tmc.cli.backend_communication;

import hy.tmc.cli.backend.communication.HttpResult;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class HTTPResultTest {
    
    private HttpResult r;
    
    
    @Before
    public void setUp(){
        r = new HttpResult("asd", 200, true);
    }
    
    
    @Test
    public void successfullWhenSuccess(){
        assertTrue(r.isSuccess());
    }
    
    @Test
    public void canBeSettedUnsuccessfullAgain(){
        r.setSuccess(false);
        assertFalse(r.isSuccess());
    }
    
    @Test
    public void dataCanBeSet(){
        r.setData("dsa");
        assertEquals("dsa", r.getData());
    }
    
    @Test
    public void statusCodeCanBeSet(){
        r.setStatusCode(404);
        assertEquals(404, r.getStatusCode());
    }
    
}
