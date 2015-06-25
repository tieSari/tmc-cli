
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import org.junit.*;
import static org.junit.Assert.*;

public class NumerotiedusteluTest {

    String klassName = "Paaohjelma";
    Reflex.ClassRef<Object> klass;
    MockInOut mio;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void luokkaJulkinen() {
        assertTrue("Luokan " + klassName + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    /*
     * osa 1
     */
    @Points("133.1")
    @Test
    public void tulostaaMenunJaLopettaa() throws Throwable {
        String syote = "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String tuloste = mio.getOutput();
        String[] rivit = tuloste.split("\n");

        String rivi = "1 lisää numero";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "2 hae numerot";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "3 hae puhelinnumeroa vastaava henkilö";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "x lopeta";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.1")
    @Test
    public void numeronLisays() throws Throwable {
        String syote = "1\npekka\n040-12345\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "kenelle";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "numero";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.1")
    @Test
    public void numeronLisaysKahdelleHenkilolle() throws Throwable {
        String syote = "1\npekka\n040-12345\n1\nmikko\n040-34343\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
    }

    @Points("133.1")
    @Test
    public void kahdenNumeronLisaysYhdelle() throws Throwable {
        String syote = "1\npekka\n040-12345\n1\npekka\n040-34343\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
    }

    @Points("133.1")
    @Test
    public void lisatynNumeronHaku() throws Throwable {
        String syote = "1\npekka\n040-12345\n2\npekka\nx\n";
        mio = new MockInOut(syote);
        assertEquals(0, mio.getOutput().length());
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "kenen";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.1")
    @Test
    public void kahdenLisatynNumeronHaku() throws Throwable {
        String syote = "1\npekka\n040-12345\n1\npekka\n040-43212\n2\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "040-43212";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.1")
    @Test
    public void eiYlimaaraista() throws Throwable {
        String syote = "1\npekka\n040-12345\n1\njukka\n040-11111\n1\npekka\n040-43212\n2\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "040-43212";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-11111";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.1")
    @Test
    public void olematonEiLoydy() throws Throwable {
        String syote = "1\npekka\n040-12345\n1\njukka\n040-11111\n1\npekka\n040-43212\n2\narto\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "040-43212";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-11111";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "ei löytynyt";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    /*
     * 
     */
    
    @Points("133.2")
    @Test
    public void lisatynHenkilonHaku() throws Throwable {
        String syote = "1\npekka\n040-12345\n3\n040-12345\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "numero";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.2")
    @Test
    public void kaksinumeroisenHakuMolemmillaNumeroilla1() throws Throwable {
        String syote = "1\npekka\n040-12345\n1\njukka\n040-11111\n1\npekka\n040-43212\n3\n040-12345\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "numero";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-11111";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-43212";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.2")
    @Test
    public void kaksinumeroisenHakuMolemmillaNumeroilla2() throws Throwable {
        String syote = "1\npekka\n040-12345\n1\njukka\n040-11111\n1\npekka\n040-43212\n3\n040-43212\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "numero";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    /*
     * osa 2
     */
    @Points("133.3")
    @Test
    public void tulostaaMenunJaLopettaaOsa2() throws Throwable {
        String syote = "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String tuloste = mio.getOutput();
        String[] rivit = tuloste.split("\n");

        String rivi = "4 lisää osoite";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "5 hae henkilön tiedot";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.3")
    @Test
    public void osotteenLisays() throws Throwable {
        String syote = "4\npekka\nMannerheimintie\nhelsinki\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "kenelle";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "katu";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "kaupunki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.3")
    @Test
    public void osotteenHaku() throws Throwable {
        String syote = "4\npekka\nmannerheimintie\nhelsinki\n5\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "ei puhelinta";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.3")
    @Test
    public void osoitteettomanTietojenHaku() throws Throwable {
        String syote = "1\npekka\n09-12345\n4\nantti\nmannerheimintie\nhelsinki\n5\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "mannerheimintie";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "ei puhelinta";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "osoite ei tiedossa";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.3")
    @Test
    public void numeroOsoitteelliselle() throws Throwable {
        String syote = "4\npekka\nmannerheimintie\nhelsinki\n1\npekka\n09-12345\n5\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "ei puhelinta";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.3")
    @Test
    public void numeroOsoitteelliselle2() throws Throwable {
        String syote = "4\npekka\nmannerheimintie\nhelsinki\n1\npekka\n09-12345\n2\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.3")
    @Test
    public void osoiteNumerolliselle() throws Throwable {
        String syote = "1\npekka\n09-12345\n4\npekka\nmannerheimintie\nhelsinki\n5\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "ei puhelinta";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.3")
    @Test
    public void olematontaEiTunneta() throws Throwable {
        String syote = "1\npekka\n09-12345\n4\npekka\nmannerheimintie\nhelsinki\n5\nseppo\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "mannerheimintie";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "ei puhelinta";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "osoite ei tiedossa";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "ei löytynyt";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    /*
     * osa 3
     */
    @Points("133.4")
    @Test
    public void tulostaaMenunJaLopettaaOsa3() throws Throwable {
        String syote = "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String tuloste = mio.getOutput();
        String[] rivit = tuloste.split("\n");

        String rivi = "6 poista henkilön tiedot";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.4")
    @Test
    public void poisto() throws Throwable {
        String syote = "4\npekka\nMannerheimintie\nhelsinki\n6\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "kenet";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.4")
    @Test
    public void poistettuEiLoydy() throws Throwable {
        String syote = "4\npekka\nMannerheimintie\nhelsinki\n6\npekka\n5\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "helsinki";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "ei löytynyt";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.4")
    @Test
    public void olemattomanPoisto() throws Throwable {
        String syote = "4\npekka\nMannerheimintie\nhelsinki\n6\njukka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "kenet";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.4")
    @Test
    public void poistoJaHaut1() throws Throwable {
        String syote = "1\njukka\n02-212121\n4\npekka\nmannerheimintie\nhelsinki\n1\npekka\n09-12345\n5\npekka\n6\npekka\n"
                + "2\n09-12345\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "ei löytynyt";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.4")
    @Test
    public void poistoJaHaut2() throws Throwable {
        String syote = "1\njukka\n02-212121\n4\npekka\nmannerheimintie\nhelsinki\n1\npekka\n09-12345\n1\npekka\n09-54321\n6\npekka\n"
                + "2\n09-54321\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "ei löytynyt";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.4")
    @Test
    public void poistoJaHaut3() throws Throwable {
        String syote = "1\njukka\n02-212121\n4\npekka\nmannerheimintie\nhelsinki\n1\npekka\n09-12345\n1\npekka\n09-54321\n6\npekka\n"
                + "5\npekka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "ei löytynyt";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.4")
    @Test
    public void poistoJaHaut4() throws Throwable {
        String syote = "1\njukka\n02-212121\n4\npekka\nmannerheimintie\nhelsinki\n1\npekka\n09-12345\n1\npekka\n09-54321\n6\npekka\n"
                + "5\njukka\nx\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "ei löytynyt";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    /*
     * osa 4
     */
    @Points("133.5")
    @Test
    public void tulostaaMenunJaLopettaaOsa4() throws Throwable {
        String syote = "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String tuloste = mio.getOutput();
        String[] rivit = tuloste.split("\n");

        String rivi = "6 poista henkilön tiedot";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "7 filtteröity listaus";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.5")
    @Test
    public void filtteroituHaku1() throws Throwable {
        String syote = ""
                + "1\njukka\n02-212121\n"
                + "4\npekka\nmannerheimintie\nhelsinki\n"
                + "1\npekka\n09-12345\n"
                + "1\npekka\n09-54321\n"
                + "7\njukka\n"
                + "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "ei löytynyt";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "osoite ei tiedossa";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.5")
    @Test
    public void filtteroituHaku2() throws Throwable {
        String syote = ""
                + "1\njukka\n02-212121\n"
                + "4\npekka\nmannerheimintie\nhelsinki\n"
                + "1\npekka\n09-12345\n"
                + "1\npekka\n09-54321\n"
                + "7\npekka\n"
                + "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "ei löytynyt";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "osoite ei tiedossa";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.5")
    @Test
    public void filtteroituHaku3() throws Throwable {
        String syote = ""
                + "1\njukka\n02-212121\n"
                + "4\npekka\nmannerheimintie\nhelsinki\n"
                + "1\npekka\n09-12345\n"
                + "1\npekka\n09-54321\n"
                + "7\nseppo\n"
                + "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "ei löytynyt";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "osoite ei tiedossa";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.5")
    @Test
    public void filtteroituHaku4() throws Throwable {
        String syote = ""
                + "1\njukka\n02-212121\n"
                + "4\npekka\nmannerheimintie\nhelsinki\n"
                + "1\npekka\n09-12345\n"
                + "1\npekka\n09-54321\n"
                + "7\nhelsinki\n"
                + "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "ei löytynyt";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "osoite ei tiedossa";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
    }

    @Points("133.5")
    @Test
    public void filtteroituHaku5() throws Throwable {
        String syote = ""
                + "1\njukka\n02-212121\n"
                + "4\npekka\nmannerheimintie\nhelsinki\n"
                + "1\npekka\n09-12345\n"
                + "1\npekka\n09-54321\n"
                + "7\nkk\n"
                + "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "ei löytynyt";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "osoite ei tiedossa";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));

        int pekka = nro(rivit, "pekka");
        int jukka = nro(rivit, "jukka");
        assertTrue("tarkasta että tulostus on esimerkin mukainen, kun syöte on " + toS(syote) + " "
                + "pitäisi jukan tietojen olla ennen pekan tietoja", jukka < pekka);
        pekka = nro(rivit, "pekka");
        jukka = nro(rivit, "02-21212");
        assertTrue("tarkasta että tulostus on esimerkin mukainen, kun syöte on " + toS(syote) + " "
                + "pitäisi jukan tietojen olla ennen pekan tietoja", jukka < pekka);
        pekka = nro(rivit, "helsinki");
        jukka = nro(rivit, "02-21212");
        assertTrue("tarkasta että tulostus on esimerkin mukainen, kun syöte on " + toS(syote) + " "
                + "pitäisi jukan tietojen olla ennen pekan tietoja", jukka < pekka);
    }

    @Points("133.5")
    @Test
    public void filtteroituHaku6() throws Throwable {
        String syote = ""
                + "1\njukka\n02-212121\n"
                + "4\npekka\nmannerheimintie\nhelsinki\n"
                + "1\npekka\n09-12345\n"
                + "1\npekka\n09-54321\n"
                + "1\nantti\n040-111222\n"
                + "7\nkk\n"
                + "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "ei löytynyt";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "osoite ei tiedossa";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "antti";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-111222";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));

        int pekka = nro(rivit, "pekka");
        int jukka = nro(rivit, "jukka");

        assertTrue("tarkasta että tulostus on esimerkin mukainen, kun syöte on " + toS(syote) + " "
                + "pitäisi jukan tietojen olla ennen pekan tietoja", jukka < pekka);
        pekka = nro(rivit, "pekka");
        jukka = nro(rivit, "02-21212");
        assertTrue("tarkasta että tulostus on esimerkin mukainen, kun syöte on " + toS(syote) + " "
                + "pitäisi jukan tietojen olla ennen pekan tietoja", jukka < pekka);
        pekka = nro(rivit, "helsinki");
        jukka = nro(rivit, "02-21212");
        assertTrue("tarkasta että tulostus on esimerkin mukainen, kun syöte on " + toS(syote) + " "
                + "pitäisi jukan tietojen olla ennen pekan tietoja", jukka < pekka);
    }

    @Points("133.5")
    @Test
    public void filtteroituHaku7() throws Throwable {
        String syote = ""
                + "1\njukka\n02-212121\n"
                + "4\npekka\nmannerheimintie\nhelsinki\n"
                + "1\npekka\n09-12345\n"
                + "1\npekka\n09-54321\n"
                + "1\nantti\n040-111222\n"
                + "7\na\n"
                + "x\n";
        mio = new MockInOut(syote);
        doStuff(syote);
        String[] rivit = mio.getOutput().split("\n");

        String rivi = "ei löytynyt";
        assertFalse(viestiEi(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "02-212121";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "osoite ei tiedossa";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "mannerheimintie";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "helsinki";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-12345";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "09-54321";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "jukka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "pekka";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "antti";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));
        rivi = "040-111222";
        assertTrue(viesti(rivi, syote), sisaltaa(rivit, rivi));

        int pekka = nro(rivit, "pekka");
        int jukka = nro(rivit, "jukka");
        int antti = nro(rivit, "antti");

        assertTrue("tarkasta että tulostus on esimerkin mukainen"
                + "\nkun syöte on " + toS(syote) + " "
                + "\npitäisi antin tietojen olla ennen jukan tietoja", antti < jukka);
        assertTrue("tarkasta että tulostus on esimerkin mukainen\n"
                + "kun syöte on " + toS(syote) + " "
                + "\npitäisi jukan tietojen olla ennen pekan tietoja", jukka < pekka);
        pekka = nro(rivit, "pekka");
        jukka = nro(rivit, "02-21212");
        assertTrue("tarkasta että tulostus on esimerkin mukainen\n"
                + "kun syöte on " + toS(syote) + " "
                + "\npitäisi jukan tietojen olla ennen pekan tietoja", jukka < pekka);
        pekka = nro(rivit, "helsinki");
        jukka = nro(rivit, "02-21212");
        assertTrue("tarkasta että tulostus on esimerkin mukainen, kun syöte on " + toS(syote) + " "
                + "\npitäisi jukan tietojen olla ennen pekan tietoja", jukka < pekka);
    }
    /*
     * filtteröity lista
     */
    /*
     * helpers
     */

    private String viesti(String rivi, String syote) {
        return "tarkasta että tulostus on esimerkin mukainen, "
                + "olisi pitänyt tulostua rivi joka sisältää \"" + rivi + "\"\n"
                + "kun syöte oli " + toS(syote)+"\n"
                + "\nOhjelma tulosti:\n"+mio.getOutput();
    }

    private String viestiEi(String rivi, String syote) {
        return "tarkasta että tulostus on esimerkin mukainen, "
                + "ei olisi saanut tulostua riviä joka sisältää \"" + rivi + "\"\n"
                + "kun syöte oli " + toS(syote)+"\n"
                + "\nOhjelma tulosti:\n"+mio.getOutput();
    }

    private String f(String syote) {
        return "\nkäyttäjän syöte oli:\n" + syote;
    }

    private void suorita(String error) throws Throwable {
        String[] args = new String[0];
        klass.staticMethod("main").
                returningVoid().
                taking(String[].class).withNiceError(error).
                invoke(args);
    }

    private void doStuff(String syote) throws Throwable {


        try {
            suorita(f(syote));
        } catch (Throwable t) {
            if (t.toString().contains("NoSuch")) {
                fail("ohjelmasi suorituksen pitäisi loppua syötteellä " + toS(syote));
            }

            new MockInOut(syote);
            suorita(f(syote));
        }
    }

    private String toS(String syote) {
        return "\n"+syote;
        //return syote.replaceAll("\n", "<enter>");
    }

    private boolean sisaltaa(String[] rivit, String haettava) {
        return hae(rivit, haettava) != null;
    }

    private String hae(String[] rivit, String haettava) {

        for (String rivi : rivit) {
            if (rivi.toLowerCase().contains(haettava.toLowerCase())) {
                return rivi;
            }
        }

        return null;
    }

    private int nro(String[] rivit, String haettava) {
        for (int i = 0; i < rivit.length; i++) {
            String rivi = rivit[i];

            if (rivi.toLowerCase().contains(haettava.toLowerCase())) {
                return i;
            }
        }

        return -1;
    }
}
