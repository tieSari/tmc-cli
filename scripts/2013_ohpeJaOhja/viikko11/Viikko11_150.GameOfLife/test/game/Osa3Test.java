package game;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import gameoflife.GameOfLifeAlusta;
import java.lang.reflect.Constructor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

@Points("150.3")
public class Osa3Test {

    @Test
    public void elossaOlevatNaapuritVasenYlaKulma() {
        GameOfLifeAlusta oa = luoAlusta(2, 2);
        oa.muutaElavaksi(1, 0);
        oa.muutaElavaksi(1, 1);
        oa.muutaElavaksi(0, 1);

        String k = ""
                + "OmaAlusta oa = new OmaAlusta(2, 2);\n"
                + "oa.muutaElavaksi(1, 0);\n"
                + "oa.muutaElavaksi(1, 1);\n"
                + "oa.muutaElavaksi(0, 1);\n"
                + "oa.getElossaOlevienNaapurienLukumaara(0, 0);\n";

        try {
            assertEquals("Tarkista että elossa olevat naapurit toimivat kulmatapauksessa.\n"
                    + "Suoritettiin koodi\n" + k, 3, oa.getElossaOlevienNaapurienLukumaara(0, 0));
        } catch (Exception e) {
            fail("Poikkeus koodilla\n" + k + "lisätietoja" + e);
        }
    }



    @Test
    public void elossaOlevatNaapuritReuna() {
        GameOfLifeAlusta oa = luoAlusta(3, 3);

        String k = ""
                + "OmaAlusta oa = new OmaAlusta(3, 3);\n"
                + "oa.muutaElavaksi(0, 0);\n"
                + "oa.muutaElavaksi(1, 0);\n"
                + "oa.muutaElavaksi(1, 1);\n"
                + "oa.muutaElavaksi(1, 2);\n"
                + "oa.muutaElavaksi(0, 2);\n"
                + "oa.getElossaOlevienNaapurienLukumaara(0, 1);\n";

        oa.muutaElavaksi(0, 0);
        oa.muutaElavaksi(1, 0);
        oa.muutaElavaksi(1, 1);
        oa.muutaElavaksi(1, 2);
        oa.muutaElavaksi(0, 2);

        try {
            assertEquals("Tarkista että elossa olevat naapurit lasketaan oikein vasemmassa reunassa."
                    + "Suoritettiin koodi\n" + k, 5, oa.getElossaOlevienNaapurienLukumaara(0, 1));
        } catch (Exception e) {
            fail("Poikkeus koodilla\n" + k + "lisätietoja" + e);
        }
    }

    @Test
    public void elossaOlevatNaapuritOikeaReuna() {
        GameOfLifeAlusta oa = luoAlusta(2, 3);

        oa.muutaElavaksi(1, 0);
        oa.muutaElavaksi(0, 0);
        oa.muutaElavaksi(0, 2);
        oa.muutaElavaksi(1, 2);

        String k = ""
                + "OmaAlusta oa = new OmaAlusta(2, 3);\n"
                + "oa.muutaElavaksi(1, 0);\n"
                + "oa.muutaElavaksi(0, 0);\n"
                + "oa.muutaElavaksi(1, 2);\n"
                + "oa.muutaElavaksi(0, 2);\n"
                + "oa.getElossaOlevienNaapurienLukumaara(1, 1);\n";

        try {
            assertEquals("Tarkista että elossa olevat naapurit lasketaan oikein oikeassa reunassa."
                    + "Suoritettiin koodi\n" + k, 4, oa.getElossaOlevienNaapurienLukumaara(1, 1));
        } catch (Exception e) {
            fail("Poikkeus koodilla\n" + k + "lisätietoja" + e);
        }
    }

    @Test
    public void elossaOlevatNaapuritKeskella() {
        GameOfLifeAlusta oa = luoAlusta(3, 3);

        oa.muutaElavaksi(1, 0);
        oa.muutaElavaksi(0, 0);
        oa.muutaElavaksi(0, 2);
        oa.muutaElavaksi(1, 2);
        oa.muutaElavaksi(2, 2);

        String k = ""
                + "OmaAlusta oa = new OmaAlusta(3, 3);\n"
                + "oa.muutaElavaksi(1, 0);\n"
                + "oa.muutaElavaksi(0, 0);\n"
                + "oa.muutaElavaksi(0, 2);\n"
                + "oa.muutaElavaksi(1, 2);\n"
                + "oa.muutaElavaksi(2, 2);\n"
                + "oa.getElossaOlevienNaapurienLukumaara(1, 1);\n";

        try {
            assertEquals("Tarkista että elossa olevat naapurit lasketaan oikein jos piste ei reunalla."
                    + "Suoritettiin koodi\n" + k, 5, oa.getElossaOlevienNaapurienLukumaara(1, 1));
        } catch (Exception e) {
            fail("Poikkeus koodilla\n" + k + "lisätietoja" + e);
        }
    }

    @Test
    public void elossaOlevatNaapuritOikeaAlaKulma() {
        GameOfLifeAlusta oa = luoAlusta(5, 5);
        oa.muutaElavaksi(0, 3);
        oa.muutaElavaksi(0, 2);
        oa.muutaElavaksi(0, 1);
        oa.muutaElavaksi(1, 0);
        oa.muutaElavaksi(2, 0);
        oa.muutaElavaksi(3, 0);
        oa.muutaElavaksi(3, 3);
        oa.muutaElavaksi(3, 4);
        oa.muutaElavaksi(4, 3);

        String k = ""
                + "OmaAlusta oa = new OmaAlusta(5, 5);\n"
                + "oa.muutaElavaksi(1, 0);\n"
                + "oa.muutaElavaksi(2, 0);\n"
                + "oa.muutaElavaksi(3, 0);\n"
                + "oa.muutaElavaksi(0, 1);\n"
                + "oa.muutaElavaksi(0, 2);\n"
                + "oa.muutaElavaksi(0, 3);\n"
                + "oa.muutaElavaksi(3, 3);\n"
                + "oa.muutaElavaksi(3, 4);\n"
                + "oa.muutaElavaksi(4, 3);\n"
                + "oa.getElossaOlevienNaapurienLukumaara(4, 4);\n";

        try {
            assertEquals("Tarkista että elossa oleviin ei lasketa mitä sattuu.\n"
                    + "Suoritettiin koodi\n" + k, 3, oa.getElossaOlevienNaapurienLukumaara(4, 4));
        } catch (Exception e) {
            fail("Poikkeus koodilla\n" + k + "lisätietoja" + e);
        }
    }    
    
    @Test
    public void eiLaskeItseaanElossaOlevaksi() {
        GameOfLifeAlusta oa = luoAlusta(3, 3);

        oa.muutaElavaksi(1, 0);
        oa.muutaElavaksi(0, 0);
        oa.muutaElavaksi(0, 1);
        oa.muutaElavaksi(2, 0);
        oa.muutaElavaksi(0, 2);
        oa.muutaElavaksi(1, 2);
        oa.muutaElavaksi(2, 2);
        oa.muutaElavaksi(1, 1);

        String k = ""
                + "OmaAlusta oa = new OmaAlusta(3, 3);\n"
                + "oa.muutaElavaksi(1, 0);\n"
                + "oa.muutaElavaksi(0, 0);\n"
                + "oa.muutaElavaksi(0, 1);\n"
                + "oa.muutaElavaksi(2, 0);\n"
                + "oa.muutaElavaksi(0, 2);\n"
                + "oa.muutaElavaksi(1, 2);\n"
                + "oa.muutaElavaksi(2, 2);\n"
                + "oa.muutaElavaksi(1, 1);\n"
                + "oa.getElossaOlevienNaapurienLukumaara(1, 1);\n";

        try {
            assertEquals("Tarkista että elossa olevat naapurit lasketaan oikein "
                    + " jos itse elossa.\n"
                    + "Suoritettiin koodi\n" + k, 7, oa.getElossaOlevienNaapurienLukumaara(1, 1));
        } catch (Exception e) {
            fail("Poikkeus koodilla\n" + k + "lisätietoja" + e);
        }
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
