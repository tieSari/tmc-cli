
import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.awt.Point;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.*;
import robotti.Ohjain;

@Points("24.2")
public class B_RatkaiseTest {

    @Rule
    public MockStdio io = new MockStdio();

    @Test
    public void ratkaisuKunLaatikkoSamallaPaikalla() {
        testMode();
        String input = "ratkaise\nsammuta\n";
        io.setSysIn(input);
        try {
            Paaohjelma.main(null);
        } catch (NoSuchElementException e) {
            Assert.fail("Varmista että poistut toistolauseesta kun komentoa \"sammuta\" kutsutaan. Virhe: " + e.getMessage());
        }
        input = input.replaceAll("\n", " ");

        List<String> komennot = komennot();
        if (komennot == null || komennot.size() < 5) {
            Assert.fail("Kun syötetään komennot " + input + ", robotin tulee liikkua siten, että se työntää laatikon kohdealueelle.");
        }

        Assert.assertTrue("Komennon " + input + ", tulee ohjata robotti työntämään laatikko kohdealueelle.", Ohjain.laatikkoX() == Ohjain.tavoiteX() && Ohjain.laatikkoY() == Ohjain.tavoiteY() && Ohjain.laatikkoX() >= 0 && Ohjain.laatikkoY() >= 0);
    }

    private List<String> komennot() {
        try {
            Method m = Ohjain.class.getDeclaredMethod("getKomennot");
            m.setAccessible(true);
            return (List<String>) m.invoke(null);
        } catch (Throwable e) {
        }

        return null;
    }

    private List<Point> getSijainnit() {
        try {
            Method m = Ohjain.class.getDeclaredMethod("getSijainnit");
            m.setAccessible(true);
            return (List<Point>) m.invoke(null);
        } catch (Throwable e) {
        }
        return null;
    }

    private void testMode() {
        try {
            Method m = Ohjain.class.getDeclaredMethod("testMode");
            m.setAccessible(true);
            m.invoke(null);
        } catch (Throwable e) {
            throw new Error("Jotain meni pieleen!", e);
        }
        
        setAlaArvoSijaintia();
    }
    
    
    private void setAlaArvoSijaintia() {
        try {
            Method m = Ohjain.class.getDeclaredMethod("alaArvoSijaintia", boolean.class);
            m.setAccessible(true);
            m.invoke(null, true);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
