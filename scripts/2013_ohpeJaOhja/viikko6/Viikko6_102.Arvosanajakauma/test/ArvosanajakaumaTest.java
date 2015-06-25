import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Arrays;
import java.util.NoSuchElementException;
import org.junit.*;
import static org.junit.Assert.*;

@Points("102.1 102.2 102.3")
public class ArvosanajakaumaTest {

    @Rule
    public MockStdio io = new MockStdio();

    @Test
    public void lukeeSyotteenOikein() {
        int[][] luvut = {{34}, {11, 55, 52}, {}};
        for (int i = 0; i < luvut.length; i++) {
            testaaSyote(luvut[i]);
        }
    }

    @Test
    public void sisaltaaOikeatTulosrivit() {
        int[][] luvut = {{34}, {11, 55, 52}};
        for (int i = 0; i < luvut.length; i++) {
            testaaTuloste(luvut[i]);
        }
    }

    @Test
    public void testi1() {
        int[] luvut = {34};
        int[] jakauma = {0, 1, 0, 0, 0, 0};
        double pros = 100;
        testaa(luvut, jakauma, pros);
    }

    @Test
    public void testi2() {
        int[] luvut = {36};
        int[] jakauma = {0, 0, 1, 0, 0, 0};
        double pros = 100;
        testaa(luvut, jakauma, pros);
    }

    @Test
    public void testi3() {
        int[] luvut = {44};
        int[] jakauma = {0, 0, 0, 1, 0, 0};
        double pros = 100;
        testaa(luvut, jakauma, pros);
    }

    @Test
    public void testi4() {
        int[] luvut = {46};
        int[] jakauma = {0, 0, 0, 0, 1, 0};
        double pros = 100;
        testaa(luvut, jakauma, pros);
    }

    @Test
    public void testi5() {
        int[] luvut = {54};
        int[] jakauma = {0, 0, 0, 0, 0, 1};
        double pros = 100;
        testaa(luvut, jakauma, pros);
    }


    @Test
    public void testiHylsy() {
        int[] luvut = {4};
        int[] jakauma = {1, 0, 0, 0, 0, 0};
        double pros = 0;
        testaa(luvut, jakauma, pros);
    }

    @Test
    public void testiMonta1() {
        int[] luvut = {34, 39};
        int[] jakauma = {0, 1, 1, 0, 0, 0};
        double pros = 100;
        testaa(luvut, jakauma, pros);
    }

    @Test
    public void testiMonta2() {
        int[] luvut = {3, 59};
        int[] jakauma = {1, 0, 0, 0, 0, 1};
        double pros = 50;
        testaa(luvut, jakauma, pros);
    }

    @Test
    public void testiMonta3() {
        int[] luvut = {44, 12, 58, 29, 60};
        int[] jakauma = {2, 0, 0, 1, 0, 2};
        double pros = 60;
        testaa(luvut, jakauma, pros);
    }

    @Test
    public void testiMonta4() {
        int[] luvut = {44, 12, 58, 34, 46};
        int[] jakauma = {1, 1, 0, 1, 1, 1};
        double pros = 80;
        testaa(luvut, jakauma, pros);
    }

    @Test
    public void testiMonta5() {
        int[] luvut = {34, 41, 53, 36, 55, 27, 43, 35, 40, 11};
        int[] jakauma = {2, 1, 2, 3, 0, 2};
        double pros = 80;
        testaa(luvut, jakauma, pros);
    }

    @Test
    public void vaaraSyote() {
        int[] luvut = {42, 61, 15, -2};
        int[] jakauma = {1, 0, 0, 1, 0, 0};
        double pros = 50;
        try {
            testaa(luvut, jakauma, pros);
        } catch (AssertionError e) {
            fail("Muistathan että syötteet jotka eivät ole välillä 0-60 pitää jättää huomiotta!\n"+e);
        }
    }

    /*
     * helpers
     */
    private void testaa(int[] luvut, int[] jakauma, double pros) {
        io.setSysIn(syote(luvut) + "-1\n");
        Paaohjelma.main(new String[0]);
        String[] rivit = io.getSysOut().split("\n");

        String pros2 = (""+pros).replace('.', ',');
        
        String rivi = haeRivi(rivit, "sentti");
        assertTrue("syötteellä " + toS(luvut) + " hyväksymisprosenti tulisi olla " + pros + ", tulostui: \"" + rivi + "\"", 
                rivi.contains("" + pros) || rivi.contains(pros2));

        assertFalse("tarkasta että ohjelma tulostaa rivin jolla teksti \"Hyväksymisprosentti:\"", rivi == null);
        for (int i = 0; i < 6; i++) {
            rivi = haeRivi(rivit, i + ":");
            tarkastaArvosana(i, jakauma[i], rivi, luvut);
        }
    }

    private void tarkastaArvosana(int i, int odotettu, String rivi, int[] luvut) {
        if (odotettu == 0) {
            assertFalse("syötteellä " + toS(luvut) + " arvosanan " + i + " ilmoittavalla rivillä ei saisi olla tähtiä, "
                    + "nyt tulostui \"" + rivi + "\"", rivi.contains("*"));
            return;
        }

        String tahdet = "";
        for (int j = 0; j < odotettu; j++) {
            tahdet += "*";
        }

        assertTrue("syötteellä " + toS(luvut) + " pitäisi tulostaa rivi \""+i+": "+tahdet
                + "  nyt tulostui \"" + rivi + "\"", rivi.contains(tahdet));
        assertFalse("syötteellä " + toS(luvut) + " pitäisi tulostaa rivi \""+i+": "+tahdet
                + "  nyt tulostui \"" + rivi + "\"", rivi.contains(tahdet+"*"));
    }

    private void testaaTuloste(int[] luvut) {
        io.setSysIn(syote(luvut) + "-1\n");
        Paaohjelma.main(new String[0]);
        String[] rivit = io.getSysOut().split("\n");

        String rivi = haeRivi(rivit, "jakauma");
        assertFalse("tarkasta että ohjelma tulostaa rivin jolla teksti \"Arvosanajakauma:\"", rivi == null);
        rivi = haeRivi(rivit, "sentti");
        assertFalse("tarkasta että ohjelma tulostaa rivin jolla teksti \"Hyväksymisprosentti:\"", rivi == null);
        for (int i = 0; i < 6; i++) {
            rivi = haeRivi(rivit, i + ":");
            assertFalse("tarkasta että ohjelma tulostaa rivin jolla teksti \"" + i + ":\"", rivi == null);
        }
    }

    private void testaaSyote(int[] luvut) {
        io.setSysIn(syote(luvut) + "-1\n");
        try {
            Paaohjelma.main(new String[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("ohjelmasi viittaa taulukon tai listan ulkopuolelle syötteellä " + toS(luvut));
        } catch (NoSuchElementException e) {
            fail("ohjelmasi pitäisi pysähtyä syötteellä " + toS(luvut));
        } catch (ArithmeticException e) {
            if (toS(luvut).equals("-1")) {
                fail("ohjelmasi tekee nollallajaon kun syötteenä on pelkkä -1 eli yhtään numeroa ei anneta."
                        + "\nVika lienee hyväksymisprosentin laskemisessa. Siellä tulee ottaa huomioon tilanne jossa"
                        + "yhtään syötettyä numeroa ei ole");
            } else {
                fail("ohjelmasi tekee nollallajaon syötteellä " + toS(luvut));
            }
        } catch (Exception e) {
            fail("jotain odottamatonta tapahtui syötteellä " + toS(luvut) + " lisätietoa " + e.getMessage());
        }
    }

    private String syote(int[] luvut) {
        String mj = "";

        for (int luku : luvut) {
            mj += luku + "\n";
        }

        return mj;
    }

    private String toS(int[] luvut) {
        if (luvut.length == 0) {
            return "-1";
        }

        return Arrays.toString(luvut).replace("[", "").replace("]", "") + ", -1";
    }

    private String haeRivi(String[] rivit, String haettava) {
        for (String rivi : rivit) {
            if (rivi.contains(haettava)) {
                return rivi;
            }
        }

        return null;
    }
}
