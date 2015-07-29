package hy.tmc.cli.listeners;

import fi.helsinki.cs.tmc.langs.domain.RunResult;
import fi.helsinki.cs.tmc.langs.domain.RunResult.Status;
import static fi.helsinki.cs.tmc.langs.domain.RunResult.Status.PASSED;
import static fi.helsinki.cs.tmc.langs.domain.RunResult.Status.TESTS_FAILED;
import fi.helsinki.cs.tmc.langs.domain.TestResult;
import hy.tmc.cli.frontend.formatters.TestResultFormatter;
import hy.tmc.cli.testhelpers.builders.RunResultBuilder;
import hy.tmc.cli.testhelpers.builders.TestResultBuilder;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestsListenerTest {

    private TestsListener tl;
    private TestResultFormatter formatter;

    @Before
    public void setUp() {
        formatter = mock(TestResultFormatter.class);
        when(formatter.interpretStatus(any(RunResult.class))).thenReturn("hello world");
    }

    @Test
    public void parseDataTest() {
        tl = new TestsListener(null, null, null, formatter);
        assertEquals("hello world", tl.parseData(createResult(PASSED)).get());
        tl.parseData(createResult(TESTS_FAILED));
        verify(formatter, times(0)).getStackTrace(any(TestResult.class));
    }

    @Test
    public void parseDataPassesShowStacktraceParam() {
        tl = new TestsListener(null, null, null, formatter, true);
        verify(formatter, times(0)).getStackTrace(any(TestResult.class));
        tl.parseData(createResult(TESTS_FAILED));
        verify(formatter, times(1)).getStackTrace(any(TestResult.class));
    }

    /**
     * create new runresult with status
     */
    private RunResult createResult(Status status) {
        return new RunResultBuilder().withStatus(status)
                .withTest(
                        new TestResultBuilder().build()
                ).build();
    }

}
