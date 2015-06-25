
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArvauspeliTest {

    String klassName = "Arvauspeli";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setup() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    @Points("105.1")
    public void onOlemassaMetodiOnkoSuurempiKuin() throws Throwable {
        MockInOut io = new MockInOut("e\n\n\n\n\n\n\n\n\n");
        Arvauspeli peli = new Arvauspeli();

        String metodi = "onkoSuurempiKuin";
        assertTrue("tee luokkaan Arvauspeli metodi public boolean onkoSuurempiKuin(int luku)",
                klass.method(peli, metodi).returning(boolean.class).taking(int.class).isPublic());


        String v = "Ongelman aiheutti \n"
                + "Arvauspeli a = new Arvauspeli();"
                + "a.onkoSuurempiKuin(1);\n";

        klass.method(peli, metodi).returning(boolean.class).taking(int.class).
                withNiceError(v).invoke(1);
    }

    @Test
    @Points("105.1")
    public void onkoSuurempiKuinPalauttaaFalseKunE() throws Throwable {
        MockInOut io = new MockInOut("e\n");
        Arvauspeli peli = new Arvauspeli();

        String metodi = "onkoSuurempiKuin";

        Boolean result = klass.method(peli, metodi).returning(boolean.class).taking(int.class).invoke(44);

        assertFalse("Kun on luotu olio Arvauspeli peli = new Arvauspeli(); \n"
                + "Jos käyttäjä syöttää e metodia peli.onkoSuurempiKuin kutsuttaessa, tulee metodin palauttaa false.", result);

        String out = io.getOutput();
        String v = "Kun on luotu olio Arvauspeli peli = new Arvauspeli(); \n"
                + "metodikutsun onkoSuurempiKuin(44); pitäisi tulostaa "
                + "\"Onko lukusi suurempi kuin 44 luku? (k/e)\". ";
        assertTrue(v + "\nNyt se ei tulostanut mitään", out.length() > 0);
        assertTrue(v + "\nNyt tulostui\n" + out, out.contains("" + 44) && out.contains("nko lukusi suurempi kuin"));
    }

    @Test
    @Points("105.1")
    public void onkoSuurempiKuinPalauttaaTrueKunK() throws Throwable {
        MockInOut io = new MockInOut("k\n");

        Arvauspeli peli = new Arvauspeli();

        String metodi = "onkoSuurempiKuin";

        Boolean result = klass.method(peli, metodi).returning(boolean.class).taking(int.class).invoke(55);

        assertTrue("Kun on luotu olio Arvauspeli peli = new Arvauspeli(); \n"
                + "Jos käyttäjä syöttää k metodia onkoSuurempiKuin kutsuttaessa, tulee metodin palauttaa true.", result);

        String out = io.getOutput();
        String v = "Kun on luotu olio Arvauspeli peli = new Arvauspeli(); \n"
                + "metodikutsun peli.onkoSuurempiKuin(55); pitäisi tulostaa "
                + "\"Onko lukusi suurempi kuin 55 luku? (k/e)\". ";
        assertTrue(v + "\nNyt se ei tulostanut mitään", out.length() > 0);
        assertTrue(v + "\nNyt tulostui\n" + out, out.contains("" + 55) && out.contains("nko lukusi suurempi kuin"));

    }

    /*
     * 
     */
    @Test
    @Points("105.2")
    public void onOlemassaMetodiKeskiarvo() throws Throwable {
        String metodi = "keskiarvo";
        Arvauspeli peli = new Arvauspeli();
        assertTrue("tee luokkaan Arvauspeli metodi public int keskiarvo(int ekaLuku, int tokaLuku)",
                klass.method(peli, metodi).returning(int.class).taking(int.class, int.class).isPublic());


        String v = "Ongelman aiheutti \n"
                + "Arvauspeli a = new Arvauspeli();"
                + "a.keskiarvo(3, 8);\n";

        klass.method(peli, metodi).returning(int.class).taking(int.class, int.class).
                withNiceError(v).invoke(3, 8);
    }

    @Test
    @Points("105.2")
    public void metodiKeskiarvoToimiiOikein() {
        int[][] luvutJaOletetut = {{1, 1, 1}, {3, 4, 3}, {6, 12, 9}, {100, 100, 100}};

        for (int[] luvut : luvutJaOletetut) {
            int tulos = -1;
            try {
                Arvauspeli peli = new Arvauspeli();
                tulos = klass.method(peli, "keskiarvo").returning(int.class).taking(int.class, int.class).invoke(luvut[0], luvut[1]);
            } catch (Throwable ex) {
                Logger.getLogger(ArvauspeliTest.class.getName()).log(Level.SEVERE, null, ex);
            }

            Assert.assertEquals("Tarkista että lukujen " + luvut[0] + " ja " + luvut[1] + " keskiarvo on " + luvut[2] + ".", luvut[2], tulos);
        }

    }

    @Points("105.3")
    @Test(timeout = 10000)
    public void testaaArvausLogiikkaa() {
        testaaLukuaVerbose(1, 10, null);
        testaaLukuaVerbose(1, 10, null);
        testaaLukuaVerbose(1, 10, null);
        testaaLukua(1, 100, null);
        testaaLukua(1, 10000000, null);
        testaaLukua(50, 150, null);
        testaaLukua(50, 150, false);
        testaaLukua(78, 1193, null);
        testaaLukua(78, 1193, true);
    }

    private void testaaLukuaVerbose(int ala, int yla, Boolean vastausAina) {
        ArvauspeliTest.BinaariHaunTulokset tulokset = generoiTulokset(ala, yla, vastausAina);
        // määrittele valmiiksi vastaukset, joiden pitäisi johtaa lukuun
        List<String> vastaukset = tulokset.getVastaukset();

        String input = "";
        String vastausLista = "";
        for (int i = 0; i < vastaukset.size(); i++) {
            input += vastaukset.get(i) + "\n";
            vastausLista += vastaukset.get(i);
            if (i < vastaukset.size() - 1) {
                vastausLista += ", ";
            }
        }

        MockInOut mio = new MockInOut(input);

        try {
            Arvauspeli peli = new Arvauspeli();
            peli.pelaa(ala, yla);
        } catch (NoSuchElementException e) {
            fail("Ohjelmasi yrittää lukea numeroa lukua vaikka ei enää tarvitse. \n"
                    + "Kaikissa tapauksissa ei tarvitse tehdä maksimimäärää kysymyksiä. \n"
                    + "Lukua " + tulokset.valittuLuku + " lukuvälistä " + ala + "-" + yla + " haettaessa "
                    + "selviää vastauksilla: "
                    + vastausLista);
        } catch (Exception e) {
            fail("Metodi arvausPeli() heitti poikkeuksen lukuvälillä "
                    + ala + "-" + yla + ", vastauksilla: "
                    + vastausLista
                    + " testaa metodia vielä käsin ennen testien suorittamista: " + e.toString());
        }

        String output = mio.getOutput();

        Pattern pattern = Pattern.compile(".*?kuin\\s+(\\d+)", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(output);

        int mones = 1;
        for (Integer kysyttyLuku : tulokset.getKysytytLuvut()) {
            if (!matcher.find()) {
                fail("Arvauspelin pitäisi kysyä " + tulokset.getKysytytLuvut().size() + " kysymystä lukuvälillä " + ala + "-" + yla + ", kun luku on: " + tulokset.valittuLuku + ", syötteellä: " + vastausLista + ". \n"
                        + "Ohjelmasi tulosti:\n" + output
                        + "\nTarkista, että ohjelmasi toimii tehtävänannon mukaisesti.");
            }

            String tarjottuLukuString = matcher.group(1);
            int tarjottuLuku = Integer.parseInt(tarjottuLukuString);
            if (tarjottuLuku != kysyttyLuku) {
                fail("Arvauspeli kysyi väärää lukua " + tarjottuLuku + " " + mones + ". kysymyksessä, kun olisi pitänyt kysyä lukua " + kysyttyLuku + "\n lukuvälillä " + ala + "-" + yla + ", syötteellä: " + vastausLista + ". Arvattava luku oli " + tulokset.valittuLuku + ""
                        + "\nOhjelmasi tulostus:\n" + output);
            }
            mones++;
        }

        Pattern pattern2 = Pattern.compile(".*luku\\s+on\\s+(\\d+).*", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher2 = pattern2.matcher(output);

        boolean vastausLoytyy = matcher2.matches();
        if (!vastausLoytyy) {
            fail("Metodi arvausPeli() ei tulosta valittua lukua. Tarkista, että ohjelmasi toimii tehtävänannon mukaisesti.");
        }

        String palautettuLukuString = matcher2.group(1);
        int palautettuLuku = Integer.parseInt(palautettuLukuString);

        int luku = tulokset.getValittuLuku();
        if (palautettuLuku != luku) {
            fail("Arvauspeli palautti valituksi luvuksi " + palautettuLuku
                    + ", kun sen olisi pitänyt olla " + luku + ". Lukuväli oli "
                    + ala + "-" + yla + " ja syötteenä käytettiin vastauksia: " + vastausLista);
        }
    }

    private void testaaLukua(int ala, int yla, Boolean vastausAina) {
        ArvauspeliTest.BinaariHaunTulokset tulokset = generoiTulokset(ala, yla, vastausAina);
        // määrittele valmiiksi vastaukset, joiden pitäisi johtaa lukuun
        List<String> vastaukset = tulokset.getVastaukset();

        String input = "";
        String vastausLista = "";
        for (int i = 0; i < vastaukset.size(); i++) {
            input += vastaukset.get(i) + "\n";
            vastausLista += vastaukset.get(i);
            if (i < vastaukset.size() - 1) {
                vastausLista += ", ";
            }
        }

        MockInOut mio = new MockInOut(input);

        try {
            Arvauspeli peli = new Arvauspeli();
            peli.pelaa(ala, yla);
        } catch (NoSuchElementException e) {
            fail("Ohjelmasi yrittää lukea numeroa lukua vaikka ei enää tarvitse. "
                    + "Kaikissa tapauksissa ei tarvitse tehdä maksimimäärää kysymyksiä. "
                    + "Lukua " + tulokset.valittuLuku + " lukuvälistä " + ala + "-" + yla + " haettaessa "
                    + "selviää vastauksilla: "
                    + vastausLista);
        } catch (Exception e) {
            fail("Metodi arvausPeli() heitti poikkeuksen lukuvälillä "
                    + ala + "-" + yla + ", vastauksilla: "
                    + vastausLista
                    + " testaa metodia vielä käsin ennen testien suorittamista: " + e.toString());
        }

        String output = mio.getOutput();

        Pattern pattern = Pattern.compile(".*?kuin\\s+(\\d+)", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(output);

        int mones = 1;
        for (Integer kysyttyLuku : tulokset.getKysytytLuvut()) {
            if (!matcher.find()) {
                fail("Arvauspelin pitäisi kysyä " + tulokset.getKysytytLuvut().size() + " kysymystä lukuvälillä " + ala + "-" + yla + ", kun luku on: " + tulokset.valittuLuku + ", syötteellä: " + vastausLista + ". Tarkista, että ohjelmasi toimii tehtävänannon mukaisesti.");
            }

            String tarjottuLukuString = matcher.group(1);
            int tarjottuLuku = Integer.parseInt(tarjottuLukuString);
            if (tarjottuLuku != kysyttyLuku) {
                fail("Arvauspeli kysyi väärää lukua " + tarjottuLuku + " " + mones + ". kysymyksessä, kun olisi pitänyt kysyä lukua " + kysyttyLuku + " lukuvälillä " + ala + "-" + yla + ", syötteellä: " + vastausLista + ". Arvattava luku oli" + tulokset.valittuLuku);
            }
            mones++;
        }

        Pattern pattern2 = Pattern.compile(".*luku\\s+on\\s+(\\d+).*", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher2 = pattern2.matcher(output);

        boolean vastausLoytyy = matcher2.matches();
        if (!vastausLoytyy) {
            fail("Metodi arvausPeli() ei tulosta valittua lukua. Tarkista, että ohjelmasi toimii tehtävänannon mukaisesti.");
        }

        String palautettuLukuString = matcher2.group(1);
        int palautettuLuku = Integer.parseInt(palautettuLukuString);

        int luku = tulokset.getValittuLuku();
        if (palautettuLuku != luku) {
            fail("Arvauspeli palautti valituksi luvuksi " + palautettuLuku
                    + ", kun sen olisi pitänyt olla " + luku + ". Lukuväli oli "
                    + ala + "-" + yla + " ja syötteenä käytettiin vastauksia: " + vastausLista);
        }
    }

    private ArvauspeliTest.BinaariHaunTulokset generoiTulokset(int ala, int yla, Boolean vastausAina) {
        Random random = new Random();
        int nykyinenAla = ala;
        int nykyinenYla = yla;

        List<String> vastaukset = new ArrayList<String>();
        List<Integer> kysytytLuvut = new ArrayList<Integer>();
        while (nykyinenAla < nykyinenYla) {
            int puolivali = (nykyinenAla + nykyinenYla) / 2;
            kysytytLuvut.add(puolivali);

            boolean suurempi;
            if (vastausAina != null) {
                suurempi = vastausAina.booleanValue();
            } else {
                suurempi = random.nextBoolean();
            }

            if (suurempi) {
                vastaukset.add("k");
                nykyinenAla = puolivali + 1;
            } else {
                vastaukset.add("e");
                nykyinenYla = puolivali;
            }
        }

        return new ArvauspeliTest.BinaariHaunTulokset(nykyinenAla, vastaukset, kysytytLuvut);
    }

    private class BinaariHaunTulokset {

        private int valittuLuku;
        private List<String> vastaukset;
        private List<Integer> kysytytLuvut;

        public BinaariHaunTulokset(int valittuLuku,
                List<String> vastaukset, List<Integer> kysytytLuvut) {
            this.valittuLuku = valittuLuku;
            this.vastaukset = vastaukset;
            this.kysytytLuvut = kysytytLuvut;
        }

        public int getValittuLuku() {
            return valittuLuku;
        }

        public List<Integer> getKysytytLuvut() {
            return kysytytLuvut;
        }

        public List<String> getVastaukset() {
            return vastaukset;
        }
    }
}
