import java.util.*;
import org.junit.Test;

import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Arrays;

@Points("3.2")
public class MainTest {
    public void testi(String pohja, int tulos) {
        int uusi = Main.ketjumaara(pohja);
        assertTrue("Kun pohja on " + pohja + ", ketjuja tulisi olla " +
                   tulos + ", mutta metodisi palauttaa " + uusi + ".",
                   uusi == tulos);
    }
    
    @Test(timeout=1000)
    public void esimerkit() {
        testi("A?C?", 6);
        testi("???", 36);
        testi("AGAG", 1);
        testi("A???T", 20);
    }
    
    @Test(timeout=1000)
    public void pienet() {
        testi("?", 4);
        testi("A?", 3);
        testi("?A", 3);
        testi("??", 12);
        testi("A?A", 3);
        testi("A?C", 2);
        testi("A??A", 6);
        testi("A??C", 7);
    }

    @Test(timeout=1000)
    public void suuri1() {
        testi("?A???C??A?", 1260);
    }
    
    @Test(timeout=1000)
    public void suuri2() {
        testi("T????T????", 4860);
    }

    @Test(timeout=1000)
    public void suuri3() {
        testi("A?A?A?A?A?", 243);
    }

    @Test(timeout=1000)
    public void suuri4() {
        testi("AC???T??G?", 420);
    }
    
    
}
