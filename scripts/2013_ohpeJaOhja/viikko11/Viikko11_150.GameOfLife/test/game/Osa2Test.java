package game;

import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import gameoflife.GameOfLifeAlusta;
import java.lang.reflect.Constructor;
import junit.framework.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

@Points("150.2")
public class Osa2Test {

    @Test
    public void alustusTaysillaKaikkiElossa() {
        GameOfLifeAlusta oa = luoAlusta(4, 5);
        try {
            oa.alustaSatunnaisetPisteet(1);
        } catch (Exception e) {
            fail(""
                    + "OmaAlusta alusta = new OmaAlusta(4,5);\n"
                    + "alusta.alustaSatunnaisetPisteet(1.0);\n"
                    + "aiheutti virheen: " + e);
        }
        assertEquals("Kun suoritetaan koodi\n"
                + "OmaAlusta alusta = new OmaAlusta(4,5);\n"
                + "alusta.alustaSatunnaisetPisteet(1.0);\n"
                + "Tarkista että kaikki alkiot ovat elossa. Elossa olevien prosentti:"
                + "", 100, 100 * prosenttiaElossa(oa.getAlusta()), 3);
    }
    
    @Test
    public void alustusNollillaKaikkiKuolleena() {
        GameOfLifeAlusta oa = luoAlusta(4, 5);
        try {
            oa.alustaSatunnaisetPisteet(0);
        } catch (Exception e) {
            fail(""
                    + "OmaAlusta alusta = new OmaAlusta(4,5);\n"
                    + "alusta.alustaSatunnaisetPisteet(0.0);\n"
                    + "aiheutti virheen: " + e);
        }
        assertEquals("Kun suoritetaan koodi\n"
                + "OmaAlusta alusta = new OmaAlusta(4,5);\n"
                + "alusta.alustaSatunnaisetPisteet(0.0);\n"
                + "Tarkista että yhtään alkiota ei elossa. Elossa olevien prosentti:"
                + "", 0, 100 * prosenttiaElossa(oa.getAlusta()), 3);
    }

    @Test
    public void alustusPuoletEiNollaEikaYksi() {
        GameOfLifeAlusta oa = luoAlusta(20, 20);
        try {
            oa.alustaSatunnaisetPisteet(0.5);
        } catch (Exception e) {
            fail(""
                    + "OmaAlusta alusta = new OmaAlusta(20,20);\n"
                    + "alusta.alustaSatunnaisetPisteet(0.5);\n"
                    + "aiheutti virheen: " + e);
        }
        assertEquals("Kun suoritetaan koodi\n"
                + "OmaAlusta alusta = new OmaAlusta(20,20);\n"
                + "alusta.alustaSatunnaisetPisteet(0.5);\n"
                + "Tarkista että yhtään alkiota ei elossa. Elossa olevien prosentti:"
                + "", 50, 100 * prosenttiaElossa(oa.getAlusta()), 5);
    }

    @Test
    public void alustusKahdenKymmenenProsentinSatunnaisuudellaOikein() {
        GameOfLifeAlusta oa = luoAlusta(20, 20);
        try {
            oa.alustaSatunnaisetPisteet(0.2);
        } catch (Exception e) {
            fail(""
                    + "OmaAlusta alusta = new OmaAlusta(20,20);\n"
                    + "alusta.alustaSatunnaisetPisteet(0.2);\n"
                    + "aiheutti virheen: " + e);
        }
        assertEquals("Kun suoritetaan koodi\n"
                + "OmaAlusta alusta = new OmaAlusta(20,20);\n"
                + "alusta.alustaSatunnaisetPisteet(0.2);\n"
                + "Tarkista että yhtään alkiota ei elossa. Elossa olevien prosentti:"
                + "", 20, 100 * prosenttiaElossa(oa.getAlusta()), 5);
    }    

    @Test
    public void alustusKahdeksanKymmenenProsentinSatunnaisuudellaOikein() {

        GameOfLifeAlusta oa = luoAlusta(20, 20);
        try {
            oa.alustaSatunnaisetPisteet(0.8);
        } catch (Exception e) {
            fail(""
                    + "OmaAlusta alusta = new OmaAlusta(20,20);\n"
                    + "alusta.alustaSatunnaisetPisteet(0.8);\n"
                    + "aiheutti virheen: " + e);
        }
        assertEquals("Kun suoritetaan koodi\n"
                + "OmaAlusta alusta = new OmaAlusta(20,20);\n"
                + "alusta.alustaSatunnaisetPisteet(0.8);\n"
                + "Tarkista että yhtään alkiota ei elossa. Elossa olevien prosentti:"
                + "", 80, 100 * prosenttiaElossa(oa.getAlusta()), 5);
    }   

    public static double prosenttiaElossa(boolean[][] matriisi) {
        int koko = matriisi.length * matriisi[0].length;

        int lkm = 0;
        for (boolean[] rivi : matriisi) {
            for (boolean alkio : rivi) {
                if (alkio) {
                    lkm++;
                }
            }
        }

        return 1.0 * lkm / koko;
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
