import org.junit.Test;

import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;

@Points("1.4")
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
        testaaPieni("ABCBXA", true, "ABCBA");
        testaaPieni("ABCBAX", true, "ABCBA");
        testaaPieni("ABCXBA", true, "ABCBA");
        testaaPieni("ABCDE", false, "");
        testaaPieni("BAAAAC", false, "");
    }
    
    @Test(timeout=1000)
    public void pienet() {
        testaaPieni("XABBA", true, "ABBA");
        testaaPieni("AXBBA", true, "ABBA");
        testaaPieni("ABXBA", true, "ABBA");
        testaaPieni("ABBXA", true, "ABBA");
        testaaPieni("ABBAX", true, "ABBA");

        testaaPieni("XABABA", true, "ABABA");
        testaaPieni("AXBABA", true, "ABABA");
        testaaPieni("ABXABA", true, "ABABA");
        testaaPieni("ABAXBA", true, "ABABA");
        testaaPieni("ABABXA", true, "ABABA");
        testaaPieni("ABABAX", true, "ABABA");
        
        testaaPieni("AA", true, "A");
        testaaPieni("AAA", true, "AA");
        testaaPieni("AAAA", true, "AAA");
        testaaPieni("AAAAA", true, "AAAA");
        testaaPieni("AAAAAA", true, "AAAAA");

        testaaPieni("AB", true, "A");
        testaaPieni("AAB", true, "AA");
        testaaPieni("AAAB", true, "AAA");
        testaaPieni("AAAAB", true, "AAAA");
        testaaPieni("BA", true, "A");
        testaaPieni("BAA", true, "AA");
        testaaPieni("BAAA", true, "AAA");
        testaaPieni("BAAAA", true, "AAAA");

        testaaPieni("AXAYA", false, "");
        testaaPieni("ABC", false, "");
        testaaPieni("AAAXAAAXA", false, "");
        testaaPieni("AAAXYZAAA", false, "");
        testaaPieni("AAAXYZAAAA", false, "");
        testaaPieni("AAAAXYZAAA", false, "");
    }
    
    @Test(timeout=1000)
    public void suuri1() {
        int n = 100000;
        char[] t = new char[n];
        for (int i = 0; i < n; i++) t[i] = 'A';
        testaaSuuri(new String(t), true);
    }

    @Test(timeout=1000)
    public void suuri2() {
        int n = 100000;
        char[] t = new char[n];
        for (int i = 0; i < n; i++) t[i] = 'A';
        t[12345] = 'B';
        testaaSuuri(new String(t), true);
    }

    @Test(timeout=1000)
    public void suuri3() {
        int n = 100000;
        char[] t = new char[n];
        for (int i = 0; i < n; i++) t[i] = 'A';
        t[12345] = 'B';
        t[54321] = 'C';
        testaaSuuri(new String(t), false);
    }
    
}
