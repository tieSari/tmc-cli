import org.junit.Test;

import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Arrays;

@Points("03.5")
public class MainTest {
    public void pieniTesti(int[] luvut, long tulos) {
        String sisalto = Arrays.toString(luvut);
        long uusi = Main.pieninMuutos(luvut);
        assertTrue("Taulukossa " + sisalto + " pienin muutos on " + tulos +
                   ", mutta metodisi palauttaa tuloksen " + uusi + ".", uusi == tulos);
    }

    public void suuriTesti(int[] luvut, long tulos) {
        long uusi = Main.pieninMuutos(luvut);
        assertTrue("Metodi toimii väärin suurella syötteellä.", uusi == tulos);
    }
    
    @Test(timeout=1000)
    public void esimerkit() {
        pieniTesti(new int[] {1, 7, 3, 4}, 7);
        pieniTesti(new int[] {1, 2, 1, 2}, 2);
        pieniTesti(new int[] {7, 7, 7, 7}, 0);
        pieniTesti(new int[] {1, 1, 1, 100, 1}, 99);
    }

    @Test(timeout=1000)
    public void pienet() {
        pieniTesti(new int[] {1}, 0);
        pieniTesti(new int[] {1, 1}, 0);
        pieniTesti(new int[] {1, 2}, 1);
        pieniTesti(new int[] {2, 1}, 1);
        pieniTesti(new int[] {1, 1, 1}, 0);
        pieniTesti(new int[] {1, 1, 2}, 1);
        pieniTesti(new int[] {1, 2, 1}, 1);
        pieniTesti(new int[] {2, 1, 1}, 1);

        pieniTesti(new int[] {1, 2, 3}, 2);
        pieniTesti(new int[] {1, 3, 2}, 2);
        pieniTesti(new int[] {2, 1, 3}, 2);
        pieniTesti(new int[] {2, 3, 1}, 2);
        pieniTesti(new int[] {3, 1, 2}, 2);
        pieniTesti(new int[] {3, 2 ,1}, 2);

        pieniTesti(new int[] {-1000000000, 1000000000}, 2000000000);
        pieniTesti(new int[] {-1000000000, -1000000000,
                               1000000000, 1000000000}, 4000000000L);
        pieniTesti(new int[] {-1000000000, -1000000000, -1000000000,
                               1000000000, 1000000000, 1000000000}, 6000000000L);
        
        pieniTesti(new int[] {1, 2, 1, 1, 3, 2, 3}, 5);
        pieniTesti(new int[] {2, 9, 3, 3, 3, 4, 7}, 12);
        pieniTesti(new int[] {5, 1, 1, 9, 9, 2, 2}, 19);
        pieniTesti(new int[] {1, 9, 1, 6, 7, 8, 1}, 21);
    }
    
    @Test(timeout=1000)
    public void suuri1() {
        int n = 100000;
        int[] luvut = new int[n];
        for (int i = 0; i < n; i++) {
            luvut[i] = 1;
        }
        suuriTesti(luvut, 0);
    }

    @Test(timeout=1000)
    public void suuri2() {
        int n = 100000;
        int[] luvut = new int[n];
        long x = 12345;
        long a = 113678239;
        long b = 936667127;
        for (int i = 0; i < n; i++) {
            x = (x*a)%b;
            luvut[i] = (int)x;
        }
        suuriTesti(luvut, 23425636861815L);
    }    
    
}
