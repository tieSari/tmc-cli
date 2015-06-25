
import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.NoSuchElementException;
import org.junit.*;
import static org.junit.Assert.*;

@Points("51")
public class KirjaimetErikseenTest {

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
            KirjaimetErikseen.main(new String[0]);
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
        KirjaimetErikseen.main(new String[0]);
        String tulos = io.getSysOut();

        String odotettu = "1. kirjain: P";
        testaa(sana, odotettu, tulos);
        odotettu = "2. kirjain: e";
        testaa(sana, odotettu, tulos);
        odotettu = "3. kirjain: k";
        testaa(sana, odotettu, tulos);
        odotettu = "4. kirjain: k";
        testaa(sana, odotettu, tulos);
        odotettu = "5. kirjain: a";
        testaa(sana, odotettu, tulos);
    }

    @Test
    public void testi2() {
        String sana = "Liisa";

        io.setSysIn(sana);
        KirjaimetErikseen.main(new String[0]);
        String tulos = io.getSysOut();

        String odotettu = "1. kirjain: L";
        testaa(sana, odotettu, tulos);
        odotettu = "2. kirjain: i";
        testaa(sana, odotettu, tulos);
        odotettu = "3. kirjain: i";
        testaa(sana, odotettu, tulos);
        odotettu = "4. kirjain: s";
        testaa(sana, odotettu, tulos);
        odotettu = "5. kirjain: a";
        testaa(sana, odotettu, tulos);
    }

    @Test
    public void testi3() {
        String sana = "Esko";

        io.setSysIn(sana);
        KirjaimetErikseen.main(new String[0]);
        String tulos = io.getSysOut();

        String odotettu = "1. kirjain: E";
        testaa(sana, odotettu, tulos);
        odotettu = "2. kirjain: s";
        testaa(sana, odotettu, tulos);
        odotettu = "3. kirjain: k";
        testaa(sana, odotettu, tulos);
        odotettu = "4. kirjain: o";
        testaa(sana, odotettu, tulos);
    }

    @Test
    public void testi4() {
        String sana = "Jo";

        io.setSysIn(sana);
        KirjaimetErikseen.main(new String[0]);
        String tulos = io.getSysOut();

        String odotettu = "1. kirjain: J";
        testaa(sana, odotettu, tulos);
        odotettu = "2. kirjain: o";
        testaa(sana, odotettu, tulos);
    }
}
