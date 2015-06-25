import fi.helsinki.cs.tmc.edutestutils.Points;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashSet;

@Points("81")
public class OhjelmaTest {
    String merkit = "abcdefghijklmnopqrstuvwxyz";

    @Test
    public void testaaSalasananMerkit() {
        SalasananArpoja arpoja = new SalasananArpoja(13);
        for (int i = 0; i < 10; i++) {
            String sala = arpoja.luoSalasana();
            for (char c : sala.toCharArray()) {
                assertTrue("Salasanassasi \"" + sala + "\" oleva merkki '" +
                        c + "' ei ole välillä a-z!", merkit.indexOf(c) != -1);
            }
        }
    }

    @Test
    public void testaaSalasananPituus() {
        SalasananArpoja arpoja = new SalasananArpoja(13);
        for (int i = 0; i < 10; i++) {
            String sala = arpoja.luoSalasana();
            assertEquals("Salasanasi \"" + sala + "\" pituus on väärä!",
                    13, sala.length());
        }
    }

    @Test
    public void testaaSalasananSatunnaisuus() {
        HashSet<String> hs = new HashSet<String>();
        HashSet<Character> cs = new HashSet<Character>();
        SalasananArpoja arpoja = new SalasananArpoja(13);
        for (int i = 0; i < 1000; i++) {
            String sala = arpoja.luoSalasana();
            hs.add(sala);
            for (char c : sala.toCharArray()) {
                cs.add(c);
            }
        }

        int k = hs.size();
        assertTrue("1000:sta salasanasta löytyi vain " + k +
                " eri salasanaa! Ei kauhean satunnaista!", k > 900);
        int ck = cs.size();
        assertTrue("1000:sta salasanasta löytyi vain " + ck +
                " eri merkkiä! Ei tarpeeksi satunnaista!", ck > 20);
    }

    @Test
    public void testaaVaihtelevaSalasananPituus() throws Exception {
        for (int i = 0; i < 100; i++) {
            SalasananArpoja arpoja = new SalasananArpoja(i);
            String salasana = arpoja.luoSalasana();
            assertTrue("Palautetun salasanan pituus " + salasana.length() +
                    " ei vastannut konstruktorille annettua pituutta: " + i,
                    (i == salasana.length()));
        }
    }
}
