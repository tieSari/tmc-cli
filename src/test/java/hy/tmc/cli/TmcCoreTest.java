package hy.tmc.cli;

import com.google.common.util.concurrent.ListenableFuture;
import fi.helsinki.cs.tmc.langs.domain.RunResult;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.core.TmcCore;
import hy.tmc.core.exceptions.TmcCoreException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

/**
 * Tests that TmcCore-dependency is added correctly to pom.xml.
 */
public class TmcCoreTest {

    private TmcCore core;
    private CliSettings settings;

    @Before
    public void setup() {
        this.core = new TmcCore();
        ConfigHandler handler = Mockito.mock(ConfigHandler.class);
        String emptyResponseThatShouldInvokeExceptionInCore = "";
        Mockito.when(handler.readServerAddress()).thenReturn(
                emptyResponseThatShouldInvokeExceptionInCore
        );
        this.settings = new CliSettings(handler, "wrong path", "887");
    }

    @Test(expected = TmcCoreException.class)
    public void coreThrowsException() throws Exception {
        this.core.getCourse(settings, "wrong/path");
    }

    @Test
    public void testTmcLangsViaCore() throws Exception{
        ListenableFuture<RunResult> result = this.core.test(
                "testResources/2014-mooc-no-deadline/viikko1/Viikko1_001.Nimi", settings
        );
        assertEquals("TESTS_FAILED", result.get().status.toString());
    }

    
}
