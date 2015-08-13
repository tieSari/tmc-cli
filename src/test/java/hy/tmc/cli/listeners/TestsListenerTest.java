package hy.tmc.cli.listeners;

import com.google.common.util.concurrent.Futures;

import fi.helsinki.cs.tmc.core.domain.submission.Validations;
import fi.helsinki.cs.tmc.langs.domain.RunResult;
import fi.helsinki.cs.tmc.langs.domain.RunResult.Status;
import fi.helsinki.cs.tmc.langs.domain.TestResult;

import hy.tmc.cli.frontend.formatters.CheckstyleFormatter;
import hy.tmc.cli.frontend.formatters.TestResultFormatter;
import hy.tmc.cli.testhelpers.builders.RunResultBuilder;
import hy.tmc.cli.testhelpers.builders.TestResultBuilder;
import hy.tmc.cli.testhelpers.builders.ValidationsBuilder;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import static fi.helsinki.cs.tmc.langs.domain.RunResult.Status.PASSED;
import static fi.helsinki.cs.tmc.langs.domain.RunResult.Status.TESTS_FAILED;

import static org.junit.Assert.assertEquals;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestsListenerTest {

    private TestResultFormatter formatter;
    private CheckstyleFormatter checks;
    private DataOutputStream out;
    private Socket socket;
    private ArgumentCaptor<byte[]> captor;

    @Before
    public void setUp() {
        formatter = mock(TestResultFormatter.class);
        checks = mock(CheckstyleFormatter.class);
        out = mock(DataOutputStream.class);
        socket = mock(Socket.class);
        when(formatter.interpretStatus(any(RunResult.class))).thenReturn("hello world");
        when(formatter.getFailedTestOutput(any(TestResult.class))).thenReturn("fail_output ");
        when(formatter.someTestsFailed()).thenReturn("some fail msg ");
        when(formatter.getStackTrace(any(TestResult.class))).thenReturn("stack!");
        when(formatter.howMuchTestsFailed(anyInt())).thenReturn("n fails ");
        when(formatter.noTestsPassed()).thenReturn("none passed ");

        captor = ArgumentCaptor.forClass(byte[].class);

        when(checks.checkstyleErrors(anyString(), any(List.class))).thenReturn("checkstyle");

    }

    @Test
    public void testTestsPassWithCheckstyleErrorsPrintsDifferentMessage() throws IOException {
        TestsListener tlPassed = new TestsListener(Futures.immediateFuture(createResult(PASSED)),
            Futures.immediateFuture(createCheckstyleResult("fail")), out, socket, formatter, checks,
            false);
        tlPassed.run();

        verify(out, times(3)).write(captor.capture());

        List<byte[]> calls = captor.getAllValues();
        assertEquals("All tests passed, but there are checkstyle errors.\n",
            new String(calls.get(0)));
        assertEquals("checkstyle", new String(calls.get(1)));
        assertEquals("\n", new String(calls.get(2)));
        verify(socket).close();
    }

    @Test
    public void testGenerallyCallsBothFormatters() throws IOException {
        TestsListener tlTestFails =
            new TestsListener(Futures.immediateFuture(createResult(TESTS_FAILED)),
                Futures.immediateFuture(createCheckstyleResult("fail")), out, socket, formatter,
                checks, false);
        tlTestFails.run();

        verify(out, times(3)).write(captor.capture());

        List<byte[]> calls = captor.getAllValues();
        assertEquals("some fail msg none passed n fails fail_output ", new String(calls.get(0)));
        assertEquals("checkstyle", new String(calls.get(1)));
        assertEquals("\n", new String(calls.get(2)));
    }

    @Test
    public void testStackTraceIsPrintedIfVerboseIsTrue() throws IOException {
        TestsListener tlTestFails =
            new TestsListener(Futures.immediateFuture(createResult(TESTS_FAILED)),
                Futures.immediateFuture(createCheckstyleResult("fail")), out, socket, formatter,
                checks, true);
        tlTestFails.run();

        verify(out, times(3)).write(captor.capture());

        List<byte[]> calls = captor.getAllValues();
        assertEquals("some fail msg none passed n fails fail_output stack!",
            new String(calls.get(0)));
        assertEquals("checkstyle", new String(calls.get(1)));
        assertEquals("\n", new String(calls.get(2)));
    }

    private Validations createCheckstyleResult(String strategy) {
        return new ValidationsBuilder().withStrategy(strategy)
            .withValidationError("a.java", 3, 3, "ugly font", "asdf").build();
    }

    /**
     * create new runresult with status
     */
    private RunResult createResult(Status status) {
        return new RunResultBuilder().withStatus(status).withTest(new TestResultBuilder().build())
            .build();
    }

}
