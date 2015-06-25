package game;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import gameoflife.GameOfLifeAlusta;
import java.lang.reflect.Constructor;
import static org.junit.Assert.*;
import org.junit.Test;

@Points("150.4")
public class Osa4Test {

    @Test
    public void hoidaSoluEiKatsoMuitaSoluja() {
        GameOfLifeAlusta oa = luoAlusta(1, 1);

        String k = ""
                + "OmaAlusta oa = new OmaAlusta(1,1);\n"
                + "oa.hoidaSolu(0,0);\n";

        try {
            oa.hoidaSolu(0, 0, 0);
        } catch (Exception e) {
            fail("Seuraavan koodin suorittaminen johti poikkeukseen \n" + k
                    + "Lisätietoja" + e);
        }
    }

    @Test
    public void kuoleeJosVainYksiElossaOlevaNaapuri() {
        GameOfLifeAlusta oa = luoAlusta(2, 2);
        oa.muutaElavaksi(0, 0);
        oa.muutaElavaksi(0, 1);
        oa.hoidaSolu(0, 0, 1);

        String k = ""
                + "OmaAlusta oa = new OmaAlusta(2,2);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.muutaElavaksi(0,1);\n"
                + "oa.hoidaSolu(0,0,1);\n"
                + "oa.onElossa(0,0)\n";

        assertEquals("Jos yksi elossa oleva naapuri, elossa oleva solu kuolee. "
                + "Suoritettiin koodi\n"
                + k, false, oa.onElossa(0, 0));
    }

    @Test
    public void kuoleeJosEiNaapureitaElossa() {
        GameOfLifeAlusta oa = luoAlusta(2, 2);
        oa.muutaElavaksi(0, 0);
        oa.hoidaSolu(0, 0, 0);
        String k = ""
                + "OmaAlusta oa = new OmaAlusta(2,2);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.hoidaSolu(0,0);\n"
                + "oa.onElossa(0,0)\n";

        assertEquals("Jos ei yhtään elossa olevaa naapuria, elossa oleva solu kuolee."
                + "Suoritettiin koodi\n"
                + k, false, oa.onElossa(0, 0));
    }

    @Test
    public void kuoleeJosYliKolmeNaapuriaElossa() {
        GameOfLifeAlusta oa = luoAlusta(3, 3);
        oa.muutaElavaksi(1, 1);
        oa.muutaElavaksi(0, 0);
        oa.muutaElavaksi(1, 0);
        oa.muutaElavaksi(2, 0);
        oa.muutaElavaksi(0, 1);
        oa.hoidaSolu(1, 1, 4);
        String k = ""
                + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                + "oa.muutaElavaksi(1,1);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.muutaElavaksi(1,0);\n"
                + "oa.muutaElavaksi(2,0);\n"
                + "oa.muutaElavaksi(0,1);\n"
                + "oa.hoidaSolu(1,1);\n"
                + "oa.onElossa(1,1)\n";

        assertEquals("Jos 4 elossa olevaa naapuria, elossa oleva solu kuolee.\n" + k,
                false, oa.onElossa(1, 1));

        oa.muutaElavaksi(0, 2);
        oa.muutaElavaksi(1, 1);
        oa.hoidaSolu(1, 1, 5);

        k = ""
                + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                + "oa.muutaElavaksi(1,1);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.muutaElavaksi(1,0);\n"
                + "oa.muutaElavaksi(2,0);\n"
                + "oa.muutaElavaksi(0,1);\n"
                + "oa.muutaElavaksi(0,2);\n"
                + "oa.hoidaSolu(1,1);\n"
                + "oa.onElossa(1,1)\n";

        assertEquals("Jos 5 elossa olevaa naapuria, elossa oleva solu kuolee.\n" + k, false, oa.onElossa(1, 1));


        oa.muutaElavaksi(1, 2);
        oa.muutaElavaksi(1, 1);
        oa.hoidaSolu(1, 1, 6);

        k = ""
                + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                + "oa.muutaElavaksi(1,1);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.muutaElavaksi(1,0);\n"
                + "oa.muutaElavaksi(2,0);\n"
                + "oa.muutaElavaksi(0,1);\n"
                + "oa.muutaElavaksi(0,2);\n"
                + "oa.muutaElavaksi(1,2);\n"
                + "oa.hoidaSolu(1,1);\n"
                + "oa.onElossa(1,1)\n";

        assertEquals("Jos 6 elossa olevaa naapuria, elossa oleva solu kuolee.\n" + k, false, oa.onElossa(1, 1));

        oa.muutaElavaksi(2, 2);
        oa.muutaElavaksi(1, 1);
        oa.hoidaSolu(1, 1, 7);

        k = ""
                + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                + "oa.muutaElavaksi(1,1);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.muutaElavaksi(1,0);\n"
                + "oa.muutaElavaksi(2,0);\n"
                + "oa.muutaElavaksi(0,1);\n"
                + "oa.muutaElavaksi(0,2);\n"
                + "oa.muutaElavaksi(1,2);\n"
                + "oa.muutaElavaksi(2,2);\n"
                + "oa.hoidaSolu(1,1);\n"
                + "oa.onElossa(1,1)\n";

        assertEquals("Jos 7 elossa olevaa naapuria, elossa oleva solu kuolee.\n" + k, false, oa.onElossa(1, 1));

    }

    /*
     * 
     */
    @Test
    public void pysyyElossaJosKaksiTaiKolmeElavaaNaapuria() {
        GameOfLifeAlusta oa = luoAlusta(3, 3);
        oa.muutaElavaksi(1, 1);
        oa.muutaElavaksi(0, 0);
        oa.muutaElavaksi(1, 0);
        oa.hoidaSolu(1, 1, 2);
        String k = ""
                + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                + "oa.muutaElavaksi(1,1);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.muutaElavaksi(1,0);\n"
                + "oa.hoidaSolu(1,1);\n"
                + "oa.onElossa(1,1)\n";

        assertEquals("Jos 2 elossa olevaa naapuria, elossa oleva solu pysyy elossa.\n" + k,
                true, oa.onElossa(1, 1));

        oa.muutaElavaksi(2, 0);
        oa.hoidaSolu(1, 1, 3);

        k = ""
                + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                + "oa.muutaElavaksi(1,1);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.muutaElavaksi(1,0);\n"
                + "oa.muutaElavaksi(2,0);\n"
                + "oa.hoidaSolu(1,1);\n"
                + "oa.onElossa(1,1)\n";

        assertEquals("Jos 3 elossa olevaa naapuria, elossa oleva solu pysyy elossa.\n" + k,
                true, oa.onElossa(1, 1));
    }

    @Test
    public void kuollutMuuttuuElavaksiJosKolmeElavaaNaapuria() {
        GameOfLifeAlusta oa = luoAlusta(3, 3);
        oa.muutaElavaksi(0, 0);
        oa.muutaElavaksi(1, 0);
        oa.muutaElavaksi(2, 0);
        oa.hoidaSolu(1, 1, 3);
        String k = ""
                + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.muutaElavaksi(1,0);\n"
                + "oa.muutaElavaksi(2,0);\n"
                + "oa.hoidaSolu(1,1);\n"
                + "oa.onElossa(1,1)\n";

        assertEquals("Jos 3 elossa olevaa naapuria, kuollut solu muuttuu eläväksi.\n" + k,
                true, oa.onElossa(1, 1));

    }

    @Test
    public void kuollutPysyyKuolleenaJosEiKolmeaElavaaNaapuria() {
        GameOfLifeAlusta oa = luoAlusta(3, 3);
        oa.hoidaSolu(1, 1, 0);
        String k = ""
                + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                + "oa.hoidaSolu(1,1);\n"
                + "oa.onElossa(1,1)\n";

        assertEquals("Jos 0 elossa olevaa naapuria, kuollut solu pysyy kuolleena.\n" + k,
                false, oa.onElossa(1, 1));

        oa.muutaElavaksi(0, 0);
        oa.hoidaSolu(1, 1, 1);
        k = ""
                + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.hoidaSolu(1,1);\n"
                + "oa.onElossa(1,1)\n";

        assertEquals("Jos 1 elossa oleva naapuri, kuollut solu pysyy kuolleena.\n" + k,
                false, oa.onElossa(1, 1));

        oa.muutaElavaksi(0, 0);
        oa.muutaElavaksi(1, 0);
        oa.hoidaSolu(1, 1, 2);
        k = ""
                + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.muutaElavaksi(1,0);\n"
                + "oa.hoidaSolu(1,1);\n"
                + "oa.onElossa(1,1)\n";

        assertEquals("Jos 2 elossa olevaa naapuria, kuollut solu pysyy kuolleena.\n" + k,
                false, oa.onElossa(1, 1));

        oa.muutaElavaksi(0, 0);
        oa.muutaElavaksi(1, 0);
        oa.muutaElavaksi(2, 0);
        oa.muutaElavaksi(0, 1);
        oa.hoidaSolu(1, 1, 4);
        k = ""
                + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.muutaElavaksi(1,0);\n"
                + "oa.muutaElavaksi(2,0);\n"
                + "oa.muutaElavaksi(0,1);\n"
                + "oa.hoidaSolu(1,1);\n"
                + "oa.onElossa(1,1)\n";

        assertEquals("Jos 4 elossa olevaa naapuria, kuollut solu pysyy kuolleena.\n" + k,
                false, oa.onElossa(1, 1));

        oa.muutaElavaksi(0, 0);
        oa.muutaElavaksi(1, 0);
        oa.muutaElavaksi(2, 0);
        oa.muutaElavaksi(0, 1);
        oa.muutaElavaksi(0, 2);
        oa.hoidaSolu(1, 1, 5);
        k = ""
                + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.muutaElavaksi(1,0);\n"
                + "oa.muutaElavaksi(2,0);\n"
                + "oa.muutaElavaksi(0,1);\n"
                + "oa.muutaElavaksi(0,2);\n"
                + "oa.hoidaSolu(1,1);\n"
                + "oa.onElossa(1,1)\n";

        assertEquals("Jos 5 elossa olevaa naapuria, kuollut solu pysyy kuolleena.\n" + k,
                false, oa.onElossa(1, 1));

        oa.muutaElavaksi(0, 0);
        oa.muutaElavaksi(1, 0);
        oa.muutaElavaksi(2, 0);
        oa.muutaElavaksi(0, 1);
        oa.muutaElavaksi(0, 2);
        oa.muutaElavaksi(1, 2);
        oa.hoidaSolu(1, 1, 6);
        k = ""
                + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.muutaElavaksi(1,0);\n"
                + "oa.muutaElavaksi(2,0);\n"
                + "oa.muutaElavaksi(0,1);\n"
                + "oa.muutaElavaksi(0,2);\n"
                + "oa.muutaElavaksi(1,2);\n"
                + "oa.hoidaSolu(1,1);\n"
                + "oa.onElossa(1,1)\n";

        assertEquals("Jos 6 elossa olevaa naapuria, kuollut solu pysyy kuolleena.\n" + k,
                false, oa.onElossa(1, 1));


        oa.muutaElavaksi(0, 0);
        oa.muutaElavaksi(1, 0);
        oa.muutaElavaksi(2, 0);
        oa.muutaElavaksi(0, 1);
        oa.muutaElavaksi(0, 2);
        oa.muutaElavaksi(1, 2);
        oa.muutaElavaksi(2, 2);
        oa.hoidaSolu(1, 1, 7);
        k = ""
                + "OmaAlusta oa = new OmaAlusta(3,3);\n"
                + "oa.muutaElavaksi(0,0);\n"
                + "oa.muutaElavaksi(1,0);\n"
                + "oa.muutaElavaksi(2,0);\n"
                + "oa.muutaElavaksi(0,1);\n"
                + "oa.muutaElavaksi(0,2);\n"
                + "oa.muutaElavaksi(1,2);\n"
                + "oa.muutaElavaksi(2,2);\n"
                + "oa.hoidaSolu(1,1);\n"
                + "oa.onElossa(1,1)\n";

        assertEquals("Jos 7 elossa olevaa naapuria, kuollut solu pysyy kuolleena.\n" + k,
                false, oa.onElossa(1, 1));
    }

    private GameOfLifeAlusta luoAlusta(int leveys, int korkeus) {
        Class alusta = ReflectionUtils.findClass("game.OmaAlusta");
        Constructor c = ReflectionUtils.requireConstructor(alusta, int.class, int.class);
        try {
            return (GameOfLifeAlusta) ReflectionUtils.invokeConstructor(c, leveys, korkeus);
        } catch (Throwable ex) {
            fail("Onhan pakkauksessa game olevalla luokalla OmaAlusta konstruktori public OmaAlusta(int leveys, int korkeus), ja onhan luokalla määre public? Perithän myös luokan GameOfLifeAlusta?");
        }

        return null;
    }
}
