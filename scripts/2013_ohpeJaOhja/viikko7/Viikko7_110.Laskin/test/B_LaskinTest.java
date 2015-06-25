
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class B_LaskinTest {
    String klassName = "Laskin";
    Reflex.ClassRef<Object> klass;
    
    String luokanNimi = "Laskin";
    Class c;

    @Before
    public void setup() {
        klass = Reflex.reflect(klassName);
        try {
            c = ReflectionUtils.findClass(luokanNimi);
        } catch (Throwable e) {
        }
    }

    @Points("110.2")
    @Test
    public void luokkaJulkinen() {
        assertTrue("Luokan " + klassName + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassName + " {...\n}", klass.isPublic());
    }
    
    @Points("110.2")
    @Test
    public void eiYlimaaraisiaMuuttujia() {
        saniteettitarkastus(klassName, 2, "Lukija-olion tallettavan sekä suoritettujen operaatioiden määrän "
                + "muistavan oliomuuttujan");
    }    
    
    @Points("110.2")
    @Test
    public void onMetodiKaynnista() throws Throwable {                
        String metodi = "kaynnista";
        new MockInOut("lopetus\n");

        String vv = "virheen aiheuttanut koodi Laskin laskin = new Laskin();";
        Object olio = klass.constructor().takingNoParams().withNiceError(vv).invoke();


        assertTrue("tee luokalle " + klassName + " metodi public void " + metodi + "() ", klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi  Laskin laskin = new Laskin(); "
                + "laskin.kaynnista();";

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError(v).invoke();                
    }

    @Points("110.2")
    @Test
    public void onSumma() throws Throwable {
        String metodi = "summa";
        
        new MockInOut("2\n3\n");

        String vv = "virheen aiheuttanut koodi Laskin laskin = new Laskin();";
        Object olio = klass.constructor().takingNoParams().withNiceError(vv).invoke();


        assertTrue("tee luokalle " + klassName + " metodi private void " + metodi + "() ", klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPrivate());

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError().invoke();          
        
    }

    @Points("110.2")
    @Test
    public void onErotus() throws Throwable {
        new MockInOut("2\n3\n");        
        String metodi = "erotus";
        String vv = "virheen aiheuttanut koodi Laskin laskin = new Laskin();";
        Object olio = klass.constructor().takingNoParams().withNiceError(vv).invoke();

        assertTrue("tee luokalle " + klassName + " metodi private void " + metodi + "() ", klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPrivate());

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError().invoke();          
        
    }

    @Points("110.2")
    @Test
    public void onTulo() throws Throwable {
        new MockInOut("2\n3\n");        
        String metodi = "tulo";
        String vv = "virheen aiheuttanut koodi Laskin laskin = new Laskin();";
        Object olio = klass.constructor().takingNoParams().withNiceError(vv).invoke();

        assertTrue("tee luokalle " + klassName + " metodi private void " + metodi + "() ", klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPrivate());

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError().invoke();          

    }

    @Points("110.2")
    @Test
    public void onStatistiikka() throws Throwable {
        String metodi = "statistiikka";
        String vv = "virheen aiheuttanut koodi Laskin laskin = new Laskin();";
        Object olio = klass.constructor().takingNoParams().withNiceError(vv).invoke();


        assertTrue("tee luokalle " + klassName + " metodi private void " + metodi + "() ", klass.method(olio, metodi)
                .returningVoid().takingNoParams().isPrivate());

        klass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError().invoke();                  
    }

    @Points("110.2")
    @Test
    public void onLukijaMutteiSkanneria() {
        Field[] kentat = ReflectionUtils.findClass(luokanNimi).getDeclaredFields();

        int lukija = 0;
        for (Field field : kentat) {
            if (field.toString().contains("Lukija")) {
                lukija++;
            }
            assertFalse("älä käytä luokassa Laskin suoraan Scanneria, eli poista " + kentta(field.toString()), field.toString().contains("Scanner"));
            assertFalse("et tarvitse \"stattisia muuttujia\", poista " + kentta(field.toString()), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("luokan kaikkien oliomuuttujien näkyvyyden tulee olla private, muuta " + kentta(field.toString()), field.toString().contains("private"));
        }
        assertTrue("luokassa laskin tulee olla oliomuuttujana Lukija-tyyppinen olio", lukija == 1);

        lueKoodi();
    }

    @Points("110.3")
    @Test
    public void summaToimii1() throws Throwable {
        MockInOut mio = new MockInOut("2\n3\n");
        Object laskin = newLaskin();
        String met = "summa";
        try {
            doStuff(met, laskin);
        } catch (Exception e) {
            fail("metodin " + met + " kutsuminen aiheuttaa poikkeuksen\n"
                    + "syy saattaa olla se että Lukija-olioa ei ole luotu konstruktorissa.");
        }
        String tulos = mio.getOutput();

        assertTrue("toimiiko metodi " + met + " oikein? valittaessa " + met + " ja syötettäessä 2 ja 3 tulostui " + tulos, tulos.contains("5"));
    }

    @Points("110.3")
    @Test
    public void summaToimii2() throws Throwable {
        int l1 = 10;
        int l2 = -4;
        int t = 6;
        MockInOut mio = new MockInOut(l1 + "\n" + l2 + "\n");
        Object laskin = newLaskin();
        String met = "summa";
        try {
            doStuff(met, laskin);
        } catch (Exception e) {
            fail("metodin " + met + " kutsuminen aiheuttaa poikkeuksen\n"
                    + "syy saattaa olla se että Lukija-olioa ei ole luotu konstruktorissa.");
        }
        String tulos = mio.getOutput();

        assertTrue("toimiiko metodi " + met + " oikein? valittaessa " + met + " ja syötettäessä " + l1 + " ja " + l2 + " tulostui " + tulos, tulos.contains("" + t));
    }

    @Points("110.3")
    @Test
    public void erotusToimii() throws Throwable {
        int l1 = 10;
        int l2 = 4;
        int t = 6;
        MockInOut mio = new MockInOut(l1 + "\n" + l2 + "\n");
        Object laskin = newLaskin();
        String met = "erotus";
        try {
            doStuff(met, laskin);
        } catch (NullPointerException e) {
            fail("metodin " + met + " kutsuminen aiheuttaa poikkeuksen\n"
                    + "syy saattaa olla se että Lukija-olioa ei ole luotu konstruktorissa.");
        }
        String tulos = mio.getOutput();

        assertTrue("toimiiko metodi " + met + " oikein? valittaessa " + met + " ja syötettäessä " + l1 + " ja " + l2 + " tulostui " + tulos, tulos.contains("" + t));
    }

    @Points("110.3")
    @Test
    public void erotusToimii2() throws Throwable {
        int l1 = 11;
        int l2 = 19;
        int t = -8;
        MockInOut mio = new MockInOut(l1 + "\n" + l2 + "\n");
        Object laskin = newLaskin();
        String met = "erotus";
        try {
            doStuff(met, laskin);
        } catch (Exception e) {
            fail("metodin " + met + " kutsuminen aiheuttaa poikkeuksen!");
        }
        String tulos = mio.getOutput();

        assertTrue("toimiiko metodi " + met + " oikein? valittaessa " + met + " ja syötettäessä " + l1 + " ja " + l2 + " tulostui " + tulos, tulos.contains("" + t));
    }

    @Points("110.3")
    @Test
    public void tuloToimii() throws Throwable {
        int l1 = 3;
        int l2 = 7;
        int t = 21;
        MockInOut mio = new MockInOut(l1 + "\n" + l2 + "\n");
        Object laskin = newLaskin();
        String met = "tulo";
        try {
            doStuff(met, laskin);
        } catch (NullPointerException e) {
            fail("metodin " + met + " kutsuminen aiheuttaa poikkeuksen\n"
                    + "syy saattaa olla se että Lukija-olioa ei ole luotu konstruktorissa.");
        }
        String tulos = mio.getOutput();

        assertTrue("toimiiko metodi " + met + " oikein? valittaessa " + met + " ja syötettäessä " + l1 + " ja " + l2 + " tulostui " + tulos, tulos.contains("" + t));
    }

    @Points("110.3")
    @Test
    public void tuloToimii2() throws Throwable {
        int l1 = -4;
        int l2 = 3;
        int t = -12;
        MockInOut mio = new MockInOut(l1 + "\n" + l2 + "\n");
        Object laskin = newLaskin();
        String met = "tulo";

        try {
            doStuff(met, laskin);
        } catch (Exception e) {
            fail("metodin " + met + " kutsuminen aiheuttaa poikkeuksen!");
        }
        String tulos = mio.getOutput();

        assertTrue("toimiiko metodi " + met + " oikein? valittaessa " + met + " ja syötettäessä " + l1 + " ja " + l2 + " tulostui " + tulos, tulos.contains("" + t));
    }

    @Points("110.3")
    @Test
    public void montaKomentoaEiAiheutaPoikkeusta() throws Throwable {
        String input = "summa\n1\n2\ntulo\n2\n3\nerotus\n4\n2\nsumma\n3\n3\nlopetus\n";
        MockInOut mio = new MockInOut(input);
        Object laskin = newLaskin();
        input = input.replaceAll("\n", "<enter>");
        try {
            doStuff("kaynnista", laskin);
        } catch (Throwable e) {
            fail("new Laskin.kaynnista(); aiheuttaa poikkeuksen käyttäjän syötteellä " + input + ", lisätietoja " + e);

        }
        String[] tulos = mio.getOutput().split("\n");
    }

    @Points("110.3")
    @Test
    public void kokoSovellus1() throws Throwable {
        String input = "summa\n1\n2\nlopetus\n";
        MockInOut mio = new MockInOut(input);
        Object laskin = newLaskin();
        input = input.replaceAll("\n", "<enter>");
        try {
            doStuff("kaynnista", laskin);
        } catch (Throwable e) {
            fail("new Laskin.kaynnista(); aiheuttaa poikkeuksen käyttäjän syötteellä " + input + ", lisätietoja " + e);
        }
        String[] tulos = mio.getOutput().split("\n");
        String summa = hae(tulos, "summa");
        assertFalse("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa lukee \"summa\"", summa == null);
        assertTrue("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa \"lukujen summa 3\" " + summa, summa.contains("3"));
    }

    @Points("110.3")
    @Test
    public void kokoSovellus2() throws Throwable {
        String input = "summa\n1\n2\ntulo\n2\n3\nerotus\n4\n2\nlopetus\n";
        MockInOut mio = new MockInOut(input);
        Object laskin = newLaskin();
        input = input.replaceAll("\n", "<enter>");
        try {
            doStuff("kaynnista", laskin);
        } catch (Throwable e) {
            fail("new Laskin.kaynnista(); aiheuttaa poikkeuksen käyttäjän syötteellä " + input + ", lisätietoja " + e);
        }
        String[] tulos = mio.getOutput().split("\n");
        String res = hae(tulos, "summa");
        assertFalse("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa lukee \"summa\"", res == null);
        assertTrue("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa \"lukujen summa 3\" " + res, res.contains("3"));

        res = hae(tulos, "tulo");
        assertFalse("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa lukee \"tulo\"", res == null);
        assertTrue("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa \"lukujen tulo 6\" " + res, res.contains("6"));

        res = hae(tulos, "erotus");
        assertFalse("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa lukee \"erotus\"", res == null);
        assertTrue("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa \"lukujen erotus 2\" " + res, res.contains("2"));
    }

    @Points("110.4")
    @Test
    public void statistiikkaTulostaa() throws Throwable {

        MockInOut mio = new MockInOut("");
        Object laskin = newLaskin();
        String met = "statistiikka";

        try {
            doStuff(met, laskin);
        } catch (Exception e) {
            fail("metodin " + met + " kutsuminen aiheuttaa poikkeuksen!");
        }
        String tulos = mio.getOutput();

        assertTrue("Metodin statistiikka kutsun tulee tulostaa merkkijono \"laskuja laskettiin n\", missä n kokonaisluku", tulos.contains("laskettiin"));
    }

    @Points("110.4")
    @Test
    public void statistiikka1() throws Throwable {
        String input = "lopetus\n";
        MockInOut mio = new MockInOut(input);
        Object laskin = newLaskin();
        input = input.replaceAll("\n", "<enter>");
        try {
            doStuff("kaynnista", laskin);
        } catch (Throwable e) {
            fail("new Laskin.kaynnista(); aiheuttaa poikkeuksen käyttäjän syötteellä " + input + ", lisätietoja " + e);
        }
        String[] tulos = mio.getOutput().split("\n");
        String res = hae(tulos, "laskettiin");
        assertFalse("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa lukee \"laskuja laskettiin\"", res == null);
        assertTrue("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa \"laskuja laskettiin 0\" " + res, res.contains("0"));

        int lask = 0;
        for (String rivi : tulos) {
            if (rivi.contains("laskettiin")) {
                lask++;
            }
        }
        assertTrue("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa lukee \"laskuja laskettiin\" vain kerran!", lask == 1);
    }

    @Points("110.4")
    @Test
    public void statistiikka2() throws Throwable {
        String input = "summa\n1\n2\ntulo\n2\n3\nerotus\n4\n2\nlopetus\n";
        MockInOut mio = new MockInOut(input);
        Object laskin = newLaskin();
        input = input.replaceAll("\n", "<enter>");
        try {
            doStuff("kaynnista", laskin);
        } catch (Throwable e) {
            fail("new Laskin.kaynnista(); aiheuttaa poikkeuksen käyttäjän syötteellä " + input + ", lisätietoja " + e);
        }
        String[] tulos = mio.getOutput().split("\n");
        String res = hae(tulos, "laskettiin");
        assertFalse("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa lukee \"laskuja laskettiin\"", res == null);
        assertTrue("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa \"laskuja laskettiin 3\" " + res, res.contains("3"));

    }

    @Points("110.4")
    @Test
    public void statistiikka3() throws Throwable {
        String input = "summa\n1\n2\nrajaarvo\nerotus\n4\n2\nlopetus\n";
        MockInOut mio = new MockInOut(input);
        Object laskin = newLaskin();
        input = input.replaceAll("\n", "<enter>");
        try {
            doStuff("kaynnista", laskin);
        } catch (Throwable e) {
            fail("new Laskin.kaynnista(); aiheuttaa poikkeuksen käyttäjän syötteellä " + input + "\n"
                    + "huomaathan että jos ohjelmalle annetaan tuntematon komento niinkun nyt käy, ohjelman tulee hyvätä komento ja jatkaa kysymällä seuraava komento\n lisätietoja " + e);

        }
        String[] tulos = mio.getOutput().split("\n");
        String res = hae(tulos, "laskettiin");
        assertFalse("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa lukee \"laskuja laskettiin\"", res == null);
        assertTrue("Syötteellä " + input + " new Laskin.kaynnista(); pitäisi tulostaa rivi jossa \"laskuja laskettiin 2\" " + res, res.contains("2"));
    }

    private Object newLaskin() throws Throwable {
        try {
            c = ReflectionUtils.findClass(luokanNimi);
            return ReflectionUtils.invokeConstructor(c.getConstructor());
        } catch (Throwable t) {
            try {
                Class laskinClass = Class.forName(luokanNimi);
                java.lang.reflect.Constructor[] laskinConstructor = laskinClass.getDeclaredConstructors();
                laskinConstructor[0].setAccessible(true);
                return laskinConstructor[0].newInstance();
            } catch (Throwable t2) {
            }
            throw t;
        }
    }

    private void doStuff(String meto, Object olio) throws Throwable {
        try {
            Method metodi = haeMetodi(c, meto);
            metodi.setAccessible(true);
            metodi.invoke(olio);
            //ReflectionUtils.invokeMethod(void.class, metodi, olio);
        } catch (Throwable t) {
            throw t;
        }
    }

    private void kaynnista(Object olio) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "kaynnista");
            ReflectionUtils.invokeMethod(void.class, metodi, olio);
        } catch (Throwable t) {
            throw t;
        }
    }

    private String kentta(String toString) {
        return toString.replace(luokanNimi + ".", "");
    }

    private Method haeMetodi(Class c, String nimi) {
        for (Method m : c.getDeclaredMethods()) {
            if (m.getName().equals(nimi)) {
                String mj = m.toString();
                if (mj.contains("()") && mj.contains("void")) {
                    return m;
                }
            }
        }
        return null;
    }

    private void lueKoodi() {
        int lukijoita = 0;
        try {
            Scanner lukija = new Scanner(new File("src/Laskin.java"));
            StringBuilder sb = new StringBuilder();

            while (lukija.hasNextLine()) {
                sb.append(lukija.nextLine());

            }
            String rivi = sb.toString().replaceAll("\\s+", "");
            if (rivi.contains("Scanner(")) {
                fail("Laskin-luokassa ei saa käyttää Scannereita!");
            }
            if (rivi.contains("newLukija(")) {
                lukijoita++;
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals("Laskin-luokassa tulee luoda Lukija-olio täsmälleen kerran, nyt luotuja Lukijoita", 1, lukijoita);
    }

    private String hae(String[] rivit, String sana) {
        for (String rivi : rivit) {
            if (rivi.contains(sana)) {
                return rivi;
            }
        }

        return null;
    }
    
        private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("et tarvitse \"stattisia muuttujia\", poista luokalta " + klassName + " muuttuja " + kentta(field.toString(), klassName), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("luokan kaikkien oliomuuttujien näkyvyyden tulee olla private, muuta luokalta " + klassName + " löytyi: " + kentta(field.toString(), klassName), field.toString().contains("private"));
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
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "");
    }
}
