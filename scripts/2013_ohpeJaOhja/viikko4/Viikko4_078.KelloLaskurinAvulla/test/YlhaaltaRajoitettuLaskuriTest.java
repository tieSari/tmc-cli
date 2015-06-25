
import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.*;
import static org.junit.Assert.*;

public class YlhaaltaRajoitettuLaskuriTest {

    @Rule
    public MockStdio io = new MockStdio();
    String luokanNimi = "YlhaaltaRajoitettuLaskuri";
    Class laskuriLuokka;

    @Points("78.1")
    @Test
    public void mainEiHeitaPoikkeusta() {
        Method m = null;
        try {
            String[] args = new String[0];
            m = ReflectionUtils.requireMethod(Paaohjelma.class, "main", args.getClass());
        } catch (Throwable e) {
            e.printStackTrace();
            fail("main puuttuu " + e.getMessage());
        }

        String virhe = "";
        if (m.toString().indexOf("thr") > 0) {
            virhe = m.toString().substring(+m.toString().indexOf("thr"));
        }

        assertFalse("main-metodin ei tule heittää poikkeusta, poista määrittelystä " + virhe, m.toString().contains("throws"));
    }
    Reflex.ClassRef<Object> klass;
    String klassName = "YlhaaltaRajoitettuLaskuri";

    @Before
    public void haeLuokka() {
        klass = Reflex.reflect(klassName);
    }

    @Points("78.1")
    @Test
    public void luokkaJulkinen() {
        assertTrue("Luokan " + klassName + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Points("78.1")
    @Test
    public void testaaKonstruktori() throws Throwable {
        Reflex.MethodRef1<Object, Object, Integer> ctor = klass.constructor().taking(int.class).withNiceError();
        assertTrue("Määrittele luokalle " + klassName + " julkinen konstruktori: public " + klassName + "(int ylarajanAlkuarvo)", ctor.isPublic());
        ctor.invoke(4);
    }

    @Points("78.1")
    @Test
    public void onkoMetodiaSeuraava() throws Throwable {
        String metodi = "seuraava";

        //Reflex.ClassRef<Object> klass = Reflex.reflect(klassName);
        Object olio = newLaskuri(4);

        assertTrue("tee luokalle " + klassName + " metodi public void " + metodi + "() ", klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi YlhaaltaRajoitettuLaskuri l = new (YlhaaltaRajoitettuLaskuri4); "
                + "l.seuraava()";

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    @Points("78.1")
    @Test
    public void toStringAlussaOikein() throws Throwable {
        int raja = 10;
        Object laskuri = newLaskuri(raja);
        String vastaus = toString(laskuri);

        String odotettu = "0";
        assertFalse("Tee " + luokanNimi + "-luokalle metodi public String toString() tehtävänannon ohjeen mukaan", vastaus.contains("@"));
        assertTrue("luotiin kortti = new YlhaaltaRajoitettuLaskuri(" + raja + "); metodi toString ei toimi oikein, tulostuu " + vastaus, vastaus.contains(odotettu));
    }

    @Points("78.1")
    @Test
    public void laskuriKasvaa() throws Throwable {
        int raja = 3;
        Object laskuri = newLaskuri(raja);

        seuraava(laskuri);

        String vastaus = toString(laskuri);
        String odotettu = "1";

        assertTrue("kun laskurille kutsutaan seuraava, pitäisi sen arvo kasvaa, tarkista seuraava: \n"
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); laskuri.seuraava(); System.out.println(laskuri);", vastaus.contains(odotettu));

        seuraava(laskuri);

        vastaus = toString(laskuri);
        odotettu = "2";

        assertTrue("kun laskurille kutsutaan seuraava, pitäisi sen arvo kasvaa, tarkista seuraava: "
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); laskuri.seuraava(); laskuri.seuraava(); System.out.println(laskuri);", vastaus.contains(odotettu));
    }

    @Points("78.1")
    @Test
    public void laskuriNollaantuuJaKasvaaTaas() throws Throwable {
        int raja = 2;
        Object laskuri = newLaskuri(raja);

        seuraava(laskuri);
        seuraava(laskuri);
        seuraava(laskuri);

        String vastaus = toString(laskuri);
        String odotettu = "0";

        assertTrue("laskurin pitäisi nollautua kun arvo kasvaa yli ylärajan, tarkista seuraava: \n"
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); laskuri.seuraava(); laskuri.seuraava(); laskuri.seuraava(); System.out.println(laskuri);", vastaus.contains(odotettu));

        seuraava(laskuri);

        vastaus = toString(laskuri);
        odotettu = "1";

        assertTrue("laskurin pitäisi lähteä taas kasvamaan nollaantumisen jälkeen, tarkista seuraava: "
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); laskuri.seuraava(); laskuri.seuraava(); laskuri.seuraava(); laskuri.seuraava(); System.out.println(laskuri);", vastaus.contains(odotettu));
    }

    @Points("78.1")
    @Test
    public void eiYlimaaraisiaMuuttujia1() {
        saniteettitarkastus();
    }

    /*
     * osa 2
     */
    @Points("78.2")
    @Test
    public void etunollaAlussa() throws Throwable {
        int raja = 10;
        Object laskuri = newLaskuri(raja);
        String vastaus = toString(laskuri);

        String odotettu = "00";

        assertTrue("Tulostuksessa pitäisi olla mukana etunolla, ks 78.2:n tehtävämäärittely! Kun luodaan uusi laskuri, tulostuu " + vastaus, vastaus.contains(odotettu));
    }

    @Points("78.2")
    @Test
    public void etunollaPysyy() throws Throwable {
        int raja = 3;
        Object laskuri = newLaskuri(raja);

        seuraava(laskuri);
        String vastaus = toString(laskuri);
        String odotettu = "01";


        assertTrue("Tulostuksessa pitäisi olla mukana etunolla, ks 78.2:n tehtävämäärittely! Tarkasta seuraava: "
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); laskuri.seuraava();  System.out.println(laskuri);" + vastaus, vastaus.contains(odotettu));

        seuraava(laskuri);
        vastaus = toString(laskuri);
        odotettu = "02";

        assertTrue("Tulostuksessa pitäisi olla mukana etunolla. Tarkasta seuraava: "
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); laskuri.seuraava(); laskuri.seuraava();  System.out.println(laskuri);" + vastaus, vastaus.contains(odotettu));

        seuraava(laskuri);
        vastaus = toString(laskuri);
        odotettu = "03";

        assertTrue("Tulostuksessa pitäisi olla mukana etunolla. Tarkasta seuraava: "
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); laskuri.seuraava();  laskuri.seuraava(); laskuri.seuraava(); System.out.println(laskuri);" + vastaus, vastaus.contains(odotettu));

        seuraava(laskuri);
        vastaus = toString(laskuri);
        odotettu = "00";

        assertTrue("Toimiiko etunolla laskurin pyörähtämisen jälkeen? Tarkasta seuraava: "
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); laskuri.seuraava();  laskuri.seuraava(); laskuri.seuraava(); laskuri.seuraava(); System.out.println(laskuri);" + vastaus, vastaus.contains(odotettu));

    }

    @Points("78.2")
    @Test
    public void etunollaVaanYksinumeroisilla() throws Throwable {
        int raja = 12;
        Object laskuri = newLaskuri(raja);

        for (int i = 0; i < 10; i++) {
            seuraava(laskuri);
        }

        String vastaus = toString(laskuri);
        String odotettu = "10";


        assertTrue("Etunolla pitää olla vain yksinumeroisen laskurin arvon tulostuksessa. Tarkasta seuraava: "
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); //10 kertaa komento laskuri.seuraava();  System.out.println(laskuri);" + vastaus, vastaus.contains(odotettu) && !vastaus.contains("0" + odotettu));

        seuraava(laskuri);
        vastaus = toString(laskuri);
        odotettu = "11";

        assertTrue("Etunolla pitää olla vain yksinumeroisen laskurin arvon tulostuksessa. Tarkasta seuraava: "
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); //11 kertaa komento laskuri.seuraava();  System.out.println(laskuri);" + vastaus, vastaus.contains(odotettu) && !vastaus.contains("0" + odotettu));

        seuraava(laskuri);
        vastaus = toString(laskuri);
        odotettu = "12";

        assertTrue("Etunolla pitää olla vain yksinumeroisen laskurin arvon tulostuksessa. Tarkasta seuraava: "
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); //12 kertaa komento laskuri.seuraava();  System.out.println(laskuri);" + vastaus, vastaus.contains(odotettu) && !vastaus.contains("0" + odotettu));


        seuraava(laskuri);
        vastaus = toString(laskuri);
        odotettu = "00";

        assertTrue("Etunolla palaa jälleen laskurin pyörähdettyä. Tarkasta seuraava: "
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); //13 kertaa komento laskuri.seuraava();  System.out.println(laskuri);" + vastaus, vastaus.contains(odotettu) && !vastaus.contains("0" + odotettu));
    }

    @Points("78.2")
    @Test
    public void eiYlimaaraisiaMuuttujia2() {
        saniteettitarkastus();
    }

    /*
     * osa 3
     */
    @Points("78.3")
    @Test
    public void onkoMetodiaArvo() throws Throwable {
        String metodi = "arvo";

        Object olio = newLaskuri(4);

        assertTrue("tee luokalle " + klassName + " metodi public int " + metodi + "() ", klass.method(olio, metodi)
                .returning(int.class).takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi YlhaaltaRajoitettuLaskuri l = new YlhaaltaRajoitettuLaskuri(4); "
                + "l.arvo()";

        klass.method(olio, metodi)
                .returning(int.class).takingNoParams().withNiceError(v).invoke();
    }

    @Points("78.3")
    @Test
    public void arvoJaToStringYksimielisia() throws Throwable {
        int raja = 12;
        Object laskuri = newLaskuri(raja);

        for (int i = 0; i < 20; i++) {
            String vastausToString = toString(laskuri);
            String vastausArvo = etunollaa(arvo(laskuri));

            seuraava(laskuri);

            assertTrue("tomiiko metodi arvo oikein? Testaa seuraava\n"
                    + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); //" + i + " kertaa komento laskuri.seuraava();  System.out.println(laskuri.arvo());", vastausToString.contains("" + vastausArvo));
        }
    }

    @Points("78.3")
    @Test
    public void kelloEtenee() {
        io.setSysIn("0\n0\n0\n");
        //Paaohjelma.main(new String[0]);
        main();
        String[] rivit = io.getSysOut().split("\n");
        int eka = etsiEka(rivit);

        assertFalse("Tee pääohjelmastasi kello tehtävän 78.3 ohjeen mukaan. Poista ensin kaikki ylimääräinen. "
                + "Ensimmäisenä  pitäisi tulostua 00:00", eka == -1);

        assertTrue("Tee pääohjelmastasi kello tehtävän 78.3 ohjeen mukaan. Poista ensin kaikki ylimääräinen."
                + "Ensimmäisenä  pitäisi tulostua 00:00", rivit[eka].contains("00:00"));

        tarkistaEteneminen(rivit, eka);
    }

    /*
     * ei liikaa oliomuuttujia
     */
    @Points("78.1 78.2 78.3 78.4")
    @Test
    public void eiYlimaaraisiaMuuttujia() {
        Field[] kentat = ReflectionUtils.findClass(luokanNimi).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("et tarvitse \"stattisia muuttujia\", poista " + kentta(field.toString()), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("luokan kaikkien oliomuuttujien näkyvyyden tulee olla private, muuta " + kentta(field.toString()), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }
            assertTrue("et tarvitse " + luokanNimi + "-luokalle kuin kaksi oliomuuttujaa (ks. tehtävänanto), poista ylimääräiset", var < 3);
        }
    }

    @Points("78.3")
    @Test
    public void eiYlimaaraisiaMuuttujia3() {
        saniteettitarkastus();
    }

    /*
     * osa 4
     */
    @Points("78.4")
    @Test
    public void onkoMetodiaAsetaArvo() throws Throwable {
        String metodi = "asetaArvo";

        Object olio = newLaskuri(4);

        assertTrue("tee luokalle " + klassName + " metodi public void " + metodi + "(int uusiArvo) ", klass.method(olio, metodi)
                .returningVoid().taking(int.class).isPublic());

        String v = "\nVirheen aiheuttanut koodi YlhaaltaRajoitettuLaskuri l = new YlhaaltaRajoitettuLaskuri(4); "
                + "l.asetaArvo(2)";

        klass.method(olio, metodi)
                .returningVoid().taking(int.class).withNiceError(v).invoke(2);
    }

    @Points("78.4")
    @Test
    public void arvoAsettuu() throws Throwable {
        int raja = 4;
        Object laskuri = newLaskuri(raja);

        int odotettu = 3;
        asetaArvo(laskuri, odotettu);


        int arvo = arvo(laskuri);
        assertEquals("toimiiko metodi asetaArvo(int uusiArvo) halutulla tavalla?, tarkista seuraava: \n"
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); laskuri.asetaArvo(3); System.out.println(laskuri);", arvo, odotettu);

        seuraava(laskuri);
        odotettu = 4;
        arvo = arvo(laskuri);
        assertEquals("laskurin pitäisi edetä normaalisti uuden arvon asettamisen jälkeen, tarkista seuraava: "
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); laskuri.asetaArvo(3); laskuri.seuraava(); System.out.println(laskuri);", arvo, odotettu);

        seuraava(laskuri);
        odotettu = 0;
        arvo = arvo(laskuri);
        assertEquals("laskurin pitäisi edetä ja nollautua normaalisti uuden arvon asettamisen jälkeen, tarkista seuraava: "
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); laskuri.asetaArvo(3); laskuri.seuraava(); System.out.println(laskuri);", arvo, odotettu);

    }

    @Points("78.4")
    @Test
    public void vaaraArvoEiAsetu() throws Throwable {
        int raja = 4;
        Object laskuri = newLaskuri(raja);

        int odotettu = 0;
        asetaArvo(laskuri, -1);

        int arvo = arvo(laskuri);
        assertEquals("Negatiivisen arvon asettamisen ei pitäisi muuttaa laskurin arvoa, tarkista seuraava:\n"
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); laskuri.asetaArvo(-1); System.out.println(laskuri);", odotettu, arvo);

        asetaArvo(laskuri, raja + 1);
        arvo = arvo(laskuri);
        assertEquals("Liian ison arvon asettamisen ei pitäisi muuttaa laskurin arvoa, tarkista seuraava:\n"
                + "laskuri = new YlhaaltaRajoitettuLaskuri(" + raja + "); laskuri.asetaArvo(5); laskuri.seuraava(); System.out.println(laskuri);", odotettu, arvo);
    }

    @Points("78.4")
    @Test
    public void tarkkaKelloEtenee() {
        io.setSysIn("50\n59\n23\n");
        //Paaohjelma.main(new String[0]);
        main();
        String[] rivit = io.getSysOut().split("\n");
        int eka = etsiEka(rivit);

        assertFalse("Tee pääohjelmastasi kello tehtävän 78.4 ohjeen mukaan.  "
                + "Syötteellä 50 59 23 ensimmäisenä  pitäisi tulostua 23:59:50", eka == -1);

        assertTrue("Tee pääohjelmastasi kello tehtävän 78.4 ohjeen mukaan.  "
                + "Syötteellä 50 59 23 ensimmäisenä pitäisi tulostua 23:59:50. Nyt tulostuu " + rivit[eka], rivit[eka].contains("23:59:50"));

        tarkistaTarkanKellonEteneminen(rivit, eka);
    }

    @Points("78.4")
    @Test
    public void eiYlimaaraisiaMuuttujia4() {
        saniteettitarkastus();
    }

    private void saniteettitarkastus() throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(luokanNimi).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("et tarvitse \"stattisia muuttujia\", poista " + kentta(field.toString()), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("luokan kaikkien oliomuuttujien näkyvyyden tulee olla private, muuta " + kentta(field.toString()), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }
            assertTrue("et tarvitse " + luokanNimi + "-luokalle kuin laskurin arvon ja ylärajan muistavat oliomuuttujat, poista ylimääräiset", var < 3);
        }
    }

    /*
     * helpers
     */
    private void main() {
        try {
            String[] args = new String[0];
            Method m = ReflectionUtils.requireMethod(Paaohjelma.class, "main", args.getClass());

            ReflectionUtils.invokeMethod(Void.TYPE, m, null, (Object) args);
        } catch (Throwable e) {
            fail("you fail! " + e.getMessage());
        }
    }

    private String toString(Object olio) throws Throwable {
        String metodi = "toString";

        String v = "\nVirheen aiheuttanut koodi l = new YlhaaltaRajoitettuLaskuri(4); l.toString()";

        return klass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke();
    }

    private void seuraava2(Object kortti) throws Throwable {
        Method metodi = ReflectionUtils.requireMethod(laskuriLuokka, "seuraava");
        ReflectionUtils.invokeMethod(void.class, metodi, kortti);
    }

    private void seuraava(Object kortti) throws Throwable {
        String metodi = "seuraava";

        String v = "\nVirheen aiheuttanut koodi l = new YlhaaltaRajoitettuLaskuri(4); l.seuraava()";

        klass.method(kortti, metodi)
                .returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    private int arvo(Object kortti) throws Throwable {
        String metodi = "arvo";

        String v = "\nVirheen aiheuttanut koodi l = new YlhaaltaRajoitettuLaskuri(4); l.arvo()";

        return klass.method(kortti, metodi)
                .returning(int.class).takingNoParams().withNiceError(v).invoke();
    }

    private void asetaArvo(Object kortti, int arvo) throws Throwable {
        String metodi = "asetaArvo";

        String v = "\nVirheen aiheuttanut koodi l = new YlhaaltaRajoitettuLaskuri(4); l.asetaArvo(" + arvo + ")";

        klass.method(kortti, metodi)
                .returningVoid().taking(int.class).withNiceError(v).invoke(arvo);
    }

    private Object newLaskuri(int alku) throws Throwable {
        Reflex.MethodRef1<Object, Object, Integer> ctor = klass.constructor().taking(int.class).withNiceError();
        assertTrue("Määrittele luokalle " + klassName + " julkinen konstruktori: public " + klassName + "(int ylarajanAlkuarvo)", ctor.isPublic());
        return ctor.invoke(alku);
    }

    private String kentta(String toString) {
        return toString.replace(luokanNimi + ".", "");
    }

    private String etunollaa(int arvo) {
        if (arvo > 9) {
            return "" + arvo;
        }

        return "0" + arvo;
    }

    private int etsiEka(String[] rivit) {
        for (int i = 0; i < rivit.length; i++) {
            if (rivit[i].matches(".*\\d\\d:\\d\\d")) {
                return i;
            }
        }

        return -1;
    }

    private void tarkistaEteneminen(String[] rivit, int alku) {
        try {
            String a = rivit[61 + alku + 1];
        } catch (ArrayIndexOutOfBoundsException a) {
            fail("Kellosi pitäisi edetä enemmän kuin 60 sekuntia!");
        }

        int sekunninPaasta = 1;
        String pitaisi = "00:01";
        assertTrue(sekunninPaasta + " sekunnin päästä kellon pitäisi olla " + pitaisi, rivit[sekunninPaasta + alku].contains(pitaisi));

        sekunninPaasta = 60;
        pitaisi = "01:00";
        assertTrue(sekunninPaasta + " sekunnin päästä kellon pitäisi olla " + pitaisi, rivit[sekunninPaasta + alku].contains(pitaisi));

        sekunninPaasta = 61;
        pitaisi = "01:01";
        assertTrue(sekunninPaasta + " sekunnin päästä kellon pitäisi olla " + pitaisi, rivit[sekunninPaasta + alku].contains(pitaisi));

    }

    private void tarkistaTarkanKellonEteneminen(String[] rivit, int alku) {
        try {
            String a = rivit[15 + alku + 1];
        } catch (ArrayIndexOutOfBoundsException a) {
            fail("Kellosi pitäisi edetä enemmän kuin 15 sekuntia!");
        }
        
        int sekunninPaasta = 1;
        String pitaisi = "23:59:51";
        assertTrue("alkuarvolla 23:59:50, " + sekunninPaasta + " sekunnin päästä kellon pitäisi olla " + pitaisi, rivit[sekunninPaasta + alku].contains(pitaisi));

        sekunninPaasta = 9;
        pitaisi = "23:59:59";
        assertTrue("alkuarvolla 23:59:50, " + sekunninPaasta + " sekunnin päästä kellon pitäisi olla " + pitaisi, rivit[sekunninPaasta + alku].contains(pitaisi));

        sekunninPaasta = 10;
        pitaisi = "00:00:00";
        assertTrue("alkuarvolla 23:59:50, " + sekunninPaasta + " sekunnin päästä kellon pitäisi olla " + pitaisi, rivit[sekunninPaasta + alku].contains(pitaisi));

        sekunninPaasta = 11;
        pitaisi = "00:00:01";
        assertTrue("alkuarvolla 23:59:50, " + sekunninPaasta + " sekunnin päästä kellon pitäisi olla " + pitaisi, rivit[sekunninPaasta + alku].contains(pitaisi));
    }
}
