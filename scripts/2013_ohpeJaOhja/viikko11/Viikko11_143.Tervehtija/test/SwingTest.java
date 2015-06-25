
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.awt.Dimension;
import javax.swing.JFrame;
import org.fest.swing.finder.WindowFinder;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.fest.swing.launcher.ApplicationLauncher;
import org.junit.Test;
import tervehtija.kayttoliittyma.Main;
import junit.framework.Assert;

@Points("143")
public class SwingTest extends FestSwingJUnitTestCase {

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

    @Test
    public void kokoJaOtsikkoOikea() {
        Dimension d = frame.component().getSize();
        Assert.assertTrue("Aseta JFramen leveydeksi vähintään 400 ja korkeudeksi vähintään 200",d.height>150 && d.width>300);
        Assert.assertEquals("Aseta JFramelle oikea otsikko", "Swing on",frame.component().getTitle());
    }

    @Test
    public void testaaViestinLahetysta() {
        JLabelFixture messageLabel = null;
        String oli = "";
        try {
            messageLabel = frame.label();
        } catch (Throwable t) {
            Assert.fail("Luothan käyttöliittymään JLabel-olion ja lisäät sen JFrame-olion sisältämään Container-olioon?");
        }

        try {
            oli = messageLabel.text();
            messageLabel.requireText("Moi!");
        } catch (Throwable t) {
            Assert.fail("Onhan luomasi JLabel-olion tekstinä \"Moi!\"?\n"
                    + "Nyt JLabel-olion teksti oli: \"" + oli + "\" .");
        }

    }
}
