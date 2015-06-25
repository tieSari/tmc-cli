import java.util.*;
import org.junit.Test;

import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Arrays;

@Points("03.4")
public class MainTest {
    public void pieniTesti(int[] tulot, int[] lahdot, int tulos) {
        String sisalto1 = Arrays.toString(tulot);
        String sisalto2 = Arrays.toString(lahdot);
        int uusi = Main.ennatys(tulot, lahdot);
        assertTrue("Kun tuloajat ovat " + sisalto1 + " ja lähtöajat ovat " + sisalto2 + ", " +
                   "ennätyksen tulisi olla " + tulos + ", mutta metodisi palauttaa " + uusi + ".",
                   uusi == tulos);
    }

    public void suuriTesti(int[] tulot, int[] lahdot, int tulos) {
        int uusi = Main.ennatys(tulot, lahdot);
        assertTrue("Metodi toimii väärin suurella syötteellä.", uusi == tulos);
    }
    
    @Test(timeout=1000)
    public void esimerkit() {
        pieniTesti(new int[] {3, 4, 5, 9}, new int[] {8, 6, 10, 12}, 3);
        pieniTesti(new int[] {3, 2, 10, 1}, new int[] {8, 9, 20, 5}, 3);
        pieniTesti(new int[] {1, 3, 5}, new int[] {2, 4, 6}, 1);
        pieniTesti(new int[] {100, 999}, new int[] {1000, 1001}, 2);
    }

    @Test(timeout=1000)
    public void pienet() {
        pieniTesti(new int[] {1}, new int[] {2}, 1);
        pieniTesti(new int[] {1, 2}, new int[] {3, 4}, 2);
        pieniTesti(new int[] {1, 3}, new int[] {2, 4}, 1);
        pieniTesti(new int[] {1, 2, 3}, new int[] {4, 5, 6}, 3);
        pieniTesti(new int[] {1, 3, 4}, new int[] {2, 5, 6}, 2);
        
        pieniTesti(new int[] {1, 1000000000}, new int[] {999999999, 1000000001}, 1);
        pieniTesti(new int[] {1, 999999999}, new int[] {1000000000, 1000000001}, 2);
        
        pieniTesti(new int[] {2, 9, 1, 5},
                   new int[] {4, 11, 12, 7}, 2);
        pieniTesti(new int[] {6, 2, 9, 1, 5},
                   new int[] {10, 4, 11, 12, 7}, 3);
        pieniTesti(new int[] {1, 3, 5, 6, 7},
                   new int[] {2, 4, 8, 9, 10}, 3);
        pieniTesti(new int[] {1, 3, 5, 6, 7},
                   new int[] {11, 4, 8, 9, 10}, 4);
        pieniTesti(new int[] {1, 2, 3, 4, 5},
                   new int[] {6, 7, 8, 9, 10}, 5);
        pieniTesti(new int[] {1, 2, 3, 4, 5},
                   new int[] {10, 9, 8, 7, 6}, 5);
    }
    
    @Test(timeout=1000)
    public void suuri1() {
        int n = 100000;
        int[] tulot = new int[n];
        int[] lahdot = new int[n];
        for (int i = 0; i < n; i++) {
            tulot[i] = 2*i+1;
            lahdot[i] = 2*i+2;
        }
        suuriTesti(tulot, lahdot, 1);
    }

    @Test(timeout=1000)
    public void suuri2() {
        int n = 100000;
        int[] tulot = new int[n];
        int[] lahdot = new int[n];
        for (int i = 0; i < n; i++) {
            tulot[i] = i+1;
            lahdot[i] = n+i+1;
        }
        suuriTesti(tulot, lahdot, 100000);        
    }    
    
    @Test(timeout=1000)
    public void suuri3() {
        int n = 100000;
        int[] tulot = new int[n];
        int[] lahdot = new int[n];
        for (int i = 0; i < n; i++) {
            tulot[i] = 2*i+1;
            lahdot[i] = 24*i+2;
        }
        suuriTesti(tulot, lahdot, 91666);        
    }        

    @Test(timeout=1000)
    public void suuri4() {
        int n = 100000;
        int[] tulot = new int[n];
        int[] lahdot = new int[n];
        for (int i = 0; i < n; i++) {
            tulot[i] = 4*i+1;
            lahdot[i] = 18*i+2;
        }
        suuriTesti(tulot, lahdot, 77778);        
    }        
    
}
