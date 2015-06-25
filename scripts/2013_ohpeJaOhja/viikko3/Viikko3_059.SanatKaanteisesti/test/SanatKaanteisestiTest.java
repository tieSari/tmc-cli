import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.*;
import static org.junit.Assert.*;

@Points("59")
public class SanatKaanteisestiTest {
    @Rule
    public MockStdio io = new MockStdio();

    public String sanitize(String s) {
        return s.replaceAll("\r\n", "\n").replaceAll("\r", "\n").replaceAll("\n", " ").replaceAll("  ", " ");
    }

    @Test
    public void testi1() {
        String[] sanat = {"Java", "Ruby"};
        String vastaus = "Ruby Java";

        io.setSysIn(toS(sanat) + "\n");
        SanatKaanteisesti.main(new String[0]);
        String out = sanitize(io.getSysOut());

        assertTrue("syötteellä " + toS2(sanat) + " pitäisi tulostaa " + vastaus, out.contains(vastaus));
    }
    
    @Test
    public void testi2() {
        String[] sanat = {"Kahvi", "Maito", "Tee", "Powerking"};
        String vastaus = "Powerking Tee Maito Kahvi";

        io.setSysIn(toS(sanat) + "\n");
        SanatKaanteisesti.main(new String[0]);
        String out = sanitize(io.getSysOut());

        assertTrue("syötteellä " + toS2(sanat) + " pitäisi tulostaa " + vastaus, out.contains(vastaus));
    }
    
    @Test
    public void testi3() {
        String[] sanat = {"while", "if", "muuttuja", "print", "metodi", "sijoitus", "olio", "luokka", "lista", "taulukko", "pino", "puu"};
        String vastaus = "puu pino taulukko lista luokka olio sijoitus metodi print muuttuja if while";

        io.setSysIn(toS(sanat) + "\n");
        SanatKaanteisesti.main(new String[0]);
        String out = sanitize(io.getSysOut());

        assertTrue("syötteellä " + toS2(sanat) + " pitäisi tulostaa " + vastaus, out.contains(vastaus));
    }    

    private static String toS(String[] sanat) {
        String s = "";

        for (String sana : sanat) {
            s += sana + "\n";
        }

        return s;
    }

    private static String toS2(String[] sanat) {
        String s = "";

        for (String sana : sanat) {
            s += sana + " ";
        }

        return s + "\"\"";
    }
}
