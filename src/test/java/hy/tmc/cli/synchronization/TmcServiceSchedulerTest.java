package hy.tmc.cli.synchronization;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class TmcServiceSchedulerTest {

    private TmcServiceScheduler tmcServiceScheduler;

    @Before
    public void before() throws Exception {
        tmcServiceScheduler = TmcServiceScheduler.getScheduler();
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testGetScheduler() throws Exception {
        fail("TODO");
    }




} 
