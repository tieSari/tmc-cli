
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import junit.framework.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JoukkueetJaPelaajatTest<_Joukkue, _Pelaaja> {

    private String joukkueNimi = "Joukkue";
    private String pelaajaNimi = "Pelaaja";
    public String maalitNimi = "maalit";
    
    Reflex.ClassRef<Object> klassJ;
    String klassNameJ = "Joukkue";
    Reflex.ClassRef<Object> klassP;
    String klassNameP = "Pelaaja";

    @Before
    public void haeLuokka() {
        klassJ = Reflex.reflect(klassNameJ);
    }

    @Points("90.1")
    @Test
    public void luokkaJulkinen() {
        assertTrue("Luokan " + klassNameJ + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassNameJ + " {...\n}", klassJ.isPublic());
    }

    @Points("90.1")
    @Test
    public void eiYlimaaraisiaMuuttujiaLuokassaJoukkue() {
        saniteettitarkastus("Joukkue", 3, "joukkueen nimen, pelaajat ja maksimikoon muistavat oliomuuttujat");
    }

    @Points("90.1")
    @Test
    public void testaaJoukkueKonstruktori() throws Throwable {
        Reflex.MethodRef1<Object, Object, String> ctor = klassJ.constructor().taking(String.class).withNiceError();
        assertTrue("Määrittele luokalle " + klassNameJ + " julkinen konstruktori: public " + klassNameJ + "(String nimi)", ctor.isPublic());
        ctor.invoke("HIFK");
    }

    public Object luo(String nimi) throws Throwable {
        Reflex.MethodRef1<Object, Object, String> ctor = klassJ.constructor().taking(String.class).withNiceError();
        return ctor.invoke(nimi);
    }

    @Test
    @Points("90.1")
    public void luokanJoukkueMetodiHaeNimi() throws Throwable {
        String metodi = "haeNimi";

        Reflex.ClassRef<Object> tuoteClass = Reflex.reflect(klassNameJ);

        Object olio = luo("HIFK");

        assertTrue("tee luokalle " + klassNameJ + " metodi public String " + metodi + "() ", tuoteClass.method(olio, metodi)
                .returning(String.class).takingNoParams().isPublic());


        String v = "\nVirheen aiheuttanut koodi Joukkue j = new Joukkue(\"HIFK\"); "
                + "j.haeNimi();";

        assertEquals("Tarkasta koodi Joukkue j = new Joukkue(\"HIFK\"); "
                + "j.haeNimi();", "HIFK", tuoteClass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke());

        olio = luo("Flyers");

        v = "\nVirheen aiheuttanut koodi Joukkue j = new Joukkue(\"Flyers\"); "
                + "j.haeNimi();";

        assertEquals("Tarkasta koodi Joukkue j = new Joukkue(\"Flyers\"); "
                + "j.haeNimi();", "Flyers", tuoteClass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke());
    }


    /*
     * 
     */
    @Points("90.2")
    @Test
    public void luokkaPelaajaJulkinen() {
        klassP = Reflex.reflect(klassNameP);
        assertTrue("Luokan " + klassNameP + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassNameJ + " {...\n}", klassJ.isPublic());
    }

    @Points("90.2")
    @Test
    public void eiYlimaaraisiaMuuttujiaPelaajalla() {
        saniteettitarkastus("Pelaaja", 2, "nimen ja maalimäärän muistavat oliomuuttujat");
    }

    @Points("90.2")
    @Test
    public void testaaPelaajanKaksiparametrinenKonstruktori() throws Throwable {
        klassP = Reflex.reflect(klassNameP);
        Reflex.MethodRef2<Object, Object, String, Integer> ctor = klassP.constructor().taking(String.class, int.class).withNiceError();
        assertTrue("Määrittele luokalle " + klassNameP + " julkinen konstruktori: public " + klassNameP + "(String nimi, int maaleja)", ctor.isPublic());
        ctor.invoke("Arto", 39);
    }

    @Points("90.2")
    @Test
    public void testaaPelaajanYksiparametrinenKonstruktori() throws Throwable {
        klassP = Reflex.reflect(klassNameP);
        Reflex.MethodRef1<Object, Object, String> ctor = klassP.constructor().taking(String.class).withNiceError();
        assertTrue("Määrittele luokalle " + klassNameP + " julkinen konstruktori: public " + klassNameP + "(String nimi)", ctor.isPublic());
        ctor.invoke("Arto");
    }

    public Object luoP(String nimi, int maaleja) throws Throwable {
        klassP = Reflex.reflect(klassNameP);
        Reflex.MethodRef2<Object, Object, String, Integer> ctor = klassP.constructor().taking(String.class, int.class).withNiceError();
        return ctor.invoke(nimi, maaleja);
    }

    public Object luoP(String nimi) throws Throwable {
        klassP = Reflex.reflect(klassNameP);
        Reflex.MethodRef1<Object, Object, String> ctor = klassP.constructor().taking(String.class).withNiceError();
        return ctor.invoke(nimi);
    }

    @Points("90.2")
    @Test
    public void pelaajanMetodiHaeNimi() throws Throwable {
        klassP = Reflex.reflect(klassNameP);

        String metodi = "haeNimi";

        Reflex.ClassRef<Object> tuoteClass = Reflex.reflect(klassNameP);

        Object olio = luoP("Arto", 39);

        assertTrue("tee luokalle " + klassNameP + " metodi public String " + metodi + "() ", tuoteClass.method(olio, metodi)
                .returning(String.class).takingNoParams().isPublic());


        String v = "\nVirheen aiheuttanut koodi Pelaaja p = new Pelaaja(\"Arto\", 39); "
                + "p.haeNimi();";

        assertEquals("Tarkasta koodi Pelaaja p = new Pelaaja(\"Arto\", 39); "
                + "p.haeNimi();", "Arto", tuoteClass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke());

        olio = luoP("Edsger");

        v = "\nVirheen aiheuttanut koodi Pelaaja p = new Pelaaja(\"Edsger\"); "
                + "p.haeNimi();";

        assertEquals("Tarkasta koodi Pelaaja p = new Pelaaja(\"Edsger\"); "
                + "p.haeNimi();", "Edsger", tuoteClass.method(olio, metodi)
                .returning(String.class).takingNoParams().withNiceError(v).invoke());
    }

    @Points("90.2")
    @Test
    public void pelaajanMetodiMaalit() throws Throwable {
        klassP = Reflex.reflect(klassNameP);

        String metodi = "maalit";

        Reflex.ClassRef<Object> tuoteClass = Reflex.reflect(klassNameP);

        Object olio = luoP("Arto", 39);

        assertTrue("tee luokalle " + klassNameP + " metodi public int " + metodi + "() ", tuoteClass.method(olio, metodi)
                .returning(int.class).takingNoParams().isPublic());


        String v = "\nVirheen aiheuttanut koodi Pelaaja p = new Pelaaja(\"Arto\", 39); "
                + "p.maalit();";

        int t = tuoteClass.method(olio, metodi)
                .returning(int.class).takingNoParams().withNiceError(v).invoke();

        assertEquals("Tarkasta koodi Pelaaja p = new Pelaaja(\"Arto\", 39); "
                + "p.maalit();", 39, t);

        olio = luoP("Edsger");

        v = "\nVirheen aiheuttanut koodi Pelaaja p = new Pelaaja(\"Edsger\"); "
                + "p.maalit();";

        t = tuoteClass.method(olio, metodi)
                .returning(int.class).takingNoParams().withNiceError(v).invoke();

        assertEquals("Tarkasta koodi Pelaaja p = new Pelaaja(\"Edsger\"); "
                + "p.maalit();", 0, t);
    }

    /*
     * 
     */
    @Test
    @Points("90.2")
    public void testLuoPelaajaNimellaJaMaaleillaToString() {
        Class joukkue = Utils.getClass(pelaajaNimi);
        Constructor constructor = null;
        try {
            constructor = ReflectionUtils.requireConstructor(joukkue, String.class, int.class);
        } catch (Throwable e) {
            fail("Onhan luokalla " + pelaajaNimi + " konstruktori, joka saa parametrina merkkijonon ja kokonaislukutyyppisen muuttujan.");
        }

        Method haeNimi = null;
        try {
            haeNimi = ReflectionUtils.requireMethod(joukkue, "haeNimi");
        } catch (Throwable e) {
            fail("Onhan luokalla " + pelaajaNimi + " metodi haeNimi, joka palauttaa merkkijonon.");
        }

        if (haeNimi.getReturnType() != String.class) {
            fail("Onhan luokalla " + pelaajaNimi + " metodi haeNimi, joka palauttaa merkkijonon.");
        }

        Object pelaaja = null;
        String pelaajanNimi = "Antti L";
        int tehdytMaalit = 3;

        try {
            pelaaja = ReflectionUtils.invokeConstructor(constructor, pelaajanNimi, tehdytMaalit);
        } catch (Throwable ex) {
            fail("Pelaajan luominen epäonnistui, tarkista että konstruktori saa parametrina merkkijonon ja sillä on määre public.");
        }

        String pelaajanToString = pelaaja.toString();

        Assert.assertTrue("Tarkista että Pelaaja-luokan toString metodi tulostaa sekä nimen että maalit.",
                pelaajanToString.contains(pelaajanNimi) && pelaajanToString.contains("" + tehdytMaalit));
    }

    /*
     * 
     */
    @Test
    @Points("90.3")
    public void luokanJoukkueMetodiLisaa() throws Throwable {

        String metodi = "lisaaPelaaja";

        Reflex.ClassRef<Object> tuoteClass = Reflex.reflect(klassNameJ);
        Object olio = luo("HIFK");

        assertTrue("tee luokalle " + klassNameJ + " metodi public void " + metodi + "(Pelaaja lisattava) ", tuoteClass.method(olio, metodi)
                .returningVoid().taking(ReflectionUtils.findClass("Pelaaja")).isPublic());

        Reflex.ClassRef<_Joukkue> _JoukkueRef = Reflex.reflect("Joukkue");

        _Joukkue j = _JoukkueRef.constructor().taking(String.class).invoke("HIFK");

        Reflex.ClassRef<_Pelaaja> _PelaajaRef = Reflex.reflect("Pelaaja");

        String v = "\nVirheen aiheuttanut koodi Joukkue j = new Joukkue(\"HIFK\"); Pelaaja p = new Pelaaja(\"Arto\");"
                + " j.lisaaPelaaja(p);";

        _Pelaaja p = _PelaajaRef.constructor().taking(String.class).invoke("Arto");

        Class<_Pelaaja> c = _PelaajaRef.cls();

        _JoukkueRef.method(j, metodi).returningVoid().taking(c).withNiceError(v).invoke(p);

    }

    @Test
    @Points("90.3")
    public void luokallaJoukkueTulostaPelaajat() throws Throwable {

        String metodi = "tulostaPelaajat";

        Reflex.ClassRef<Object> tuoteClass = Reflex.reflect(klassNameJ);
        Object olio = luo("HIFK");

        assertTrue("tee luokalle " + klassNameJ + " metodi public void " + metodi + "() ", tuoteClass.method(olio, metodi)
                .returningVoid().takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi Joukkue j = new Joukkue(\"HIFK\");"
                + " j.tulostaPelaajat();";

        tuoteClass.method(olio, metodi)
                .returningVoid().takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points("90.3")
    public void testLisaaPelaaja() {
        Class joukkueClass = Utils.getClass(joukkueNimi);
        Constructor constructor = null;
        try {
            constructor = ReflectionUtils.requireConstructor(joukkueClass, String.class);
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " konstruktori, joka saa parametrina merkkijonon.");
        }

        Method haeNimi = null;
        try {
            haeNimi = ReflectionUtils.requireMethod(joukkueClass, "haeNimi");
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " metodi haeNimi, joka palauttaa merkkijonon.");
        }

        if (haeNimi.getReturnType() != String.class) {
            fail("Onhan luokalla " + joukkueNimi + " metodi haeNimi, joka palauttaa merkkijonon.");
        }

        Object joukkue = null;
        String joukkueenNimi = "Jokivirran tsemppi";

        try {
            joukkue = ReflectionUtils.invokeConstructor(constructor, joukkueenNimi);
        } catch (Throwable ex) {
            fail("Joukkueen luominen epäonnistui, tarkista että konstruktori saa parametrina merkkijonon.");
        }

        String saatuNimi = null;
        try {
            saatuNimi = ReflectionUtils.invokeMethod(String.class, haeNimi, joukkue);
        } catch (Throwable ex) {
            fail("Metodin haeNimi kutsuminen epäonnistui, tarkista että se on kuten tehtävänannossa.");
        }

        Assert.assertEquals("Jos Joukkueen nimeksi annetaan konstruktorissa Apu, tulee metodin haeNimi palauttaa merkkijono Apu.", joukkueenNimi, saatuNimi);

        Method lisaaPelaaja = null;
        Class pelaajaClass = Utils.getClass("Pelaaja");
        try {
            lisaaPelaaja = ReflectionUtils.requireMethod(joukkueClass, "lisaaPelaaja", pelaajaClass);
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " metodi lisaaPelaaja, joka saa parametrinaan Pelaaja-tyyppisen olion.");
        }

        Constructor pelaajaConstructor = null;
        try {
            pelaajaConstructor = ReflectionUtils.requireConstructor(pelaajaClass, String.class);
        } catch (Throwable e) {
            fail("Onhan luokalla Pelaaja konstruktori joka saa parametrinaan pelaajan nimen.");
        }

        try {
            ReflectionUtils.invokeMethod(void.class, lisaaPelaaja, joukkue, ReflectionUtils.invokeConstructor(pelaajaConstructor, "Matti"));
            ReflectionUtils.invokeMethod(void.class, lisaaPelaaja, joukkue, ReflectionUtils.invokeConstructor(pelaajaConstructor, "Teppo"));
            ReflectionUtils.invokeMethod(void.class, lisaaPelaaja, joukkue, ReflectionUtils.invokeConstructor(pelaajaConstructor, "Aira Samulin"));
        } catch (Throwable t) {
            fail("Tarkista että luokalla Joukkue oleva metodi lisaaPelaaja saa parametrikseen pelaajan ja toimii oikein.");
        }

        MockInOut mio = new MockInOut("");

        Method tulostaPelaajat = null;
        try {
            tulostaPelaajat = ReflectionUtils.requireMethod(joukkueClass, "tulostaPelaajat");
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " metodi tulostaPelaajat.");
        }

        try {
            ReflectionUtils.invokeMethod(void.class, tulostaPelaajat, joukkue);
        } catch (Throwable t) {
            fail("Tulostaajan metodi tulostaPelaajat pelaajien tiedot.");
        }

        String output = mio.getOutput().toLowerCase();
        Assert.assertTrue("Metodikutsun tulostaPelaajat pitäisi tulostaa lisättyjen pelaajien tiedot. \n"
                + "Kokeile pääohjelmassasi että seuraava koodi toimii: \n"
                + "Joukkue j = new Joukkue(\"RAGE\"); Pelaaja p = new Pelaaja(\"Jaakko\"); "
                + "j.lisaaPelaaja(p); j.tulostaPelaajat();",
                output.contains("matti") && output.contains("teppo") && output.contains("aira"));
    }

    /*
     * 
     */
    @Test
    @Points("90.4")
    public void luokallaJoukkueMetodiAsetaMaksimikoko() throws Throwable {

        String metodi = "asetaMaksimikoko";

        Reflex.ClassRef<Object> tuoteClass = Reflex.reflect(klassNameJ);
        Object olio = luo("HIFK");

        assertTrue("tee luokalle " + klassNameJ + " metodi public void " + metodi + "(int maksimi) ", tuoteClass.method(olio, metodi)
                .returningVoid().taking(int.class).isPublic());

        String v = "\nVirheen aiheuttanut koodi Joukkue j = new Joukkue(\"HIFK\");"
                + " j.asetaMaksimikoko(22);";

        tuoteClass.method(olio, metodi)
                .returningVoid().taking(int.class).withNiceError(v).invoke(22);
    }

    @Test
    @Points("90.4")
    public void luokallaJoukkueMetodiKoko() throws Throwable {

        String metodi = "koko";

        Reflex.ClassRef<Object> tuoteClass = Reflex.reflect(klassNameJ);
        Object olio = luo("HIFK");

        assertTrue("tee luokalle " + klassNameJ + " metodi public int " + metodi + "() ", tuoteClass.method(olio, metodi)
                .returning(int.class).takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi Joukkue j = new Joukkue(\"HIFK\");"
                + " j.koko();";

        tuoteClass.method(olio, metodi)
                .returning(int.class).takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points("90.4")
    public void testKoko() {
        Class joukkueClass = Utils.getClass(joukkueNimi);
        Constructor constructor = null;
        try {
            constructor = ReflectionUtils.requireConstructor(joukkueClass, String.class);
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " konstruktori, joka saa parametrina merkkijonon.");
        }

        Method kokoMethod = null;
        try {
            kokoMethod = ReflectionUtils.requireMethod(joukkueClass, "koko");
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " metodi koko, joka palauttaa kokonaislukutyyppisen arvon (int).");
        }

        if (kokoMethod.getReturnType() != int.class) {
            fail("Onhan metodin koko palautustyyppi varmasti int ja metodilla määre public.");
        }

        Object joukkue = null;
        String joukkueenNimi = "Jokivirran tsemppi";

        try {
            joukkue = ReflectionUtils.invokeConstructor(constructor, joukkueenNimi);
        } catch (Throwable ex) {
            fail("Joukkueen luominen epäonnistui, tarkista että konstruktori saa parametrina merkkijonon.");
        }

        int saatuKoko = -1;
        try {
            saatuKoko = ReflectionUtils.invokeMethod(int.class, kokoMethod, joukkue);
        } catch (Throwable ex) {
            fail("Metodin koko kutsuminen epäonnistui, tarkista että se on kuten tehtävänannossa.");
        }

        Assert.assertEquals("Tyhjän joukkueen koon tulee olla 0", 0, saatuKoko);

        Method lisaaPelaaja = null;
        Class pelaajaClass = Utils.getClass("Pelaaja");
        try {
            lisaaPelaaja = ReflectionUtils.requireMethod(joukkueClass, "lisaaPelaaja", pelaajaClass);
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " metodi lisaaPelaaja, joka saa parametrinaan Pelaaja-tyyppisen olion.");
        }

        Constructor pelaajaConstructor = null;
        try {
            pelaajaConstructor = ReflectionUtils.requireConstructor(pelaajaClass, String.class);
        } catch (Throwable e) {
            fail("Onhan luokalla Pelaaja konstruktori joka saa parametrinaan pelaajan nimen.");
        }

        try {
            ReflectionUtils.invokeMethod(void.class, lisaaPelaaja, joukkue, ReflectionUtils.invokeConstructor(pelaajaConstructor, "Matti"));
            ReflectionUtils.invokeMethod(void.class, lisaaPelaaja, joukkue, ReflectionUtils.invokeConstructor(pelaajaConstructor, "Teppo"));
            ReflectionUtils.invokeMethod(void.class, lisaaPelaaja, joukkue, ReflectionUtils.invokeConstructor(pelaajaConstructor, "Aira Samulin"));
        } catch (Throwable t) {
            fail("Tarkista että luokalla Joukkue oleva metodi lisaaPelaaja saa parametrikseen pelaajan ja toimii oikein.");
        }

        saatuKoko = -1;
        try {
            saatuKoko = ReflectionUtils.invokeMethod(int.class, kokoMethod, joukkue);
        } catch (Throwable ex) {
            fail("Metodin koko kutsuminen epäonnistui, tarkista että se on kuten tehtävänannossa.");
        }

        Assert.assertEquals("Kun luodaan joukkue yksiparametrisella konstruktorilla tyyliin\n"
                + "Joukkue j = new Joukkue(\"SaPKo\");"
                + "kolmen pelaajan lisäämisen jälkeen joukkueen koon tulee olla 3\n"
                + "HUOM: jos olet jo kohdassa 90.4, huomioi että joukkeen oletusmaksimikooksi täytyy asettaa 16"
                + "\n", 3, saatuKoko);


    }

    /*
     * 
     */
    @Test
    @Points("90.4")
    public void testAsetaMaksimikoko() {
        Class joukkueClass = Utils.getClass(joukkueNimi);
        Constructor constructor = null;
        try {
            constructor = ReflectionUtils.requireConstructor(joukkueClass, String.class);
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " konstruktori, joka saa parametrina merkkijonon.");
        }

        Method kokoMethod = null;
        try {
            kokoMethod = ReflectionUtils.requireMethod(joukkueClass, "koko");
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " metodi koko, joka palauttaa kokonaislukutyyppisen arvon (int).");
        }

        if (kokoMethod.getReturnType() != int.class) {
            fail("Onhan metodin koko palautustyyppi varmasti int ja metodilla määre public.");
        }

        Object joukkue = null;
        String joukkueenNimi = "Jokivirran tsemppi";

        try {
            joukkue = ReflectionUtils.invokeConstructor(constructor, joukkueenNimi);
        } catch (Throwable ex) {
            fail("Joukkueen luominen epäonnistui, tarkista että konstruktori saa parametrina merkkijonon.");
        }

        int saatuKoko = -1;
        try {
            saatuKoko = ReflectionUtils.invokeMethod(int.class, kokoMethod, joukkue);
        } catch (Throwable ex) {
            fail("Metodin koko kutsuminen epäonnistui, tarkista että se on kuten tehtävänannossa.");
        }

        Assert.assertEquals("Tyhjän joukkueen koon tulee olla 0", 0, saatuKoko);

        Method lisaaPelaaja = null;
        Class pelaajaClass = Utils.getClass("Pelaaja");
        try {
            lisaaPelaaja = ReflectionUtils.requireMethod(joukkueClass, "lisaaPelaaja", pelaajaClass);
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " metodi lisaaPelaaja, joka saa parametrinaan Pelaaja-tyyppisen olion.");
        }

        Constructor pelaajaConstructor = null;
        try {
            pelaajaConstructor = ReflectionUtils.requireConstructor(pelaajaClass, String.class);
        } catch (Throwable e) {
            fail("Onhan luokalla Pelaaja konstruktori joka saa parametrinaan pelaajan nimen.");
        }

        Method asetaMaksimikoko = null;
        try {
            asetaMaksimikoko = ReflectionUtils.requireMethod(joukkueClass, "asetaMaksimikoko", int.class);
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " metodi asetaMaksimikoko, joka saa parametriksi kokonaislukutyyppisen arvon (int).");
        }

        if (asetaMaksimikoko.getReturnType() != void.class) {
            fail("Onhan metodin asetaMaksimikoko palautustyyppi varmasti void ja metodilla määre public.");
        }

        try {
            ReflectionUtils.invokeMethod(void.class, asetaMaksimikoko, joukkue, 1);
        } catch (Throwable ex) {
            fail("Metodikutsu asetaMaksimikoko epäonnistui. Onhan metodilla määre public.");
        }

        try {
            ReflectionUtils.invokeMethod(void.class, lisaaPelaaja, joukkue, ReflectionUtils.invokeConstructor(pelaajaConstructor, "Matti"));
            ReflectionUtils.invokeMethod(void.class, lisaaPelaaja, joukkue, ReflectionUtils.invokeConstructor(pelaajaConstructor, "Teppo"));
            ReflectionUtils.invokeMethod(void.class, lisaaPelaaja, joukkue, ReflectionUtils.invokeConstructor(pelaajaConstructor, "Aira Samulin"));
        } catch (Throwable t) {
            fail("Tarkista että luokalla Joukkue oleva metodi lisaaPelaaja saa parametrikseen pelaajan ja toimii oikein.");
        }

        saatuKoko = -1;
        try {
            saatuKoko = ReflectionUtils.invokeMethod(int.class, kokoMethod, joukkue);
        } catch (Throwable ex) {
            fail("Metodin koko kutsuminen epäonnistui, tarkista että se on kuten tehtävänannossa.");
        }

        Assert.assertEquals("Jos joukkueen maksimikoko on 1, tulee joukkueen koon tulee olla 1 kolmen pelaajan lisäämisen jälkeen", 1, saatuKoko);
    }

    /*
     * 
     */
    @Test
    @Points("90.5")
    public void luokallaJoukkueMetodiMaalit() throws Throwable {

        String metodi = "maalit";

        Reflex.ClassRef<Object> tuoteClass = Reflex.reflect(klassNameJ);
        Object olio = luo("HIFK");

        assertTrue("tee luokalle " + klassNameJ + " metodi public int " + metodi + "() ", tuoteClass.method(olio, metodi)
                .returning(int.class).takingNoParams().isPublic());

        String v = "\nVirheen aiheuttanut koodi Joukkue j = new Joukkue(\"HIFK\");"
                + " j.maalit();";

        tuoteClass.method(olio, metodi)
                .returning(int.class).takingNoParams().withNiceError(v).invoke();
    }

    @Test
    @Points("90.5")
    public void testJoukkueenMaalit() {
        Class joukkueClass = Utils.getClass(joukkueNimi);
        Constructor constructor = null;
        try {
            constructor = ReflectionUtils.requireConstructor(joukkueClass, String.class);
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " konstruktori, joka saa parametrina merkkijonon.");
        }

        Method kokoMethod = null;
        try {
            kokoMethod = ReflectionUtils.requireMethod(joukkueClass, "koko");
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " metodi koko, joka palauttaa kokonaislukutyyppisen arvon (int).");
        }

        if (kokoMethod.getReturnType() != int.class) {
            fail("Onhan metodin koko palautustyyppi varmasti int ja metodilla määre public.");
        }

        Object joukkue = null;
        String joukkueenNimi = "Jokivirran tsemppi";

        try {
            joukkue = ReflectionUtils.invokeConstructor(constructor, joukkueenNimi);
        } catch (Throwable ex) {
            fail("Joukkueen luominen epäonnistui, tarkista että konstruktori saa parametrina merkkijonon.");
        }

        int saatuKoko = -1;
        try {
            saatuKoko = ReflectionUtils.invokeMethod(int.class, kokoMethod, joukkue);
        } catch (Throwable ex) {
            fail("Metodin koko kutsuminen epäonnistui, tarkista että se on kuten tehtävänannossa.");
        }

        Assert.assertEquals("Tyhjän joukkueen koon tulee olla 0", 0, saatuKoko);

        Method lisaaPelaaja = null;
        Class pelaajaClass = Utils.getClass("Pelaaja");
        try {
            lisaaPelaaja = ReflectionUtils.requireMethod(joukkueClass, "lisaaPelaaja", pelaajaClass);
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " metodi lisaaPelaaja, joka saa parametrinaan Pelaaja-tyyppisen olion.");
        }

        Constructor pelaajaConstructor = null;
        try {
            pelaajaConstructor = ReflectionUtils.requireConstructor(pelaajaClass, String.class, int.class);
        } catch (Throwable e) {
            fail("Onhan luokalla Pelaaja konstruktori joka saa parametrinaan merkkijonon ja kokonaislukutyyppisen muuttujan.");
        }

        try {
            ReflectionUtils.invokeMethod(void.class, lisaaPelaaja, joukkue, ReflectionUtils.invokeConstructor(pelaajaConstructor, "Matti", 3));
            ReflectionUtils.invokeMethod(void.class, lisaaPelaaja, joukkue, ReflectionUtils.invokeConstructor(pelaajaConstructor, "Teppo", 4));
            ReflectionUtils.invokeMethod(void.class, lisaaPelaaja, joukkue, ReflectionUtils.invokeConstructor(pelaajaConstructor, "Aira Samulin", 3));
        } catch (Throwable t) {
            fail("Tarkista että luokalla Joukkue oleva metodi lisaaPelaaja saa parametrikseen pelaajan ja toimii oikein.");
        }

        saatuKoko = -1;
        try {
            saatuKoko = ReflectionUtils.invokeMethod(int.class, kokoMethod, joukkue);
        } catch (Throwable ex) {
            fail("Metodin koko kutsuminen epäonnistui, tarkista että se on kuten tehtävänannossa.");
        }


        Method maalienMaara = null;
        try {
            maalienMaara = ReflectionUtils.requireMethod(joukkueClass, "maalit");
        } catch (Throwable e) {
            fail("Onhan luokalla " + joukkueNimi + " metodi maalit, jolla on määre public.");
        }

        int maaleja = -1;

        try {
            maaleja = ReflectionUtils.invokeMethod(int.class, maalienMaara, joukkue);
        } catch (Throwable t) {
            fail("Onhan luokan " + joukkueNimi + " metodin maalit määre public ja palautusarvo kokonaislukutyyppinen muuttuja (int).");
        }


        Assert.assertEquals("Tarkista että Joukkueen maalit-metodi palauttaa joukkueen pelaajien maalien summan.", 10, maaleja);
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
