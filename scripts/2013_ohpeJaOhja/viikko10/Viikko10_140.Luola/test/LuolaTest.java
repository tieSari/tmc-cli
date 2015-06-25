
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import fi.helsinki.cs.tmc.edutestutils.Reflex.ClassRef;
import fi.helsinki.cs.tmc.edutestutils.Reflex.MethodRef0;
import fi.helsinki.cs.tmc.edutestutils.Reflex.MethodRef5;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

@Points("140.1 140.2 140.3 140.4")
public class LuolaTest {

    ClassRef luola;
    MethodRef5<Void, Object, Integer, Integer, Integer, Integer, Boolean> cons;
    MethodRef0<Object, Void> run;

    @Before
    public void hae() {
        luola = Reflex.reflect("luola.Luola");
        cons = luola.constructor().taking(Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Boolean.TYPE);
        assertTrue("Tee luokalle Luola konstruktori public Luola(int leveys, int korkeus, int hirvioita, int siirtoja, boolean hirviotLiikkuvat)", cons.isPublic());
        run = luola.method("run").returningVoid().takingNoParams();
        assertTrue("Tee luokalle Luola metodi public void run()", run.isPublic());
    }

    class P implements Runnable {

        Object luola;
        PipedOutputStream toOut, fromOut;
        PipedInputStream toIn, fromIn;
        PrintStream toOut2;
        Scanner fromIn2;

        public P(int x, int y, int hirv, int siirt, boolean b) throws Throwable {
            toIn = new PipedInputStream();
            toOut = new PipedOutputStream(toIn);
            toOut2 = new PrintStream(toOut);

            fromIn = new PipedInputStream();
            //fromIn2 = new Scanner(fromIn);
            fromOut = new PipedOutputStream(fromIn);

            System.setIn(toIn);
            System.setOut(new PrintStream(fromOut));

            luola = cons.invoke(x, y, hirv, siirt, b);

        }
        public volatile boolean running;

        @Override
        public void run() {
            try {
                run.withNiceError().invokeOn(luola);
            } catch (NoSuchElementException e) {
                // ignore
            } catch (Throwable ex) {
                throw new Error("Jokin meni pieleen:", ex);
            }
        }

        public void write(String s) {
            toOut2.print(s);
            toOut2.flush();
        }

        public String read() throws IOException {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
            String out = "";
            while (fromIn.available() > 0) {
                out += (char) fromIn.read();
            }
            return out;
        }
    }

    @Test
    public void testaaEnsimmainenTulostus() throws Throwable {
        LuolaTest.P p = null;
        String viesti = "Testattiin Luola luola = new Luola(5,5,3,3,false); luola.run();\n";

        try {
            p = new LuolaTest.P(5, 5, 3, 3, false);
        } catch (Throwable t) {
            fail(viesti+"Lisätietoa virheestä "+t);
        }

        Thread t = new Thread(p);
        t.start();

        String s = p.read();
        t.interrupt();
        s = s.replaceAll("\r\n", "\n");
        s = s.replaceAll("\r", "\n");

        String[] rivit = s.split("\n");
        assertTrue(viesti + "Tulostusrivejä pitäisi olla vähintään 3. Tulostuksesi oli:\n" + s,
                rivit.length >= 3);

        assertEquals(viesti + "Et tulostanut ensimmäisellä rivillä siirtojen määrää. Tulostuksesi oli:\n" + s,
                "3",
                rivit[0].trim());
        assertTrue(viesti + "Tulosteen toisen rivin pitäisi olla tyhjä. Tulostuksesi oli: \n" + s,
                "".equals(rivit[1].trim()));
        assertEquals(viesti + "Et tulostanut pelaajan koordinaatteja. Tulostuksesi oli:\n" + s,
                "@ 0 0",
                rivit[2].trim());

        assertTrue(viesti + "Tulostusrivejä pitäisi olla vähintään 5. Tulostuksesi oli:\n" + s,
                rivit.length >= 5);
        for (int i = 1; i <= 3; i++) {
            if (!rivit[2 + i].startsWith("h")) {
                fail(viesti + "Et tulostanut hirviöriviä. Virheellinen rivi on:\n" + rivit[2 + i] + "\nKoko tulostuksesi oli:\n" + s);
            }
        }
        assertTrue(viesti + "Tulostusrivejä pitäisi olla vähintään 11. Tulostuksesi oli:\n" + s,
                rivit.length >=11);
        assertTrue(viesti + "Ennen karttaa pitäisi olla tyhjä rivi. Tulostuksesi oli: \n" + s,
                "".equals(rivit[6].trim()));
        for (int i = 1; i <= 5; i++) {
            if (rivit[6 + i].length() != 5) {
                fail(viesti + "Karttarivillä on väärä pituus. Virheellinen rivi on:\n" + rivit[6 + i] + "\nKoko tulostuksesi oli:\n" + s);
            }
        }
    }

    @Test
    public void testaaHavio() throws Throwable {
        LuolaTest.P p = new LuolaTest.P(5, 5, 5, 5, true);
        p.write("w\nw\nw\nw\nw\n");
        String viesti = "Testattiin Luola luola = new Luola(5,5,5,5,false); luola.run();\n";
        Thread t = new Thread(p);
        t.start();

        String s = p.read();

        assertTrue(viesti + "Pelin pitäisi loppua häviöön kun liikutaan 5 kertaa! Tulosteesi oli:\n" + s,
                s.contains("VISIT"));

    }

    @Test
    public void testaaVoitto() throws Throwable {
        LuolaTest.P p = new LuolaTest.P(4, 4, 1, 100, false);
        p.write("s\ns\ns\nd\nw\nw\nw\nd\ns\ns\ns\nd\nw\nw\nw\n");
        String syote = "s s s d w w w d s s s d w w w";
        String viesti = "Testattiin Luola luola = new Luola(4,4,1,100,false); luola.run();\n";
        Thread t = new Thread(p);
        t.start();

        String s = p.read();

        assertTrue(viesti + "Pelin pitäisi loppua voittoon kun käydään kaikissa ruuduissa! \n"
                + "\nKun syöte oli "+syote
                + "\nTulosteesi oli:\n" + s,
                s.contains("VOITIT"));
    }

    @Test
    public void testaaEttaLamppujaVahennetaanVainKunKatsotaanTilanne() throws Throwable {
        LuolaTest.P p = new LuolaTest.P(4, 4, 2, 100, false);
        p.write("swswswswswsw\ns\ns\ns\nd\nw\nw\nw\nd\ns\ns\ns\nd\nw\nw\nw\n");
        String viesti = "Testattiin Luola luola = new Luola(4,4,1,100,false); luola.run();\n";

        Thread t = new Thread(p);
        t.start();

        String s = p.read();

        assertTrue(viesti + "Lampun välkäytysten määrän tulee pienentyä yhdellä vuoroa kohti. Pelaaja saa kävellä pimeässä niin paljon kuin haluaa ilman että lampun virta vähenee.",
                containsInOrder(s, "99", "98", "97"));
    }

    private boolean containsInOrder(String data, String... args) {
        int lastIndex = -1;
        for (String arg : args) {
            if (!data.contains(arg)) {
                return false;
            }

            if (data.indexOf(arg) <= lastIndex) {
                return false;
            }

            lastIndex = data.indexOf(arg);
        }

        return true;
    }

    @Test
    public void testaaVuoro() throws Throwable {

        LuolaTest.P p = new LuolaTest.P(10, 10, 5, 100, true);

        String viesti = "Testattiin Luola luola = new Luola(10,10,5,100,true); luola.run();\n";
        String viesti2 = "Tulosteesi oli:\n";
        Thread t = new Thread(p);
        t.start();

        String eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        String[] ekat = eka.split("\n");
        assertEquals(viesti + "Alussa vuorojen määrän pitäisi olla 100. " + viesti2 + eka,
                "100",
                ekat[0].trim());

        p.write("s\n");

        String toka = p.read();
        toka = toka.replaceAll("\r\n", "\n");
        toka = toka.replaceAll("\r", "\n");
        String[] tokat = toka.split("\n");
        assertEquals(viesti + "Yhden vuoron jälkeen vuorojen määrän pitäisi olla 99. " + viesti2 + toka,
                "99",
                tokat[0].trim());
        assertEquals(viesti + "Pelaajan pitäisi liikkua alaspäin kun annetaan komento s. " + viesti2 + toka,
                "@ 0 1",
                tokat[2].trim());

        boolean liik = false;
        for (int i = 1; i < 4; i++) {
            if (!ekat[2 + i].equals(tokat[2 + i])) {
                liik = true;
            }
        }
        assertTrue(viesti + "Yksikään hirviö ei liikkunut! " + viesti2 + eka + toka,
                liik);

    }

    @Test
    public void testaaLiikkuminen() throws Throwable {
        LuolaTest.P p = new LuolaTest.P(10, 10, 5, 100, false);
        String viesti = "Testattiin Luola luola = new Luola(10,10,5,100,false); luola.run();\n";
        String viesti2 = "Tulosteesi oli:\n";
        Thread t = new Thread(p);
        t.start();

        String eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        String[] ekat = eka.split("\n");

        p.write("s\n");
        eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        ekat = eka.split("\n");
        assertEquals(viesti + "Pelaajan pitäisi liikkua alaspäin kun annetaan komento s. " + viesti2 + eka,
                "@ 0 1",
                ekat[2].trim());

        p.write("dd\n");
        eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        ekat = eka.split("\n");
        assertEquals(viesti + "Pelaajan pitäisi liikkua kaksi oikealle kun annetaan komento dd. " + viesti2 + eka,
                "@ 2 1",
                ekat[2].trim());
        eka = p.read();

        p.write("w\n");
        eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        ekat = eka.split("\n");
        assertEquals(viesti + "Pelaajan pitäisi liikkua ylöspäin kun annetaan komento w. " + viesti2 + eka,
                "@ 2 0",
                ekat[2].trim());

        p.write("a\n");
        eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        ekat = eka.split("\n");
        assertEquals(viesti + "Pelaajan pitäisi liikkua vasemmalle kun annetaan komento a. " + viesti2 + eka,
                "@ 1 0",
                ekat[2].trim());


        p.write("ssssssddddsdsdsdsdsdsdsdsdssssdddssssdddssdsdsdsdsdsdsdsd\n");
        eka = p.read();
        eka = eka.replaceAll("\r\n", "\n");
        eka = eka.replaceAll("\r", "\n");
        ekat = eka.split("\n");
        assertEquals(viesti + "Pelaajan liikkumisen tulisi pysähtyä luolan seiniin." + viesti2 + eka,
                "@ 9 9",
                ekat[2].trim());

    }
}
