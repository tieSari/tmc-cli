import java.util.*;
import org.junit.Test;

import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Arrays;

@Points("3.3")
public class MainTest {
    public void testi(int x, int tulos) {
        int uusi = Main.nopanheitot(x);
        assertTrue("Kun x on " + x + ", heittosarjoja tulisi olla " + tulos +
                   ", mutta metodisi palauttaa " + uusi + ".", uusi == tulos);
    }
    
    @Test(timeout=1000)
    public void esimerkit() {
        testi(4, 8);
        testi(2, 2);
        testi(3, 4);
        testi(1, 1);
        testi(12, 1936);
    }
    
    @Test(timeout=1000)
    public void pienet() {
        testi(5, 16);
        testi(6, 32);
        testi(7, 63);
        testi(8, 125);
        testi(9, 248);
        testi(10, 492);
        testi(11, 976);
    }    

    @Test(timeout=1000)
    public void suuri1() {
        testi(18, 117920);
    }    

    @Test(timeout=1000)
    public void suuri2() {
        testi(20, 463968);
    }    
    
    
}
