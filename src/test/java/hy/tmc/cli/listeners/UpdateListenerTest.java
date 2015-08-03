package hy.tmc.cli.listeners;

import hy.tmc.core.domain.Exercise;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UpdateListenerTest {

    UpdateDownloadingListener ul;

    public UpdateListenerTest() {
        ul = new UpdateDownloadingListener(null, null, null);
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
        String expected = "Downloaded 3 exercises.";
        assertEquals(expected, ul.parseData(exs).get());
    }

}
