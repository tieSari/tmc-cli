import org.junit.Test;

import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Arrays;

@Points("03.3")
public class MainTest {
    public void pieniTesti(int[] luvut, long tulos) {
        String sisalto = Arrays.toString(luvut);
        long uusi = Main.pieninKasvatus(luvut);
        assertTrue("Taulukossa " + sisalto + " pienin kasvatus on " + tulos +
                   ", mutta metodisi palauttaa tuloksen " + uusi + ".", uusi == tulos);
    }

    public void suuriTesti(int[] luvut, long tulos) {
        long uusi = Main.pieninKasvatus(luvut);
        assertTrue("Metodi toimii väärin suurella syötteellä.", uusi == tulos);
    }
    
    @Test(timeout=1000)
    public void esimerkit() {
        pieniTesti(new int[] {2, 3, 2, 3}, 4);
        pieniTesti(new int[] {2, 2, 2, 2}, 6);
        pieniTesti(new int[] {2, 4, 1, 7}, 0);
        pieniTesti(new int[] {3, 1, 2, 1, 1}, 7);
    }

    @Test(timeout=1000)
    public void pienet() {
        pieniTesti(new int[] {5}, 0);
        pieniTesti(new int[] {1, 2}, 0);
        pieniTesti(new int[] {2, 1}, 0);
        pieniTesti(new int[] {1, 1}, 1);
        pieniTesti(new int[] {1, 2, 3}, 0);
        pieniTesti(new int[] {1, 3, 2}, 0);
        pieniTesti(new int[] {2, 1, 3}, 0);
        pieniTesti(new int[] {2, 3, 1}, 0);
        pieniTesti(new int[] {3, 1, 2}, 0);
        pieniTesti(new int[] {3, 2, 1}, 0);
        pieniTesti(new int[] {1, 2, 2}, 1);
        pieniTesti(new int[] {2, 2, 1}, 1);
        pieniTesti(new int[] {2, 2, 2}, 3);
        
        pieniTesti(new int[] {-1000000000, 0, 1000000000}, 0);
        pieniTesti(new int[] {1000000000, 1000000000, 1000000000}, 3);
                
        pieniTesti(new int[] {3, 1, 1, 4, 2, 2, 2}, 13);
        pieniTesti(new int[] {5, 1, 1, 1, 1, 2, 1}, 16);
        pieniTesti(new int[] {2, 2, 2, 3, 2, 2, 4}, 18);
    }
    
    @Test(timeout=1000)
    public void suuri1() {
        int n = 100000;
        int[] luvut = new int[n];
        for (int i = 0; i < n; i++) {
            luvut[i] = 1;
        }
        suuriTesti(luvut, 4999950000L);
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
        suuriTesti(luvut, 25030);
    }    
    
}
