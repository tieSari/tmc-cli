package hy.tmc.cli.listeners;

import com.google.common.base.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultListenerTest {

    public DefaultListenerTest() {
    }

    @Before
    public void setUp() {
    }

    @After 
    public void tearDown() {
    }

    @Test
    public void parseDataReturnsTheStringIfPresent() {
        DefaultListener listener = new DefaultListener(null, null, null);
        assertEquals("asdf", listener.parseData("asdf").get());
    }

    @Test
    public void parseDataReturnsAbsentIfNullOrEmpty() {
        DefaultListener listener = new DefaultListener(null, null, null);
        assertEquals(Optional.absent(), listener.parseData(""));
        assertEquals(Optional.absent(), listener.parseData(null));
    }

}
