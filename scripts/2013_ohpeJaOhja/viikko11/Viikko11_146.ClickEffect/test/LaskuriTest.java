
import clicker.sovelluslogiikka.Laskuri;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("146.1")
public class LaskuriTest {

    public static final String SOVELLUSLOGIIKAN_LUOKAN_NIMI =
            "clicker.sovelluslogiikka.OmaLaskuri";
    Reflex.ClassRef<Object> klass;
    String klassName = SOVELLUSLOGIIKAN_LUOKAN_NIMI;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void eiYlimaaraisiaMuuttujia() {
        saniteettitarkastus(klassName, 1, "arvon tallentavan oliomuuttujan");
    }

    @Test
    public void tyhjaKonstruktori() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        assertTrue("Määrittele luokalle " + s(klassName) + " julkinen konstruktori: public " + klassName + "()", ctor.isPublic());
        String v = "virheen aiheutti koodi new OmaLaskuri();";
        ctor.withNiceError(v).invoke();
    }

    @Test
    public void toteuttaaRajapinnan() {
        Class clazz = ReflectionUtils.findClass(klassName);

        boolean toteuttaaRajapinnan = false;
        Class kali = Laskuri.class;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(kali)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Toteuttaahan luokka OmaLaskuri rajapinnan Laskuri?");
        }
    }

    @Test
    public void testaaLampomittari() throws Throwable {
        Laskuri olio = (Laskuri) luo();

        String k1 = ""
                + "\nLaskuri c = new OmaLaskuri();\n"
                + "c.annaArvo();\n";

        assertEquals(k1, 0, (int) klass.method(olio, "annaArvo").returning(int.class).takingNoParams().withNiceError(k1).invoke());

        k1 = ""
                + "\nLaskuri c = new OmaLaskuri();\n"
                + "c.kasvata();\n"
                + "c.annaArvo();\n";

        klass.method(olio, "kasvata").returningVoid().takingNoParams().withNiceError(k1).invoke();

        assertEquals(k1, 1, (int) klass.method(olio, "annaArvo").returning(int.class).takingNoParams().withNiceError(k1).invoke());
    }

    @Test
    public void testaaOmaLaskuri() {
        Laskuri laskuri = luoSovelluslogiikanInstanssi();
        Assert.assertEquals("Laskurin arvon tulee olla alussa 0", 0, laskuri.annaArvo());

        for (int i = 1; i < 100; i++) {
            laskuri.kasvata();
            Assert.assertEquals("Laskurin arvon tulee olla " + i + " kasvata()-kutsun jälkeen " + i + ".", i, laskuri.annaArvo());
        }
    }

    public Object luo() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        return ctor.invoke();
    }

    public static Laskuri luoSovelluslogiikanInstanssi() {
        Reflex.ClassRef<?> luokka;
        try {
            luokka = Reflex.reflect(SOVELLUSLOGIIKAN_LUOKAN_NIMI);
        } catch (Throwable t) {
            Assert.fail("Luokkaa " + SOVELLUSLOGIIKAN_LUOKAN_NIMI + " ei ole olemassa. Tässä tehtävässä täytyy luoda kyseinen luokka.");
            return null;
        }
        if (!Laskuri.class.isAssignableFrom(
                luokka.getReferencedClass())) {
            Assert.fail("Luokan " + SOVELLUSLOGIIKAN_LUOKAN_NIMI + " täytyy "
                    + "toteuttaa rajapinta " + Laskuri.class.getName());
        }

        Object instanssi;
        try {
            instanssi = luokka.constructor().takingNoParams().invoke();
        } catch (Throwable t) {
            Assert.fail("Luokalla " + SOVELLUSLOGIIKAN_LUOKAN_NIMI + " ei ole julkista parametritonta konstruktoria.");
            return null;
        }

        return (Laskuri) instanssi;
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
