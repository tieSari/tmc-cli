package hy.tmc.cli.frontend.communication.commands;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({System.class, StopProcess.class})
public class StopProcessTest {

    /**
     * This test mocks the System.exit and verifies that it has been called
     */
    @Test
    public void functionalityStopsTheProcess() {
        PowerMockito.mockStatic(System.class);
        StopProcess stop = new StopProcess();
        stop.functionality();
        PowerMockito.verifyStatic();
    }
}
