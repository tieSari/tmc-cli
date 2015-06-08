package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.testhelpers.FrontendStub;
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
        FrontendStub frontendStub = new FrontendStub();
        StopProcess stop = new StopProcess(frontendStub);
        stop.functionality();
        PowerMockito.verifyStatic();
    }

}
