
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SanatutkimusTest {

    Class sanaLuokka;
    private final String ENKOODAUSONGELMIA = "Mac- ja windowskäyttäjillä voi olla ongelmia ä ja ö -merkkejä sisältävien sanojen kanssa.\n"
            + "Tässä tapauksessa luo Scanner seuraavasti: Scanner lukija = new Scanner(tiedosto, \"UTF-8\");";
    String klassName = "sanatutkimus.Sanatutkimus";
    Reflex.ClassRef<Object> classRef;

    @Before
    public void setUp() {
        classRef = Reflex.reflect(klassName);

        assertTrue("Luokan " + s(klassName) + " pitää olla julkinen, eli se tulee määritellä\n"
                + "public class " + s(klassName) + " {...\n}", classRef.isPublic());

        try {
            sanaLuokka = ReflectionUtils.findClass("sanatutkimus.Sanatutkimus");
            assertNotNull(sanaLuokka);
        } catch (Exception e) {
            fail("Olethan luonut luokan Sanatutkimus pakkaukseen sanatutkimus?");
        } catch (Throwable t) {
            fail("Olethan luonut luokan \"Sanatutkimus\" pakkaukseen sanatutkimus?");
        }
    }

    @Test
    @Points("130.1")
    public void eiYlimaaraisiaMuuttujia() {
        saniteettitarkastus(klassName, 20, "");
    }

    @Test
    @Points("130.1")
    public void onKonstruktoriAnalyysi() throws Throwable {
        Reflex.MethodRef1<Object, Object, File> ctor = classRef.constructor().taking(File.class).withNiceError();
        assertTrue("Määrittele luokalle " + s(klassName) + " julkinen konstruktori: \n"
                + "public " + s(klassName) + "(File tiedosto)", ctor.isPublic());
        String v = "virheen aiheutti koodi new Sanatutkimus( new File(\"src/pienilista.txt\") );\n";
        ctor.withNiceError(v).invoke(new File("pienilista.txt"));
    }

    public Object luo(File file) throws Throwable {
        return classRef.constructor().taking(File.class).invoke(file);
    }

    @Test
    @Points("130.1")
    public void onMetodiSanojenMaara() throws Throwable {
        Object o = luo(new File("pienilista.txt"));


        assertTrue("Lisää luokalle Sanatutkimus metodi public int sanojenMaara()", classRef.method(o, "sanojenMaara").returning(int.class).takingNoParams().isPublic());

        String k = "Sanatutkimus s = new Sanatutkimus( new File(\"src/pienilista.txt\") );\n"
                + "s.sanojenMaara();";

        assertEquals(k, 24, (int) classRef.method(o, "sanojenMaara").returning(int.class).takingNoParams().withNiceError(k).invoke());
    }

    @Test
    @Points("130.1")
    public void pieniFile() throws Throwable {

        File eka;
        eka = File.createTempFile("eka", "txt");
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(eka), "UTF-8");
        out.append("sana1\nsana2\nsana3\nsana4\nsana5\n");
        out.flush();
        out.close();

        Object o = luo(eka);

        assertTrue("Lisää luokalle Sanatutkimus metodi public int sanojenMaara()", classRef.method(o, "sanojenMaara").returning(int.class).takingNoParams().isPublic());

        String k = "Tiedoston sisältönä:\n"
                + "sana1\nsana2\nsana3\nsana4\nsana5"
                + "\ns.sanojenMaara();";

        assertEquals(k, 5, (int) classRef.method(o, "sanojenMaara").returning(int.class).takingNoParams().withNiceError(k).invoke());

    }

    @Test
    @Points("130.1")
    public void hiemanIsompiFile() throws Throwable {

        File eka;
        eka = File.createTempFile("eka", "txt");
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(eka), "UTF-8");
        out.append("sana1\nsana2\nsana3\nsana4\nsana5\nlisäsana\n");
        out.flush();
        out.close();

        Object o = luo(eka);

        assertTrue("Lisää luokalle Sanatutkimus metodi public int sanojenMaara()", classRef.method(o, "sanojenMaara").returning(int.class).takingNoParams().isPublic());

        String k = "Tiedoston sisältönä:\n"
                + "sana1\nsana2\nsana3\nsana4\nsana5\nlisäsana\n"
                + "\ns.sanojenMaara();";

        assertEquals(k, 6, (int) classRef.method(o, "sanojenMaara").returning(int.class).takingNoParams().withNiceError(k).invoke());

    }

    @Test
    @Points("130.1")
    public void sanojenMaaraIsoLista() throws Throwable {
        Object o = luo(new File("sanalista.txt"));

        assertTrue("Lisää luokalle Sanatutkimus metodi public int sanojenMaara()", classRef.method(o, "sanojenMaara").returning(int.class).takingNoParams().isPublic());

        String k = "Sanatutkimus s = new Sanatutkimus( new File(\"src/sanalista.txt\") );\n"
                + "s.sanojenMaara();";

        assertEquals(k, 91591, (int) classRef.method(o, "sanojenMaara").returning(int.class).takingNoParams().withNiceError(k).invoke());

    }

    /*
     *
     */
    @Test
    @Points("130.2")
    public void onMetodiZZZ() throws Throwable {
        Object o = luo(new File("pienilista.txt"));

        assertTrue("Lisää luokalle Sanatutkimus metodi public List<String> kirjaimenZSisaltavatSanat()", classRef.method(o, "kirjaimenZSisaltavatSanat").returning(List.class).takingNoParams().isPublic());

        String k = "Sanatutkimus s = new Sanatutkimus( new File(\"src/pienilista.txt\") );\n"
                + "s.kirjaimenZSisaltavatSanat();";

        classRef.method(o, "kirjaimenZSisaltavatSanat").returning(List.class).takingNoParams().withNiceError(k).invoke();
    }

    @Test
    @Points("130.2")
    public void zetallaAlkavatPieniLista() throws Throwable {
        Object o = luo(new File("pienilista.txt"));

        assertTrue("Lisää luokalle Sanatutkimus metodi public List<String> kirjaimenZSisaltavatSanat()", classRef.method(o, "kirjaimenZSisaltavatSanat").returning(List.class).takingNoParams().isPublic());

        String k = "Sanatutkimus s = new Sanatutkimus( new File(\"src/pienilista.txt\") );\n"
                + "s.kirjaimenZSisaltavatSanat();";

        List<String> zeta = (List<String>) classRef.method(o, "kirjaimenZSisaltavatSanat").returning(List.class)
                .takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nPalautettu lista oli null.", zeta == null);

        List<String> odotettu = new ArrayList<String>();
        Collections.addAll(odotettu, "appenzeller", "appenzellerjuusto", "gorgonzola", "gorgonzolajuusto");

        Collections.sort(zeta);
        assertEquals(k + "\npalautetun listan pituus väärä\nPalautettu lista oli: " + zeta, 4, zeta.size());
        assertEquals(k, odotettu, zeta);
    }

    @Test
    @Points("130.2")
    public void zKirjainTest() throws Exception, Throwable {
        FileWriter n;
        File eka = File.createTempFile("eka", "txt");
        n = new FileWriter(eka);
        n.append("jazz\nzombi\nbomb\npalmu\nzzz\nunilla\nzoo\n");
        n.flush();
        n.close();

        ArrayList<String> odotettu = new ArrayList<String>() {
            {
                add("jazz");
                add("zombi");
                add("zoo");
                add("zzz");
            }
        };

        Object o = luo(eka);

        String k = "Tiedoston sisältönä:\n"
                + "jazz\nzombi\nbomb\npalmu\nzzz\nunilla\nzoo\n"
                + "\ns.sanojenMaara();";

        List<String> zeta = (List<String>) classRef.method(o, "kirjaimenZSisaltavatSanat").returning(List.class).takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nPalautettu lista oli null.", zeta == null);

        Collections.sort(zeta);
        assertEquals(k + "\npalautetun listan pituus väärä\nPalautettu lista oli: " + zeta, 4, zeta.size());


        k = "Tiedoston sisältönä:\n"
                + "jazz\nzombi\nbomb\npalmu\nzzz\nunilla\nzoo\n"
                + "\ns.sanojenMaara();\n"
                + "palautit: " + zeta;

        assertEquals(k, odotettu, zeta);
    }

    @Test
    @Points("130.2")
    public void ztanSisaltavatSanaLista() throws Throwable {
        Object o = luo(new File("sanalista.txt"));

        assertTrue("Lisää luokalle Sanatutkimus metodi public List<String> kirjaimenZSisaltavatSanat()", classRef.method(o, "kirjaimenZSisaltavatSanat").returning(List.class).takingNoParams().isPublic());

        String k = "Sanatutkimus s = new Sanatutkimus( new File(\"src/sanalista.txt\") );\n"
                + "s.kirjaimenZSisaltavatSanat();";

        List<String> zeta = (List<String>) classRef.method(o, "kirjaimenZSisaltavatSanat").returning(List.class).takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nPalautettu lista oli null.", zeta == null);

        List<String> odotettu = new ArrayList<String>() {
            {
                add("appenzeller");
                add("appenzellerjuusto");
                add("azeri");
                add("blazer");
                add("buzuki");
                add("gorgonzola");
                add("gorgonzolajuusto");
                add("gorgonzolanjuusto");
                add("intermezzo");
                add("jazz");
                add("jazzfestivaali");
                add("jazzklubi");
                add("jazzlaulaja");
                add("jazzmessu");
                add("jazzmusiikki");
                add("jazzmuusikko");
                add("jazzorkesteri");
                add("jazztanssi");
                add("kamikazelentäjä");
                add("mezzoforte");
                add("mezzopiano");
                add("mezzosopraano");
                add("mezzotinto");
                add("nizzansalaatti");
                add("ouzo");
                add("paparazzi");
                add("pizza");
                add("pizzeria");
                add("pizzicato");
                add("puzzle");
                add("puzzlepeli");
                add("scherzo");
                add("zambo");
                add("zen");
                add("zenbuddhalaisuus");
                add("zeniitti");
                add("zeppeliini");
                add("zirkoni");
                add("zirkonium");
                add("zloty");
                add("zombi");
                add("zombie");
                add("zoologi");
                add("zoologia");
                add("zoologinen");
                add("zoomata");
                add("zoomaus");
                add("zoomi");
                add("zoonoosi");
                add("zucchini");
                add("zulu");
            }
        };

        Collections.sort(zeta);
        assertEquals(ENKOODAUSONGELMIA+"\n"+k + "\npalautetun listan pituus väärä\nPalautettu lista oli: " + zeta, 51, zeta.size());
        assertEquals(k, odotettu, zeta);
    }

    /*
     * 
     */
    @Test
    @Points("130.3")
    public void onMetodilPaatteiset() throws Throwable {
        Object o = luo(new File("pienilista.txt"));

        assertTrue("Lisää luokalle Sanatutkimus metodi public List<String> kirjaimeenLPaattyvatSanat()", classRef.method(o, "kirjaimeenLPaattyvatSanat").returning(List.class).takingNoParams().isPublic());

        String k = "Sanatutkimus s = new Sanatutkimus( new File(\"src/pienilista.txt\") );\n"
                + "s.kirjaimeenLPaattyvatSanat();";

        classRef.method(o, "kirjaimeenLPaattyvatSanat").returning(List.class).takingNoParams().withNiceError(k).invoke();
    }

    @Test
    @Points("130.3")
    public void lLoppuisetPieniLista() throws Throwable {
        Object o = luo(new File("pienilista.txt"));

        String k = "Sanatutkimus s = new Sanatutkimus( new File(\"src/pienilista.txt\") );\n"
                + "s.kirjaimeenLPaattyvatSanat();";

        List<String> zeta = (List<String>) classRef.method(o, "kirjaimeenLPaattyvatSanat").returning(List.class)
                .takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nPalautettu lista oli null.", zeta == null);

        List<String> odotettu = new ArrayList<String>();
        Collections.addAll(odotettu, "askel", "kennel", "petkel");

        Collections.sort(zeta);
        assertEquals(k + "\npalautetun listan pituus väärä\nPalautettu lista oli: " + zeta, 3, zeta.size());
        assertEquals(k, odotettu, zeta);
    }

    @Test
    @Points("130.3")
    public void lPaateTest() throws Exception, Throwable {
        FileWriter n;
        File eka = File.createTempFile("eka", "txt");
        n = new FileWriter(eka);
        n.append("kannel\ntalo\nsammal\nlol\nhoh\njoo\nlossi\nl\n");
        n.flush();
        n.close();

        String lisa = "Lisääthän sanat ArrayListiin, jonka metodi palauttaa?";
        ArrayList<String> odotettu = new ArrayList<String>() {
            {
                add("kannel");
                add("l");
                add("lol");
                add("sammal");
            }
        };

        Object o = luo(eka);

        String k = "Tiedoston sisältönä:\n"
                + "kannel\ntalo\nsammal\nlol\nhoh\njoo\nlossi\nl\n"
                + "\ns.kirjaimeenLPaattyvatSanat();";

        List<String> zeta = (List<String>) classRef.method(o, "kirjaimeenLPaattyvatSanat").returning(List.class).takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nPalautettu lista oli null.", zeta == null);

        Collections.sort(zeta);
        assertEquals(k + "\npalautetun listan pituus väärä\nPalautettu lista oli: " + zeta, 4, zeta.size());
        assertEquals(k, odotettu, zeta);

    }

    @Test
    @Points("130.3")
    public void lPaateTestMallilla() throws Throwable {
        Object o = luo(new File("sanalista.txt"));

        assertTrue("Lisää luokalle Sanatutkimus metodi public List<String> kirjaimeenLPaattyvatSanat()", classRef.method(o, "kirjaimeenLPaattyvatSanat").returning(List.class).takingNoParams().isPublic());

        String k = "Sanatutkimus s = new Sanatutkimus( new File(\"src/sanalista.txt\") );\n"
                + "s.kirjaimeenLPaattyvatSanat();";

        List<String> zeta = (List<String>) classRef.method(o, "kirjaimeenLPaattyvatSanat").returning(List.class).takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nPalautettu lista oli null.", zeta == null);

        List<String> odotettu = new ArrayList<String>() {
            {
                add("alkucocktail");
                add("alkutaival");
                add("askel");
                add("baseball");
                add("becquerel");
                add("cocktail");
                add("diesel");
                add("edistysaskel");
                add("elontaival");
                add("elämäntaival");  //
                add("emmental");
                add("erämaataival");  //
                add("goodwill");
                add("gospel");
                add("hajasävel");     //
                add("harmosammal");
                add("havusammal");
                add("hetesammal");
                add("huippusävel");   //
                add("hyppyaskel");
                add("hölkkäaskel");   //
                add("ilonkyynel");
                add("iskusävel");     //
                add("jouhikannel");
                add("juoksuaskel");
                add("kajal");
                add("kannel");
                add("kardaaninivel");
                add("karhunsammal");
                add("karstasammal");
                add("kennel");
                add("ketjuommel");
                add("keuhkosammal");
                add("kiertonivel");
                add("kilpisammal");
                add("kinnernivel");
                add("kivisammal");
                add("kokoaskel");
                add("kokosävelaskel");    //
                add("korallisammal");
                add("koristeommel");
                add("korpitaival");
                add("koruommel");
                add("korusävel");   //
                add("kukonaskel");
                add("kulosammal");
                add("kynsisammal");
                add("kyynel");
                add("kyynärnivel");   //
                add("kärkinivel");   //
                add("kävelyaskel");  //
                add("laakasammal");
                add("lahosammal");
                add("lehtisammal");
                add("leposävel");   //
                add("leukanivel");
                add("liekosammal");
                add("lomasävel");
                add("lonkkanivel");
                add("lopputaival");
                add("maksasammal");
                add("metsätaival");   //
                add("mosel");
                add("murrosnivel");
                add("nilkkanivel");
                add("nivel");
                add("normaalisävel");  //
                add("nukkasammal");
                add("nuotiosammal");
                add("näkinsammal");   //
                add("olkanivel");
                add("ommel");
                add("osasävel");    //
                add("pallonivel");
                add("pascal");
                add("perussävel");  //
                add("petkel");
                add("pikataival");
                add("pohjasävel"); //
                add("polvinivel");
                add("puoliaskel");
                add("puolisävelaskel");  //
                add("rahkasammal");
                add("rannenivel");
                add("reikäommel");  //
                add("reunaommel");
                add("rial");
                add("ristiaskel");
                add("ristinivel");
                add("ruskosammal");
                add("sammal");
                add("seinäsammal");  //
                add("seppel");
                add("sial");
                add("siirtymätaival");   //
                add("siksakkiommel");
                add("sisal");
                add("sivuaskel");
                add("skool");
                add("sorminivel");
                add("soul");
                add("suikerosammal");
                add("suoraommel");
                add("surunkyynel");
                add("sävel");   //
                add("sävelaskel");  //
                add("taival");
                add("tanssiaskel");
                add("tappinivel");
                add("trial");
                add("tunnussävel");  //
                add("tuulikannel");
                add("vaihtoaskel");
                add("valenivel");
                add("vapaataival");
                add("varvasaskel");
                add("varvasnivel");
                add("vauhtiaskel");
                add("vemmel");
                add("virrensävel");  //
                add("virsikannel");
                add("yläsävel");  //
            }
        };

        Collections.sort(zeta);
        assertEquals(k + "\npalautetun listan pituus väärä\nPalautettu lista oli: " + zeta, 122, zeta.size());
        assertEquals(k, odotettu, zeta);
    }

    /*
     * 
     */
    @Test
    @Points("130.4")
    public void onMetodiPalindromit() throws Throwable {
        Object o = luo(new File("pienilista.txt"));

        assertTrue("Lisää luokalle Sanatutkimus metodi public List<String> palindromit()", classRef.method(o, "palindromit").returning(List.class).takingNoParams().isPublic());

        String k = "Sanatutkimus s = new Sanatutkimus( new File(\"src/pienilista.txt\") );\n"
                + "s.palindromit();";

        classRef.method(o, "palindromit").returning(List.class).takingNoParams().withNiceError(k).invoke();
    }

    @Test
    @Points("130.4")
    public void palindromitPieniLista() throws Throwable {
        Object o = luo(new File("pienilista.txt"));

        String k = "Sanatutkimus s = new Sanatutkimus( new File(\"src/pienilista.txt\") );\n"
                + "s.palindromit();";

        List<String> zeta = (List<String>) classRef.method(o, "palindromit").returning(List.class)
                .takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nPalautettu lista oli null.", zeta == null);

        List<String> odotettu = new ArrayList<String>();
        Collections.addAll(odotettu, "autioitua", "suuruus", "utu");

        Collections.sort(zeta);
        assertEquals(k + "\npalautetun listan pituus väärä\nPalautettu lista oli: " + zeta, 3, zeta.size());
        assertEquals(k, odotettu, zeta);
    }

    @Test
    @Points("130.4")
    public void palindromitTest() throws Exception, Throwable {
        FileWriter n;
        File eka = File.createTempFile("eka", "txt");
        n = new FileWriter(eka);
        n.append("hissi\na\nb\nlol\nsaippuakauppias\nsiilo\nabba\n");
        n.flush();
        n.close();

        ArrayList<String> odotettu = new ArrayList<String>() {
            {
                add("a");
                add("abba");
                add("b");
                add("lol");
                add("saippuakauppias");
            }
        };

        Object o = luo(eka);

        String k = "Tiedoston sisältönä:\n"
                + "hissi\na\nb\nlol\nsaippuakauppias\nsiilo\nabba\n"
                + "\ns.palindromit();";

        List<String> zeta = (List<String>) classRef.method(o, "palindromit").returning(List.class).takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nPalautettu lista oli null.", zeta == null);

        Collections.sort(zeta);
        assertEquals(k + "\npalautetun listan pituus väärä\nPalautettu lista oli: " + zeta, 5, zeta.size());
        assertEquals(k, odotettu, zeta);

    }

    @Test
    @Points("130.4")
    public void palindromitSanalistalla() throws Throwable {
        Object o = luo(new File("sanalista.txt"));

        assertTrue("Lisää luokalle Sanatutkimus metodi public List<String> kirjaimenZSisaltavatSanat()", classRef.method(o, "palindromit").returning(List.class).takingNoParams().isPublic());

        String k = "Sanatutkimus s = new Sanatutkimus( new File(\"src/sanalista.txt\") );\n"
                + "s.palindromit();";

        List<String> zeta = (List<String>) classRef.method(o, "palindromit").returning(List.class).takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nPalautettu lista oli null.", zeta == null);

        List<String> odotettu = new ArrayList<String>() {
            {
                add("ajaja");
                add("akka");
                add("ala");
                add("alla");
                add("autioitua");
                add("ele");
                add("enne");
                add("hah");
                add("heh");
                add("huh");
                add("hyh");
                add("häh");       //
                add("imaami");
                add("isi");
                add("niin");
                add("oho");
                add("olo");
                add("opo");
                add("otto");
                add("piip");
                add("pop");
                add("sadas");
                add("sammas");
                add("sees");
                add("siis");
                add("sus");
                add("suuruus");
                add("sylys");
                add("sytytys");
                add("syys");
                add("syöppöys");   //
                add("tuut");
                add("tyyt");
                add("tööt");     //
                add("utu");
                add("yty");
                add("älä");     //
                add("ämmä");  //
                add("ässä");   //
            }
        };

        Collections.sort(zeta);
        assertEquals(k + "\npalautetun listan pituus väärä\nPalautettu lista oli: " + zeta, 39, zeta.size());
        assertEquals(k, odotettu, zeta);
    }

    /*
     * 
     */
    @Test
    @Points("130.5")
    public void onMetodiKaikkiVokaalitSisaltavatSanat() throws Throwable {
        Object o = luo(new File("pienilista.txt"));

        assertTrue("Lisää luokalle Sanatutkimus metodi public List<String> kaikkiVokaalitSisaltavatSanat()", classRef.method(o, "kaikkiVokaalitSisaltavatSanat").returning(List.class).takingNoParams().isPublic());

        String k = "Sanatutkimus s = new Sanatutkimus( new File(\"src/pienilista.txt\") );\n"
                + "s.kaikkiVokaalitSisaltavatSanat();";

        classRef.method(o, "kaikkiVokaalitSisaltavatSanat").returning(List.class).takingNoParams().withNiceError(k).invoke();
    }

    @Test
    @Points("130.5")
    public void kaikkiVokaalitSisaltavatSanatPieniLista() throws Throwable {
        Object o = luo(new File("pienilista.txt"));

        String k = "Sanatutkimus s = new Sanatutkimus( new File(\"src/pienilista.txt\") );\n"
                + "s.kaikkiVokaalitSisaltavatSanat();";

        List<String> zeta = (List<String>) classRef.method(o, "kaikkiVokaalitSisaltavatSanat").returning(List.class)
                .takingNoParams().withNiceError(k).invoke();
        assertFalse(k + "\nPalautettu lista oli null.", zeta == null);

        List<String> odotettu = new ArrayList<String>();
        Collections.addAll(odotettu, "juustohöyläperiaate", "valkokaulustyöläinen");

        Collections.sort(zeta);
        assertEquals(k + "\npalautetun listan pituus väärä\nPalautettu lista oli: " + zeta, 2, zeta.size());
        assertEquals(k, odotettu, zeta);
    }

//    @Test
//    @Points("130.5")
//    public void kaikkiVokaalitSisaltavatSanatTest() throws Exception, Throwable {
//        FileWriter n;
//        File eka = File.createTempFile("eka", "txt");
//        n = new FileWriter(eka);
//        n.append("myöhäiselokuva\nb\nympäristöntuhoaja\nsiilo\nabba\naeiouyäö\n");
//        n.flush();
//        n.close();
//
//        String lisa = "Lisääthän sanat ArrayListiin, jonka metodi palauttaa?";
//        ArrayList<String> odotettu = new ArrayList<String>() {
//            {
//                add("aeiouyäö");                
//                add("myöhäiselokuva");
//            }
//        };
//
//        Object o = luo(eka);
//
//        String k = "Tiedoston sisältönä:\n"
//                + "myöhäiselokuva\nb\nympäristöntuhoaja\nsiilo\nabba\naeiouyäö\n"
//                + "\ns.palindromit();";
//
//        List<String> zeta = (List<String>) classRef.method(o, "kaikkiVokaalitSisaltavatSanat").returning(List.class).takingNoParams().withNiceError(k).invoke();
//
//        assertFalse(k + "\nPalautettu lista oli null.", zeta == null);
//
//        Collections.sort(zeta);
//        assertEquals(k + "\npalautetun listan pituus väärä\nPalautettu lista oli: " + zeta, 2, zeta.size());
//        assertEquals(k, odotettu, zeta);
//
//    }

    @Test
    @Points("130.5")
    public void kaikkiVokaalitSisaltavatSanatSanalistalla() throws Throwable {
        Object o = luo(new File("sanalista.txt"));

        assertTrue("Lisää luokalle Sanatutkimus metodi public List<String> kaikkiVokaalitSisaltavatSanat()", classRef.method(o, "kaikkiVokaalitSisaltavatSanat").returning(List.class).takingNoParams().isPublic());

        String k = "Sanatutkimus s = new Sanatutkimus( new File(\"src/sanalista.txt\") );\n"
                + "s.kaikkiVokaalitSisaltavatSanat();";

        List<String> zeta = (List<String>) classRef.method(o, "kaikkiVokaalitSisaltavatSanat").returning(List.class).takingNoParams().withNiceError(k).invoke();

        assertFalse(k + "\nPalautettu lista oli null.", zeta == null);

        List<String> odotettu = new ArrayList<String>() {
            {
                add("arvostelukyvyttömästi");
                add("juustohöyläperiaate");
                add("kotitaloustyöntekijä");
                add("maataloustyöntekijä");
                add("myöhäiselokuva");
                add("suojatyöntekijä");
                add("taloustyöntekijä");
                add("ulkomaantyöntekijä");
                add("valkokaulustyöläinen");
                add("valkokaulustyöntekijä");
                add("ympäristönsuojelija");
            }
        };

        Collections.sort(zeta);
        assertEquals(k + "\npalautetun listan pituus väärä\nPalautettu lista oli: " + zeta, 11, zeta.size());
        assertEquals(k, odotettu, zeta);
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
