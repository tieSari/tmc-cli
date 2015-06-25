import org.junit.Test;

import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;

@Points("1.2")
public class MainTest {

    public void testaaPieni(String mjono, int tulos) {
        int uusi = Main.pisinOsuus(mjono);
        assertTrue("Merkkijonossa " + mjono + " pisimmän osuuden pituus on " +
                   tulos + ", mutta metodisi palauttaa " + uusi + ".",uusi == tulos);
    }

    public void testaaSuuri(String mjono, int tulos) {
        int uusi = Main.pisinOsuus(mjono);
        assertTrue("Metodisi toimii väärin suurella syötteellä.", uusi == tulos);
    }
    
    
    @Test(timeout=1000)
    public void esimerkit() {
        testaaPieni("AABBBCC", 3);
        testaaPieni("AABBCC", 2);
        testaaPieni("XXXXXXX", 7);
        testaaPieni("AAABBBB", 4);
        testaaPieni("AAAABBB", 4);
        testaaPieni("ABBABBBA", 3);
    }

    @Test(timeout=1000)
    public void pienet() {
        testaaPieni("A", 1);
        testaaPieni("AB", 1);
        testaaPieni("AA", 2);
        testaaPieni("ABCDEF", 1);
        testaaPieni("AAAAAA", 6);
        testaaPieni("AABBCC", 2);
        testaaPieni("ABABCCAB", 2);
        testaaPieni("ABABABCC", 2);
        testaaPieni("CCABABAB", 2);
        testaaPieni("AAABAAA", 3);
        testaaPieni("AAABBAAA", 3);
        testaaPieni("AAABBBAAA", 3);
        testaaPieni("AAABBBBAAA", 4);
        testaaPieni("AABCCCDDEEEEFFGGGH", 4);
    }
    
    @Test(timeout=1000)
    public void suuri1() {
        int n = 100000;
        char[] t = new char[n];
        for (int i = 0; i < n; i++) t[i] = (char)('A'+(i%2));
        testaaSuuri(new String(t), 1);        
    }

    @Test(timeout=1000)
    public void suuri2() {
        int n = 100000;
        char[] t = new char[n];
        for (int i = 0; i < n; i++) t[i] = 'A';
        testaaSuuri(new String(t), n);
    }

    @Test(timeout=1000)
    public void suuri3() {
        int n = 100000;
        int m = 1000;
        char[] t = new char[n];
        for (int i = 0; i < n; i++) t[i] = 'A';
        t[m] = 'B';
        testaaSuuri(new String(t), n-m-1);
    }    
}