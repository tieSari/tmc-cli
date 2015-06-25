
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class OpiskelijaTest {

    String luokanNimi = "Opiskelija";
    Class luokka;
    Reflex.ClassRef<Object> klass;
    String klassName = "Opiskelija";

    @Before
    public void haeLuokka() {
        klass = Reflex.reflect(klassName);
    }

    @Points("88.1")
    @Test
    public void luokkaJulkinen() {
        assertTrue("Luokan " + klassName + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Points("88.1")
    @Test
    public void testaaKonstruktori() throws Throwable {
        Reflex.MethodRef2<Object, Object, String, String> ctor = klass.constructor().taking(String.class, String.class).withNiceError();
        assertTrue("Määrittele luokalle " + klassName + " julkinen konstruktori: public " + klassName + "(String nimi, String opiskelijanumero)", ctor.isPublic());
        ctor.invoke("Pekka", "1234567");
    }

    public Object luo(String nimi, String opnro) throws Throwable {
        Reflex.MethodRef2<Object, Object, String, String> ctor = klass.constructor().taking(String.class, String.class).withNiceError();
        return ctor.invoke(nimi, opnro);
    }

    @Points("88.1")
    @Test
    public void mainEiHeitaPoikkeusta() {
        Method m = null;
        try {
            String[] args = new String[0];
            m = ReflectionUtils.requireMethod(Main.class, "main", args.getClass());
        } catch (Throwable e) {
            e.printStackTrace();
            fail("main-metodi puuttuu: " + e.getMessage());
        }

        String virhe = "";
        if (m.toString().indexOf("thr") > 0) {
            virhe = m.toString().substring(+m.toString().indexOf("thr"));
        }

        assertFalse("main-metodin ei tule heittää poikkeusta, poista määrittelystä: " + virhe, m.toString().contains("throws"));
    }

    @Points("88.1")
    @Test
    public void eiYlimaaraisiaMuuttujia() {
        saniteettitarkastus(2, "nimen ja opiskelijanumeron muistavat oliomuuttujat");
    }

    @Points("88.1")
    @Test
    public void metodiHaeNimi() throws Throwable {
        String metodi = "haeNimi";

        Reflex.ClassRef<Object> tuoteClass = Reflex.reflect(klassName);

        Object olio = luo("Pekka", "123456");

        assertTrue("tee luokalle " + klassName + " metodi public String " + metodi + "() ", tuoteClass.method(olio, metodi)
                .returning(String.class).takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi Opiskelija o = new Opiskelija(\"Pekka\",\"123456\"); "
                + "o.haeNimi();";

        assertEquals("Tarkasta koodi Opiskelija o = new Opiskelija(\"Pekka\",\"123456\"); "
                + "o.haeNimi();", "Pekka", tuoteClass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke());

        tuoteClass.method(new Object(), metodi).returningVoid().takingNoParams().withNiceError(metodi);
    }

    @Points("88.1")
    @Test
    public void metodiHaeOpiskelijanumero() throws Throwable {
        Class c = ReflectionUtils.findClass(luokanNimi);
        String metodi = "haeOpiskelijanumero";

        Reflex.ClassRef<Object> tuoteClass = Reflex.reflect(klassName);

        Object olio = luo("Pekka", "123456");

        assertTrue("tee luokalle " + klassName + " metodi public String " + metodi + "() ", tuoteClass.method(olio, metodi)
                .returning(String.class).takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi Opiskelija o = new Opiskelija(\"Pekka\",\"123456\"); "
                + "o.haeOpiskelijanumero();";

        assertEquals("Tarkasta koodi o = new Opiskelija(\"Pekka\",\"123456\"); o.haeOpiskelijanumero();", "123456", tuoteClass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke());

    }

    @Points("88.1")
    @Test
    public void toStringOikein() throws Throwable {
        String nimi = "Teemu Teekkari \\o/";
        String opiskelijanumero = "12345T";
        Object opiskelija = newOpiskelija(nimi, opiskelijanumero);
        String vastaus = toString(opiskelija);

        assertFalse("Tee " + luokanNimi + "-luokalle metodi public String toString() tehtävänannon ohjeen mukaan", vastaus.contains("@"));
        assertTrue("luotiin opiskelija = new Opiskelija(\"" + nimi + "\", \"" + opiskelijanumero + "\"), mutta metodi toString() ei toimi oikein, tulostuu: " + vastaus,
                vastaus.contains(nimi) && vastaus.contains(opiskelijanumero));
    }
    
        @Points("88.1")
    @Test
    public void toStringOikeinValitKunnossa() throws Throwable {
        String nimi = "Teemu Teekkari \\o/";
        String opiskelijanumero = "12345T";
        Object opiskelija = newOpiskelija(nimi, opiskelijanumero);
        String vastaus = toString(opiskelija);

        assertFalse("Tee " + luokanNimi + "-luokalle metodi public String toString() tehtävänannon ohjeen mukaan", vastaus.contains("@"));
        assertTrue("luotiin opiskelija = new Opiskelija(\"" + nimi + "\", \"" + opiskelijanumero + "\"), mutta metodi toString() ei toimi oikein, tulostuu: " + vastaus+"\nOnhan Opiskelijan nimen ja opiskelijanumeron välissä välilyönti?",
                vastaus.contains(" ("));
    }


    @Points("88.2")
    @Test
    public void kysyyNatisti() throws Throwable {
        String[] inputRows = new String[]{
            "Arto Vihavainen", "33442111", "", "hopi"
        };

        String input = "";
        for (String row : inputRows) {
            input = input + row + "\n";
        }

        MockInOut mio = new MockInOut(input);
        try {
            Main.main(new String[0]);
        } catch (NoSuchElementException e) {
            // ignore
        } catch (Throwable t) {
            fail("Ohjelma heittää poikkeuksen: " + t.toString());
        }

        String output = mio.getOutput();

        String v = "Syötteellä \"Arto Vihavainen\", \"33442111\", \"\")";

        assertTrue("Ohjelman pitäisi kysyä ensin käyttäjältä nimeä tulostamalla kysymys \"Nimi:\"",output.contains("imi"));
        assertTrue("Ohjelman pitäisi kysyä käyttäjältä opiskelijanumeroa tulostamalla kysymys \"Opiskelijanumero:\"",output.contains("skelijanumer"));
    
        assertFalse(v+" ohjelman pitäisi lopettaa kysely heti kun käyttäjä syöttää tyhjän nimen.\n"
                + "Nyt ohjelma kysyy vielä 'olemattomalle' opiskelijanumeroa!\n"
                + "Ohjelmsi tulostus on \n"+output,output.substring(output.indexOf("skelijanumero")+"skelijanumero".length()).contains("skelijanumero"));        
        
        
    }    
    
    @Points("88.2")
    @Test
    public void opiskelijalistaToimii1() throws Throwable {
        String[] inputRows = new String[]{
            "Arto Vihavainen", "33442111", "", "hopi"
        };

        String input = "";
        for (String row : inputRows) {
            input = input + row + "\n";
        }

        MockInOut mio = new MockInOut(input);
        try {
            Main.main(new String[0]);
        } catch (NoSuchElementException e) {
            // ignore
        } catch (Throwable t) {
            fail("Ohjelma heittää poikkeuksen: " + t.toString());
        }

        String output = mio.getOutput();

        assertTrue("Syötteellä \"Arto Vihavainen\", \"33442111\", \"\"  opiskelijalista ei tulostunut oikein \n"
                + "Tarkista, että pääohjelmasi toimii tehtävänannon esimerkin mukaisesti. \n"
                + "Tulostit: " + output,
                output.contains("Arto Vihavainen (33442111)"));

    }

    @Points("88.2")
    @Test
    public void opiskelijalistaToimii2() throws Throwable {
        String[] inputRows = new String[]{
            "Martti Tienari", "932847214", "Arto Vihavainen", "33442111",
            "Esko Ukkonen", "84732", "", "hip"
        };

        String input = "";
        for (String row : inputRows) {
            input = input + row + "\n";
        }

        MockInOut mio = new MockInOut(input);
        try {
            Main.main(new String[0]);
        } catch (NoSuchElementException e) {
            // ignore
        } catch (Throwable t) {
            fail("Ohjelma heittää poikkeuksen: " + t.toString());
        }

        String output = mio.getOutput();

        for (String row : inputRows) {
            if (row.isEmpty()) {
                break;
            }
            assertTrue("Opiskelijalistasta ei löytynyt kaikkia testin syöttämiä opiskelijoita. \n"
                    + "Tarkista, että pääohjelmasi toimii tehtävänannon esimerkin mukaisesti. Ohjelmasi tulosti:\n" + output,
                    output.contains(row));
        }
    }

    @Points("88.3")
    @Test
    public void opiskelijahakuTesti0() throws Throwable {
        String[] inputRows = new String[]{
            "Martti", "123", "Arto", "333", "", "to"
        };

        String[] expectedResults = new String[]{
            "Arto", "333"
        };
        
        String[] unExpectedResults = new String[]{
            "Matti", "123"
        };
        String input = "";
        for (String row : inputRows) {
            input = input + row + "\n";
        }

        MockInOut mio = new MockInOut(input);
        try {
            Main.main(new String[0]);
        } catch (NoSuchElementException e) {
            // ignore
        } catch (Throwable t) {
            fail("Ohjelma heittää poikkeuksen: " + t.toString());
        }

        String output = mio.getOutput();
        int index = output.indexOf("Tulokset");
        if (index < 0) {
            fail("Ohjelman pitäisi tulostaa sana \"Tulokset\" ennen hakutuloksia. \n"
                    + "Tarkista, että pääohjelmasi toimii tehtävänannon esimerkin mukaisesti.");
        }

        output = output.substring(index);
        for (String row : expectedResults) {
            assertTrue("Opiskelijahaku ei tuottanut testin odottamia tuloksia syötteellä"
                    + "\"Martti\" \"123\" \"Arto\" \"333\" ja hakusanalla \"to\"\n"
                    + "Tarkista, että pääohjelmasi toimii tehtävänannon esimerkin mukaisesti. Ohjelmasi tulostus:\n" + output,
                    output.contains(row));
            
        }
        for (String row : unExpectedResults) {
             assertFalse("Opiskelijahaku tulosti liikaa syötteellä"
                    + "\"Martti\" \"123\" \"Arto\" \"333\" ja hakusanalla \"to\"\n"
                    + "Tarkista, että pääohjelmasi toimii tehtävänannon esimerkin mukaisesti. Ohjelmasi tulostus:\n" + output,
                    output.contains(row));
        }
    }

    @Points("88.3")
    @Test
    public void opiskelijahakuTesti1() throws Throwable {
        String[] inputRows = new String[]{
            "Martti Tienari", "932847214", "Arto Vihavainen", "33442111",
            "Esko Ukkonen", "84732R", "", "kone"
        };

        String[] expectedResults = new String[]{
            "Esko Ukkonen", "84732R"
        };
        String[] unExpectedResults = new String[]{
            "Martti Tienari", "932847214", "Arto Vihavainen", "33442111"
        };

        String input = "";
        for (String row : inputRows) {
            input = input + row + "\n";
        }

        MockInOut mio = new MockInOut(input);
        try {
            Main.main(new String[0]);
        } catch (NoSuchElementException e) {
            // ignore
        } catch (Throwable t) {
            fail("Ohjelma heittää poikkeuksen: " + t.toString());
        }

        String output = mio.getOutput();
        int index = output.indexOf("Tulokset");
        if (index < 0) {
            fail("Ohjelman pitäisi tulostaa sana \"Tulokset\" ennen hakutuloksia. \n"
                    + "Tarkista, että pääohjelmasi toimii tehtävänannon esimerkin mukaisesti.");
        }

        output = output.substring(index);

        for (String row : expectedResults) {
            assertTrue("Opiskelijahaku ei tuottanut testin odottamia tuloksia. \n"
                    + "Tarkista, että pääohjelmasi toimii tehtävänannon esimerkin mukaisesti.",
                    output.contains(row));
        }
        for (String row : unExpectedResults) {
             assertFalse("Opiskelijahaku tulosti liikaa syötteellä"
                    + "\"Martti\" \"123\" \"Arto\" \"333\" ja hakusanalla \"to\"\n"
                    + "Tarkista, että pääohjelmasi toimii tehtävänannon esimerkin mukaisesti. Ohjelmasi tulostus:\n" + output,
                    output.contains(row));
        }
    }

    @Points("88.3")
    @Test
    public void opiskelijahakuTesti2() throws Throwable {
        String[] inputRows = new String[]{
            "Eka opiskelija", "932847214", "Toka opiskelija", "33442111",
            "Kolmas hopiskelija", "84732R", "", "ka"
        };

        String[] expectedResults = new String[]{
            "Eka opiskelija", "932847214", "Toka opiskelija", "33442111",};
         String[] unExpectedResults = new String[]{
            "Kolmas hopiskelija", "84732R"
        };
        String input = "";
        for (String row : inputRows) {
            input = input + row + "\n";
        }

        MockInOut mio = new MockInOut(input);
        try {
            Main.main(new String[0]);
        } catch (NoSuchElementException e) {
            // ignore
        } catch (Throwable t) {
            fail("Ohjelma heittää poikkeuksen: " + t.toString());
        }

        String output = mio.getOutput();
        int index = output.indexOf("Tulokset");
        if (index < 0) {
            fail("Ohjelman pitäisi tulostaa sana \"Tulokset\" ennen hakutuloksia. \n"
                    + "Tarkista, että pääohjelmasi toimii tehtävänannon esimerkin mukaisesti.");
        }

        output = output.substring(index);

        for (String row : expectedResults) {
            assertTrue("Opiskelijahaku ei tuottanut testin odottamia tuloksia. \n"
                    + "Tarkista, että pååohjelmasi toimii tehtävänannon esimerkin mukaisesti.",
                    output.contains(row));
        }
                for (String row : unExpectedResults) {
             assertFalse("Opiskelijahaku tulosti liikaa syötteellä"
                    + "\"Martti\" \"123\" \"Arto\" \"333\" ja hakusanalla \"to\"\n"
                    + "Tarkista, että pääohjelmasi toimii tehtävänannon esimerkin mukaisesti. Ohjelmasi tulostus:\n" + output,
                    output.contains(row));
        }
    }

    private String toString(Object kortti) throws Throwable {
        Method toString = ReflectionUtils.requireMethod(luokka, "toString");
        return ReflectionUtils.invokeMethod(String.class, toString, kortti);
    }

    private Object newOpiskelija(String nimi, String opiskelijanumero) {
        try {
            luokka = ReflectionUtils.findClass(luokanNimi);
            return ReflectionUtils.invokeConstructor(luokka.getConstructor(String.class, String.class), nimi, opiskelijanumero);
        } catch (Throwable t) {
            fail("tarkista että seuraava onnistuu pääohjelmassa:\n "
                    + " Opiskelija op = new Opiskelija(\"" + nimi + "\", \"" + opiskelijanumero + "\");");
        }
        return null;
    }

    private void saniteettitarkastus(int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("et tarvitse \"stattisia muuttujia\", poista " + kentta(field.toString()), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("luokan kaikkien oliomuuttujien näkyvyyden tulee olla private, muuta löytyi: " + kentta(field.toString()), field.toString().contains("private"));
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

    private String kentta(String toString) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "");
    }
}
