import org.junit.Test;

import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Arrays;

@Points("1.5")
public class MainTest {
    public void pieniTesti(String alku, String loppu) {
        String uusi = Main.jarjestaMerkit(alku);
        assertTrue("Merkkijonon " + alku + " oikea järjestys on " + loppu +
                   ", mutta metodisi antaa järjestyksen " + uusi + ".", uusi.equals(loppu));
    }

    public void suuriTesti(String alku, String loppu) {
        String uusi = Main.jarjestaMerkit(alku);
        assertTrue("Metodisi toimii väärin suurella syötteellä.", uusi.equals(loppu));
    }
    
    @Test(timeout=1000)
    public void esimerkit() {
        pieniTesti("AAABBB", "ABABAB");
        pieniTesti("CBAED", "ABCDE");
        pieniTesti("AXAXA", "AXAXA");
        pieniTesti("CBAXXXX", "XAXBXCX");
    }

    @Test(timeout=1000)
    public void pienet() {
        pieniTesti("A", "A");
        pieniTesti("AB", "AB");
        pieniTesti("BA", "AB");

        pieniTesti("AAAACCC", "ACACACA");
        pieniTesti("AAAACCCC", "ACACACAC");
        pieniTesti("AAAACCCCC", "CACACACAC");
        
        pieniTesti("HABC", "ABCH");
        pieniTesti("HHABC", "ABHCH");
        pieniTesti("HHHABC", "AHBHCH");
        pieniTesti("HHHHABC", "HAHBHCH");        
        
        pieniTesti("AAABBBCCCCCCCDDDDD", "ABABACBCDCDCDCDCDC");
        pieniTesti("AAABBBCCCCCDDDDDDD", "ABABADBDCDCDCDCDCD");

        pieniTesti("AAABBBCCCXXX", "ABABABCXCXCX");
        pieniTesti("AAABBBCCCXXXX", "ABABABXCXCXCX");
        pieniTesti("AAABBBCCCXXXXX", "ABABAXBXCXCXCX");
        pieniTesti("AAABBBCCCXXXXXX", "ABABXAXBXCXCXCX");    
    }
    
    @Test(timeout=1000)
    public void suuri1() {
        int n = 100000;
        char[] t = new char[n];
        for (int i = 0; i < n; i++) t[i] = (char)('A'+(i%2));
        suuriTesti(new String(t), new String(t));
    }

    @Test(timeout=1000)
    public void suuri2() {
        int n = 100000;
        char[] t = new char[n];
        for (int i = 0; i < n; i++) t[i] = (char)('A'+(i%2));
        char[] u = new char[n];
        for (int i = 0; i < n/2; i++) u[i] = 'A';
        for (int i = n/2; i < n; i++) u[i] = 'B';
        suuriTesti(new String(u), new String(t));
    }

    @Test(timeout=1000)
    public void suuri3() {
        int n = 99999;
        char[] t = new char[n];
        char[] u = new char[n];
        for (int i = 0; i < n; i++) {
            if (i%2 == 0) t[i] = 'X';
            else if (i < n/2) t[i] = 'A';
            else t[i] = 'B';
            u[i] = t[i];
        }
        Arrays.sort(u);
        suuriTesti(new String(u), new String(t));
    }    
    
    @Test(timeout=1000)
    public void bonus() {
        pieniTesti("BDBCDCAAAC", "ABABACDCDC");
        pieniTesti("CDABCADBDB", "ABABCDBDCD");
        pieniTesti("BADDDABDCA", "ABADADBDCD");
        pieniTesti("BCBBACDCBC", "ABCBCBCBCD");
        pieniTesti("DBCAAADCCD", "ABACADCDCD");
        pieniTesti("BDCDBCCAAB", "ABABCBCDCD");
        pieniTesti("ACCBBAAADA", "ABABACACAD");
        pieniTesti("CADDACDBBC", "ABABCDCDCD");
        pieniTesti("CBCABBADAC", "ABABACBCDC");
        pieniTesti("AADDBCBDBD", "ABADBDBDCD");
        pieniTesti("CADAACBDBA", "ABABACADCD");
        pieniTesti("BADACCCAAA", "ABACACACAD");
        pieniTesti("BDDACDACDB", "ABADBDCDCD");
        pieniTesti("BBAACADADA", "ABABACADAD");
        pieniTesti("BABBADCCCC", "ABACBCBCDC");
        pieniTesti("BACCCDAABA", "ABABACACDC");
        pieniTesti("DBACCBDBDB", "ABCBCDBDBD");
        pieniTesti("CBBADDDACB", "ABABCDBDCD");
        pieniTesti("BCDCDDAAAA", "ABACADADCD");
        pieniTesti("DCCADCBDCA", "ABACDCDCDC");
        pieniTesti("DCCBBCCBBC", "BCBCBCBCDC");
        pieniTesti("CAAACABADD", "ABACACADAD");
        pieniTesti("ACCADBBABB", "ABABABCBCD");
        pieniTesti("BDABBAACAC", "ABABABACDC");
        pieniTesti("BCADADAABA", "ABABACADAD");
        pieniTesti("CDBCBACAAA", "ABABACACDC");
        pieniTesti("CBCACACDDD", "ABACDCDCDC");
        pieniTesti("BADDDDCCAC", "ABADCDCDCD");
        pieniTesti("ABBDBBBCAC", "ABABCBCBDB");
        pieniTesti("CCBAADCDBB", "ABABCBCDCD");
        pieniTesti("CBDCAACCDA", "ABACACDCDC");
        pieniTesti("ADAADBABBC", "ABABABADCD");
        pieniTesti("BCADCACDDC", "ABACDCDCDC");
        pieniTesti("DBCADDADBD", "ADADBDBDCD");
        pieniTesti("ADBACBACDD", "ABABADCDCD");
        pieniTesti("AACACADCBD", "ABACACADCD");
        pieniTesti("CDCADACABC", "ABACACDCDC");
        pieniTesti("BCCCDBBBCA", "ABCBCBCBCD");
        pieniTesti("DCADABDADA", "ABADADADCD");
        pieniTesti("CCDACBBADD", "ABABCDCDCD");
        pieniTesti("CCBADBDAAD", "ABABADCDCD");
        pieniTesti("BCDDDBCAAC", "ABABCDCDCD");
        pieniTesti("DBBDDBCCAC", "ABCBCDBDCD");
        pieniTesti("DBADBCBAAC", "ABABABCDCD");
        pieniTesti("CABADDBBAC", "ABABABCDCD");
        pieniTesti("BDDBDCAABD", "ABADBDBDCD");
        pieniTesti("DCADDCADBC", "ABADCDCDCD");
        pieniTesti("CBBAAADDAA", "ABABACADAD");
        pieniTesti("CCBACAAAAC", "ABACACACAC");
        pieniTesti("ADADDBDDCA", "ADADADBDCD");        
    }
    
}
