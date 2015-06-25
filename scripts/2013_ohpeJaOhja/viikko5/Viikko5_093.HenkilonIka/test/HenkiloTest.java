
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.lang.reflect.Constructor;
import java.util.Calendar;
import org.junit.Test;
import static org.junit.Assert.*;

public class HenkiloTest {

    @Test
    @Points("93.1")
    public void testaaTanaanSyntyneenHenkilonIka() {
        Calendar nyt = Calendar.getInstance();
        int vuosi = nyt.get(Calendar.YEAR);
        int kuukausi = nyt.get(Calendar.MONTH) + 1;
        int paiva = nyt.get(Calendar.DATE);
        Henkilo henkilo = new Henkilo("Sälli", paiva, kuukausi, vuosi);
        int ika = henkilo.ika();
        assertTrue("Tänään syntyneen henkilön ikä pitäisi olla 0 vuotta, "
                + "palautettu ikä oli: " + ika, (ika == 0));
    }

    @Test
    @Points("93.1")
    public void testaaVuosiSittenSyntyneenHenkilonIka() {
        Calendar nyt = Calendar.getInstance();

        nyt.setLenient(true);
        nyt.add(Calendar.YEAR, -1);

        int vuosi = nyt.get(Calendar.YEAR);
        int kuukausi = nyt.get(Calendar.MONTH) + 1;
        int paiva = nyt.get(Calendar.DATE);
        Henkilo henkilo = new Henkilo("Sälli", paiva, kuukausi, vuosi);
        int ika = henkilo.ika();
        assertTrue("Vuosi sitten syntyneen henkilön ikä pitäisi olla 1 vuotta, "
                + "palautettu ikä oli: " + ika, (ika == 1));
    }

    @Test
    @Points("93.1")
    public void testaaPaivaaVailleVuosiSittenSyntyneenHenkilonIka() {
        Calendar nyt = Calendar.getInstance();

        nyt.setLenient(true);
        nyt.add(Calendar.YEAR, -1);
        nyt.add(Calendar.DATE, 1);

        int vuosi = nyt.get(Calendar.YEAR);
        int kuukausi = nyt.get(Calendar.MONTH) + 1;
        int paiva = nyt.get(Calendar.DATE);
        Henkilo henkilo = new Henkilo("Sälli", paiva, kuukausi, vuosi);
        int ika = henkilo.ika();
        assertTrue("Päivää vaille vuosi sitten syntyneen henkilön ikä pitäisi olla 0 vuotta, "
                + "palautettu ikä oli: " + ika, (ika == 0));
    }

    @Test
    @Points("93.1")
    public void testaaVajaa27VuottaSittenSyntyneenHenkilonIka() {
        Calendar nyt = Calendar.getInstance();

        nyt.setLenient(true);
        nyt.add(Calendar.YEAR, -27);
        nyt.add(Calendar.MONTH, 5);
        nyt.add(Calendar.DATE, 27);

        int vuosi = nyt.get(Calendar.YEAR);
        int kuukausi = nyt.get(Calendar.MONTH) + 1;
        int paiva = nyt.get(Calendar.DATE);
        Henkilo henkilo = new Henkilo("Sälli", paiva, kuukausi, vuosi);
        int ika = henkilo.ika();
        assertTrue("Yli 26, mutta alle 27 vuotta sitten syntyneen henkilön ikä pitäisi olla 26 vuotta, "
                + "palautettu ikä oli: " + ika, (ika == 26));
    }

    @Test
    @Points("93.2")
    public void testaaVanhempiKuin1() {
        Henkilo h1 = new Henkilo("Jarmo", 15, 9, 1954);
        Henkilo h2 = new Henkilo("Petteri", 4, 4, 1978);
        assertTrue("Henkilön (" + h1 + ") pitäisi olla vanhempi kuin (" + h2 + ")", h1.vanhempiKuin(h2));
        assertFalse("Henkilön (" + h2 + ") pitäisi olla nuorempi (eli ei-vanhempi) kuin (" + h1 + ")", h2.vanhempiKuin(h1));
    }

    @Test
    @Points("93.2")
    public void testaaVanhempiKuin2() {
        Henkilo h1 = new Henkilo("Helga", 31, 12, 2009);
        Henkilo h2 = new Henkilo("Janika", 1, 1, 2010);
        assertTrue("Henkilön " + h1 + " pitäisi olla vanhempi kuin " + h2, h1.vanhempiKuin(h2));
        assertFalse("Henkilön " + h2 + " pitäisi olla nuorempi (eli ei-vanhempi) kuin " + h1, h2.vanhempiKuin(h1));
    }

    @Points("93.3")
    @Test
    public void konstruktoritLoytyy() {
        try {
            Henkilo.class.getConstructor(String.class, Paivays.class);
        } catch (Throwable e) {
            fail("luokalla Henkilo pitää olla konstruktori, jonka määrittely on muotoa: public Henkilo(String nimi, Paivays syntymapaiva) {...}");
        }
        try {
            Henkilo.class.getConstructor(String.class);
        } catch (Throwable e) {
            fail("luokalla Henkilo pitää olla konstruktori, jonka määrittely on muotoa: public Henkilo(String nimi) {...}");
        }
    }

    @Points("93.3")
    @Test
    public void testaaKonstruktorit() {
        Constructor<Henkilo> c1;
        try {
            c1 = Henkilo.class.getConstructor(String.class, Paivays.class);
        } catch (Throwable e) {
            fail("luokalla Henkilo pitää olla konstruktori, jonka määrittely on muotoa: public Henkilo(String nimi, Paivays syntymapaiva) {...}");
            return;
        }

        Henkilo h1;
        try {
            h1 = c1.newInstance("Sepe", new Paivays(29, 2, 2012));
        } catch (Throwable e) {
            fail("konstruktorin kutsuminen aiheutti poikkeuksen: public Henkilo(String nimi, Paivays syntymapaiva) {...}: " + e.toString());
            return;
        }

        String string = h1.toString();
        assertTrue("Kutsuttaessa new Henkilo(\"Sepe\", new Paivays(29, 2, 2012)) pitäisi käyttää annettua syntymäpäivää",
                string.contains("29.2.2012"));
        assertTrue("Kutsuttaessa new Henkilo(\"Sepe\", new Paivays(29, 2, 2012)) pitäisi käyttää annettua nimeä",
                string.contains("Sepe"));

        Constructor<Henkilo> c2;
        try {
            c2 = Henkilo.class.getConstructor(String.class);
        } catch (Throwable e) {
            fail("luokalla Henkilo pitää olla konstruktori, jonka määrittely on muotoa: public Henkilo(String nimi) {...}");
            return;
        } 

        Henkilo h2;
        try {
            h2 = c2.newInstance("Sade");
        } catch (Throwable e) {
            fail("konstruktorin kutsuminen aiheutti poikkeuksen: public Henkilo(String nimi) {...}: " + e.toString());
            return;
        }

        Calendar nyt = Calendar.getInstance();
        int vuosi = nyt.get(Calendar.YEAR);
        int kuukausi = nyt.get(Calendar.MONTH) + 1;
        int paiva = nyt.get(Calendar.DATE);

        string = h2.toString();
        assertTrue("Kutsuttaessa new Henkilo(\"Sade\") pitäisi käyttää tätä päivää syntymäpäivänä",
                string.contains(" " + paiva + "." + kuukausi + "." + vuosi));
        assertTrue("Kutsuttaessa new Henkilo(\"Sade\") pitäisi nimenä olla \"Sade\"",
                string.contains("Sade"));
    }
}
