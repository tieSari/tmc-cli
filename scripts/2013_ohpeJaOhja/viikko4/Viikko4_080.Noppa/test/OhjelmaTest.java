
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Random;
import org.powermock.modules.junit4.rule.PowerMockRule;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.easymock.EasyMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import static org.powermock.api.easymock.PowerMock.*;
import static org.easymock.EasyMock.*;

@Points("80")
@PrepareForTest({Noppa.class, Ohjelma.class})
public class OhjelmaTest {

    @Rule
    public PowerMockRule p = new PowerMockRule();

    @Test
    public void noppaTarjoaaKaikkiLuvut() {
        int sarmat = new Random().nextInt(15) + 2;
        Noppa n = new Noppa(sarmat);

        int luku = n.heita();
        assertTrue("Kun luodaan Noppa n = new Noppa(" + sarmat + "); ja kutsutaan n.heita(), palautus oli " + luku + " vaikka tuloksen pitäisi olla valillä 1..." + sarmat, luku > 0 && luku <= sarmat);
        int i = 0;
        while (true) {
            int uusi = n.heita();
            if (uusi != luku) {
                break;
            }
            assertTrue("Kun luodaan Noppa n = new Noppa(" + sarmat + "); ja kutsutaan n.heita(), palautus oli " + luku + " vaikka tuloksen pitäisi olla valillä 1..." + sarmat, luku > 0 && luku <= sarmat);

            i++;
            if (i == 20) {
                fail("Noppasi tuottaa jokaisella heitolla saman luvun! Testaa että pääohjelma runko toimii järkevästi");
            }
        }
        int[] luvut = new int[sarmat+1];
        for (int j = 0; j < 1000; j++) {
            luku = n.heita();
            assertTrue("kun luodaan Noppa n = new Noppa(" + sarmat + "); ja kutsutaan n.heita(), palautus oli " + luku + " vaikka tuloksen pitäisi olla valillä 1..." + sarmat, luku > 0 && luku <= sarmat);
            luvut[ luku]++;
        }

        for (int j = 1; j < luvut.length; j++) {
            assertTrue("Toimiiko noppasi oikein? Kun luotiin Noppa n = new Noppa(" + sarmat + "); tuhannella heitolla lukua " + j + " oli vain " + luvut[j] + " kappaletta.", luvut[j] > 10);
        }
    }
}
