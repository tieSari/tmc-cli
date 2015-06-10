package hy.tmc.cli.backend;

import hy.tmc.cli.domain.Course;
import java.util.Date;
import java.util.HashMap;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

public class CacheTest {

    @Test
    public void timestampTest() {
        Date date = new Date();
        Cache.update(new HashMap<Integer, Course>());
        assertNotEquals(Cache.getLastUpdated().getTime(), date.getTime());

    }

}
