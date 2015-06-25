import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.*;
import static org.junit.Assert.*;

@Points("54")
public class LoppuosaTest {
    @Rule
    public MockStdio io = new MockStdio();

    public String sanitize(String s) {
        return s.replaceAll("\r\n", "\n").replaceAll("\r", "\n").replaceAll("\n", " ").replaceAll("  ", " ");
    }
    
    @Test
    public void eikaiPoikkeusta() throws Exception {
        io.setSysIn("Java\n3\n");
        try {
            Loppuosa.main(new String[0]);
        } catch (Exception e) {
            String v = "\n\npaina nappia show backtrace, virheen syy löytyy hieman alempaa kohdasta "
                    + "\"Caused by\"\n"
                    + "klikkaamalla pääset suoraan virheen aiheuttaneelle koodiriville";

            throw new Exception("syötteellä \"Java 3\"" + v, e);
        }
    }    

    @Test
    public void testi1() {
        io.setSysIn("Java\n3\n");
        Loppuosa.main(new String[0]);
        String out = io.getSysOut().replaceAll(" ", "");

        assertTrue("ohjelmasi tulee tulostaa lopuksi \"Tulos: \"", out.contains("Tulos:"));

        assertTrue("Syötteellä Java 3 ohjelmasi tulee tulostaa lopuksi \"Tulos: ava\"", out.contains("Tulos:ava"));
        assertFalse("Syötteellä Java 3 ohjelmasi tulee tulostaa lopuksi \"Tulos: ava\"", out.contains("Tulos:Java"));
    }
 
    @Test
    public void testi2() {
        io.setSysIn("Ohjelmointi\n5\n");
        Loppuosa.main(new String[0]);
        String out = io.getSysOut().replaceAll(" ", "");

        assertTrue("ohjelmasi tulee tulostaa lopuksi \"Tulos: \"", out.contains("Tulos:"));

        assertTrue("Syötteellä Ohjelmointi 5 ohjelmasi tulee tulostaa lopuksi "
                + "\"Tulos: ointi\"", out.contains("Tulos:ointi"));
        assertFalse("Syötteellä Ohjelmointi 5 ohjelmasi tulee tulostaa lopuksi "
                + "\"Tulos: ointi\"", out.contains("Tulos:mointi"));
    }    
    
    
    @Test
    public void testi3() {
        io.setSysIn("Tietojenkasittelytiede\n14\n");
        Loppuosa.main(new String[0]);
        String out = io.getSysOut().replaceAll(" ", "");;

        assertTrue("ohjelmasi tulee tulostaa lopuksi \"Tulos: \"", out.contains("Tulos:"));

        assertTrue("Syötteellä Tietojenkäsittelytiede 14 ohjelmasi tulee tulostaa lopuksi "
                + "\"Tulos: kasittelytiede\"", out.contains("Tulos:kasittelytiede"));
        assertFalse("Syötteellä Tietojenkäsittelytiede 14 ohjelmasi tulee tulostaa lopuksi "
                + "\"Tulos: kasittelytiede\"", out.contains("Tulos:nkasittelytiede"));
    }      
}

