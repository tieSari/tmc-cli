import org.junit.Test;

import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Arrays;

@Points("2.2")
public class MainTest {
    public void pieniTesti(int[] luvut, int tulos) {
        String sisalto = Arrays.toString(luvut);
        int uusi = Main.yleisinLuku(luvut);
        assertTrue("Taulukossa " + sisalto + " yleisin luku on " + tulos +
                   ", mutta metodisi palauttaa luvun " + uusi + ".", uusi == tulos);
    }

    public void suuriTesti(int[] luvut, int tulos) {
        int uusi = Main.yleisinLuku(luvut);
        assertTrue("Metodi toimii väärin suurella syötteellä.", uusi == tulos);
    }
    
    @Test(timeout=1000)
    public void esimerkit() {
        pieniTesti(new int[] {4, 1, 1, 2, 1}, 1);
        pieniTesti(new int[] {5, 5, 5, 2}, 5);
        pieniTesti(new int[] {5, 5, 5, 5}, 5);
        pieniTesti(new int[] {1, 2, 2}, 2);
    }

    @Test(timeout=1000)
    public void pienet() {
        pieniTesti(new int[] {5}, 5);
        pieniTesti(new int[] {5, 5}, 5);
        pieniTesti(new int[] {5, 5, 5}, 5);
        pieniTesti(new int[] {5, 5, 5, 5}, 5);
        pieniTesti(new int[] {5, 5, 5, 5, 5}, 5);
        
        pieniTesti(new int[] {2, 2, 3}, 2);
        pieniTesti(new int[] {2, 3, 2}, 2);
        pieniTesti(new int[] {3, 2, 2}, 2);

        pieniTesti(new int[] {2, 2, 2, 3}, 2);
        pieniTesti(new int[] {2, 2, 3, 2}, 2);
        pieniTesti(new int[] {2, 3, 2, 2}, 2);
        pieniTesti(new int[] {3, 2, 2, 2}, 2);
    
        pieniTesti(new int[] {2, 2, 2, 2, 3}, 2);
        pieniTesti(new int[] {2, 2, 2, 3, 2}, 2);
        pieniTesti(new int[] {2, 2, 3, 2, 2}, 2);
        pieniTesti(new int[] {2, 3, 2, 2, 2}, 2);
        pieniTesti(new int[] {3, 2, 2, 2, 2}, 2);
        pieniTesti(new int[] {2, 2, 2, 3, 3}, 2);
        pieniTesti(new int[] {2, 2, 3, 2, 3}, 2);
        pieniTesti(new int[] {2, 3, 2, 2, 3}, 2);
        pieniTesti(new int[] {3, 2, 2, 2, 3}, 2);
        pieniTesti(new int[] {2, 2, 3, 3, 2}, 2);
        pieniTesti(new int[] {2, 3, 2, 3, 2}, 2);
        pieniTesti(new int[] {3, 2, 2, 3, 2}, 2);
        pieniTesti(new int[] {2, 3, 3, 2, 2}, 2);
        pieniTesti(new int[] {3, 2, 3, 2, 2}, 2);
        pieniTesti(new int[] {3, 3, 2, 2, 2}, 2);
        
        pieniTesti(new int[] {0}, 0);
        pieniTesti(new int[] {1000000000}, 1000000000);
        pieniTesti(new int[] {-1000000000}, -1000000000);
    }
    
    @Test(timeout=1000)
    public void suuri1() {
        int n = 99999;
        int[] luvut = new int[n];
        int c = 0;
        for (int i = 0; i < n; i++) {
            if (i%2 == 0) luvut[i] = 0;
            else luvut[i] = ++c;
        }
        suuriTesti(luvut, 0);
    }

    @Test(timeout=1000)
    public void suuri2() {
        int n = 99999;
        int[] luvut = new int[n];
        int c = 0;
        for (int i = 0; i < n; i++) {
            if (i%2 == 0) luvut[i] = 0;
            else luvut[i] = 1+(int)(Math.random()*100000000);
        }
        suuriTesti(luvut, 0);
    }
    
    
}
