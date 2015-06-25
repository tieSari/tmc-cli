
import clicker.sovelluslogiikka.Laskuri;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("146.2")
public class PainikeKuuntelijaTest {

    public static final String SOVELLUSLOGIIKAN_LUOKAN_NIMI =
            "clicker.kayttoliittyma.KlikkaustenKuuntelija";
    Reflex.ClassRef<Object> klass;
    String klassName = SOVELLUSLOGIIKAN_LUOKAN_NIMI;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void kaksiparametrinenKonstruktori() throws Throwable {
        Reflex.MethodRef2<Object, Object, Laskuri, JLabel> ctor = klass.constructor().taking(Laskuri.class, JLabel.class).withNiceError();
        assertTrue("Määrittele luokalle " + s(klassName) + " julkinen konstruktori: public " + s(klassName) + "(Laskuri laskuri, JLabel leibeli)", ctor.isPublic());
        String v = "virheen aiheutti koodi new KlikkaustenKuuntelija(new OmaLaskuri(), new JLabel());";
        ctor.withNiceError(v).invoke(new Laskuri() {
            @Override
            public int annaArvo() {
                return 0;
            }

            @Override
            public void kasvata() {
            }
        }, new JLabel());
    }

    @Test
    public void jokuRotiOliomuuttujissa() {
        saniteettitarkastus(klassName, 10, "");
    }

    @Test
    public void toteuttaaRajapinnan() {
        Class clazz = ReflectionUtils.findClass(klassName);

        boolean toteuttaaRajapinnan = false;
        Class kali = ActionListener.class;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(kali)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Toteuttaahan luokka KlikkaustenKuuntelija rajapinnan ActionListener?");
        }
    }

    @Test
    public void painikeToimii() throws Throwable {
        Reflex.MethodRef2<Object, Object, Laskuri, JLabel> ctor = klass.constructor().taking(Laskuri.class, JLabel.class).withNiceError();

        String v = "virheen aiheutti koodi \n"
                + "KlikkaustenKuuntelija kuuntelija = new KlikkaustenKuuntelija(new OmaLaskuri(), new JLabel());\n"
                + "kuuntelija.actionPerformed(new ActionEvent(...));";
        Object o = ctor.withNiceError(v).invoke(new Laskuri() {
            @Override
            public int annaArvo() {
                return 0;
            }

            @Override
            public void kasvata() {
            }
        }, new JLabel());
        
        klass.method(o, "actionPerformed").returningVoid().
                taking(ActionEvent.class).withNiceError(v).invoke(new ActionEvent(new JButton(), 1, "foo"));
    }

    @Test
    public void testaaPainikeKuuntelija() {
        String nappiTeksti = "Napin teksti";

        Laskuri laskuri = LaskuriTest.luoSovelluslogiikanInstanssi();
        int laskurinNumero = laskuri.annaArvo();

        JLabel tekstiLabel = new JLabel();
        JButton sourceButton = new JButton(nappiTeksti);

        ActionListener tapahtumanKuuntelija = luoActionListener(laskuri, tekstiLabel);

        ActionEvent tapahtuma = new ActionEvent(sourceButton, 1, "command");
        tapahtumanKuuntelija.actionPerformed(tapahtuma);

        Assert.assertEquals("Painikkeen tekstin ei tule muuttua kun napin tekstiä painetaan.", nappiTeksti, sourceButton.getText());
        Assert.assertEquals("Kun painiketta painetaan, Laskurin arvon tulisi kasvaa yhdellä.", laskurinNumero + 1, laskuri.annaArvo());
        Assert.assertEquals("Kun painiketta painetaan, tekstin pitäisi päivittyä vastaamaan laskurin arvoa. Arvon pitäisi olla \"" + (laskurinNumero + 1) + "\", mutta nyt oli \"" + tekstiLabel.getText().trim() + "\".", "" + (laskurinNumero + 1), tekstiLabel.getText().trim());
    }

    private ActionListener luoActionListener(Laskuri laskuri, JLabel teksti) {
        Reflex.ClassRef<?> luokka;
        try {
            luokka = Reflex.reflect(SOVELLUSLOGIIKAN_LUOKAN_NIMI);
        } catch (Throwable t) {
            Assert.fail("Luokkaa " + SOVELLUSLOGIIKAN_LUOKAN_NIMI + " ei ole olemassa. Tässä tehtävässä täytyy luoda kyseinen luokka.");
            return null;
        }
        if (!ActionListener.class.isAssignableFrom(
                luokka.getReferencedClass())) {
            Assert.fail("Luokan " + SOVELLUSLOGIIKAN_LUOKAN_NIMI + " täytyy "
                    + "toteuttaa rajapinta " + Laskuri.class.getName());
        }

        Object instanssi;
        try {
            instanssi = luokka.constructor().taking(Laskuri.class, JLabel.class).invoke(laskuri, teksti);
        } catch (Throwable t) {
            Assert.fail("Luokalla " + SOVELLUSLOGIIKAN_LUOKAN_NIMI + " ei määrettä public tai sillä ei ole julkista konstruktoria public KlikkaustenKuuntelija(Laskuri laskuri, JLabel teksti).");
            return null;
        }

        return (ActionListener) instanssi;
    }

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "").replace("java.io.", "");
    }

    private String s(String klassName) {
        return klassName.substring(klassName.lastIndexOf(".") + 1);
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("et tarvitse \"stattisia muuttujia\", poista luokalta " + s(klassName) + " muuttuja " + kentta(field.toString(), s(klassName)), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("luokan kaikkien oliomuuttujien näkyvyyden tulee olla private, muuta luokalta " + s(klassName) + " löytyi: " + kentta(field.toString(), klassName), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }
            assertTrue("et tarvitse " + s(klassName) + "-luokalle kuin " + m + ", poista ylimääräiset", var <= n);
        }
    }
}