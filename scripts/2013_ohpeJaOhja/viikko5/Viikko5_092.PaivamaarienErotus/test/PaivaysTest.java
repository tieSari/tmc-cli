
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;
import org.junit.*;
import static org.junit.Assert.*;

public class PaivaysTest {

    Reflex.ClassRef<Object> klass;
    String klassName = "Paivays";

    @Before
    public void haeLuokka() {
        klass = Reflex.reflect(klassName);
    }

    @Points("92.1")
    @Test
    public void eiLiikaaOliomuuttujiaPaivaykseen1() {
        saniteettitarkastus("Paivays", 3, "Älä lisää Paivays-luokalle uusia oliomuuttujia, niitä ei tarvita");
    }

    public void onMetodiErotusVuosissa() throws Throwable {
        String metodi = "erotusVuosissa";

        Paivays olio = new Paivays(1, 1, 2011);
        Paivays olio2 = new Paivays(1, 1, 2009);

        assertTrue("tee luokalle " + klassName + " metodi public int " + metodi + "(Paivays verrattava) ", klass.method(olio, metodi)
                .returning(int.class).taking(Paivays.class).isPublic());

        String v = "\nVirheen aiheuttanut koodi Paivays p = new Paivays(1, 1, 2011); Paivays p2 = new Paivays(1, 1, 2009); "
                + "p.erotusVuosissa(p2);";

        klass.method(olio, metodi)
                .returning(int.class).taking(Paivays.class).withNiceError(v).invoke(olio2);

    }

    @Points("92.1")
    @Test
    public void taysienVuosienErotus() {
        Paivays kolm = new Paivays(3, 7, 2011);
        Paivays toka = new Paivays(2, 6, 2010);
        Paivays eka = new Paivays(1, 5, 2000);

        tarkasta(toka, eka, 10);
        tarkasta(toka, eka, 10);
        tarkasta(kolm, toka, 1);
    }

    /*
     *
     */
    @Points("92.2")
    @Test
    public void eiLiikaaOliomuuttujiaPaivaykseen2() {
        saniteettitarkastus("Paivays", 3, "Älä lisää Paivays-luokalle uusia oliomuuttujia, niitä ei tarvita");
    }
    Random arpa = new Random();

    @Points("92.2")
    @Test
    public void taysienVuosienErotusToimiiEdelleen() {
        Paivays kolm = new Paivays(3, 7, 2011);
        Paivays toka = new Paivays(2, 6, 2010);
        Paivays eka = new Paivays(1, 5, 2000);

        tarkasta(toka, eka, 10);
        tarkasta(toka, eka, 10);
        tarkasta(kolm, toka, 1);
    }
    
    @Points("92.2")
    @Test
    public void vuosienErotusVerrataanAinaPienempaanKuuEri() {
        Paivays toka = new Paivays(10, 2, 2012);
        Paivays eka = new Paivays(9, 5, 2011);

        tarkasta(toka, eka, 0);

        toka = new Paivays(9, 2, 2012);
        eka = new Paivays(10, 5, 2011);

        tarkasta(toka, eka, 0);

        for (int i = 0; i < 4; i++) {
            int ensin = 1990 + arpa.nextInt(10);
            int vuodenPaasta = 1 + arpa.nextInt(10);

            toka = new Paivays(10, 2, ensin + vuodenPaasta);
            eka = new Paivays(9, 5, ensin);
            tarkasta(toka, eka, vuodenPaasta - 1);
        }
    }

    @Points("92.2")
    @Test
    public void vuosienErotusVerrataanAinaPienempaanKuuSama() {
        Paivays toka = new Paivays(9, 3, 2012);
        Paivays eka = new Paivays(10, 3, 2011);

        tarkasta(toka, eka, 0);

        toka = new Paivays(10, 3, 2012);
        eka = new Paivays(10, 3, 2011);

        tarkasta(toka, eka, 1);

        for (int i = 0; i < 4; i++) {
            int ensin = 1990 + arpa.nextInt(10);
            int vuodenPaasta = 1 + arpa.nextInt(10);

            toka = new Paivays(10, 2, ensin + vuodenPaasta);
            eka = new Paivays(9, 5, ensin);
            tarkasta(toka, eka, vuodenPaasta - 1);
        }


        for (int i = 0; i < 4; i++) {
            int ensin = 1990 + arpa.nextInt(10);
            int vuodenPaasta = 1 + arpa.nextInt(10);

            int d = arpa.nextInt(20);
            toka = new Paivays(d, 5, ensin + vuodenPaasta);
            eka = new Paivays(10, 5, ensin);
            int kompensaatio = d < 10 ? 1 : 0;
            tarkasta(toka, eka, vuodenPaasta - kompensaatio);
        }

    }

    /*
     *
     */
    @Points("92.3")
    @Test
    public void eiLiikaaOliomuuttujiaPaivaykseen3() {
        saniteettitarkastus("Paivays", 3, "Älä lisää Paivays-luokalle uusia oliomuuttujia, niitä ei tarvita");
    }

    @Points("92.3")
    @Test
    public void vuosienErotusLopullinen() {
        Paivays toka = new Paivays(9, 3, 2012);
        Paivays eka = new Paivays(10, 3, 2011);

        tarkasta2(toka, eka, 0);

        toka = new Paivays(10, 3, 2012);
        eka = new Paivays(10, 3, 2011);

        tarkasta2(toka, eka, 1);

        for (int i = 0; i < 4; i++) {
            int ensin = 1990 + arpa.nextInt(10);
            int vuodenPaasta = 1 + arpa.nextInt(10);

            toka = new Paivays(10, 2, ensin + vuodenPaasta);
            eka = new Paivays(9, 5, ensin);
            tarkasta2(toka, eka, vuodenPaasta - 1);
        }


        for (int i = 0; i < 4; i++) {
            int ensin = 1990 + arpa.nextInt(10);
            int vuodenPaasta = 1 + arpa.nextInt(10);

            int d = arpa.nextInt(20);
            toka = new Paivays(d, 5, ensin + vuodenPaasta);
            eka = new Paivays(10, 5, ensin);
            int kompensaatio = d < 10 ? 1 : 0;
            tarkasta2(toka, eka, vuodenPaasta - kompensaatio);
        }

    }
    /*
     *
     */

    private int erotus(Object pvm1, Object pvm2) {
        String metodiNimi = "erotusVuosissa";
        try {
            Method metodi = ReflectionUtils.requireMethod(Paivays.class, metodiNimi, Paivays.class);
            return ReflectionUtils.invokeMethod(int.class, metodi, pvm1, pvm2);
        } catch (Throwable t) {
            t.printStackTrace();
            fail("tee luokalle Paivays metodi public int erotusVuosissa(Paivays verrattava)");
        }
        return -999999;
    }

    private void saniteettitarkastus(String luokanNimi, int muuttujia, String msg) throws SecurityException {

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
            assertTrue(msg, var <= muuttujia);
        }
    }

    private String kentta(String toString) {
        return toString.replace("LyyraKortti" + ".", "");
    }

    private void tarkasta(Paivays toka, Paivays eka, int odotus) {
        assertEquals("päiväysten " + toka + " ja " + eka + " erotus lasketaan väärin", odotus, erotus(toka, eka));
    }

    private void tarkasta2(Paivays toka, Paivays eka, int erotus) {
        tarkasta(toka, eka, erotus);
        tarkasta(eka, toka, erotus);
    }
}
