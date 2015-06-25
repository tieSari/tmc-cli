import org.junit.Test;

import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Arrays;

@Points("03.1")
public class MainTest {
    public void pieniTesti(int[] luvut, int tulos) {
        String sisalto = Arrays.toString(luvut);
        int uusi = Main.pieninEtaisyys(luvut);
        assertTrue("Taulukossa " + sisalto + " pienin etäisyys on " + tulos +
                   ", mutta metodisi palauttaa etäisyyden " + uusi + ".", uusi == tulos);
    }

    public void suuriTesti(int[] luvut, int tulos) {
        int uusi = Main.pieninEtaisyys(luvut);
        assertTrue("Metodi toimii väärin suurella syötteellä.", uusi == tulos);
    }
    
    @Test(timeout=1000)
    public void esimerkit() {
        pieniTesti(new int[] {4, 1, 6, 2}, 1);
        pieniTesti(new int[] {3, 1, 5, 1}, 0);
        pieniTesti(new int[] {2, 60}, 58);
        pieniTesti(new int[] {-1000, 1001, 0}, 1000);
    }

    @Test(timeout=1000)
    public void pienet() {
        pieniTesti(new int[] {1, 1}, 0);
        pieniTesti(new int[] {1, 3}, 2);
        pieniTesti(new int[] {3, 1}, 2);
        
        pieniTesti(new int[] {1, 1, 1}, 0);
        pieniTesti(new int[] {1, 1, 2}, 0);
        pieniTesti(new int[] {1, 2, 1}, 0);
        pieniTesti(new int[] {2, 1, 1}, 0);        
        
        pieniTesti(new int[] {1, 2, 6}, 1);
        pieniTesti(new int[] {1, 6, 2}, 1);
        pieniTesti(new int[] {2, 1, 6}, 1);
        pieniTesti(new int[] {2, 6, 1}, 1);
        pieniTesti(new int[] {6, 1, 2}, 1);
        pieniTesti(new int[] {6, 2, 1}, 1);

        pieniTesti(new int[] {1000000000, -1000000000}, 2000000000);
        
        pieniTesti(new int[] {5, 2, 9, 7}, 2);
        pieniTesti(new int[] {1, 4, 7, 10}, 3);
        pieniTesti(new int[] {10, 7, 4, 1}, 3);
        pieniTesti(new int[] {3, 1000, 5, 3}, 0);        
    }
    
    @Test(timeout=1000)
    public void suuri1() {
        int n = 100000;
        int[] luvut = new int[n];
        long x = 12345;
        long a = 113678239;
        long b = 936667127;
        for (int i = 0; i < n; i++) {
            x = (x*a)%b;
            luvut[i] = (int)x;
        }
        suuriTesti(luvut, 0);
    }

    @Test(timeout=1000)
    public void suuri2() {
        int n = 100000;
        int[] luvut = new int[n];
        for (int i = 0; i < n; i++) {
            luvut[i] = i*1000;
        }
        suuriTesti(luvut, 1000);
    }
}
