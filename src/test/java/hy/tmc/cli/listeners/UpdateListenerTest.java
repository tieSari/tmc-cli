package hy.tmc.cli.listeners;

import fi.helsinki.cs.tmc.core.domain.Exercise;

import hy.tmc.cli.TmcCli;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UpdateListenerTest {

    UpdateDownloadingListener ul;
    TmcCli cli;

    public UpdateListenerTest() {
        cli = mock(TmcCli.class);
        ul = new UpdateDownloadingListener(cli, null, null, null);
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testParseData() {
        List<Exercise> exs = new ArrayList<>();
        exs.add(new Exercise());
        exs.add(new Exercise());
        exs.add(new Exercise());
        String expected = "3 updates downloaded";
        assertEquals(expected, ul.parseData(exs).get());
    }

    @Test
    public void testExtraActions() throws IOException {
        List<Exercise> exs = new ArrayList<>();
        exs.add(new Exercise());
        exs.add(new Exercise());
        exs.add(new Exercise());
        ul.extraActions(exs);
        verify(cli).refreshLastUpdate();
    }

    @Test
    public void testExtraActionsCallsNothingWithNoUpdates() throws IOException {
        ul.extraActions(new ArrayList<Exercise>());
        verify(cli, times(0)).refreshLastUpdate();
    }

}
