import org.junit.Test;

import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;

@Points("1.3")
public class MainTest {
    public void testaaPieni(String mjono, boolean tulos, String malli) {
        if (tulos) {
            assertTrue("Merkkijonosta " + mjono + " saa palindromin " + malli +
                       ", mutta metodisi palauttaa 'false'.",
                    Main.melkeinPalindromi(mjono) == tulos);
        } else {
            assertTrue("Merkkijonosta " + mjono + " ei saa palindromia" +
                       ", mutta metodisi palauttaa 'true'.",
                    Main.melkeinPalindromi(mjono) == tulos);
        }
    }

    public void testaaSuuri(String mjono, boolean tulos) {
        assertTrue("Metodisi toimii väärin suurella syötteellä.",
                   Main.melkeinPalindromi(mjono) == tulos);
    }
    
    @Test(timeout=1000)
    public void esimerkit() {
        testaaPieni("AABB", true, "ABBA");
        testaaPieni("ABBBB", true, "BBABB");
        testaaPieni("ABCBA", true, "ABCBA");
        testaaPieni("ABBB", false, "");
        testaaPieni("AABCD", false, "");
    }

    @Test(timeout=1000)
    public void pienet() {
        testaaPieni("A", true, "A");
        testaaPieni("AA", true, "AA");
        testaaPieni("AB", false, "");
        testaaPieni("AAA", true, "AAA");
        testaaPieni("AAB", true, "ABA");
        testaaPieni("ABB", true, "BAB");
        testaaPieni("ADDBEAEB", true, "ADBEEBDA");
        testaaPieni("ADDBEAEC", false, "");
        testaaPieni("ADDBEAEBB", true, "ADBEBEBDA");
        testaaPieni("ADDXEAEBB", true, "ADBEXEBDA");
        testaaPieni("ADDXEAEBY", false, "");
        testaaPieni("MAGMGHHGMAMMGM", true, "MAGHMGMMGMHGAM");
        testaaPieni("MAGMGHHGMAAMMGM", true, "MAGHMGMAMGMHGAM");
        testaaPieni("MAGMGHHGMCBMMGM", false, "");
    }
    
    @Test(timeout=1000)
    public void suuri1() {
        int n = 100000;
        char[] t = new char[n];
        for (int i = 0; i < n; i++) t[i] = (char)('A'+i%4);
        testaaSuuri(new String(t), true);
    }

    @Test(timeout=1000)
    public void suuri2() {
        int n = 100000;
        char[] t = new char[n];
        for (int i = 0; i < n; i++) t[i] = (char)('A'+i%4);
        t[12345] = 'X';
        testaaSuuri(new String(t), false);
    }    
}