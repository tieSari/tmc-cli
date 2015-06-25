
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.*;
import junit.framework.Assert;
import kysely.Main;
import org.fest.swing.core.ComponentMatcher;
import org.fest.swing.core.matcher.JLabelMatcher;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.fest.swing.launcher.ApplicationLauncher;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("144")
public class KyselyTest extends FestSwingJUnitTestCase {

    private FrameFixture frame;

    @Override
    protected void onSetUp() {
        try {
            ApplicationLauncher.application(Main.class).start();

            robot().settings().delayBetweenEvents(100);

            frame = WindowFinder.findFrame(JFrame.class).using(robot());

        } catch (Throwable t) {
            Assert.fail("Luohan käyttöliittymäsi JFrame-olion ja asetathan sen näkyväksi komennolla frame.setVisible(true)?");
        }
        Dimension d = frame.component().getSize();
        Assert.assertTrue("Aseta JFramen leveydeksi vähintään 100 ja korkeudeksi vähintään 200", d.height > 150 && d.width > 50);

        frame.resizeTo(new Dimension(250, 400));

    }

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
            c = frame.robot.finder().find(new KyselyTest.M("(?i).*" + teksti + ".*"));
        } catch (ComponentLookupException e) {
            fail("Ei löytynyt"+tyyppi.toString().replaceAll("class", "") +"-tyyppistä komponenttia, jossa lukisi \"" + teksti + "\".\n\nLisätietoja:\n" + e);
        }
        assertEquals("Komponentti, jossa lukee \"" + teksti + "\" ei ole oikeaa tyyppiä!",
                tyyppi,
                c.getClass());
        return c;
    }

    @Test
    public void testaaKomponentitLoytyvat() {
        LayoutManager lm = ((JFrame)frame.component()).getContentPane().getLayout();
 
        assertTrue("Käyttöliittymän asettelijan pitäisi olla BoxLayout",lm instanceof BoxLayout);
        
        try {
            JLabelFixture k1 = frame.label(JLabelMatcher.withText("Oletko?"));
        } catch (ComponentLookupException e) {
            fail("Et lisännyt JLabel-komponentteja tekstillä\"Oletko?\".\n\nLisätietoja:\n" + e);
        }

        try {
            JLabelFixture k2 = frame.label(JLabelMatcher.withText("Miksi?"));
        } catch (ComponentLookupException e) {
            fail("Et lisännyt JLabel-komponentteja tekstillä \"Miksi?\".\n\nLisätietoja:\n" + e);
        }        
        
        varmista("kyllä", JCheckBox.class);
        varmista("en", JCheckBox.class);
        varmista("siksi", JRadioButton.class);
        varmista("se on kivaa", JRadioButton.class);
        varmista("valmis", JButton.class);
    }

    @Test
    public void testaaRadioButtonit() {
        JRadioButton r1 = (JRadioButton) varmista("siksi", JRadioButton.class);
        JRadioButton r2 = (JRadioButton) varmista("se on kivaa", JRadioButton.class);

        assertFalse("Alussa minkään vaihtoehdon ei pitäisi olla valittuna!",
                r1.isSelected() || r2.isSelected());

        frame.robot.focus(r1);
        frame.robot.click(r1);
        frame.robot.waitForIdle();
        assertTrue("Vaihtoehdon \"siksi\" pitäisi tulla valituksi kun sitä klikataan. Varmista että käyttöliittymä on niin iso että nappi siksi näkyy!",
                r1.isSelected());
        assertFalse("Vaihtoehdon \"se on kivaa\" ei pitäisi tulla valituksi kun vaihtoehtoa \"siksi\" klikataan",
                r2.isSelected());

        frame.robot.focus(r2);
        frame.robot.click(r2);
        frame.robot.waitForIdle();
        assertTrue("Vaihtoehdon \"se on kivaa\" pitäisi tulla valituksi kun sitä klikataan.",
                r2.isSelected());
        assertFalse("Vaihtoehdon \"siksi\" pitäisi muuttua epävalituksi kun vaihtoehto \"se on kivaa\" valitaan.",
                r1.isSelected());

        frame.robot.focus(r1);
        frame.robot.click(r1);
        frame.robot.waitForIdle();
        assertTrue("Vaihtoehdon \"siksi\" pitäisi tulla valituksi kun sitä klikataan.",
                r1.isSelected());
        assertFalse("Vaihtoehdon \"se on kivaa\" pitäisi muuttua epävalituksi kun vaihtoehto \"siksi\" valitaan.",
                r2.isSelected());

    }

    @Test
    public void testaaJarjestys() {
        JFrame jf = frame.targetCastedTo(JFrame.class);
        assertEquals("Et käytä BoxLayout-asettelijaa!",
                BoxLayout.class,
                jf.getContentPane().getLayout().getClass());
        Component[] cs = jf.getContentPane().getComponents();
        assertEquals("Ensimmäisen komponentin tulisi olla JLabel.",
                JLabel.class,
                cs[0].getClass());
        assertEquals("Toisen komponentin tulisi olla JCheckBox.",
                JCheckBox.class,
                cs[1].getClass());
        assertEquals("Kolmannen komponentin tulisi olla JCheckBox.",
                JCheckBox.class,
                cs[2].getClass());
        assertEquals("Neljännen komponentin tulisi olla JLabel.",
                JLabel.class,
                cs[3].getClass());
        assertEquals("Viidennen komponentin tulisi olla JRadioButton.",
                JRadioButton.class,
                cs[4].getClass());
        assertEquals("Kuudennen komponentin tulisi olla JRadioButton.",
                JRadioButton.class,
                cs[5].getClass());
        assertEquals("Viimeisen komponentin tulisi olla JButton.",
                JButton.class,
                cs[6].getClass());
    }
}
