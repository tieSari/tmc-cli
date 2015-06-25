import java.util.*;
import org.junit.Test;

import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Arrays;

@Points("3.4")
public class MainTest {
    public void testi(int omenat[], int tulos) {
        String sisalto = Arrays.toString(omenat);
        int uusi = Main.jaaOmenat(omenat);        
        assertTrue("Kun omenat ovat " + sisalto + ", parhaassa jakotavassa " +
                   "ero on " + tulos + ", mutta metodisi antaa " + uusi + ".", tulos == uusi);
    }
    
    @Test(timeout=1000)
    public void esimerkit() {
        testi(new int[] {5, 3, 6, 2, 9}, 1);
        testi(new int[] {2, 2}, 0);
        testi(new int[] {999}, 999);
        testi(new int[] {999, 1, 1, 1}, 996);
    }
    
    @Test(timeout=1000)
    public void osa1() {
        testi(new int[] {1}, 1);
        testi(new int[] {1, 1}, 0);
        testi(new int[] {1, 1, 1}, 1);
        testi(new int[] {1, 1, 1, 1}, 0);
        testi(new int[] {1, 1, 1, 1, 1}, 1);
        testi(new int[] {1, 1, 1, 1, 1, 1}, 0);
    }         

    @Test(timeout=1000)
    public void osa2() {
        testi(new int[] {1, 2}, 1);
        testi(new int[] {1, 2, 3}, 0);
        testi(new int[] {1, 2, 3, 4}, 0);
        testi(new int[] {1, 2, 3, 4, 5}, 1);
        testi(new int[] {1, 2, 3, 4, 5, 6}, 1);
        testi(new int[] {20, 15, 15}, 10);
        testi(new int[] {1, 1000}, 999);
        testi(new int[] {1000, 1000}, 0);
        testi(new int[] {1000, 999, 999}, 998);
    }
    
    @Test(timeout=1000)
    public void suuri1() {    
        testi(new int[] {460, 731, 780, 421, 236, 912, 572, 192, 802,
                         187, 772, 391, 388, 10, 663}, 1);
    }
    
    @Test(timeout=1000)
    public void suuri2() {    
        testi(new int[] {696, 397, 401, 58, 30, 159, 323, 58, 305, 399,
                         368, 689, 947, 405, 887}, 0);
    }

    @Test(timeout=1000)
    public void suuri3() {    
        testi(new int[] {777, 777, 777, 333, 333, 333, 333, 333, 333,
                         333, 333, 333, 333, 333, 333}, 111);
    }
    
    
}
