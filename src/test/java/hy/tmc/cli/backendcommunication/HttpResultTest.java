package hy.tmc.cli.backendcommunication;

<<<<<<< HEAD
import hy.tmc.cli.backend.communication.HttpResult;
=======
>>>>>>> 7e7a21132628c119da1f04ccfde7cba48da84eab
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class HttpResultTest {
    
    private HttpResult res;
    
    
    @Before
    public void setUp() {
        res = new HttpResult("asd", 200, true);
    }
    
    
    @Test
    public void successfullWhenSuccess() {
        assertTrue(res.isSuccess());
    }
    
    @Test
    public void canBeSettedUnsuccessfullAgain() {
        res.setSuccess(false);
        assertFalse(res.isSuccess());
    }
    
    @Test
    public void dataCanBeSet() {
        res.setData("dsa");
        assertEquals("dsa", res.getData());
    }
    
    @Test
    public void statusCodeCanBeSet() {
        res.setStatusCode(404);
        assertEquals(404, res.getStatusCode());
    }
    
}
