
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import fi.helsinki.cs.tmc.edutestutils.Reflex.MethodRef1;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class AnalyysiTest {

    String luokanNimi = "Analyysi";
    Class analyysiClass;
    String klassName = "tiedosto.Analyysi";
    Reflex.ClassRef<Object> classRef;

    @Before
    public void setUp() {

        classRef = Reflex.reflect(klassName);
        try {
            analyysiClass = ReflectionUtils.findClass("tiedosto." + luokanNimi);
        } catch (Throwable t) {
            fail("Olethan luonut luokan " + luokanNimi + " joka on pakkauksessa tiedosto?");
        }
    }

    @Test
    @Points("129.1")
    public void luokkaAnalyysi() {
        classRef = Reflex.reflect(klassName);

        assertTrue("Luokan " + s(klassName) + " pitää olla julkinen, eli se tulee määritellä\n"
                + "public class " + s(klassName) + " {...\n}", classRef.isPublic());
    }

    @Test
    @Points("129.1")
    public void eiYlimaaraisiaMuuttujia() {
        saniteettitarkastus(klassName, 10, "");
    }

    @Test
    @Points("129.1")
    public void onKonstruktoriAnalyysi() throws Throwable {
        MethodRef1<Object, Object, File> ctor = classRef.constructor().taking(File.class).withNiceError();
        assertTrue("Määrittele luokalle " + s(klassName) + " julkinen konstruktori: \n"
                + "public " + s(klassName) + "(File tiedosto)", ctor.isPublic());
        String v = "virheen aiheutti koodi new Analyysi( new File(\"test/testitiedosto.txt\") );\n";
        ctor.withNiceError(v).invoke(new File("testitiedosto.txt"));
    }

    public Object luo(File file) throws Throwable {
        return classRef.constructor().taking(File.class).invoke(file);
    }

    @Test
    @Points("129.1")
    public void onMetodiRivimaara() throws Throwable {
        Object o = luo(new File("testitiedosto.txt"));


        assertTrue("Lisää luokalle Analyysi metodi public int rivimaara()", classRef.method(o, "rivimaara").returning(int.class).takingNoParams().isPublic());

        String k = "Analyysi a = new Analyysi( new File(\"test/testitiedosto.txt\") );\n"
                + "a.rivimaara();";

        assertEquals(k, 3, (int) classRef.method(o, "rivimaara").returning(int.class).takingNoParams().withNiceError(k).invoke());

    }

    @Test
    @Points("129.1")
    public void rivimaaraaVoidaanKutsuaUseitaKertoja() throws Throwable {
        Object o = luo(new File("testitiedosto.txt"));


        assertTrue("Lisää luokalle Analyysi metodi public int rivimaara()", classRef.method(o, "rivimaara").returning(int.class).takingNoParams().isPublic());

        String k = "Analyysi a = new Analyysi( new File(\"test/testitiedosto.txt\") );\n"
                + "a.rivimaara();\n";

        assertEquals(k, 3, (int) classRef.method(o, "rivimaara").returning(int.class).takingNoParams().withNiceError(k).invoke());

        k += "a.rivimaara();\n";

        assertEquals(k, 3, (int) classRef.method(o, "rivimaara").returning(int.class).takingNoParams().withNiceError(k).invoke());

        k += "a.rivimaara();\n";

        assertEquals(k, 3, (int) classRef.method(o, "rivimaara").returning(int.class).takingNoParams().withNiceError(k).invoke());

    }

    private void testaaRivimaaraMetodia(File tiedosto, int riveja, String tdst) {
        int rivit = -1;
        Object riviObj = luoAnalyysiOlio(tiedosto);

        Method rivimaaraMethod = ReflectionUtils.requireMethod(analyysiClass, "rivimaara");

        try {
            rivit = ReflectionUtils.invokeMethod(int.class, rivimaaraMethod, riviObj);
        } catch (Throwable t) {
            fail("Kaappaathan mahdolliset poikkeukset oikein?\n"
                    + "Rivimäärän laskemisessa tapahtui virhe: " + t);
        }
        assertEquals("Kun tiedostossa oli " + riveja + " riviä, ohjelmasi löysi niitä " + rivit + ".\n"
                + "tiedoston sisältö:\n"
                + "------\n"
                + tdst
                + "\n------\n",
                riveja, rivit);

    }

    private void testaaMerkkejaMetodia(File tiedosto, int merkkeja, String tdst) {
        int merkit = -1;
        Object riviObj = luoAnalyysiOlio(tiedosto);

        Method merkkejaMethod = ReflectionUtils.requireMethod(analyysiClass, "merkkeja");

        try {
            merkit = ReflectionUtils.invokeMethod(int.class, merkkejaMethod, riviObj);
        } catch (Throwable t) {
            fail("Kaappaathan mahdolliset poikkeukset oikein?\n"
                    + "Merkkien laskemisessa tapahtui virhe: " + t);
        }
        assertEquals("Kun tiedostossa oli " + merkkeja + " merkkiä, ohjelmasi löysi niitä " + merkit + ". "
                + "Laskethan myös rivinvaihdot merkeiksi? \n"
                + "tiedoston sisältö:\n"
                + "------\n"
                + tdst
                + "\n-------\n"
                + "", merkkeja, merkit);
    }

    private Object luoAnalyysiOlio(File tiedosto) {
        try {
            Constructor riviConstruktor = ReflectionUtils.requireConstructor(analyysiClass, File.class);
            return ReflectionUtils.invokeConstructor(riviConstruktor, tiedosto);

        } catch (Throwable e) {
        }

        return null;
    }

    @Test
    @Points("129.1")
    public void yksiRivi() {
        try {
            FileWriter n;
            File eka;
            eka = File.createTempFile("eka", "txt");

            n = new FileWriter(eka);
            n.append("sana1");
            n.flush();
            n.close();
            testaaRivimaaraMetodia(eka, 1, "sana1");
        } catch (IOException ex) {
            fail("Testitiedoston kirjoittaminen ei onnistunut! Lisätietoa: " + ex);
        }
    }

    @Test
    @Points("129.1")
    public void viisiRiviaTest() {
        try {
            FileWriter n;
            File eka;
            eka = File.createTempFile("eka", "txt");
            n = new FileWriter(eka);
            n.append("sana1\nsana2\nsana3\nsana4\nsana5\n");
            n.flush();
            n.close();
            testaaRivimaaraMetodia(eka, 5, "sana1\nsana2\nsana3\nsana4\nsana5");
        } catch (IOException ex) {
            fail("Testitiedoston kirjoittaminen ei onnistunut! Lisätietoa: " + ex);
        }
    }

    @Test
    @Points("129.1")
    public void kymmenenRiviaTest() {
        try {
            FileWriter n;
            File eka;
            eka = File.createTempFile("eka", "txt");
            n = new FileWriter(eka);
            n.append("sana1\naa\nbb\ncc\ndd  dd\nee e\nfff\nggg\nh\nsana1\n");
            n.flush();
            n.close();
            testaaRivimaaraMetodia(eka, 10, "sana1\naa\nbb\ncc\ndd  dd\nee e\nfff\nggg\nh\nsana1");
        } catch (IOException ex) {
            fail("Testitiedoston kirjoittaminen ei onnistunut! Lisätietoa: " + ex);
        }
    }

    @Test
    @Points("129.2")
    public void onMetodiMerkkeja() throws Throwable {
        Object o = luo(new File("test/testitiedosto.txt"));

        assertTrue("Lisää luokalle Analyysi metodi public int merkkeja()", classRef.method(o, "merkkeja").returning(int.class).takingNoParams().isPublic());

        String k = "Analyysi a = new Analyysi( new File(\"test/testitiedosto.txt\") );\n"
                + "a.merkkeja();\n";

        int tulos = classRef.method(o, "merkkeja").returning(int.class).takingNoParams().withNiceError(k).invoke();
        assertEquals(k, 67, (int) classRef.method(o, "merkkeja").returning(int.class).takingNoParams().withNiceError(k).invoke());
    }

    @Test
    @Points("129.2")
    public void viisiMerkkiaJaRivinvaihto() {
        try {
            FileWriter n;
            File eka;
            eka = File.createTempFile("eka", "txt");
            n = new FileWriter(eka);
            n.append("arska\n");
            n.flush();
            n.close();
            testaaMerkkejaMetodia(eka, 6, "arska");
        } catch (IOException ex) {
            fail("Testitiedoston kirjoittaminen ei onnistunut! Lisätietoa: " + ex);
        }
    }

    @Test
    @Points("129.2")
    public void kaksiMerkinRivia() {
        try {
            FileWriter n;
            File eka;
            eka = File.createTempFile("eka", "txt");
            n = new FileWriter(eka);
            n.append("a\nb\n");
            n.flush();
            n.close();
            testaaMerkkejaMetodia(eka, 4, "a\nb\n");
        } catch (IOException ex) {
            fail("Testitiedoston kirjoittaminen ei onnistunut! Lisätietoa: " + ex);
        }
    }

    @Test
    @Points("129.2")
    public void rivinvaihtoLasketaan() {
        try {
            FileWriter n;
            File eka;
            eka = File.createTempFile("eka", "txt");
            n = new FileWriter(eka);
            n.append("arska\nmusta\npekka\n");
            n.flush();
            n.close();
            testaaMerkkejaMetodia(eka, 18, "arska\nmusta\npekka");
        } catch (IOException ex) {
            fail("Testitiedoston kirjoittaminen ei onnistunut! Lisätietoa: " + ex);
        }
    }

    @Test
    @Points("129.2")
    public void molemmatMetoditToimiiPerakkain() throws Throwable {
        Object o = luo(new File("test/testitiedosto.txt"));

        assertTrue("Lisää luokalle Analyysi metodi public int merkkeja()", classRef.method(o, "merkkeja").returning(int.class).takingNoParams().isPublic());

        String k = "Analyysi a = new Analyysi( new File(\"test/testitiedosto.txt\") );\n"
                + "a.merkkeja();\n";

        assertEquals(k, 67, (int) classRef.method(o, "merkkeja").returning(int.class).takingNoParams().withNiceError(k).invoke());

        k += "a.riveja()";
        assertEquals(k, 3, (int) classRef.method(o, "rivimaara").returning(int.class).takingNoParams().withNiceError(k).invoke());
        k += "a.merkkeja()";
        assertEquals(k, 67, (int) classRef.method(o, "merkkeja").returning(int.class).takingNoParams().withNiceError(k).invoke());
        k += "a.riveja()";
        assertEquals(k, 3, (int) classRef.method(o, "rivimaara").returning(int.class).takingNoParams().withNiceError(k).invoke());


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
