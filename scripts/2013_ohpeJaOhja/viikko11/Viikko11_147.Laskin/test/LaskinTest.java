
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.List;
import java.util.Collection;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import junit.framework.Assert;
import org.fest.swing.core.ComponentMatcher;
import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.core.matcher.JLabelMatcher;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiTask;
import org.fest.swing.exception.ComponentLookupException;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.fest.swing.launcher.ApplicationLauncher;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LaskinTest extends FestSwingJUnitTestCase {

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
    }

    // ulkonäkö    
    @Points("147.1")
    @Test
    public void kokoJaOtsikkoOikea() {
        Dimension d = frame.component().getSize();
        Assert.assertTrue("Aseta JFramen leveydeksi vähintään 300 ja korkeudeksi vähintään 150", d.height > 100 && d.width > 200);
        Assert.assertEquals("Aseta JFramelle oikea otsikko", "Laskin", frame.component().getTitle());
    }

    @Points("147.1")
    @Test
    public void pohjanLayoutKunnossa() {
        LayoutManager lm = ((JFrame) frame.component()).getContentPane().getLayout();

        assertTrue("Graafisen laskimen käyttöliittymän asettelijan pitäisi olla GridLayout", lm instanceof GridLayout);

        GridLayout gl = (GridLayout) lm;

        assertEquals("Graafisen laskimen käyttöliittymän asettelijana olevan GridLayoutin rivimäärä on väärä",
                3, gl.getRows());

        assertEquals("Graafisen laskimen käyttöliittymän asettelijana olevan GridLayoutin sarakemäärä on väärä",
                1, gl.getColumns());

        assertTrue("Näppäimet sisältävän JPanelin asettelijan pitäisi olla GridLayout jolla 1 rivi ja 3 saraketta", paneliKunnossa());

    }

    @Points("147.1")
    @Test
    public void testaaKomponentitLoytyvat() {

        varmista("\\+", JButton.class);
        varmista("-", JButton.class);
        varmista("Z", JButton.class);

        Collection<JTextField> kentat = haeTekstikentat();
        assertEquals("Laskimessa väärä määrä JTextField-elementtejä", 2, kentat.size());
        int e = 0;
        int d = 0;
        for (JTextField jTextField : kentat) {
            if (jTextField.isEnabled()) {
                e++;
            } else {
                d++;
            }
        }

        assertEquals("Laskimessa väärä määrä enabled-tilassa olevia JTextFieldejä", 1, e);
        assertEquals("Laskimessa väärä määrä JTextFieldejä jotka eivät ole enabled", 1, d);
    }

    @Points("147.1")
    @Test
    public void testaaJarjestys() {
        JFrame jf = frame.targetCastedTo(JFrame.class);

        Component[] cs = jf.getContentPane().getComponents();

        assertEquals("Ensimmäisen komponentin tulisi olla JTextField ",
                JTextField.class,
                cs[0].getClass());
        assertEquals("Toisen komponentin tulisi olla JTextField ",
                JTextField.class,
                cs[1].getClass());

        assertEquals("Ensimmäisen JTextField:in sisältö alussa väärä",
                "0",
                ((JTextField) cs[0]).getText());

        assertEquals("Ensimmäisen komponentin tulisi olla JTextField joka ei ole enabled-tilassa, eli siihen ei voi kirjoittaa tekstiä",
                false,
                ((JTextField) cs[0]).isEnabled());
        assertEquals("Toisen komponentin tulisi olla JTextField joka on enabled, eli siihen pitäisi voida kirjoittaa tekstiä",
                true,
                ((JTextField) cs[1]).isEnabled());

        assertTrue(editoitavaTekstikentta() != null);
        assertTrue(tulosTekstikentta() != null);
    }

    // plus miinus, nollaus
    @Test
    @Points("147.2")
    public void plusMiinusJaNollausToimivat() {

        final JButton plus = (JButton) varmista("\\+", JButton.class);
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("2");
                plus.doClick();
            }
        });

        assertEquals("Kun kirjoitetaan 2 syötekenttään ja painetaan +, pitäisi tuloskentässä olla", "2", tulosTekstikentta().getText());

        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("7");
                plus.doClick();
            }
        });

        assertEquals("Alussa tuloskentässä on 2. Kun kirjoitetaan 7 syötekenttään ja painetaan +, pitäisi tuloskentässä olla", "9", tulosTekstikentta().getText());

        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("101");
                plus.doClick();
            }
        });

        assertEquals("Alussa tuloskentässä on 9. Kun kirjoitetaan 101 syötekenttään ja painetaan +, pitäisi tuloskentässä olla", "110", tulosTekstikentta().getText());

        final JButton miinus = (JButton) varmista("-", JButton.class);

        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("11");
                miinus.doClick();
            }
        });

        assertEquals("Alussa tuloskentässä on 110. Kun kirjoitetaan 11 syötekenttään ja painetaan -, pitäisi tuloskentässä olla", "99", tulosTekstikentta().getText());

        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("200");
                miinus.doClick();
            }
        });

        assertEquals("Alussa tuloskentässä on 99. Kun kirjoitetaan 200 syötekenttään ja painetaan -, pitäisi tuloskentässä olla", "-101", tulosTekstikentta().getText());

        final JButton nollaa = (JButton) varmista("Z", JButton.class);

        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("200");
                nollaa.doClick();
            }
        });

        assertEquals("Kun tuloskentässä on -110 ja painetaan Z, pitäisi tuloskenttään tulla", "0", tulosTekstikentta().getText());
    }

    // nollausnappi disable, virheiden käsittely   
    @Points("147.3")
    @Test
    public void nollausnappiEnaboituuJaDisabloituu() {
        final JButton nollaa = (JButton) varmista("Z", JButton.class);
        assertFalse("Aluksi Z-napin pitää olla poissa päältä", nollaa.isEnabled());

        final JButton plus = (JButton) varmista("\\+", JButton.class);
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("2");
                plus.doClick();
            }
        });

        assertTrue("Kun tuloskentässä on nollasta eriävä luku, tulee Z-napin olla päällä", nollaa.isEnabled());

        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("200");
                nollaa.doClick();
            }
        });

        assertFalse("Z-napin painamisen jälkeen, pitää napin mennä poissa päältä", nollaa.isEnabled());
    }

    @Points("147.3")
    @Test
    public void syotekenttaTyhjenee() {
        final JButton plus = (JButton) varmista("\\+", JButton.class);
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("5");
                plus.doClick();
            }
        });

        assertEquals("Kun painetaan +, tulee syötekentän tyhjentyä", "", editoitavaTekstikentta().getText());

        final JButton minus = (JButton) varmista("-", JButton.class);
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("2");
                minus.doClick();
            }
        });

        assertEquals("Kun painetaan -, tulee syötekentän tyhjentyä", "", editoitavaTekstikentta().getText());

        final JButton nollaa = (JButton) varmista("Z", JButton.class);
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                editoitavaTekstikentta().setText("2");
                nollaa.doClick();
            }
        });

        assertEquals("Kun painetaan R tulee syötekentän tyhjentyä", "", editoitavaTekstikentta().getText());
    }

    @Points("147.3")
    @Test
    public void virheellisetSyotteetIgnoroidaanPlussatessa() {
        final JButton plus = (JButton) varmista("\\+", JButton.class);

        try {

            GuiActionRunner.execute(new GuiTask() {
                @Override
                protected void executeInEDT() throws Throwable {
                    editoitavaTekstikentta().setText("xx");
                    plus.doClick();
                }
            });
        } catch (Exception e) {
            fail("Kun syötekentässä on luvuksi kelpaamaton syöte, ja painetaan +, syötekentän pitäisi tyhjentyä ja mitään muuta ei pitäisi tapahtua\n"
                    + "nyt tapahtui "+e);
        }

        assertEquals("Kun syötekentässä on luvuksi kelpaamaton syöte, ja painetaan +, syötekentän pitäisi tyhjentyä ja mitään muuta ei pitäisi tapahtua\n"
                + "syötekentän arvo", "", editoitavaTekstikentta().getText());
        assertEquals("Kun syötekentässä on luvuksi kelpaamaton syöte, ja painetaan +, syötekentän pitäisi tyhjentyä ja mitään muuta ei pitäisi tapahtua\n"
                + "Tuloskenttä oli aluksi 0 ja virheellisen syötteen jälkeen", "0", tulosTekstikentta().getText());

    }

    @Points("147.3")
    @Test
    public void virheellisetSyotteetIgnoroidaanMiinustettaessa() {
        final JButton plus = (JButton) varmista("\\-", JButton.class);

        try {

            GuiActionRunner.execute(new GuiTask() {
                @Override
                protected void executeInEDT() throws Throwable {
                    editoitavaTekstikentta().setText("xx");
                    plus.doClick();
                }
            });
        } catch (Exception e) {
            fail("Kun syötekentässä on luvuksi kelpaamaton syöte, ja painetaan -, syötekentän pitäisi tyhjentyä ja mitään muuta ei pitäisi tapahtua\n"
                    + "nyt tapahtui "+e);
        }

        assertEquals("Kun syötekentässä on luvuksi kelpaamaton syöte, ja painetaan -, syötekentän pitäisi tyhjentyä ja mitään muuta ei pitäisi tapahtua\n"
                + "syötekentän arvo", "", editoitavaTekstikentta().getText());
        assertEquals("Kun syötekentässä on luvuksi kelpaamaton syöte, ja painetaan -, syötekentän pitäisi tyhjentyä ja mitään muuta ei pitäisi tapahtua\n"
                + "Tuloskenttä oli aluksi 0 ja virheellisen syötteen jälkeen", "0", tulosTekstikentta().getText());

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

    public boolean paneliKunnossa() {
        Collection<Component> cs = frame.robot.finder().findAll(new GenericTypeMatcher(JPanel.class) {
            @Override
            protected boolean isMatching(Component t) {
                return true;
            }
        });

        boolean ok = false;
        for (Component component : cs) {
            JPanel jp = (JPanel) component;
            LayoutManager lm = jp.getLayout();
            if (lm instanceof GridLayout) {
                if (((GridLayout) lm).getRows() == 1 && ((GridLayout) lm).getColumns() == 3) {
                    ok = true;
                    break;
                }
            }
        }

        return ok;
    }

    public Collection<JTextField> haeTekstikentat() {
        return frame.robot.finder().findAll(new GenericTypeMatcher(JTextField.class) {
            @Override
            protected boolean isMatching(Component t) {
                return true;
            }
        });
    }

    public JButton minus() {
        Collection<JButton> tk = frame.robot.finder().findAll(new GenericTypeMatcher(JButton.class) {
            @Override
            protected boolean isMatching(Component t) {
                return true;
            }
        });

        for (JButton n : tk) {
            if (n.getText().equals("-")) {
                return n;
            }
        }

        return null;
    }

    public JButton reset() {
        Collection<JButton> tk = frame.robot.finder().findAll(new GenericTypeMatcher(JButton.class) {
            @Override
            protected boolean isMatching(Component t) {
                return true;
            }
        });

        for (JButton n : tk) {
            if (n.getText().equals("Z")) {
                return n;
            }
        }

        return null;
    }

    public JButton plus() {
        Collection<JButton> tk = frame.robot.finder().findAll(new GenericTypeMatcher(JButton.class) {
            @Override
            protected boolean isMatching(Component t) {
                return true;
            }
        });

        for (JButton n : tk) {
            if (n.getText().equals("+")) {
                return n;
            }
        }

        return null;
    }

    public JTextField editoitavaTekstikentta() {
        Collection<JTextField> tk = frame.robot.finder().findAll(new GenericTypeMatcher(JTextField.class) {
            @Override
            protected boolean isMatching(Component t) {
                return true;
            }
        });

        for (JTextField jTextField : tk) {
            if (jTextField.isEnabled() == true) {
                return jTextField;
            }
        }

        return null;
    }

    public JTextField tulosTekstikentta() {
        Collection<JTextField> tk = frame.robot.finder().findAll(new GenericTypeMatcher(JTextField.class) {
            @Override
            protected boolean isMatching(Component t) {
                return true;
            }
        });

        for (JTextField jTextField : tk) {
            if (jTextField.isEnabled() == false) {
                return jTextField;
            }
        }

        return null;
    }

    public Component varmista(String teksti, Class tyyppi) {
        Component c = null;
        try {
            c = frame.robot.finder().find(new LaskinTest.M("(?i).*" + teksti + ".*"));
        } catch (ComponentLookupException e) {
            fail("Ei löytynyt" + tyyppi.toString().replaceAll("class", "") + "-tyyppistä komponenttia, jossa lukisi \"" + teksti + "\".\n\nLisätietoja:\n" + e);
        }
        assertEquals("Komponentti, jossa lukee \"" + teksti + "\" ei ole oikeaa tyyppiä!",
                tyyppi,
                c.getClass());
        return c;
    }
}
