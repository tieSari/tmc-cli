
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import fi.helsinki.cs.tmc.edutestutils.Reflex.ClassRef;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tyokalut.DuplikaattienPoistaja;
import static org.junit.Assert.*;

@Points("132")
public class DuplikaattienPoistajaTest {

    String klassName = "tyokalut.OmaDuplikaattienPoistaja";
    Reflex.ClassRef<Object> klass;
    private Object olio;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void luokkaJulkinen() {
        assertTrue("Luokan " + klassName + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void eiYlimaaraisiaMuuttujia() {
        saniteettitarkastus(klassName, 2, "sanajoukon ja havaittujen duplikaattien määrän tallentavat oliomuuttujat");
    }

    @Test
    public void tyhjaKonstruktori() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        assertTrue("Määrittele luokalle " + s(klassName) + " julkinen konstruktori: "
                + "public " + s(klassName) + "()", ctor.isPublic());
        String v = "virheen aiheutti koodi new OmaUseanKaannoksenSanakirja();";
        ctor.withNiceError(v).invoke();
    }

    public Object luo() throws Throwable {
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        return ctor.invoke();
    }

    @Test
    public void toteuttaaRajapinnan() {
        Class clazz = ReflectionUtils.findClass(klassName);

        boolean toteuttaaRajapinnan = false;
        Class kali = DuplikaattienPoistaja.class;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(kali)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Toteuttaahan luokka OmaDuplikaattienPoistaja rajapinnan DuplikaattienPoistaja?");
        }
    }

    @Test
    public void lisaaMetodi() throws Throwable {
        String metodi = "lisaa";

        Object olio = luo();

        assertTrue("tee luokalle " + klassName + " metodi public void " + metodi + "(String merkkijono)",
                klass.method(olio, metodi)
                .returningVoid().taking(String.class).isPublic());

        String v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");";

        klass.method(olio, metodi)
                .returningVoid().taking(String.class).withNiceError(v).invoke("apina");
    }

    @Test
    public void havaittujenDuplikaattienMaaraMetodi() throws Throwable {
        String metodi = "getHavaittujenDuplikaattienMaara";

        Object olio = luo();

        assertTrue("tee luokalle " + s(klassName) + " metodi public int " + metodi + "()",
                klass.method(olio, metodi)
                .returning(int.class).takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.getHavaittujenDuplikaattienMaara();";

        assertEquals(v, 0, (int) klass.method(olio, metodi)
                .returning(int.class).takingNoParams().withNiceError(v).invoke());
    }

    @Test
    public void lisaysJaDuplikaatit() throws Throwable {
        Object olio = luo();

        String v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.getHavaittujenDuplikaattienMaara();";

        lisaa(olio, "apina", v);

        assertEquals(v, 0, dup(olio, v));

        v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.getHavaittujenDuplikaattienMaara();";

        lisaa(olio, "apina", v);
        assertEquals(v, 1, dup(olio, v));

        v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.getHavaittujenDuplikaattienMaara();";

        lisaa(olio, "gorilla", v);
        assertEquals(v, 1, dup(olio, v));

        v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.getHavaittujenDuplikaattienMaara();";

        lisaa(olio, "gorilla", v);
        lisaa(olio, "apina", v);

        assertEquals(v, 3, dup(olio, v));
    }

    @Test
    public void uniikitMerkkijonotMetodi() throws Throwable {
        Object olio = luo();

        assertTrue("tee luokalle " + klassName + " metodi public Set<String> getUniikitMerkkijonot() ",
                klass.method(olio, "getUniikitMerkkijonot")
                .returning(Set.class)
                .takingNoParams()
                .isPublic());

        String v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.getUniikitMerkkijonot();";

        lisaa(olio, "apina", klassName);

        klass.method(olio, "getUniikitMerkkijonot")
                .returning(Set.class)
                .takingNoParams().withNiceError(v).invoke();

    }

    @Test
    public void uniikitToimii() throws Throwable {
        Object olio = luo();

        String v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.getUniikitMerkkijonot()";

        lisaa(olio, "apina", v);

        Set<String> odot = new HashSet<String>();
        odot.add("apina");

        assertEquals(v, odot, uniq(olio, v));

        v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.getUniikitMerkkijonot()";

        lisaa(olio, "apina", v);
        assertEquals(v, odot, uniq(olio, v));

        v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.getUniikitMerkkijonot()";

        lisaa(olio, "gorilla", v);
        odot.add("gorilla");
        assertEquals(v, odot, uniq(olio, v));

        v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.lisaa(\"oranki\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.getUniikitMerkkijonot();";

        lisaa(olio, "gorilla", v);
        lisaa(olio, "oranki", v);
        lisaa(olio, "apina", v);

        lisaa(olio, "gorilla", v);
        odot.add("gorilla");
        odot.add("oranki");
        assertEquals(v, odot, uniq(olio, v));
    }

    @Test
    public void tyhjennaMetodi() throws Throwable {
        Object olio = luo();

        assertTrue("tee luokalle " + klassName + " metodi void tyhjenna() ",
                klass.method(olio, "tyhjenna")
                .returningVoid()
                .takingNoParams()
                .isPublic());

        String v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.tyhjenna();";

        lisaa(olio, "apina", klassName);

        klass.method(olio, "tyhjenna")
                .returningVoid()
                .takingNoParams().withNiceError(v).invoke();

    }

    @Test
    public void tyhjennysToimii() throws Throwable {
        Object olio = luo();

        String v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.getUniikitMerkkijonot()";

        lisaa(olio, "apina", v);

        Set<String> odot = new HashSet<String>();
        odot.add("apina");

        assertEquals(v, odot, uniq(olio, v));

        v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.getUniikitMerkkijonot()";

        lisaa(olio, "apina", v);
        assertEquals(v, odot, uniq(olio, v));

        v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.getUniikitMerkkijonot()";

        lisaa(olio, "gorilla", v);
        odot.add("gorilla");
        assertEquals(v, odot, uniq(olio, v));

        v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.lisaa(\"oranki\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.getUniikitMerkkijonot();";

        lisaa(olio, "gorilla", v);
        lisaa(olio, "oranki", v);
        lisaa(olio, "apina", v);

        lisaa(olio, "gorilla", v);
        odot.add("gorilla");

        v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.lisaa(\"oranki\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.tyhjenna();\n"
                + "s.getUniikitMerkkijonot();";

        tyhj(olio, v);

        odot = new HashSet();

        assertEquals(v, odot, uniq(olio, v));

        v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.lisaa(\"oranki\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.tyhjenna();\n"
                + "s.getHavaittujenDuplikaattienMaara();";

        assertEquals(v, 0, dup(olio, v));

        v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.lisaa(\"oranki\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.tyhjenna();\n"
                + "s.lisaa(\"kivi\");\n"
                + "s.lisaa(\"kivi\");\n"
                + "s.getUniikitMerkkijonot();";

        lisaa(olio, "kivi", v);
        lisaa(olio, "kivi", v);

        odot.add("kivi");

        assertEquals(v, odot, uniq(olio, v));

        v = "\nVirheen aiheuttanut koodi DuplikaattienPoistaja s = new OmaUseanKaannoksenSanakirja();\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.lisaa(\"gorilla\");\n"
                + "s.lisaa(\"oranki\");\n"
                + "s.lisaa(\"apina\");\n"
                + "s.tyhjenna();\n"
                + "s.lisaa(\"kivi\");\n"
                + "s.lisaa(\"kivi\");\n"
                + "s.getHavaittujenDuplikaattienMaara();";

        assertEquals(v, 1, dup(olio, v));
    }

    private void tyhj(Object o, String v) throws Throwable {
        klass.method(o, "tyhjenna")
                .returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    private Set uniq(Object o, String v) throws Throwable {
        return klass.method(o, "getUniikitMerkkijonot")
                .returning(Set.class).takingNoParams().withNiceError(v).invoke();
    }

    private int dup(Object o, String v) throws Throwable {
        return (int) klass.method(o, "getHavaittujenDuplikaattienMaara")
                .returning(int.class).takingNoParams().withNiceError(v).invoke();
    }

    private void lisaa(Object o, String sana, String v) throws Throwable {
        klass.method(o, "lisaa")
                .returningVoid().taking(String.class).withNiceError(v).invoke(sana);
    }
    /*
     * 
     */

    @Test
    public void testaaDuplikaatinLisaaminen() {
        DuplikaattienPoistaja poistaja = luoInstanssi();
        testaaMerkkijononLisays(poistaja, "eka");
        testaaMerkkijononLisays(poistaja, "toka");
        testaaMerkkijononLisays(poistaja, "kolmas");
        testaaMerkkijononLisays(poistaja, "toka");
        testaaMerkkijononLisays(poistaja, "eka");
        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");
    }

    @Test
    public void testaaTyhjennys() {
        DuplikaattienPoistaja poistaja = luoInstanssi();

        testaaMerkkijononLisays(poistaja, "eka");
        testaaMerkkijononLisays(poistaja, "toka");
        testaaMerkkijononLisays(poistaja, "kolmas");
        testaaMerkkijononLisays(poistaja, "toka");
        testaaMerkkijononLisays(poistaja, "eka");

        testaaTyhjennys(poistaja);

        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");
        testaaMerkkijononLisays(poistaja, "vika");

        testaaTyhjennys(poistaja);
    }

    private void testaaTyhjennys(DuplikaattienPoistaja poistaja) {
        poistaja.tyhjenna();
        Set<String> uniikitJonot = poistaja.getUniikitMerkkijonot();
        if (uniikitJonot == null) {
            Assert.fail("Metodi getUniikitMerkkijonot() palautti arvon null, "
                    + "vaikka paluuarvona pitää olla aina "
                    + "Set<String>-rajapinnan toteuttava olio.");
            return;
        }

        int duplikaatit = poistaja.getHavaittujenDuplikaattienMaara();
        Assert.assertTrue("Metodin tyhjenna() suorittamisen jälkeen havaittujen duplikaattien määrän pitäisi olla nolla. Palautettu arvo oli: "
                + duplikaatit, duplikaatit == 0);

        boolean tyhja = uniikitJonot.isEmpty();
        Assert.assertTrue("Metodin tyhjenna() suorittamisen jälkeen uniikkien merkkijonojen lista täytyy olla tyhjä. Palautettu lista oli: "
                + uniikitJonot.toString(), tyhja);
    }

    private void testaaMerkkijononLisays(DuplikaattienPoistaja poistaja,
            String merkkijono) {
        if (poistaja.getUniikitMerkkijonot() == null) {
            Assert.fail("Metodi getUniikitMerkkijonot() palautti arvon null, "
                    + "vaikka paluuarvona pitää olla aina "
                    + "Set<String>-rajapinnan toteuttava olio.");
            return;
        }

        boolean loytyyDuplikaatti = poistaja.getUniikitMerkkijonot().contains(merkkijono);

        int maaraEnnen = poistaja.getUniikitMerkkijonot().size();
        int duplikaatitEnnen = poistaja.getHavaittujenDuplikaattienMaara();
        poistaja.lisaa(merkkijono);

        int maaraJalkeen = poistaja.getUniikitMerkkijonot().size();
        int duplikaatitJalkeen = poistaja.getHavaittujenDuplikaattienMaara();

        if (loytyyDuplikaatti) {
            Assert.assertTrue("Uniikkien merkkijonojen määrän täytyy pysyä "
                    + " ennallaan, kun jo aiemmin lisätty merkkijono lisätään uudelleen (= duplikaatti). "
                    + "Määrä ennen lisäystä oli: " + maaraEnnen
                    + ", määrä lisäyksen jälkeen oli: " + maaraJalkeen,
                    maaraJalkeen == maaraEnnen);
            Assert.assertTrue("Duplikaattien määrän täytyy kasvaa yhdellä, "
                    + "kun jo aiemmin lisätty merkkijono lisätään uudelleen (= duplikaatti). Duplikaattien määrä ennen lisäystä oli: " + duplikaatitEnnen
                    + ", duplikaattien määrä lisäyksen jälkeen oli: " + duplikaatitJalkeen, duplikaatitJalkeen == (duplikaatitEnnen + 1));
        } else {
            Assert.assertTrue("Uniikkien merkkijonojen määrän täytyy kasvaa "
                    + " yhdellä, kun uusi uniikki merkkijono lisätään. "
                    + "Määrä ennen lisäystä oli: " + maaraEnnen
                    + ", määrä lisäyksen jälkeen oli: " + maaraJalkeen,
                    maaraJalkeen == (maaraEnnen + 1));
            Assert.assertTrue("Duplikaattien määrän täytyy pysyä ennallaan, "
                    + "kun lisätään uusi uniikki merkkijono. Duplikaattien määrä ennen lisäystä oli: " + duplikaatitEnnen
                    + ", duplikaattien määrä lisäyksen jälkeen oli: " + duplikaatitJalkeen, duplikaatitJalkeen == duplikaatitEnnen);
        }

    }

    private DuplikaattienPoistaja luoInstanssi() {
        String luokanNimi = "tyokalut.OmaDuplikaattienPoistaja";
        ClassRef<?> luokka;
        try {
            luokka = Reflex.reflect(luokanNimi);
        } catch (Throwable t) {
            Assert.fail("Luokkaa " + luokanNimi + " ei ole olemassa. Tässä tehtävässä täytyy luoda kyseinen luokka.");
            return null;
        }
        if (!DuplikaattienPoistaja.class.isAssignableFrom(
                luokka.getReferencedClass())) {
            Assert.fail("Luokan " + luokanNimi + " täytyy "
                    + "toteuttaa rajapinta tyokalut.DuplikaattienPoistaja");
        }

        Object instanssi;
        try {
            instanssi = luokka.constructor().takingNoParams().invoke();
        } catch (Throwable t) {
            Assert.fail("Luokalla " + luokanNimi + " ei ole julkista parametritonta konstruktoria.");
            return null;
        }

        return (DuplikaattienPoistaja) instanssi;
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

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "").replace("java.io.", "");
    }
}
