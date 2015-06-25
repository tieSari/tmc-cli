
import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.*;
import static org.junit.Assert.*;

@Points("52")
public class NimenKaantaminenTest {

    @Rule
    public MockStdio io = new MockStdio();

    @Test
    public void eikaiPoikkeusta() throws Exception {
        io.setSysIn("pekka\n");
        try {
            NimenKaantaminen.main(new String[0]);
        } catch (Exception e) {
            String v = "\n\npaina nappia show backtrace, virheen syy löytyy hieman alempaa kohdasta "
                + "\"Caused by\"\n" 
                + "klikkaamalla pääset suoraan virheen aiheuttaneelle koodiriville" ;

            throw new Exception("syötteellä \"Pekka\" "+v, e);
        }
    }

    @Test
    public void pekkaVaarinPain() {
        testaa("Pekka", "akkeP");
    }

    @Test
    public void katariinaVaarinPain() {
        testaa("Katariina", "aniirataK");
    }

    @Test
    public void saippuakauppiasVaarinpain() {
        testaa("saippuakauppias", "saippuakauppias");
    }

    @Test
    public void yksiMerkkinen() {
        testaa("m", "m");
    }

    @Test
    public void kaksiMerkkinen() {
        testaa("Oa", "aO");
    }

    @Test
    public void kolmeMerkkinen() {
        testaa("jIk", "kIj");
    }

    @Test
    public void todellaPitkaStringEsitys() {
        testaa("apfjviaweojmfviaowfjisadfklnrnwaieraisdf",
                "fdsiareiawnrnlkfdasijfwoaivfmjoewaivjfpa");
    }

    @Test
    public void ujoPoju() {
        testaa("ujoPoju", "ujoPoju");
    }

    @Test
    public void aattonaJanottaa() {
        testaa("aattonaJanottaa", "aattonaJanottaa");
    }

    private void testaa(String alkup, String oletettu) {
        io.setSysIn(alkup + "\n");
        NimenKaantaminen.main(new String[0]);
        assertTrue(getViesti(alkup, oletettu),
                io.getSysOut().contains(oletettu));
    }

    private String getViesti(String alkup, String oletettu) {
        return "Tarkasta että syöttellä " + alkup
                + " ohjelma tulostaa " + oletettu;
    }
}
