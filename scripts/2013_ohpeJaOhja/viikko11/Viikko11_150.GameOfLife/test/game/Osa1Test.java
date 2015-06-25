package game;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import gameoflife.GameOfLifeAlusta;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import static org.junit.Assert.*;
import org.junit.Test;

@Points("150.1")
public class Osa1Test {

    @Test
    public void luokkaOmaAlustaOlemassa() {
        ReflectionUtils.findClass("game.OmaAlusta");
    }

    @Test
    public void alustusLuoKaikkiKuolleena() throws IllegalArgumentException, IllegalAccessException {
        GameOfLifeAlusta alusta = luoAlusta(5, 5);

        eiLokaaliaTaulukkoa(alusta);

        for (boolean[] rivi : alusta.getAlusta()) {
            for (boolean solu : rivi) {
                if (solu) {
                    fail("Älä muuta luokan OmaAlusta konstruktoria. Alkoioiden pitäisi olla aluksi kuolleita.");
                }
            }
        }
    }

    @Test
    public void muutaElavaksiToimii() {
        GameOfLifeAlusta alusta = luoAlusta(5, 5);
        boolean[][] taul = alusta.getAlusta();

        assertFalse("Tarkista että luokan OmaAlusta konstruktori ei alusta alkioita eläväksi.", taul[3][3]);
        try {
            alusta.muutaElavaksi(3, 3);
        } catch (Exception e) {
            fail("Virhe suoritettaessa koodi\n"
                    + "OmaAlusta oa = new OmaAlusta(5,5);\n"
                    + "oa.muutaElavaksi(3,3)\n"
                    + "lisätietoja " + e);
        }
        assertEquals("Tarkista että metodi muutaElavaksi toimii, eli asettaa alkion arvon trueksi.\n"
                + "Suoritettaessa seuraava koodi, ei näin tapahtunut:\n"
                + "OmaAlusta oa = new OmaAlusta(5,5);\n"
                + "oa.muutaElavaksi(3,3)\n"
                + "oa.onElossa(3,3)\n"
                + "", true, taul[3][3]);
    }

    @Test
    public void muutaElavaksiEnsinXSittenY() {
        GameOfLifeAlusta alusta = luoAlusta(5, 5);
        boolean[][] taul = alusta.getAlusta();

        assertFalse("Tarkista että luokan OmaAlusta konstruktori ei alusta alkioita eläväksi.", taul[3][1]);
        alusta.muutaElavaksi(3, 1);
        assertTrue("Tarkista että alustan taulukkoa käytetään aina muodossa [x][y].", taul[3][1]);
    }

    @Test
    public void muutaElavaksiEiTeeMitaanJosOsutaanAlueenUlkopuolelle1() {
        GameOfLifeAlusta alusta = luoAlusta(5, 5);
        boolean[][] taul = alusta.getAlusta();

        assertFalse("Tarkista että luokan OmaAlusta konstruktori ei alusta alkioita eläväksi.", taul[3][3]);
        try {
            alusta.muutaElavaksi(-1, 3);
        } catch (Exception e) {
            fail("Virhe suoritettaessa koodi\n"
                    + "OmaAlusta oa = new OmaAlusta(5,5);\n"
                    + "oa.muutaElavaksi(-1,3)\n"
                    + "lisätietoja " + e);
        }
        assertTrue("Tarkista että metodi muutaElavaksi ei tee mitään jos kordinaatit eivät ole alueen sisällä\n"
                + "Suoritettaessa seuraava koodi, ei näin tapahtunut:\n"
                + "OmaAlusta oa = new OmaAlusta(5,5);\n"
                + "oa.muutaElavaksi(-1,3)\n"
                + "", Osa2Test.prosenttiaElossa(alusta.getAlusta())<0.01);
    }    
    
    @Test
    public void muutaElavaksiEiTeeMitaanJosOsutaanAlueenUlkopuolelle2() {
        GameOfLifeAlusta alusta = luoAlusta(5, 5);
        boolean[][] taul = alusta.getAlusta();

        assertFalse("Tarkista että luokan OmaAlusta konstruktori ei alusta alkioita eläväksi.", taul[3][3]);
        try {
            alusta.muutaElavaksi(1, 7);
        } catch (Exception e) {
            fail("Virhe suoritettaessa koodi\n"
                    + "OmaAlusta oa = new OmaAlusta(5,5);\n"
                    + "oa.muutaElavaksi(1,7)\n"
                    + "lisätietoja " + e);
        }
        assertTrue("Tarkista että metodi muutaElavaksi ei tee mitään jos kordinaatit eivät ole alueen sisällä\n"
                + "Suoritettaessa seuraava koodi, ei näin tapahtunut:\n"
                + "OmaAlusta oa = new OmaAlusta(5,5);\n"
                + "oa.muutaElavaksi(1,7)\n"
                + "", Osa2Test.prosenttiaElossa(alusta.getAlusta())<0.01);
    }        
    
    @Test
    public void muutaKuolleeksiToimii() {
        GameOfLifeAlusta alusta = luoAlusta(5, 5);
        boolean[][] taul = alusta.getAlusta();
        taul[3][3] = true;

        try {
            alusta.muutaKuolleeksi(3, 3);
        } catch (Exception e) {
            fail("Virhe suoritettaessa koodi\n"
                    + "OmaAlusta oa = new OmaAlusta(5,5);\n"
                    + "oa.muutaKuolleeksi(3,3)\n"
                    + "lisätietoja " + e);
        }

        assertFalse("Tarkista että metodi muutaKuolleeksi asettaa alkion kuolleeksi, eli arvon falseksi."
                + "\nSuoritettaessa seuraava koodi, ei näin tapahtunut:\n"
                + "OmaAlusta oa = new OmaAlusta(5,5);\n"
                + "oa.muutaElavaksi(3,3)\n"
                + "oa.muutaKuolleeksi(3,3)\n", taul[3][3]);
    }

    @Test
    public void muutaKuolleeksiToimiiXEnsinSittenY() {
        GameOfLifeAlusta alusta = luoAlusta(5, 5);
        boolean[][] taul = alusta.getAlusta();
        taul[3][1] = true;

        alusta.muutaKuolleeksi(3, 1);
        assertFalse("muutaKuolleeksi: Tarkista että alustan taulukkoa käytetään aina muodossa [x][y].", taul[3][1]);
    }

    @Test
    public void muutaKuolleeksiEiTeeMitaanJosOsutaanAlueenUlkopuolelle1() {
        GameOfLifeAlusta alusta = luoAlusta(5, 5);
        boolean[][] taul = alusta.getAlusta();

        assertFalse("Tarkista että luokan OmaAlusta konstruktori ei alusta alkioita eläväksi.", taul[3][3]);
        try {
            alusta.muutaKuolleeksi(-1, 3);
        } catch (Exception e) {
            fail("Virhe suoritettaessa koodi\n"
                    + "OmaAlusta oa = new OmaAlusta(5,5);\n"
                    + "oa.muutaKuolleeksi(-1,3)\n"
                    + "lisätietoja " + e);
        }
        assertTrue("Tarkista että metodi muutaKuolleeksi ei tee mitään jos kordinaatit eivät ole alueen sisällä\n"
                + "Suoritettaessa seuraava koodi, ei näin tapahtunut:\n"
                + "OmaAlusta oa = new OmaAlusta(5,5);\n"
                + "oa.muutaKuolleeksi(-1,3)\n"
                + "", Osa2Test.prosenttiaElossa(alusta.getAlusta())<0.01);
    }    

    @Test
    public void muutaKuolleeksiEiTeeMitaanJosOsutaanAlueenUlkopuolelle2() {
        GameOfLifeAlusta alusta = luoAlusta(5, 5);
        boolean[][] taul = alusta.getAlusta();

        assertFalse("Tarkista että luokan OmaAlusta konstruktori ei alusta alkioita eläväksi.", taul[3][3]);
        try {
            alusta.muutaKuolleeksi(1, 7);
        } catch (Exception e) {
            fail("Virhe suoritettaessa koodi\n"
                    + "OmaAlusta oa = new OmaAlusta(5,5);\n"
                    + "oa.muutaKuolleeksi(1,7)\n"
                    + "lisätietoja " + e);
        }
        assertTrue("Tarkista että metodi muutaKuolleeksi ei tee mitään jos kordinaatit eivät ole alueen sisällä\n"
                + "Suoritettaessa seuraava koodi, ei näin tapahtunut:\n"
                + "OmaAlusta oa = new OmaAlusta(5,5);\n"
                + "oa.muutaKuolleeksi(1,7)\n"
                + "", Osa2Test.prosenttiaElossa(alusta.getAlusta())<0.01);
    }        
    
    @Test
    public void onElossaToimii() {
        GameOfLifeAlusta alusta = luoAlusta(3, 3);
        boolean[][] taul = alusta.getAlusta();

        taul[0][1] = true;
        taul[2][2] = true;
        taul[1][0] = true;

        for (int x = 0; x < taul.length; x++) {
            for (int y = 0; y < taul[x].length; y++) {
                boolean vast = false;
                try {
                    vast = alusta.onElossa(x, y);
                } catch (Exception e) {
                    fail("Virhe suoritettaessa koodi\n"
                            + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                            + "oa.onElossa(" + x + "," + y + ")\n"
                            + "lisätietoja " + e);
                }

                assertEquals("Tarkista että metodi onElossa palauttaa true jos haluttu alkio on elossa, muuten false.\n"
                        + "Virhe koodilla\n"
                        + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                        + "oa.muutaElavaksi(0,1)\n"
                        + "oa.muutaElavaksi(2,2)\n"
                        + "oa.muutaElavaksi(1,0)\n"
                        + "oa.onElossa(" + x + "," + y + ")\n"
                        + "", taul[x][y], vast);
            }
        }
    }

    @Test
    public void onKuollutUlkopuolellaToimii() {
        GameOfLifeAlusta alusta = luoAlusta(3, 3);
        String v =
                "";
        try {

            String a = "alusta new OmaAlusta(3,3);\n";
            v = a + "alusta.onElossa(-1,1);\n";
            assertEquals("Tarkista että metodi onElossa palauttaa false taulun ulkopuolella olevalle pisteelle:" + v,
                    false, alusta.onElossa(-1, 1));
            v = a + "alusta.onElossa(1,-1);\n";
            assertEquals("Tarkista että metodi onElossa palauttaa false taulun ulkopuolella olevalle pisteelle:" + v,
                    false, alusta.onElossa(1, -1));
            v = a + "alusta.onElossa(3,3);\n";
            assertEquals("Tarkista että metodi onElossa palauttaa false taulun ulkopuolella olevalle pisteelle: "
                    + v,
                    false, alusta.onElossa(3, 1));
            v = a + "alusta.onElossa(-1,3);";
            assertEquals("Tarkista että metodi onElossa palauttaa false taulun ulkopuolella olevalle pisteelle:" + v,
                    false, alusta.onElossa(1, 3));
            v = a + "alusta.onElossa(3,-1);";
            assertEquals("Tarkista että metodi onElossa palauttaa false taulun ulkopuolella olevalle pisteelle:" + v,
                    false, alusta.onElossa(3, -1));
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Tarkista että metodi onElossa palauttaa false taulun ulkopuolella olevalle pisteelle. \n"
                    + v + "\naiheutti virheen " + e);
        }
    }

    @Test
    public void testataanXJaYOikeinPain() {
        GameOfLifeAlusta alusta = luoAlusta(10, 2);
        try {
            boolean[][] taul = alusta.getAlusta();
            taul[5][1] = true;
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Varmista että käytät koordinaatteja oikein päin. Jos luodaan alusta, jonka leveys on 10 ja korkeus 2, pitäisi alustan taulukossa olla indeksi [5][1]. Indeksit menevät siis [x][y].");
        }

        alusta = luoAlusta(2, 10);
        try {
            boolean[][] taul = alusta.getAlusta();
            taul[1][5] = true;
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Varmista että käytät koordinaatteja oikein päin. Jos luodaan alusta, jonka leveys on 2 ja korkeus 10, pitäisi alustan taulukossa olla indeksissä [1][5]. Indeksit menevät siis [x][y].");
        }
    }

    private GameOfLifeAlusta luoAlusta(int leveys, int korkeus) {
        Class alusta = ReflectionUtils.findClass("game.OmaAlusta");

        Constructor c = null;

        try {
            c = ReflectionUtils.requireConstructor(alusta, int.class, int.class);

        } catch (Throwable ex) {
            fail("Onhan pakkauksessa game olevalla luokalla OmaAlusta konstruktori public OmaAlusta(int leveys, int korkeus) , ja onhan luokalla määre public?");
        }

        Object a = null;
        try {
            a = ReflectionUtils.invokeConstructor(c, leveys, korkeus);
        } catch (Throwable t) {
            fail("Virhe koodilla OmaAlusta a = new OmaAlusta(" + leveys + "," + korkeus + ");" + t);
        }
        try {
            return (GameOfLifeAlusta) a;
        } catch (Throwable ex) {
            fail("Perithän luokka OmaAlusta luokan GameOfLifeAlusta?");
        }

        return null;
    }

    private void eiLokaaliaTaulukkoa(GameOfLifeAlusta alusta) throws IllegalArgumentException, IllegalAccessException {
        Field[] kentat = ReflectionUtils.findClass("game.OmaAlusta").getDeclaredFields();
        for (Field f : kentat) {

            if (f.toString().contains("boolean[][]")) {
                f.setAccessible(true);
                String m = "Luokan OmaAlusta ei tule luoda omaa pelialusta\n"
                        + "alusta peritään yliluokalta ja siihen päästään käsiksi metodilla getAlusta\n"
                        + "poista oliomuuttuja " + f.getName();
                assertTrue(m, f.get(alusta) == null && f.get(alusta) != alusta.getAlusta());
            }

        }

    }
}
