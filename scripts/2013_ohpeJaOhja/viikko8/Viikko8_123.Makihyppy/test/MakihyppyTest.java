
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("123.1 123.2 123.3 123.4")
public class MakihyppyTest {

    String klassName = "Main";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void luokkaJulkinen() {
        assertTrue("Luokan " + klassName + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void lopettaa() throws Throwable {
        String syote = "Arto\n\nhyppaa\nlopeta\n";
        MockInOut io = new MockInOut(syote);
        try {
            suorita(f(syote));
        } catch (Throwable e) {
            if (e.toString().contains("NoSuchElementExc")) {
                fail("Varmista, että ohjelmasi lopettaa syötteellä\n" + syote);
            }
        }
    }

    @Test
    public void yksiKierrosYksiHyppaajaTulostusKunnossa() throws Throwable {
        String syote = "Arto\n\nhyppaa\nlopeta\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] rivit = {
            "Kumpulan mäkiviikot",
            "Syötä kilpailun osallistujat yksi kerrallaan, tyhjällä merkkijonolla siirtyy hyppyvaiheeseen.",
            "  Osallistujan nimi:",
            "  Osallistujan nimi:",
            "Kilpailu alkaa!",
            "Kirjoita \"hyppaa\" niin hypätään, muuten lopetetaan:",
            "Hyppyjärjestys:",
            "  1. Arto (0 pistettä)",
            "Kierroksen 1 tulokset",
            "  Arto",
            "    pituus:",
            "    tuomaripisteet:",
            "Kirjoita \"hyppaa\" niin hypätään, muuten lopetetaan:",
            "Kiitos!",
            "Kilpailun lopputulokset:",
            "Sija    Nimi",
            "1       Arto",
            "          hyppyjen pituudet:"
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : rivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }
    }

    @Test
    public void kaksiKierrostaYksiHyppaajaTulostusKunnossa() throws Throwable {
        String syote = "Arto\n\nhyppaa\nhyppaa\nlopeta\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] rivit = {
            "Kumpulan mäkiviikot",
            "Syötä kilpailun osallistujat yksi kerrallaan, tyhjällä merkkijonolla siirtyy hyppyvaiheeseen.",
            "  Osallistujan nimi:",
            "  Osallistujan nimi:",
            "Kilpailu alkaa!",
            "Kirjoita \"hyppaa\" niin hypätään, muuten lopetetaan:",
            "Hyppyjärjestys:",
            "  1. Arto (0 pistettä)",
            "Kierroksen 1 tulokset",
            "  Arto",
            "    pituus:",
            "    tuomaripisteet:",
            "Kirjoita \"hyppaa\" niin hypätään, muuten lopetetaan:",
            "Hyppyjärjestys:",
            "  1. Arto (",
            "Kierroksen 2 tulokset",
            "  Arto",
            "    pituus:",
            "    tuomaripisteet:",
            "Kirjoita \"hyppaa\" niin hypätään, muuten lopetetaan:",
            "Kiitos!",
            "Kilpailun lopputulokset:",
            "Sija    Nimi",
            "1       Arto",
            "          hyppyjen pituudet:"
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : rivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }
    }

    @Test
    public void kolmeKierrostaYksiHyppaajaTulostusKunnossa() throws Throwable {
        String syote = "Arto\n\nhyppaa\nhyppaa\nhyppaa\nlopeta\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] rivit = {
            "Kumpulan mäkiviikot",
            "Syötä kilpailun osallistujat yksi kerrallaan, tyhjällä merkkijonolla siirtyy hyppyvaiheeseen.",
            "  Osallistujan nimi:",
            "  Osallistujan nimi:",
            "Kilpailu alkaa!",
            "Kirjoita \"hyppaa\" niin hypätään, muuten lopetetaan:",
            "Hyppyjärjestys:",
            "  1. Arto (0 pistettä)",
            "Kierroksen 1 tulokset",
            "  Arto",
            "    pituus:",
            "    tuomaripisteet:",
            "Kirjoita \"hyppaa\" niin hypätään, muuten lopetetaan:",
            "Hyppyjärjestys:",
            "  1. Arto (",
            "Kierroksen 2 tulokset",
            "  Arto",
            "    pituus:",
            "    tuomaripisteet:",
            "Kirjoita \"hyppaa\" niin hypätään, muuten lopetetaan:",
            "Hyppyjärjestys:",
            "  1. Arto (",
            "Kierroksen 3 tulokset",
            "  Arto",
            "    pituus:",
            "    tuomaripisteet:",
            "Kirjoita \"hyppaa\" niin hypätään, muuten lopetetaan:",
            "Kiitos!",
            "Kilpailun lopputulokset:",
            "Sija    Nimi",
            "1       Arto",
            "          hyppyjen pituudet:"
        };

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : rivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }
    }

    @Test
    public void yksiKierrosYksiHyppaajaPisteidenlaskuKunnossa() throws Throwable {
        String syote = "Arto\n\nhyppaa\nlopeta\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String kierros1 = kierroksenTulokset(io.getOutput(), 1);
        ArrayList<MakihyppyTest.PitPist> pitPist = pituusJaPisteet(kierros1, 1);

        assertPisteetOk(pitPist, 1, kierros1);

        loppuTulostenIlmoitus1osallistuja1kierros(io.getOutput(), pitPist);
    }

    @Test
    public void kaksiKierrostaYksiHyppaajaPisteidenlaskuKunnossa() throws Throwable {
        String syote = "Arto\n\nhyppaa\nhyppaa\nlopeta\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String out = io.getOutput();

        String kierros1 = kierroksenTulokset(out, 1);
        ArrayList<MakihyppyTest.PitPist> pitPist1 = pituusJaPisteet(kierros1, 1);
        assertPisteetOk(pitPist1, 1, kierros1);

        String kierros2 = kierroksenTulokset(out, 2);
        ArrayList<MakihyppyTest.PitPist> pitPist2 = pituusJaPisteet(kierros2, 2);
        assertPisteetOk(pitPist2, 2, kierros2);

        eiAinaSamaaTulosta(pitPist1, pitPist2, out);

        loppuTulostenIlmoitus1osallistuja2kierrosta(io.getOutput(), pitPist1, pitPist2);
    }

    @Test
    public void kolmeKierrostaYksiHyppaajaPisteidenlaskuKunnossa() throws Throwable {
        String syote = "Arto\n\nhyppaa\nhyppaa\nhyppaa\nlopeta\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String out = io.getOutput();

        String kierros1 = kierroksenTulokset(out, 1);
        ArrayList<MakihyppyTest.PitPist> pitPist1 = pituusJaPisteet(kierros1, 1);
        assertPisteetOk(pitPist1, 1, kierros1);

        String kierros2 = kierroksenTulokset(out, 2);
        ArrayList<MakihyppyTest.PitPist> pitPist2 = pituusJaPisteet(kierros2, 2);
        assertPisteetOk(pitPist2, 2, kierros2);

        String kierros3 = kierroksenTulokset(out, 3);
        ArrayList<MakihyppyTest.PitPist> pitPist3 = pituusJaPisteet(kierros3, 3);
        assertPisteetOk(pitPist3, 3, kierros3);

        eiAinaSamaaTulosta(pitPist1, pitPist2, out);
        eiAinaSamaaTulosta(pitPist1, pitPist3, out);
        eiAinaSamaaTulosta(pitPist3, pitPist2, out);

        loppuTulostenIlmoitus1osallistuja3kierrosta(io.getOutput(), pitPist1, pitPist2, pitPist3);
    }

    @Test
    public void yksiKierrosKaksiHyppaajaa() throws Throwable {
        String syote = "Arto\nPekka\n\nhyppaa\nlopeta\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String[] rivit = {
            "Kumpulan mäkiviikot",
            "Syötä kilpailun osallistujat yksi kerrallaan, tyhjällä merkkijonolla siirtyy hyppyvaiheeseen.",
            "  Osallistujan nimi:",
            "  Osallistujan nimi:",
            "  Osallistujan nimi:",
            "Kilpailu alkaa!",
            "Kirjoita \"hyppaa\" niin hypätään, muuten lopetetaan:",
            "Hyppyjärjestys:",};

        String output = io.getOutput();
        String op = output;
        for (String menuRivi : rivit) {
            int ind = op.indexOf(menuRivi);
            assertRight(menuRivi, syote, output, ind > -1);
            op = op.substring(ind + 1);
        }

        String tulos = kierroksenTulokset(output, 1);
        ArrayList<MakihyppyTest.PitPist> pitPist = pituusJaPisteet(tulos, 1);
        assertTrue(tulos.length() > 1);
        assertPisteetOk(pitPist, 1, tulos);

        loppuTulostenIlmoitus2osallistuja1kierros(output, pitPist);

        jarjestysKunnossa(output, pitPist);
    }

    @Test
    public void yksiKierrosViisiHyppaajaa() throws Throwable {
        String syote = "Arto\nPekka\nMatti\nMikko\nJukka\n\nhyppaa\nlopeta\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String output = io.getOutput();

        String tulos = kierroksenTulokset(output, 1);
        ArrayList<MakihyppyTest.PitPist> pitPist = pituusJaPisteet(tulos, 1);
        assertTrue(tulos.length() > 1);
        assertPisteetOk(pitPist, 1, tulos);

        eiSamaaPituuttaKaikilla(pitPist, tulos);

        jarjestysKunnossa(output, pitPist);
    }

    @Test
    public void kaksiKierrostaViisiHyppaajaa() throws Throwable {
        String syote = "Arto\nPekka\nMatti\nMikko\nJukka\n\nhyppaa\nhyppaa\nlopeta\n";
        MockInOut io = new MockInOut(syote);
        suorita(f(syote));

        String output = io.getOutput();

        String tulos1 = kierroksenTulokset(output, 1);
        ArrayList<MakihyppyTest.PitPist> pitPist1 = pituusJaPisteet(tulos1, 1);
        assertTrue(tulos1.length() > 1);
        assertPisteetOk(pitPist1, 1, tulos1);

        String tulos2 = kierroksenTulokset(output, 1);
        ArrayList<MakihyppyTest.PitPist> pitPist2 = pituusJaPisteet(tulos2, 2);
        assertTrue(tulos2.length() > 1);
        assertPisteetOk(pitPist2, 2, tulos2);

        oikeaHyppyjarjestys(output, pitPist1);

    }

    private String kierroksenTulokset(String output, int k) {
        String rivi = haeRiviJolla(output, "Kierroksen " + k + " tulokset");
        if (rivi == null) {
            // ei pitäs päätyä tänne
            rivi = "Kierroksen " + k + " tulokset";
        }

        int alku = output.indexOf(rivi) + (rivi).length();
        int loppu = output.substring(alku).indexOf("Kirjoita \"hyppaa\" niin hypätään, muuten lopetetaan:");
        String tulos = output.substring(alku, alku + loppu);
        return tulos;
    }

    private String f(String syote) {
        return "\nkäyttäjän syöte oli:\n" + syote;
    }

    private void suorita(String error) throws Throwable {
        String[] args = new String[0];
        klass.staticMethod("main").
                returningVoid().
                taking(String[].class).withNiceError(error).
                invoke(args);
    }

    private void assertRight(String menuRivi, String syote, String output, boolean ehto) {
        assertTrue("Varmista, että ohjelmasi tulostusasu on täsmälleen sama kuin esimerkissä\n"
                + f(syote) + "\nohjelman olisi pitänyt tulostaa oikeassa kohdassa rivi jolla lukee \"" + menuRivi + "\" "
                + "\n"
                + "ohjelmasi tulosti:\n\n" + output, ehto);
    }

    private boolean viisiLukua(String mj) {
        return mj.matches("\\d+\\s+\\d+\\s+\\d+\\s+\\d+\\s+\\d+");
    }

    private void assertPisteetOk(ArrayList<MakihyppyTest.PitPist> pitPist, int k, String kierros) {
        for (MakihyppyTest.PitPist pitPist1 : pitPist) {
            assertTrue("Hyppääjän " + pitPist1.nimi + " hypyn pituus kierroksella " + k + " ei sallitulla alueella\n"
                    + "Kierroksen " + k + " tulokset olivat: \n" + kierros, pitPist1.pit > 59 && pitPist1.pit < 121);

            for (int p : pitPist1.pist) {
                assertTrue("Hyppääjän " + pitPist1.nimi + " tuomaripisteet kierroksella " + k + " ei sallitulla alueella\n"
                        + "Kierroksen " + k + " tulokset olivat: \n" + kierros, p > -1 && p < 21);

            }
        }
    }

    private int[] otaHypyt(String rivi) {
        if (!rivi.contains("hyppyjen pituudet:")) {
            return null;
        }

        String hypyt = rivi.replaceAll("\\D", " ").trim();

        ArrayList<Integer> hyppylista = new ArrayList<Integer>();

        Scanner luvut = new Scanner(hypyt);
        while (luvut.hasNextInt()) {
            hyppylista.add(luvut.nextInt());
        }

        int[] hyppyTaulukko = new int[hyppylista.size()];

        for (int i = 0; i < hyppyTaulukko.length; i++) {
            hyppyTaulukko[i] = hyppylista.get(i);
        }

        return hyppyTaulukko;
    }

    private int laskePisteet(MakihyppyTest.PitPist... ppp) {
        int p = 0;
        for (MakihyppyTest.PitPist pp : ppp) {
            Arrays.sort(pp.pist);
            p += pp.pist[1] + pp.pist[2] + pp.pist[3] + pp.pit;
        }

        return p;
    }

    private int etsiRivi(String[] rivit, String nimi) {
        for (int i = 0; i < rivit.length; i++) {
            if (rivit[i].contains(nimi)) {
                return i;
            }
        }

        return -1;
    }

    private int otaPisteet(String rivi) {
        int p = -1;

        try {
            rivi = rivi.substring(2);
            rivi = rivi.replaceAll("\\D", "").trim();
            p = Integer.parseInt(rivi);
        } catch (Exception e) {
        }

        return p;
    }

    private void loppuTulostenIlmoitus2osallistuja1kierros(String output, ArrayList<MakihyppyTest.PitPist> pitPist) {
        MakihyppyTest.PitPist[] ppt = {pitPist.get(0)};

        lopputulostenIlmoitus1osallistuja(ppt, output);

        MakihyppyTest.PitPist[] ppt2 = {pitPist.get(1)};

        lopputulostenIlmoitus1osallistuja(ppt2, output);
    }

    private void loppuTulostenIlmoitus1osallistuja1kierros(String output, ArrayList<MakihyppyTest.PitPist> pitPist) {
        MakihyppyTest.PitPist[] ppt = {pitPist.get(0)};

        lopputulostenIlmoitus1osallistuja(ppt, output);
    }

    private void loppuTulostenIlmoitus1osallistuja2kierrosta(String output, ArrayList<MakihyppyTest.PitPist> pitPist, ArrayList<MakihyppyTest.PitPist> pitPist2) {
        MakihyppyTest.PitPist[] ppt = {pitPist.get(0), pitPist2.get(0)};

        lopputulostenIlmoitus1osallistuja(ppt, output);
    }

    private void loppuTulostenIlmoitus1osallistuja3kierrosta(String output, ArrayList<MakihyppyTest.PitPist> pitPist, ArrayList<MakihyppyTest.PitPist> pitPist2, ArrayList<MakihyppyTest.PitPist> pitPist3) {
        MakihyppyTest.PitPist[] ppt = {pitPist.get(0), pitPist2.get(0), pitPist3.get(0)};

        lopputulostenIlmoitus1osallistuja(ppt, output);
    }

    private void links(String output, MakihyppyTest.PitPist pp1, MakihyppyTest.PitPist pp2) {

        if (laskePisteet(pp1) > laskePisteet(pp2)) {
            assertTrue("kierroksen 2 hyppyjärjestys väärä\n"
                    + "ohjelmasi tulosti\n"
                    + output, output.indexOf(pp1.nimi) > output.indexOf(pp2.nimi));

        } else if (laskePisteet(pp1) < laskePisteet(pp2)) {
            assertTrue("kierroksen 2 hyppyjärjestys väärä\n"
                    + "ohjelmasi tulosti\n"
                    + output, output.indexOf(pp1.nimi) < output.indexOf(pp2.nimi));
        }
    }

    private void rechts(String output, MakihyppyTest.PitPist pp1, MakihyppyTest.PitPist pp2) {
        String loppu = output.substring(output.indexOf("Sija    Nimi") + "Sija    Nimi".length());
        if (laskePisteet(pp1) > laskePisteet(pp2)) {
            assertTrue("lopputuloksia ei esitetä oikeassa järjestyksessä\n"
                    + "ohjelmasi tulosti\n"
                    + output, loppu.indexOf(pp1.nimi) < loppu.indexOf(pp2.nimi));

        } else if (laskePisteet(pp1) < laskePisteet(pp2)) {
            assertTrue("lopputuloksia ei esitetä oikeassa järjestyksessä\n"
                    + "ohjelmasi tulosti\n"
                    + output, loppu.indexOf(pp1.nimi) > loppu.indexOf(pp2.nimi));

        }
    }

    private void hyppyJarjestysKunnossa(String output, ArrayList<MakihyppyTest.PitPist> pitPist) {

        for (int i = 0; i < pitPist.size(); i++) {
            for (int j = i + 1; j < pitPist.size(); j++) {
                links(output, pitPist.get(i), pitPist.get(j));
            }

        }

    }

    private void jarjestysKunnossa(String output, ArrayList<MakihyppyTest.PitPist> pitPist) {

        for (int i = 0; i < pitPist.size(); i++) {
            for (int j = i + 1; j < pitPist.size(); j++) {
                rechts(output, pitPist.get(i), pitPist.get(j));
            }

        }

    }

    private void lopputulostenIlmoitus1osallistuja(MakihyppyTest.PitPist[] ppt, String output) {
        String nimi = ppt[0].nimi;

        String loppu = output.substring(output.indexOf("Sija    Nimi") + "Sija    Nimi".length());
        String[] rivit = loppu.split("\n");

        int ind = etsiRivi(rivit, nimi);

        int pist = otaPisteet(rivit[ind]);
        assertFalse("Lopputulosten ilmoittamisessa virhe, "
                + "tulostuksessa olisi pitänyt olla rivi joka on muotoa\n"
                + "1       " + nimi + " (" + laskePisteet(ppt) + " pistettä)\n"
                + "ohjelma tulosti \n" + loppu, pist == -1);

        assertEquals("Hyppääjän " + nimi + " pisteet ilmoiteaan lopputulosten yhteydessä väärin\n"
                + "ohjelmasi tulostaa\n" + output, laskePisteet(ppt), pist);

        int[] hypyt = otaHypyt(rivit[ind + 1]);
        assertFalse("Hyppääjän " + nimi + " hyppyjen pituudet ilmoiteaan lopputulosten yhteydessä väärin\n"
                + "ohjelmasi tulostaa\n" + output, hypyt == null);

        assertTrue("Hyppääjän " + nimi + " hyppyjen pituudet ilmoiteaan lopputulosten yhteydessä väärin\n"
                + "ohjelmasi tulostaa\n" + output, hypyt.length == ppt.length);

        for (int i = 0; i < hypyt.length; i++) {
            assertEquals("Hyppääjän " + nimi + " hyppyjen pituudet ilmoiteaan lopputulosten yhteydessä väärin\n"
                    + "ohjelmasi tulostaa\n" + output, hypyt[i], ppt[i].pit);

        }
    }

    private void eiAinaSamaaTulosta(ArrayList<MakihyppyTest.PitPist> pitPist1, ArrayList<MakihyppyTest.PitPist> pitPist2, String out) {
        MakihyppyTest.PitPist pp1 = pitPist1.get(0);
        MakihyppyTest.PitPist pp2 = pitPist2.get(0);

        boolean sama = true;
        for (int i = 0; i < 5; i++) {
            if (pp1.pist[i] != pp2.pist[i]) {
                sama = false;
            }
        }

        assertFalse("Tyylipisteet pitää arpoa!\n"
                + "Ohjelmasi tulosti\n"
                + out, sama);

        sama = true;

        for (int i = 0; i < 5; i++) {
            if (pp1.pist[0] != pp1.pist[i]) {
                sama = false;
            }
        }

        assertFalse("Tyylipisteet pitää arpoa!\n"
                + "Ohjelmasi tulosti\n"
                + out, sama);

        sama = true;

        for (int i = 0; i < 5; i++) {
            if (pp2.pist[0] != pp2.pist[i]) {
                sama = false;
            }
        }

        assertFalse("Tyylipisteet pitää arpoa!\n"
                + "Ohjelmasi tulosti\n"
                + out, sama);
    }

    private void oikeaHyppyjarjestys(String output, ArrayList<MakihyppyTest.PitPist> pitPist) {
        output = output.substring(output.indexOf("Hyppyjärjestys") + "Hyppyja".length());
        output = output.substring(output.indexOf("Hyppyjärjestys"));

        hyppyJarjestysKunnossa(output, pitPist);
    }

    private String haeRiviJolla(String output, String teksti) {
        String[] rivit = output.split("\n");
        for (String rivi : rivit) {
            if (rivi.contains(teksti)) {
                return rivi;
            }
        }


        return null;
    }

    private void eiSamaaPituuttaKaikilla(ArrayList<MakihyppyTest.PitPist> pitPist, String tulos) {
        int[] pituudet = new int[pitPist.size()];

        int i = 0;
        for (MakihyppyTest.PitPist pp : pitPist) {
            pituudet[i++] = pp.pit;
        }

        for (int j = 0; j < pituudet.length; j++) {
            assertTrue("Hypyn pituuden piti olla välillä 60-120\n"
                    + "Ohjelmasi tulosti:\n", pituudet[j] > 59 && pituudet[j] < 121);
        }

        boolean sama = true;

        for (int j = 0; j < pituudet.length; j++) {
            if (pituudet[0] != pituudet[j]) {
                sama = false;
            }
        }

        assertFalse("Hyppyjen pituudet pitää arpoa!\n"
                + "Ohjelmasi tulosti\n"
                + tulos, sama);
    }

    class PitPist {

        String nimi;
        int pit;
        int[] pist;

        public PitPist(String nimi, int pit, int[] pist) {
            this.nimi = nimi;
            this.pit = pit;
            this.pist = pist;
        }

        @Override
        public String toString() {
            return nimi + " pit: " + pit + " pist: " + Arrays.toString(pist);

        }
    }

    private ArrayList<MakihyppyTest.PitPist> pituusJaPisteet(String kierros, int k) {
        ArrayList<MakihyppyTest.PitPist> pp = new ArrayList<MakihyppyTest.PitPist>();
        String[] rivit = kierros.split("\n");

        int r = 0;

        while (rivit[r].isEmpty()) {
            r++;
        }

        while (true) {

            String nimi = "";
            try {
                nimi = rivit[r].trim();
                r++;
                while (rivit[r].isEmpty()) {
                    r++;
                }

            } catch (Exception n) {
                fail("ongelmia tulosten ilmoittamisessa kierroksella " + k
                        + " tulostuu\n" + kierros);
            }

            int pit = -1;

            String pitMj = "";
            try {
                pitMj = (rivit[r].substring(rivit[r].indexOf("pituus:") + "pituus:".length())).trim();
                pit = Integer.parseInt(pitMj);
            } catch (Exception n) {
                fail("hyppääjän " + nimi + " pituuden ilmoittamisessa ongelmia kierroksella " + k
                        + " tulostuu\n" + kierros);
            }

            try {
                r++;
                while (rivit[r].isEmpty()) {
                    r++;
                }
            } catch (Exception n) {
                fail("ongelmia tulosten ilmoittamisessa kierroksella " + k
                        + " tulostuu\n" + kierros);
            }

            String pisteet = "";
            try {
                pisteet = rivit[r].replaceAll("\\D", " ").trim();
            } catch (Exception e) {
                fail("hyppääjän " + nimi + " tyylipisteiden ilmoittamisessa ongelmia kierroksella " + k
                        + " tulostuu\n" + kierros);
            }

            assertTrue("hyppääjän " + nimi + " tyylipisteiden ilmoittamisessa ongelmia kierroksella " + k
                    + " tulostuu\n" + kierros, viisiLukua(pisteet));
            Scanner luvut = new Scanner(pisteet);
            int[] pisTau = {0, 0, 0, 0, 0};
            try {
                pisTau[0] = luvut.nextInt();
                pisTau[1] = luvut.nextInt();
                pisTau[2] = luvut.nextInt();
                pisTau[3] = luvut.nextInt();
                pisTau[4] = luvut.nextInt();
            } catch (Exception e) {
                fail("hyppääjän " + nimi + " tyylipisteiden ilmoittamisessa ongelmia kierroksella " + k
                        + " tulostuu\n" + kierros);
            }

            pp.add(new MakihyppyTest.PitPist(nimi, pit, pisTau));

            r++;

            try {
                while (rivit[r].isEmpty()) {
                    r++;
                }
            } catch (Exception e) {
                break;
            }

            if (r > rivit.length - 2) {
                break;
            }

        }

        return pp;
    }
}
