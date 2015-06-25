import org.junit.Test;

import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Arrays;

@Points("2.1")
public class MainTest {
    public void testi(int n, int tulos) {
        int uusi = Main.ketjumaara(n);
        assertTrue("Kun pituus on " + n + ", ketjuja tulisi olla " +
                   tulos + ", mutta metodisi palauttaa " + uusi + ".",
                   uusi == tulos);
    }
    
    @Test(timeout=1000)
    public void esimerkit() {
        testi(3, 36);
        testi(1, 4);
        testi(2, 12);
        testi(5, 324);
    }

    @Test(timeout=1000)
    public void pituus6() {
        testi(6, 972);
    }    

    @Test(timeout=1000)
    public void pituus7() {
        testi(7, 2916);
    }    

    @Test(timeout=1000)
    public void pituus8() {
        testi(8, 8748);
    }    

    @Test(timeout=1000)
    public void pituus9() {
        testi(9, 26244);
    }    

    @Test(timeout=1000)
    public void pituus10() {
        testi(10, 78732);
    }    
    
    
    
    
}
