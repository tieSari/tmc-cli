package wad;

import fi.helsinki.cs.tmc.edutestutils.Points;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TwiitbergTest {

    @Test
    @Points("W5E07.1 W5E07.2 W5E07.3")
    public void palauttamallaTehtavanKerronEttaTeinJotain() {
        assertTrue(1 + 1 == 2);
    }
}
