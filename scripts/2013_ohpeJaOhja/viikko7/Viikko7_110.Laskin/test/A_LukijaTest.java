
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.*;
import static org.junit.Assert.*;

@Points("110.1")
public class A_LukijaTest {

    String klassName = "Lukija";
    Reflex.ClassRef<Object> klass;
    String luokanNimi = "Lukija";
    Class c;

    @Before
    public void setup() {
        klass = Reflex.reflect(klassName);
        try {
            c = ReflectionUtils.findClass(luokanNimi);
        } catch (Throwable e) {
        }
    }

    @Test
    public void luokkaJulkinen() {
        assertTrue("Luokan " + klassName + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void eiYlimaaraisiaMuuttujia() {
        saniteettitarkastus(klassName, 1, "Scanner-olion tallettavan oliomuuttujan");
    }

    @Test
    public void onMetodiLueMerkkijono() throws Throwable {
        new MockInOut("kahvi\nmaito\npowerking\n");
        String metodi = "lueMerkkijono";

        String vv = "virheen aiheuttanut koodi Lukija lukija = new Lukija();";
        Object olio = klass.constructor().takingNoParams().withNiceError(vv).invoke();


        assertTrue("tee luokalle " + klassName + " metodi public String " + metodi + "() ", klass.method(olio, metodi)
                .returning(String.class).takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi  Lukija lukija = new Lukija(); "
                + "lukija.lueMerkkijono();";

        klass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke();
    }

    @Test
    public void eiPoikkeuksiaMerkkijonoaLukiessa() {
        new MockInOut("kahvi\nmaito\npowerking\n");
        Object olio = newLukija();

        String syote = "kahvi<enter>maito<enter>powerking<enter>";
        try {
            lueMerkkijono(olio);
            lueMerkkijono(olio);
            lueMerkkijono(olio);
        } catch (Throwable t) {
            if (t.toString().contains("such element")) {
                fail("Käyttäjän syöte " + syote + ", lukija.lueMerkkijono(); lukija.lueMerkkijono(); System.out.print( lukija.lueMerkkijono() ); aiheutti poikkeuksien " + t + "\n"
                        + "et kai kutsu Scannerin metodia liian monta kertaa?");
            } else {
                fail("Käyttäjän syöte " + syote + ", lukija.lueMerkkijono(); lukija.lueMerkkijono(); System.out.print( lukija.lueMerkkijono() ); aiheutti poikkeuksien " + t);
            }
        }
    }

    @Test
    public void palauttaaMerkkijonon() throws Throwable {
        new MockInOut("testi\n");
        Object olio = newLukija();
        String vast1 = lueMerkkijono(olio);
        String syote = "testi<enter>";
        assertEquals("Käyttäjän syöte " + syote + ", kutsutaan lukija.lueMerkkijono() ", "testi", vast1);
    }

    @Test
    public void palauttaaMontaMerkkijonoa() throws Throwable {
        MockInOut io = new MockInOut("kahvi\nmaito\npowerking\ncoca cola\n");
        Object olio = newLukija();

        String syote = "kahvi<enter>maito<enter>powerking<enter>coca cola<enter>";

        String vast1 = lueMerkkijono(olio);
        String vast2 = lueMerkkijono(olio);
        String vast3 = lueMerkkijono(olio);
        String vast4 = lueMerkkijono(olio);

        assertEquals("Käyttäjän syöte " + syote + ", System.out.print( lukija.lueMerkkijono() );", "kahvi", vast1);
        assertEquals("Käyttäjän syöte " + syote + ", lukija.lueMerkkijono(); System.out.print( lukija.lueMerkkijono() ); ", "maito", vast2);
        assertEquals("Käyttäjän syöte " + syote + ", lukija.lueMerkkijono(); lukija.lueMerkkijono(); System.out.print( lukija.lueMerkkijono() ); ", "powerking", vast3);
        assertFalse("Käyttäjän syöte oli \"coca cola\", komento lukija.lueMerkkijono() palautti \"coca\" \n"
                + "etkai käytä Scannerin metodia next(), lue aina koko rivi eli käytö metodia nextLine()!", vast4.equals("coca"));
        assertEquals("Käyttäjän syöte " + syote + ", lukija.lueMerkkijono(); lukija.lueMerkkijono(); System.out.print( lukija.lueMerkkijono() ); \n"
                + "", "coca cola", vast4);
    }

    @Test
    public void onMetodiLueKokonaisluku() throws Throwable {
        String metodi = "lueKokonaisluku";
        new MockInOut("1\n2\n3\n");

        String vv = "virheen aiheuttanut koodi Lukija lukija = new Lukija();";
        Object olio = klass.constructor().takingNoParams().withNiceError(vv).invoke();


        assertTrue("tee luokalle " + klassName + " metodi public int " + metodi + "() ", klass.method(olio, metodi)
                .returning(int.class).takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi  Lukija lukija = new Lukija(); "
                + "lukija.lueKokonaisluku();";

        klass.method(olio, metodi)
                .returning(int.class).takingNoParams().withNiceError(v).invoke();        
        
//        String virhe = "tee luokalle " + luokanNimi + " metodi public int " + metodi + "()";
//        Method m = null;
//        try {
//            m = ReflectionUtils.requireMethod(c, metodi);
//        } catch (Throwable t) {
//            fail(virhe);
//        }
//        assertTrue(virhe, m.toString().contains("public"));
//        assertFalse(virhe, m.toString().contains("static"));
    }

    @Test
    public void eiPoikkeuksiaLukuaLukiessa() {
        new MockInOut("4\n-3\n16\n");
        Object olio = newLukija();

        String syote = "4<enter>-3<enter>16<enter>";
        try {
            lueKokonaisluku(olio);
            lueKokonaisluku(olio);
            lueKokonaisluku(olio);
        } catch (Throwable t) {
            if (t.toString().contains("such element")) {
                fail("Käyttäjän syöte " + syote + ", lukija.lueKokonaisluku(); lukija.lueKokonaisluku(); System.out.print( lukija.lueKokonaisluku() ); aiheutti poikkeuksien " + t + "\n"
                        + "et kai kutsu Scannerin metodia liian monta kertaa?");
            } else {
                fail("Käyttäjän syöte " + syote + ", lukija.lueKokonaisluku(); lukija.lueKokonaisluku(); System.out.print( lukija.lueKokonaisluku() ); aiheutti poikkeuksien " + t);
            }
        }
    }

    @Test
    public void palauttaaLuvun() throws Throwable {
        new MockInOut("4\n");
        Object olio = newLukija();
        int vast1 = lueKokonaisluku(olio);
        String syote = "4<enter>";
        assertEquals("Käyttäjän syöte " + syote + ", kutsutaan lukija.lueKokonaisluku() ", 4, vast1);
    }

    @Test
    public void palauttaaMontaLukua() throws Throwable {
        MockInOut io = new MockInOut("4\n-3\n16\n");
        Object olio = newLukija();

        String syote = "4<enter>-3<enter>16<enter>";
        int vast1 = lueKokonaisluku(olio);
        int vast2 = lueKokonaisluku(olio);
        int vast3 = lueKokonaisluku(olio);
        assertEquals("Käyttäjän syöte " + syote + ", System.out.print( lukija.lueKokonaisluku() ); ", 4, vast1);
        assertEquals("Käyttäjän syöte " + syote + ", lukija.lueKokonaisluku(); System.out.print( lukija.lueKokonaisluku() ); ", -3, vast2);
        assertEquals("Käyttäjän syöte " + syote + ", lukija.lueKokonaisluku(); lukija.lueKokonaisluku(); System.out.print( lukija.lueKokonaisluku() ); ", 16, vast3);
    }

    @Test
    public void eiPoikkeuksiaSekasinLuettaessa() {
        MockInOut io = new MockInOut("java\n4\nolio\n16\nmetodi\n");
        Object olio = newLukija();

        String syote = "java<enter>4<enter>olio<enter>16<enter>metodi<enter>";
        try {
            lueMerkkijono(olio);
            lueKokonaisluku(olio);
            lueMerkkijono(olio);
            lueKokonaisluku(olio);
            lueMerkkijono(olio);
        } catch (Throwable t) {
            if (t.toString().contains("such element")) {
                fail("Käyttäjän syöte " + syote + ", lukija.lueMerkkijono(); lukija.lueKokonaisluku(); lukija.lueMerkkijono(); lukija.lueKokonaisluku(); lukija.lueMerkkijono();  aiheutti poikkeuksien " + t + "\n"
                        + "et kai kutsu Scannerin metodia liian monta kertaa?");
            } else {
                fail("Käyttäjän syöte " + syote + ", lukija.lueMerkkijono(); lukija.lueKokonaisluku(); lukija.lueMerkkijono(); lukija.lueKokonaisluku(); lukija.lueMerkkijono();  aiheutti poikkeuksien " + t + "\n"
                        + "etkai käytä kokonaisluvun lukemiseen Scannerin metodia nextInt()?"
                        + " jos, niin käytä sen tilalla komentoa Integer.parseInt( skanneri.nextLine() ); ");
            }
        }
    }

    @Test
    public void toimiiSekasinLuettaessa() throws Throwable {
        MockInOut io = new MockInOut("java\n4\nolio\n16\nmetodi\n");
        Object olio = newLukija();

        String syote = "java<enter>4<enter>olio<enter>16<enter>metodi<enter>";

        String v1 = lueMerkkijono(olio);
        int v2 = lueKokonaisluku(olio);
        String v3 = lueMerkkijono(olio);
        int v4 = lueKokonaisluku(olio);
        String v5 = lueMerkkijono(olio);

        assertEquals("Käyttäjän syöte " + syote + ", "
                + "System.out.print( lukija.lueMerkkijono() ); ", "java", v1);
        assertEquals("Käyttäjän syöte " + syote + ", "
                + "lukija.lueMerkkijono() ; System.out.print( lukija.lueKokonaisluku() ); ", 4, v2);
        assertEquals("Käyttäjän syöte " + syote + ", "
                + "lukija.lueMerkkijono(); lukija.lueKokonaisluku(); System.out.print( lukija.lueMerkkijono() ); ", "olio", v3);
        assertEquals("Käyttäjän syöte " + syote + ", "
                + "lukija.lueMerkkijono(); lukija.lueKokonaisluku(); lukija.lueMerkkijono(); System.out.print(lukija.lueKokonaisluku() ); ", 16, v4);
        assertEquals("Käyttäjän syöte " + syote + ", "
                + "lukija.lueMerkkijono(); lukija.lueKokonaisluku(); lukija.lueMerkkijono(); lukija.lueKokonaisluku() ; System.out.print(lukija.lueMerkkijono() ); ", "metodi", v5);

    }

    private Object newLukija() {
        try {
            c = ReflectionUtils.findClass(luokanNimi);
            return ReflectionUtils.invokeConstructor(c.getConstructor());
        } catch (Throwable t) {
            fail("tarkista että seuraava onnistuu pääohjelmassa:  Lukija lukija = new Lukija();");
        }
        return null;
    }

    private String lueMerkkijono(Object lukija) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "lueMerkkijono");
            return ReflectionUtils.invokeMethod(String.class, metodi, lukija);
        } catch (Throwable t) {
            throw t;
        }
    }

    private int lueKokonaisluku(Object lukija) throws Throwable {
        try {
            Method metodi = ReflectionUtils.requireMethod(c, "lueKokonaisluku");
            return ReflectionUtils.invokeMethod(int.class, metodi, lukija);
        } catch (Throwable t) {
            throw t;
        }
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
