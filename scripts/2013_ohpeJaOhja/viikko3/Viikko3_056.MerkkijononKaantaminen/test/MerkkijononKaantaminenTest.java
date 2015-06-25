
import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.*;
import static org.junit.Assert.*;

@Points("56")
public class MerkkijononKaantaminenTest {

    @Test
    public void eikaiPoikkeusta() throws Exception {
        String kaannettava = "java";
        try {
            MerkkijononKaantaminen.kaanna(kaannettava);
        } catch (Exception e) {
            String v = "\n\npaina nappia show backtrace, virheen syy löytyy hieman alempaa kohdasta "
                    + "\"Caused by\"\n"
                    + "klikkaamalla pääset suoraan virheen aiheuttaneelle koodiriville";

            throw new Exception("syötteellä \"Java\"" + v, e);
        }
    }

    @Test
    public void eikaiPoikkeusta2() throws Exception {
        String kaannettava = "ohjelmointi";
        try {
            MerkkijononKaantaminen.kaanna(kaannettava);
        } catch (Exception e) {
            String v = "\n\npaina nappia show backtrace, virheen syy löytyy hieman alempaa kohdasta "
                    + "\"Caused by\"\n"
                    + "klikkaamalla pääset suoraan virheen aiheuttaneelle koodiriville";

            throw new Exception("syötteellä \"ohjelmointi\"" + v, e);
        }
    }    
    
    @Test
    public void testi1() {
        String kaannettava = "java";
        String odotettu = "avaj";
        String vastaus = MerkkijononKaantaminen.kaanna(kaannettava);

        assertEquals("metodi kaanna palauttaa parametrilla \"" + kaannettava + "\" ", odotettu, vastaus);
    }

    @Test
    public void testi2() {
        String kaannettava = "ohjelmointi";
        String odotettu = "itniomlejho";
        String vastaus = MerkkijononKaantaminen.kaanna(kaannettava);

        assertEquals("metodi kaanna palauttaa parametrilla \"" + kaannettava + "\" ", odotettu, vastaus);
    }

    @Test
    public void testi3() {
        String kaannettava = "sijoituslause";
        String odotettu = "esualsutiojis";
        String vastaus = MerkkijononKaantaminen.kaanna(kaannettava);

        assertEquals("metodi kaanna palauttaa parametrilla \"" + kaannettava + "\" ", odotettu, vastaus);
    }

    @Test
    public void testi4() {
        String kaannettava = "saippuakauppias";
        String odotettu = "saippuakauppias";
        String vastaus = MerkkijononKaantaminen.kaanna(kaannettava);

        assertEquals("metodi kaanna palauttaa parametrilla \"" + kaannettava + "\" ", odotettu, vastaus);
    }
}
