
import fi.helsinki.cs.tmc.edutestutils.Points;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

@Points("62")
public class PoistaViimeinenTest {

    public ArrayList a(Object... i) {
        ArrayList al = new ArrayList();
        for (Object j : i) {
            al.add(j);
        }
        return al;
    }

    public void test(ArrayList in, ArrayList out) throws Exception {
        String s = in.toString();
        try {
            PoistaViimeinen.poistaViimeinen(in);

        } catch (Exception e) {
            String v = "\n\npaina nappia show backtrace, virheen syy löytyy hieman alempaa kohdasta "
                    + "\"Caused by\"\n"
                    + "klikkaamalla pääset suoraan virheen aiheuttaneelle koodiriville";

            throw new Exception("kutsuttaessa poistaViimeinen listalla "+ in + v, e);

        }
        assertEquals("Listan " + s + " viimeisen poistaminen ei onnistunut!",
                out,
                in);
    }

    @Test
    public void testaaYhdenPituinen() throws Exception {
        test(a("XXX"), a());
        test(a("s"), a());
    }

    @Test
    public void testaaPitempi() throws Exception {
        test(a("X", "Y", "Z"), a("X", "Y"));
        test(a("a", "b", "c", "d", "e", "f", "g", "h"),
                a("a", "b", "c", "d", "e", "f", "g"));
    }
}