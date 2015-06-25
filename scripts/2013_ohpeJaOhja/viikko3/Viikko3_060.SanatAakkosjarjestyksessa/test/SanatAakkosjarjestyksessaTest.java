
import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.*;
import static org.junit.Assert.*;

@Points("60")
public class SanatAakkosjarjestyksessaTest {

    @Rule
    public MockStdio io = new MockStdio();

    public String sanitize(String s) {
        return s.replaceAll("\r\n", "\n").replaceAll("\r", "\n").replaceAll("\n", " ").replaceAll("  ", " ");
    }

    @Test
    public void testi1a() {
        String[] sanat = {"Java", "Ruby"};
        String vastaus = "Java Ruby";

        io.setSysIn(toS(sanat) + "\n");
        SanatAakkosjarjestyksessa.main(new String[0]);
        String out = sanitize(io.getSysOut());

        assertTrue("syötteellä " + toS2(sanat) + " pitäisi tulostaa " + vastaus, out.contains(vastaus));
    }

    @Test
    public void testi1b() {
        String[] sanat = {"Ruby", "Java"};
        String vastaus = "Java Ruby";

        io.setSysIn(toS(sanat) + "\n");
        SanatAakkosjarjestyksessa.main(new String[0]);
        String out = sanitize(io.getSysOut());

        assertTrue("syötteellä " + toS2(sanat) + " pitäisi tulostaa " + vastaus, out.contains(vastaus));
    }
    
    
    @Test
    public void testi2a() {
        String[] sanat = {"Kahvi", "Maito", "Tee", "Powerking"};
        String vastaus = "Kahvi Maito Powerking Tee";

        io.setSysIn(toS(sanat) + "\n");
        SanatAakkosjarjestyksessa.main(new String[0]);
        String out = sanitize(io.getSysOut());

        assertTrue("syötteellä " + toS2(sanat) + " pitäisi tulostaa " + vastaus, out.contains(vastaus));
    }
    
    @Test
    public void testi2b() {
        String[] sanat = {"Maito", "Tee", "Kahvi", "Powerking"};
        String vastaus = "Kahvi Maito Powerking Tee";

        io.setSysIn(toS(sanat) + "\n");
        SanatAakkosjarjestyksessa.main(new String[0]);
        String out = sanitize(io.getSysOut());

        assertTrue("syötteellä " + toS2(sanat) + " pitäisi tulostaa " + vastaus, out.contains(vastaus));
    }
    
    @Test
    public void testi2c() {
        String[] sanat = { "Powerking", "Tee",  "Kahvi", "Maito"};
        String vastaus = "Kahvi Maito Powerking Tee";

        io.setSysIn(toS(sanat) + "\n");
        SanatAakkosjarjestyksessa.main(new String[0]);
        String out = sanitize(io.getSysOut());

        assertTrue("syötteellä " + toS2(sanat) + " pitäisi tulostaa " + vastaus, out.contains(vastaus));
    }    
    
    @Test
    public void testi2d() {
        String[] sanat = { "Tee", "Powerking", "Maito", "Kahvi"};
        String vastaus = "Kahvi Maito Powerking Tee";

        io.setSysIn(toS(sanat) + "\n");
        SanatAakkosjarjestyksessa.main(new String[0]);
        String out = sanitize(io.getSysOut());

        assertTrue("syötteellä " + toS2(sanat) + " pitäisi tulostaa " + vastaus, out.contains(vastaus));
    }     
    
    public void testi3a() {
        String[] sanat = {"while", "if", "muuttuja", "print", "metodi", "sijoitus", "olio", "luokka", "lista", "taulukko", "pino", "puu"};
        String vastaus = "if lista luokka metodi muuttuja olio pino print puu sijoitus taulukko while";

        io.setSysIn(toS(sanat) + "\n");
        SanatAakkosjarjestyksessa.main(new String[0]);
        String out = sanitize(io.getSysOut());

        assertTrue("syötteellä " + toS2(sanat) + " pitäisi tulostaa " + vastaus, out.contains(vastaus));
    }    

    public void testi3b() {
        String[] sanat = {"if", "print",  "sijoitus", "while" , "olio", "luokka", "lista", "taulukko", "muuttuja", "pino", "puu","metodi"};
        String vastaus = "if lista luokka metodi muuttuja olio pino print puu sijoitus taulukko while";

        io.setSysIn(toS(sanat) + "\n");
        SanatAakkosjarjestyksessa.main(new String[0]);
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
