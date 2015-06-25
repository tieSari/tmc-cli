
import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.*;
import static org.junit.Assert.*;

@Points("50")
public class EnsimmaisetKirjaimetTest {

    @Rule
    public MockStdio io = new MockStdio();

    public void testaa(String sana, String odotettu, String tulos) {
        assertTrue("Syötteellä \"" + sana + "\" ohjelmasti tulostuksessa pitäisi olla \""
                + odotettu + "\"", tulos.toLowerCase().replace(" ", "").contains(odotettu.toLowerCase().replace(" ", "")));
    }

    @Test
    public void eikaiPoikkeusta() throws Exception {
        io.setSysIn("pekka\n");
        try {
            EnsimmaisetKirjaimet.main(new String[0]);
        } catch (Exception e) {
            String v = "\n\npaina nappia show backtrace, virheen syy löytyy hieman alempaa kohdasta "
                    + "\"Caused by\"\n"
                    + "klikkaamalla pääset suoraan virheen aiheuttaneelle koodiriville";

            throw new Exception("syötteellä \"Pekka\" " + v, e);
        }
    }

    @Test
    public void testi1() {
        String sana = "Pekka";

        io.setSysIn(sana);
        EnsimmaisetKirjaimet.main(new String[0]);
        String tulos = io.getSysOut();

        String odotettu = "1. kirjain: P";
        testaa(sana, odotettu, tulos);
        odotettu = "2. kirjain: e";
        testaa(sana, odotettu, tulos);
        odotettu = "3. kirjain: k";
        testaa(sana, odotettu, tulos);
    }

    @Test
    public void testi2() {
        String sana = "Liisa";

        io.setSysIn(sana);
        EnsimmaisetKirjaimet.main(new String[0]);
        String tulos = io.getSysOut();

        String odotettu = "1. kirjain: L";
        testaa(sana, odotettu, tulos);
        odotettu = "2. kirjain: i";
        testaa(sana, odotettu, tulos);
        odotettu = "3. kirjain: i";
        testaa(sana, odotettu, tulos);
    }

    @Test
    public void testi3() {
        String sana = "Esko";

        io.setSysIn(sana);
        EnsimmaisetKirjaimet.main(new String[0]);
        String tulos = io.getSysOut();

        String odotettu = "1.kirjain:E";
        testaa(sana, odotettu, tulos);
        odotettu = "2. kirjain: s";
        testaa(sana, odotettu, tulos);
        odotettu = "3. kirjain: k";
        testaa(sana, odotettu, tulos);

    }

    @Test
    public void testi4() {
        String sana = "Me";

        io.setSysIn(sana);

        try {
            EnsimmaisetKirjaimet.main(new String[0]);
        } catch (Exception e) {
            fail("muista varautua tilanteeseen jossa syötetyn nimen pituus vähemmän kuin 3 kirjainta!");
        }

        String tulos = io.getSysOut();

        String odottamaton = "irjain";
        assertFalse("Syötteellä \"" + sana + "\" ohjelmasti ei pitäisi tulostaa yhtään kirjainta", tulos.contains(odottamaton));
    }
}
