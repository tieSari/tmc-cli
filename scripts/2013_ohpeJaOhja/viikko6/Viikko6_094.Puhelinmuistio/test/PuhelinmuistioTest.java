
import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;

public class PuhelinmuistioTest {

    @Rule
    public MockStdio stdio = new MockStdio();
    Reflex.ClassRef<Object> klassH;
    String klassNameH = "Henkilo";
    Reflex.ClassRef<Object> klassP;
    String klassNameP = "Puhelinmuistio";

    @Before
    public void haeLuokka() {
        klassH = Reflex.reflect(klassNameH);
    }

    @Points("94.1")
    @Test
    public void luokkaHenkiloJulkinen() {
        assertTrue("Luokan " + klassNameH + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassNameH + " {...\n}", klassH.isPublic());
    }

    @Points("94.1")
    @Test
    public void eiYlimaaraisiaMuuttujiaLuokassaHenkilo() {
        saniteettitarkastus("Henkilo", 2, " nimen ja puhelinnumeron muistavat oliomuuttujat");
    }

    @Points("94.1")
    @Test
    public void testaaHenkiloKonstruktori() throws Throwable {
        Reflex.MethodRef2<Object, Object, String, String> ctor = klassH.constructor().taking(String.class, String.class).withNiceError();
        assertTrue("Määrittele luokalle " + klassNameH + " julkinen konstruktori: public " + klassNameH + "(String nimi, String puhelinnumero)", ctor.isPublic());
        ctor.invoke("Pekka", "040-123654");
    }

    @Points("94.1")
    @Test
    public void henkilonToString() throws Throwable {
        Object h = luoHenkilo("Pekka", "040-123654");

        assertFalse("toteuta luokalle Henkilo toString-metodi", h.toString().contains("@"));

        assertTrue("Varmista että luokan Henkilo toString-metodi toimii tehtävänannon esimerkin mukaan\n"
                + "h = new Henkilo(\"Pekka\", \"040-123654\"); System.out.print(h) tulostaa nyt:\n" + h.toString(), h.toString().contains("Pekka") && h.toString().contains("puh") && h.toString().contains("040-123654"));
    }

    @Points("94.1")
    @Test
    public void henkilonMetodiHaeNimi() throws Throwable {
        String metodi = "haeNimi";

        Reflex.ClassRef<Object> klass = Reflex.reflect(klassNameH);

        Object olio = luoHenkilo("Pekka", "040-123654");

        assertTrue("tee luokalle " + klassNameH + " metodi public String " + metodi + "() ", klass.method(olio, metodi)
                .returning(String.class).takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi h = new Henkilo(\"Pekka\", \"040-123654\"); "
                + "h.haeNimi();";

        assertEquals("Tarkasta koodi h = new Henkilo(\"Pekka\", \"040-123654\"); "
                + "h.haeNimi();", "Pekka", klass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke());

        olio = luoHenkilo("Jukka", "040-123654");

        assertTrue("tee luokalle " + klassNameH + " metodi public String " + metodi + "() ", klass.method(olio, metodi)
                .returning(String.class).takingNoParams().isPublic());

        v = "\nVirheen aiheuttanut koodi h = new Henkilo(\"Jukka\", \"040-123654\"); "
                + "h.haeNimi();";

        assertEquals("Tarkasta koodi h = new Henkilo(\"Jukka\", \"040-123654\"); "
                + "h.haeNimi();", "Jukka", klass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke());
    }

    @Points("94.1")
    @Test
    public void henkilonMetodiHaeNumero() throws Throwable {
        String metodi = "haeNumero";

        Reflex.ClassRef<Object> klass = Reflex.reflect(klassNameH);

        Object olio = luoHenkilo("Pekka", "040-123654");

        assertTrue("tee luokalle " + klassNameH + " metodi public String " + metodi + "() ", klass.method(olio, metodi)
                .returning(String.class).takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi h = new Henkilo(\"Pekka\", \"040-123654\"); "
                + "h.haeNumero();";

        assertEquals("Tarkasta koodi h = new Henkilo(\"Pekka\", \"040-123654\"); "
                + "h.haeNumero();", "040-123654", klass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke());

        olio = luoHenkilo("Pekka", "040-333444");

        assertTrue("tee luokalle " + klassNameH + " metodi public String " + metodi + "() ", klass.method(olio, metodi)
                .returning(String.class).takingNoParams().isPublic());

        v = "\nVirheen aiheuttanut koodi h = new Henkilo(\"Pekka\", \"040-333444\"); "
                + "h.haeNumero();";

        assertEquals("Tarkasta koodi h = new Henkilo(\"Pekka\", \"040-333444\"); "
                + "h.haeNumero();", "040-333444", klass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke());
    }

    @Points("94.1")
    @Test
    public void henkilonMetodiVaihdaNumeroa() throws Throwable {
        String metodi = "vaihdaNumeroa";

        Reflex.ClassRef<Object> klass = Reflex.reflect(klassNameH);

        Object olio = luoHenkilo("Pekka", "040-123654");

        assertTrue("tee luokalle " + klassNameH + " metodi public void " + metodi + "(String uusiNumero) ", klass.method(olio, metodi)
                .returningVoid().taking(String.class).isPublic());

        String v = "\nVirheen aiheuttanut koodi h = new Henkilo(\"Pekka\", \"040-123654\"); "
                + "h.vaihdaNumero(\"050-444666\");";

        klass.method(olio, metodi)
                .returningVoid().taking(String.class).withNiceError(v).invoke("050-444666");

        v = "Toimiiko metodi vaihdaNumeroa oikein?\n"
                + "h = new Henkilo(\"Pekka\", \"040-123654\"); "
                + "h.vaihdaNumero(\"050-444666\"); h.haeNumero()";

        assertEquals(v, "050-444666", klass.method(olio, "haeNumero")
                .returning(String.class).takingNoParams().withNiceError(v).invoke());

        v = "Toimiiko metodi vaihdaNumeroa oikein? Tarkasta koodi\n"
                + "h = new Henkilo(\"Pekka\", \"040-123654\"); "
                + "h.vaihdaNumero(\"050-444666\"); System.out.println(h). Tulostui:\n";

        assertTrue(v + olio.toString(), olio.toString().contains("050-444666"));

    }

    /*
     * 
     */
    @Points("94.2")
    @Test
    public void luokkaPuhelinmuistioJulkinen() {
        klassP = Reflex.reflect(klassNameP);
        assertTrue("Luokan " + klassNameP + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassNameP + " {...\n}", klassP.isPublic());
    }

    @Points("94.2")
    @Test
    public void eiYlimaaraisiaMuuttujiaLuokassaPuhelinmuistio() {
        saniteettitarkastus("Puhelinmuistio", 1, " henkilöt muistavan ArrayList<Henkilo>-tyyppisen oliomuuttujan");
    }

    @Points("94.2")
    @Test
    public void puhelinmuistoillaOnArrayList() {
        String klassName = "Puhelinmuistio";
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        boolean on = false;

        for (Field field : kentat) {
            if (field.toString().contains("ArrayList")) {
                on = true;
            }
        }

        assertTrue("Lisää luokalle " + klassName + " ArrayList<Henkilo>-tyyppinen oliomuuttuja", on);
    }

    @Points("94.2")
    @Test
    public void testaaPuhelinmuistionKonstruktori() throws Throwable {
        klassP = Reflex.reflect(klassNameP);
        Reflex.MethodRef0<Object, Object> ctor = klassP.constructor().takingNoParams().withNiceError();
        assertTrue("Luokalla " + klassNameP + "pitää olla parametriton konstruktori: public " + klassNameP + "()", ctor.isPublic());
        ctor.invoke();
    }

    @Points("94.2")
    @Test
    public void puhelinmuistionMetodiLisaa() throws Throwable {
        klassP = Reflex.reflect(klassNameP);
        String metodi = "lisaa";

        Reflex.ClassRef<Object> klass = Reflex.reflect(klassNameP);

        Object olio = klassP.constructor().takingNoParams().invoke();

        assertTrue("tee luokalle " + klassNameP + " metodi public void " + metodi + "(String nimi, String numero)", klass.method(olio, metodi)
                .returningVoid().taking(String.class, String.class).isPublic());

        String v = "\nVirheen aiheuttanut koodi p = new Puhelinmuistio(); p.lisaa(\"Pekka\", \"040-123654\");";

        klass.method(olio, metodi)
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("Pekka", "040-123654");

        Field lista = ReflectionUtils.findClass(klassNameP).getDeclaredFields()[0];
        lista.setAccessible(true);
        ArrayList hlot = (ArrayList) lista.get(olio);

        assertFalse("Puhelinmuiston henkilölista null! \n"
                + "Luo lista konstruktorissa komennolla this." + lista.getName() + " = new ArrayList<Henkilo>();", hlot == null);

        assertEquals("Varmista että koodi koodi p = new Puhelinmuistio(); p.lisaa(\"Pekka\", \"040-123654\");"
                + " luo Henkilo-olion joka asetetaan puhelinmuistion sisällä olevalle listalle. \n"
                + "Listan koko: ", 1, hlot.size());

        v = "\nVirheen aiheuttanut koodi p = new Puhelinmuistio(); p.lisaa(\"Pekka\", \"040-123654\");"
                + "p.lisaa(\"Jukka\", \"045-222333\");";

        klass.method(olio, metodi)
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("Jukka", "040-123654");

        assertEquals("Varmista että koodi koodi p = new Puhelinmuistio(); "
                + "p.lisaa(\"Pekka\", \"040-123654\"); p.lisaa(\"Jukka\", \"045-222333\");"
                + " luo Henkilo-olion joka asetetaan puhelinmuistion sisällä olevalle listalle. Listan koko: ", 2, hlot.size());
    }

    @Points("94.2")
    @Test
    public void puhelinmuistionMetodiTulostaKaikki() throws Throwable {
        klassP = Reflex.reflect(klassNameP);
        String metodi = "tulostaKaikki";

        Reflex.ClassRef<Object> klass = Reflex.reflect(klassNameP);

        Object muistio = klassP.constructor().takingNoParams().invoke();

        assertTrue("tee luokalle " + klassNameP + " metodi public void " + metodi + "()", klass.method(muistio, metodi)
                .returningVoid().takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi p = new Puhelinmuistio(); p.tulostaKaikki();";

        klass.method(muistio, "lisaa")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("Pekka", "040-123654");

        v = "p = new Puhelinmuistio(); p.lisaa(\"Pekka\", \"040-123654\"); p.tulostaKaikki();";

        klass.method(muistio, metodi).returningVoid().takingNoParams().invoke();

        String out = stdio.getSysOut();

        assertTrue("Testaa koodi " + v + "\nNyt ei tulostu mitään!", out.length() > 0);

        assertTrue("Testaa koodi " + v + "\nNyt tulostuu:\n" + out, out.contains("Pekka") && out.contains("040-123654"));


        v = "p = new Puhelinmuistio(); "
                + "p.lisaa(\"Pekka\", \"040-123654\"); "
                + "p.lisaa(\"Jukka\", \"045-332211\"); "
                + "p.tulostaKaikki();";

        klass.method(muistio, "lisaa")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("Jukka", "045-332211");

        int pit = out.length();

        klass.method(muistio, metodi).returningVoid().takingNoParams().invoke();

        String out2 = stdio.getSysOut();

        out = stdio.getSysOut().substring(pit);

        assertTrue("Testaa koodi " + v + "\nNyt tulostuu:\n" + out, out.contains("Pekka") && out.contains("040-123654"));
        assertTrue("Testaa koodi " + v + "\nNyt tulostuu:\n" + out, out.contains("Jukka") && out.contains("045-332211"));

        v = "p = new Puhelinmuistio(); "
                + "p.lisaa(\"Pekka\", \"040-123654\"); "
                + "p.lisaa(\"Jukka\", \"045-332211\"); "
                + "p.lisaa(\"Liisa\", \"050-525252\"); "
                + "p.tulostaKaikki();";

        klass.method(muistio, "lisaa")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("Liisa", "050-525252");

        pit += out.length();

        klass.method(muistio, metodi).returningVoid().takingNoParams().invoke();

        out2 = stdio.getSysOut();
        out = stdio.getSysOut().substring(pit);

        assertTrue("Testaa koodi " + v + "\nNyt tulostuu:\n" + out, out.contains("Pekka") && out.contains("040-123654"));
        assertTrue("Testaa koodi " + v + "\nNyt tulostuu:\n" + out, out.contains("Jukka") && out.contains("045-332211"));
        assertTrue("Testaa koodi " + v + "\nNyt tulostuu:\n" + out, out.contains("Liisa") && out.contains("050-525252"));
        assertTrue("Testaa koodi " + v + "\nNyt tulostuu:\n" + out, out.split("\n").length > 2);

    }

    /*
     * 
     */
    @Points("94.3")
    @Test
    public void puhelinmuistionMetodiHaeNumero() throws Throwable {
        klassP = Reflex.reflect(klassNameP);
        String metodi = "haeNumero";

        Reflex.ClassRef<Object> klass = Reflex.reflect(klassNameP);

        Object muistio = klassP.constructor().takingNoParams().invoke();

        assertTrue("tee luokalle " + klassNameP + " metodi public String " + metodi + "(String etsittava)", klass.method(muistio, metodi)
                .returning(String.class).taking(String.class).isPublic());

        String v = "\nVirheen aiheuttanut koodi p = new Puhelinmuistio(); p.haeNumero(\"Pekka\");";

        klass.method(muistio, metodi)
                .returning(String.class).taking(String.class).withNiceError(v).invoke("Pekka");

        klass.method(muistio, "lisaa")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("Pekka", "040-123654");

        v = "Tarkista koodi p = new Puhelinmuistio(); "
                + "p.lisaa(\"Pekka\", \"040-123654\"); "
                + "p.lisaa(\"Jukka\", \"045-332211\"); ";
        
         klass.method(muistio, "lisaa")
                .returningVoid().taking(String.class, String.class).withNiceError(v).invoke("Jukka", "045-332211");

         assertEquals(v+" p.haeNumero(\"Pekka\");\n", "040-123654", klass.method(muistio, metodi)
                .returning(String.class).taking(String.class).withNiceError(v).invoke("Pekka"));
         
         assertEquals(v+" p.haeNumero(\"Jukka\");\n","045-332211", klass.method(muistio, metodi)
                .returning(String.class).taking(String.class).withNiceError(v).invoke("Jukka"));
         
         String tulos = klass.method(muistio, metodi)
                .returning(String.class).taking(String.class).withNiceError(v).invoke("Mikko");
                  
         assertTrue(v+" p.haeNumero(\"Mikko\");\n", tulos.contains("ei") );          
         
//         assertEquals(v+" p.haeNumero(\"Mikko\");\n","numero ei tiedossa", klass.method(muistio, metodi)
//                .returning(String.class).taking(String.class).withNiceError(v).invoke("Mikko"));         
    }

    /*
     * 
     */
    public Object luoHenkilo(String nimi, String nro) throws Throwable {
        Reflex.MethodRef2<Object, Object, String, String> ctor = klassH.constructor().taking(String.class, String.class).withNiceError();
        return ctor.invoke(nimi, nro);
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
        return toString.replace(klassName + ".", "").replace("java.lang.", "");
    }
}
