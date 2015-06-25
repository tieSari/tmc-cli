import java.util.*;
import org.junit.Test;

import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Arrays;

@Points("2.5")
public class MainTest {
    public void testi(int x, long tulos) {
        long uusi = Main.nopanheitot(x);
        assertTrue("Kun x on " + x + ", heittosarjoja tulisi olla " + tulos +
                   ", mutta metodisi palauttaa " + uusi + ".", uusi == tulos);
    }
    
    @Test(timeout=1000)
    public void esimerkit() {
        testi(4, 8L);
        testi(30, 437513522L);
        testi(44, 6386990736226L);
        testi(39, 207991012832L);
        testi(60, 366861197229128136L);
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

    @Test(timeout=1000)
    public void suuri3() {
        testi(50, 389043663364337L);
    }    
    
    @Test(timeout=1000)
    public void suuri4() {
        testi(55, 11946757891421696L);
    }        
    
}
