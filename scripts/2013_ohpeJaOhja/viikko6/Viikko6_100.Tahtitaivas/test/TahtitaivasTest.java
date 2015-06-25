
import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class TahtitaivasTest {

    @Rule
    public MockStdio stdio = new MockStdio();
    private String tahtitaivasLuokka = "Tahtitaivas";
    private Class clazz;
    private String tulostaRiviMetodi = "tulostaRivi";
    private String tulostaMetodi = "tulosta";
    private String tahtiaViimeTulostuksessaMetodi = "tahtiaViimeTulostuksessa";
    Reflex.ClassRef<Object> klass;
    String klassName = "Tahtitaivas";

    @Before
    public void haeLuokka() {
        klass = Reflex.reflect(klassName);
        clazz = ReflectionUtils.findClass(tahtitaivasLuokka);
    }

    @Points("100.1")
    @Test
    public void luokkaJulkinen() {
        assertTrue("Luokan " + klassName + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Points("100.1 100.2 100.3")
    @Test
    public void eiYlimaaraisia() {
        saniteettitarkastus(klassName, 4, "leveyden ja korkeuden sekä tähtien tiheyden ja edellisen tulostuksen tähtimäärän muistavat oliomuuttujat");
    }

    @Points("100.1")
    @Test
    public void onKonstruktoriParametrillaTiheys() throws Throwable {
        Reflex.MethodRef1<Object, Object, Double> ctor = klass.constructor().taking(double.class).withNiceError();
        assertTrue("Määrittele luokalle " + klassName + " julkinen konstruktori: public " + klassName + "(double tiheys)", ctor.isPublic());
        ctor.invoke(25.5);
    }

    @Test
    @Points("100.1")
    public void onKonstruktoriParametreillaLeveysJaKorkeus() throws Throwable {
        Reflex.MethodRef2<Object, Object, Integer, Integer> ctor = klass.constructor().taking(int.class, int.class).withNiceError();
        assertTrue("Määrittele luokalle " + klassName + " julkinen konstruktori: public " + klassName + "(int leveys, int korkeus)", ctor.isPublic());
        ctor.invoke(50, 20);
    }

    @Test
    @Points("100.1")
    public void onKonstruktoriParametreillaTiheysLeveysJaKorkeus() throws Throwable {
        Reflex.MethodRef3<Object, Object, Double, Integer, Integer> ctor = klass.constructor().taking(double.class, int.class, int.class).withNiceError();
        assertTrue("Määrittele luokalle " + klassName + " julkinen konstruktori: public " + klassName + "(double tiheys, int leveys, int korkeus)", ctor.isPublic());
        ctor.invoke(25.0, 50, 20);
    }

    @Test
    @Points("100.1")
    public void onMetodiTulostaRivi() throws Throwable {
        String metodi = tulostaRiviMetodi;

        Object olio = klass.constructor().taking(double.class, int.class, int.class).invoke(20.0, 10, 5);

        assertTrue("tee luokalle " + klassName + " metodi public void " + metodi + "() ", klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi Tahtitaivas t = new(20.0, 10, 5); t.tulostaRivi();";

        klass.method(olio, metodi).returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points("100.1")
    public void tulostaRiviTulostaaJotain() {
        Constructor tiheysKonstruktori = ReflectionUtils.requireConstructor(clazz, double.class);
        Object inst = null;
        try {
            inst = ReflectionUtils.invokeConstructor(tiheysKonstruktori, 1.0);
        } catch (Throwable ex) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on konstruktori public " + tahtitaivasLuokka + "(double tiheys).");
        }

        int tahdet = 0;
        Method m = ReflectionUtils.requireMethod(clazz, tulostaRiviMetodi);

        try {
            ReflectionUtils.invokeMethod(void.class, m, inst);
            String out = stdio.getSysOut();
            if (out == null || out.isEmpty()) {
                fail("Tarkista että metodi " + tulostaRiviMetodi + " tulostaa edes jotain.");
            }
        } catch (Throwable t) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on metodi public void " + tulostaRiviMetodi + "() ja että sen kutsuminen tulostaa edes jotain.");
        }
    }

    @Test
    @Points("100.1")
    public void eiWhilea() {
        noForbiddens();
    }

    @Test
    @Points("100.1")
    public void tulostaRiviOletusLeveys() {
        Constructor tiheysKonstruktori = ReflectionUtils.requireConstructor(clazz, double.class);
        Object inst = null;
        try {
            inst = ReflectionUtils.invokeConstructor(tiheysKonstruktori, 1.0);
        } catch (Throwable ex) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on konstruktori public " + tahtitaivasLuokka + "(double tiheys).");
        }

        int tahdet = 0;
        Method m = ReflectionUtils.requireMethod(clazz, tulostaRiviMetodi);

        String out = "";
        try {
            ReflectionUtils.invokeMethod(void.class, m, inst);
            out = stdio.getSysOut();
            tahdet = laskeTahdet(out);
        } catch (Throwable t) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on metodi public void " + tulostaRiviMetodi + "().");
        }

        int loppu = Math.max(0,out.length()-2);
        assertFalse( "metodin "+tulostaRiviMetodi+" ei tulisi tulostaa rivinvaihtoa kuin korkeintaan lopussa. Tulostus koodilla"
                + "Tahtitaivas t = new Tahtitaivas(1.0); t.tulostaRivi();\n"
                + out,out.substring(0, loppu).contains("\n"));
        
        if (tahdet != 20) {
            fail("Varmista että konstruktori " + tahtitaivasLuokka + "(double tiheys) asettaa leveyden arvoksi 20 ja korkeuden arvoksi 10. \n"
                    + "Tarkista myös että metodi tulostaRivi tulostaa rivin leveyden verran tähtiä jos tiheys on 1. Nyt tulostus oli \n"
                    + out);
        }
    }

    @Test
    @Points("100.1")
    public void tulostaRiviAnnettuLeveys() {
        Constructor tiheysKonstruktori = ReflectionUtils.requireConstructor(clazz, double.class, int.class, int.class);
        Object inst = null;
        try {
            inst = ReflectionUtils.invokeConstructor(tiheysKonstruktori, 1.0, 5, 10);
        } catch (Throwable ex) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on konstruktori public " + tahtitaivasLuokka + "(double tiheys, int leveys, int korkeus).");
        }

        int tahdet = 0;
        Method m = ReflectionUtils.requireMethod(clazz, tulostaRiviMetodi);

        try {
            ReflectionUtils.invokeMethod(void.class, m, inst);
            String out = stdio.getSysOut();
            tahdet = laskeTahdet(out);
        } catch (Throwable t) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on metodi public void " + tulostaRiviMetodi + "().");
        }

        if (tahdet != 5) {
            fail("Varmista että konstruktori " + tahtitaivasLuokka + "(double tiheys, int leveys, int korkeus) asettaa oliomuuttujien arvoiksi annetut parametrit. Jos annat tiheydeksi 1, leveydeksi 5 ja korkeudeksi 10, tulisi yhdellä rivillä olla 5 tähteä.");
        }
    }

    @Test
    @Points("100.1")
    public void tulostaRiviAnnettuLeveys2() {
        Constructor tiheysKonstruktori = ReflectionUtils.requireConstructor(clazz, double.class, int.class, int.class);
        Object inst = null;
        try {
            inst = ReflectionUtils.invokeConstructor(tiheysKonstruktori, 1.0, 7, 10);
        } catch (Throwable ex) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on konstruktori public " + tahtitaivasLuokka + "(double tiheys, int leveys, int korkeus).");
        }

        int tahdet = 0;
        Method m = ReflectionUtils.requireMethod(clazz, tulostaRiviMetodi);

        try {
            ReflectionUtils.invokeMethod(void.class, m, inst);
            String out = stdio.getSysOut();
            tahdet = laskeTahdet(out);
        } catch (Throwable t) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on metodi public void " + tulostaRiviMetodi + "().");
        }

        if (tahdet != 7) {
            fail("Varmista että konstruktori " + tahtitaivasLuokka + "(double tiheys, int leveys, int korkeus) asettaa oliomuuttujien arvoiksi annetut parametrit. Jos annat tiheydeksi 1, leveydeksi 7 ja korkeudeksi 10, tulisi yhdellä rivillä olla 7 tähteä.");
        }
    }

    @Test
    @Points("100.1")
    public void tulostaRiviPieniTiheysAnnettuLeveys() {
        Constructor tiheysKonstruktori = ReflectionUtils.requireConstructor(clazz, double.class, int.class, int.class);
        Object inst = null;
        try {
            inst = ReflectionUtils.invokeConstructor(tiheysKonstruktori, 0.1, 10, 10);
        } catch (Throwable ex) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on konstruktori public " + tahtitaivasLuokka + "(double tiheys, int leveys, int korkeus).");
        }

        int tahdet = 0;
        Method m = ReflectionUtils.requireMethod(clazz, tulostaRiviMetodi);

        try {
            for (int i = 0; i < 1000; i++) {
                ReflectionUtils.invokeMethod(void.class, m, inst);
            }
            String out = stdio.getSysOut();
            tahdet = laskeTahdet(out);
        } catch (Throwable t) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on metodi public void " + tulostaRiviMetodi + "().");
        }

        if (tahdet < 500 || tahdet > 1500) {
            fail("Varmista että konstruktori " + tahtitaivasLuokka + "(double tiheys, int leveys, int korkeus) asettaa oliomuuttujien arvoiksi annetut parametrit. Jos annat tiheydeksi 0.1, tulisi noin 10% alueesta olla tähtiä.");
        }
    }

    @Test
    @Points("100.2")
    public void onMetodiTulosta() throws Throwable {
        String metodi = "tulosta";

        Object olio = klass.constructor().taking(double.class, int.class, int.class).invoke(20.0, 10, 5);

        assertTrue("tee luokalle " + klassName + " metodi public void " + metodi + "() ", klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi Tahtitaivas t = new(20.0, 10, 5); t.tulosta();";

        klass.method(olio, metodi).returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points("100.2")
    public void tulostaOletusLeveysJaKorkeus() {
        Constructor tiheysKonstruktori = ReflectionUtils.requireConstructor(clazz, double.class);
        Object inst = null;
        try {
            inst = ReflectionUtils.invokeConstructor(tiheysKonstruktori, 1.0);
        } catch (Throwable ex) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on konstruktori public " + tahtitaivasLuokka + "(double tiheys).");
        }

        int tahdet = 0;
        Method m = ReflectionUtils.requireMethod(clazz, tulostaMetodi);

        String out = "";
        try {
            ReflectionUtils.invokeMethod(void.class, m, inst);
            out = stdio.getSysOut();
            tahdet = laskeTahdet(out);
        } catch (Throwable t) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on metodi public void " + tulostaMetodi + "().");
        }

        if (tahdet != 200) {
            fail("Varmista että konstruktori " + tahtitaivasLuokka + "(double tiheys) asettaa leveyden arvoksi 20 ja korkeuden arvoksi 10. Tarkista myös että metodi " + tulostaMetodi + " tulostaa 200 tähteä jos tiheys on 1.\n"
                    + "Nyt tähtiä on "+tahdet+", koko tulostus:\n"+out);
        }

        if (!out.contains("\n")) {
            fail("Varmista että tähtien tulostaminen tulostaa tähtitaivaan, ei tähtiriviä. Tähtitaivaassa tulee olla myös rivinvaihtoja..");
        }
    }

    @Test
    @Points("100.2")
    public void tulostaOletusTiheys() {
        Constructor tiheysKonstruktori = ReflectionUtils.requireConstructor(clazz, int.class, int.class);
        Object inst = null;
        try {
            inst = ReflectionUtils.invokeConstructor(tiheysKonstruktori, 50, 10);
        } catch (Throwable ex) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on konstruktori public " + tahtitaivasLuokka + "(int leveys, int korkeus).");
        }

        int tahdet = 0;
        Method m = ReflectionUtils.requireMethod(clazz, tulostaMetodi);

        try {
            for (int i = 0; i < 100; i++) {
                ReflectionUtils.invokeMethod(void.class, m, inst);
            }

            String out = stdio.getSysOut();
            tahdet = laskeTahdet(out);
        } catch (Throwable t) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on metodi public void " + tulostaMetodi + "().");
        }

        if (tahdet < 2500 || tahdet > 7500) {
            fail("Varmista että konstruktori " + tahtitaivasLuokka + "(int leveys, int korkeus) asettaa oliomuuttujan tiheys oletusarvoksi 0.1 -- tällöin noin 10% alueesta pitäisi olla tähtiä.");
        }
    }

    @Test
    @Points("100.3")
    public void onMetodiTahtiaViimeTulostuksessa() throws Throwable {
        String metodi = "tahtiaViimeTulostuksessa";

        Object olio = klass.constructor().taking(double.class, int.class, int.class).invoke(20.0, 10, 5);

        assertTrue("tee luokalle " + klassName + " metodi public int " + metodi + "() ", klass.method(olio, metodi)
                .returning(int.class).takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi Tahtitaivas t = new(20.0, 10, 5); t.tulosta(); t.tahtiaViimeTulostuksessa()";

        klass.method(olio, metodi).returning(int.class).takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points("100.3")
    public void tahtiaViimeTulostuksessaKertooTahtienMaaran() {
        Constructor tiheysKonstruktori = ReflectionUtils.requireConstructor(clazz, int.class, int.class);
        Object inst = null;
        try {
            inst = ReflectionUtils.invokeConstructor(tiheysKonstruktori, 50, 10);
        } catch (Throwable ex) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on konstruktori public " + tahtitaivasLuokka + "(int leveys, int korkeus).");
        }

        int tahdet;
        Method tulosta = ReflectionUtils.requireMethod(clazz, tulostaMetodi);

        Method tahtia = ReflectionUtils.requireMethod(clazz, tahtiaViimeTulostuksessaMetodi);

        for (int i = 0; i < 100; i++) {
            String out = stdio.getSysOut();
            try {
                ReflectionUtils.invokeMethod(void.class, tulosta, inst);
            } catch (Throwable ex) {
                fail("Varmista että luokalla " + tahtitaivasLuokka + " on metodi public void " + tulostaMetodi + "().");
            }

            out = stdio.getSysOut().substring(out.length());
            int nahtyTahtia = 0;
            try {
                nahtyTahtia = ReflectionUtils.invokeMethod(int.class, tahtia, inst);
            } catch (Throwable ex) {
                fail("Varmista että luokalla " + tahtitaivasLuokka + " on metodi public int " + tahtiaViimeTulostuksessaMetodi + "().");
            }
            tahdet = laskeTahdet(out);

            if (nahtyTahtia != tahdet) {
                fail("Varmista että metodi public int tahtiaViimeTulostuksessaMetodi() palauttaa viimeisimmässä tulostuksessa olleiden tähtien määrän. \n"
                        + "Tähtiä väitetään olevan "+nahtyTahtia+" vaikka niitä todellisuudessa on "+tahdet+" kun tulostettu tähtitaivas on\n "+out);
            }
        }
    }

    @Test
    @Points("100.3")
    public void kaikkiOkJaKatriHelena() {
        Constructor tiheysKonstruktori = ReflectionUtils.requireConstructor(clazz, int.class, int.class);
        Object inst = null;
        try {
            inst = ReflectionUtils.invokeConstructor(tiheysKonstruktori, 50, 10);
        } catch (Throwable ex) {
            fail("Varmista että luokalla " + tahtitaivasLuokka + " on konstruktori public " + tahtitaivasLuokka + "(int leveys, int korkeus).");
        }

        int tahdet = 0;
        Method tulosta = ReflectionUtils.requireMethod(clazz, tulostaMetodi);

        Method tahtia = ReflectionUtils.requireMethod(clazz, tahtiaViimeTulostuksessaMetodi);

        for (int i = 0; i < 100; i++) {
            String out = stdio.getSysOut();
            try {
                ReflectionUtils.invokeMethod(void.class, tulosta, inst);
            } catch (Throwable ex) {
                fail("Varmista että luokalla " + tahtitaivasLuokka + " on metodi public void " + tulostaMetodi + "().");
            }

            out = stdio.getSysOut().substring(out.length());
            int nahtyTahtia = 0;
            try {
                nahtyTahtia = ReflectionUtils.invokeMethod(int.class, tahtia, inst);
            } catch (Throwable ex) {
                fail("Varmista että luokalla " + tahtitaivasLuokka + " on metodi public int " + tahtiaViimeTulostuksessaMetodi + "().");
            }
            tahdet = laskeTahdet(out);

            if (nahtyTahtia != tahdet) {
                fail("Varmista että metodi public int tahtiaViimeTulostuksessaMetodi() palauttaa viimeisimmässä tulostuksessa olleiden tähtien määrän.");
            }
        }


        try {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if (desktop == null || !desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                return;
            }
            java.net.URI uri = new java.net.URI("http://www.youtube.com/watch?v=7M1jdJtOntI");
            desktop.browse(uri);
        } catch (Exception e) {
        }
    }

    private int laskeTahdet(String tuloste) {
        int tahdet = 0;
        for (char c : tuloste.toCharArray()) {
            if (c == '*') {
                tahdet++;
            }
        }

        return tahdet;
    }

    private void noForbiddens() {
        try {
            Scanner lukija = new Scanner(new File("src/Tahtitaivas.java"));
            while (lukija.hasNext()) {
                String rivi = lukija.nextLine();
                if (rivi.contains("while")) {
                    fail("Koska nyt harjoitellaan for-toistolauseketta, "
                            + "et saa käyttää ohjelmassasi while-komentoa.");
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("et tarvitse \"stattisia muuttujia\", poista luokalta " + klassName + " muuttuja " + kentta(field.toString(), klassName), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("luokan kaikkien oliomuuttujien näkyvyyden tulee olla private, muuta luokalta " + klassName + " löytyi: " + kentta(field.toString(), klassName), field.toString().contains("private"));
        }

        for (Field field : kentat) {
            if (field.toString().contains("Random")) {
                n++;
            }
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }
            assertTrue("et tarvitse " + klassName + "-luokalle kuin " + m + ", poista ylimääräiset", var <= n);
        }
    }

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "");
    }
}