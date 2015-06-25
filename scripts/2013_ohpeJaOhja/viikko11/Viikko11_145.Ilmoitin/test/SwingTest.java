
import fi.helsinki.cs.tmc.edutestutils.Points;
import ilmoitin.Main;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.fest.swing.launcher.ApplicationLauncher;
import org.junit.Test;
import junit.framework.Assert;
import org.fest.swing.core.ComponentMatcher;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import static org.junit.Assert.*;

@Points("145")
public class SwingTest extends FestSwingJUnitTestCase {

    private FrameFixture frame;

    @Override
    protected void onSetUp() {
        try {
            ApplicationLauncher.application(Main.class).start();

            robot().settings().delayBetweenEvents(300);
            frame = WindowFinder.findFrame(JFrame.class).using(robot());
        } catch (Throwable t) {
            Assert.fail("Luothan JFramen run-metodissa frame = new JFrame(\"Frame\") ja asetathan sen näkyväksi metodilla setVisible.\n\n\nVirheen lisätietoja: " + t);
        }

        Dimension d = frame.component().getSize();
        Assert.assertTrue("Aseta JFramen leveydeksi vähintään 400 ja korkeudeksi vähintään 200", d.height > 150 && d.width > 350);

    }

    @Test
    public void layoutKunnossa() {
        LayoutManager lm = ((JFrame) frame.component()).getContentPane().getLayout();

        Assert.assertTrue("Käyttöliittymän asettelijan pitäisi olla GridLayout", lm instanceof GridLayout);

        GridLayout gl = (GridLayout) lm;

        Assert.assertEquals("Käyttöliittymän asettelijana olevan GridLayoutin rivimäärä on väärä",
                3, gl.getRows());

        Assert.assertEquals("Käyttöliittymän asettelijana olevan GridLayoutin sarakemäärä on väärä",
                1, gl.getColumns());
    }

    @Test
    public void oikeanNiminenNappi() {
        varmista("Päivitä", JButton.class);
        try {
            frame.label();
            frame.textBox();
        } catch (Throwable t) {
            Assert.fail("Onhan käyttöliittymässäsi kaikki komponentit? Pitäisi olla"
                    + " JLabel, JTextField ja JButton.");
        }
    }

    @Test
    public void testaaJarjestys() {
        JFrame jf = frame.targetCastedTo(JFrame.class);
        Component[] cs = jf.getContentPane().getComponents();
        assertEquals("Ensimmäisen komponentin tulisi olla JTextField.",
                JTextField.class,
                cs[0].getClass());
        assertEquals("Toisen komponentin tulisi olla JButton.",
                JButton.class,
                cs[1].getClass());
        assertEquals("Kolmannen komponentin tulisi olla JLabel.",
                JLabel.class,
                cs[2].getClass());
    }

    @Test
    public void testataanViestinnayttoaEkallaJaVahennetaanVihranPalinTuloAikaa() {
        yksiMerkkiJono("testi");
    }

    @Test
    public void testaaViestinLahetysta() {
        yksiMerkkiJono("asd");
        yksiMerkkiJono("Peruna");
        yksiMerkkiJono("isoviha");
        yksiMerkkiJono("punainen");
        yksiMerkkiJono("stone");
        yksiMerkkiJono("karpaatti");
        yksiMerkkiJono("MOOC");
        yksiMerkkiJono("on");
        yksiMerkkiJono("paras");
        yksiMerkkiJono("kthxbye");
        yksiMerkkiJono("ja vielä pikkujen ääkkösiä");

    }

    public void yksiMerkkiJono(String expected) {
        JLabelFixture messageLabel = null;
        JTextComponentFixture syote = null;
        JButtonFixture paivita = null;
        String received = "";
        try {
            messageLabel = frame.label();
            syote = frame.textBox();
            paivita = frame.button();
        } catch (Throwable t) {
            Assert.fail("Onhan käyttöliittymässäsi kaikki komponentit? Pitäisi olla"
                    + " JLabel, JTextField ja JButton.");
        }
        try {
            syote.focus().setText(expected);
            paivita.focus().click();
            received = messageLabel.text();
            messageLabel.requireText(expected);
            syote.requireEmpty();
        } catch (Throwable t) {
            Assert.fail("Asetathan luomasi JLabel-kentän tekstiksi JTextField-kentän tekstin kun JButton-nappia painetaan?\n"
                    + "Tyhjennäthän JTextFieldin samalla, eli asetat sen sisällöksi merkkijonon \"\"?\n"
                    + "\nJButtonille on luotava tapahtumankäsittelijä, joka suorittaa tekstin kopioinnin JLabeliin ja JTextField:in tyhjennyksen"
                    + "\n\n\nLisätietoja: " + t);
        }
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
            c = frame.robot.finder().find(new SwingTest.M("(?i).*" + teksti + ".*"));
        } catch (ComponentLookupException e) {
            Assert.fail("Ei löytynyt" + tyyppi.toString().replaceAll("class", "") + "-tyyppistä komponenttia, jossa lukisi \"" + teksti + "\".\n\nLisätietoja:\n" + e);
        }
        Assert.assertEquals("Komponentti, jossa lukee \"" + teksti + "\" ei ole oikeaa tyyppiä!",
                tyyppi,
                c.getClass());
        return c;
    }
}
