
import clicker.kayttoliittyma.Main;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import junit.framework.Assert;
import org.fest.swing.core.ComponentMatcher;
import org.fest.swing.core.matcher.JLabelMatcher;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiTask;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.fest.swing.launcher.ApplicationLauncher;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("146.3")
public class XKayttoliittymaTest extends FestSwingJUnitTestCase {

    private static String NAPIN_TEKSTI = "Click!";
    private FrameFixture frame;

    static class M implements ComponentMatcher {

        public final String pattern;

        public M(String p) {
            pattern = p;
        }

        @Override
        public boolean matches(Component cmpnt) {
            if (!(cmpnt instanceof AbstractButton)) {
                return false;
            }
            AbstractButton ab = (AbstractButton) cmpnt;
            return ab.getText().matches(pattern);
        }

        @Override
        public String toString() {
            return "M{" + "pattern=" + pattern + '}';
        }
    }

    public Component varmista(String teksti, Class tyyppi) {
        Component c = null;
        try {
            c = frame.robot.finder().find(new XKayttoliittymaTest.M("(?i).*" + teksti + ".*"));
        } catch (ComponentLookupException e) {
            fail("Ei löytynyt" + tyyppi.toString().replaceAll("class", "") + "-tyyppistä komponenttia, jossa lukisi \"" + teksti + "\".\n\nLisätietoja:\n" + e);
        }
        assertEquals("Komponentti, jossa lukee \"" + teksti + "\" ei ole oikeaa tyyppiä!",
                tyyppi,
                c.getClass());
        return c;
    }

    @Override
    protected void onSetUp() {
        ApplicationLauncher.application(Main.class).start();
        robot().settings().delayBetweenEvents(100);
        frame = WindowFinder.findFrame(JFrame.class).using(robot());

        Dimension d = frame.component().getSize();
        Assert.assertTrue("Aseta JFramen leveydeksi vähintään 200 ja korkeudeksi vähintään 100", d.height > 70 && d.width > 150);

    }

    @Test
    public void onKomponentit() {
        JLabelFixture teksti = null;
        try {
            teksti = frame.label(JLabelMatcher.withText("0"));
        } catch (ComponentLookupException e) {
            Assert.fail("Käyttöliittymässä ei ollut aluksi tekstikenttää arvolla 0. Varmista että käyttöliittymässä on JLabel-komponentti, jonka arvo on 0.\n\n\nLisätietoja:\n" + e);
        }

        varmista("click", JButton.class);

        System.out.println("");
    }

    @Test
    public void onkoKaikkiNakyvissa() {
        JFrame jf = frame.targetCastedTo(JFrame.class);
        Component[] cs = jf.getContentPane().getComponents();
        for (Component component : cs) {
            Point p = component.getLocation();
            int h = component.getHeight();
            int w = component.getWidth();
            Rectangle rect = component.getBounds();
            
            assertFalse("Komponentti "+component.getClass()+" ei ole näkyvissä\n"
                    + "muistahan lisätessä asettaa sille sijainnin tyyliin\n"
                    + "container.add(komponentti, BorderLayout.NORTH);\n"
                    + "nappi ja label tulee asettaa eri sijainteihin BorderLayout.NORTH ja BorderLayout.SOUTH"
                    + "", h==0 || w==0);
        }
    }

    @Test
    public void painetaanNappiaKerran() {
        JLabelFixture teksti = frame.label();
        String tekstiEnnen = teksti.text();
        Assert.assertEquals("Tekstikentän arvon tulee olla alussa \"0\".", "0", tekstiEnnen);

        final JButton nappi = (JButton) varmista("click", JButton.class);
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                nappi.doClick();
            }
        });

        String tekstiJalkeen = frame.label().text();
        Assert.assertEquals("Tekstikentän arvon tulee olla napin painamisen jälkeen \"1\".", "1", tekstiJalkeen);
    }

    @Test
    public void painetaanNappiaKolmesti() {
        JLabelFixture teksti = frame.label();
        String tekstiEnnen = teksti.text();
        Assert.assertEquals("Tekstikentän arvon tulee olla alussa \"0\".", "0", tekstiEnnen);

        final JButton nappi = (JButton) varmista("click", JButton.class);
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                nappi.doClick();
                nappi.doClick();
                nappi.doClick();
            }
        });

        String tekstiJalkeen = frame.label().text();
        Assert.assertEquals("Tekstikentän arvon tulee olla \"3\" kun nappia painetaan kolme kertaa.", "3", tekstiJalkeen);
    }
}
