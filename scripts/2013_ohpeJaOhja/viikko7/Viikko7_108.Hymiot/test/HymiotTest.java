
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Method;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

@Points("108")
public class HymiotTest {

    String klassName = "Hymiot";
    Reflex.ClassRef<Object> klass;

    @Before
    public void justForKicks() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void onLuokka() {
        klass = Reflex.reflect(klassName);
        assertTrue("Luokan " + klassName + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void onMetodiTulostaHymioityna() throws Throwable {
        String metodi = "tulostaHymioityna";

        assertTrue("tee luokkaan Hymiot metodi private static void tulostaHymioityna(String merkkijono)",
                klass.staticMethod(metodi).returningVoid().taking(String.class).isPrivate());

        kutsu("Mikke");
    }

    @Test
    public void correctOutputWithMikael() throws Throwable {
        MockInOut io = new MockInOut("");

        kutsu("Mikael");

        String output = io.getOutput();
        String[] rivit = output.split("\n");
        if (rivit.length != 3) {
            fail("Tulosteessasi on " + rivit.length + " rivinvaihtoa, kun odotettiin kolmea. "
                    + "Varmista että ohjelmasi tulostaa vain kolme riviä ja yhden rivinvaihdon jokaisen loppuun.");
        }

        if (rivit[0].contains(":):):):):):):)")) {
            fail("Tulostat liikaa hymiöitä ensimmäiselle riville, testaa metodisi toimintaa merkkijonolla \"Mikael\"");
        }

        if (rivit[2].contains(":):):):):):):)")) {
            fail("Tulostat liikaa hymiöitä viimeiselle riville, testaa metodisi toimintaa merkkijonolla \"Mikael\"");
        }

        if (!output.contains(":) Mikael")) {
            fail("Varmista että annetun merkkijonon eteen tulostetaan hymiö ja välilyönti.");
        }
        if (!output.contains(" Mikael ")) {
            fail("Varmista että annetun merkkijonon kummallekin puolelle tulostetaan yksi välilyönti.");
        }

        if (!output.contains(" Mikael :)\n")) {
            fail("Varmista että annetun merkkijonon oikealla puolella olevan välilyönnin jälkeen tulee hymiö ja rivinvaihto.");
        }
        if (!rivit[0].equals(":):):):):):)")) {
            fail("Ensimmäinen rivisi on väärä syötteellä \"Mikael\" - pitäisi olla \":):):):):):)\"");
        }
        if (!rivit[1].equals(":) Mikael :)")) {
            fail("Toinen rivisi on väärä syötteellä \"Mikael\" - pitäisi olla \":) Mikael :)\"");
        }
        if (!rivit[2].equals(":):):):):):)")) {
            fail("Kolmas rivisi on väärä syötteellä \"Mikael\" - pitäisi olla \":):):):):):)\"");
        }
    }

    @Test
    public void correctOutputWithMatti() throws Throwable {
        MockInOut io = new MockInOut("");

        kutsu("Matti");

        String output = io.getOutput();
        String[] rivit = output.split("\n");
        if (rivit.length != 3) {
            fail("Tulosteessasi on " + rivit.length + " rivinvaihtoa, kun odotettiin kolmea. "
                    + "Varmista että ohjelmasi tulostaa vain kolme riviä ja yhden rivinvaihdon jokaisen loppuun.");
        }
        if (rivit[0].contains(":):):):):):):)")) {
            fail("Tulostat liikaa hymiöitä ensimmäiselle riville, testaa metodisi toimintaa merkkijonolla \"Matti\"");
        }

        if (rivit[2].contains(":):):):):):):):)")) {
            fail("Tulostat liikaa hymiöitä viimeiselle riville, testaa metodisi toimintaa merkkijonolla \"Matti\"");
        }

        if (!output.contains(":) Matti")) {
            fail("Varmista että annetun merkkijonon eteen tulostetaan hymiö ja välilyönti.");
        }
        if (!output.contains(" Matti ")) {
            fail("Varmista että annetun merkkijonon kummallekin puolelle tulostetaan yksi välilyönti.");
        }

        if (!output.contains(" Matti  :)\n")) {
            fail("Varmista että ohjelmasi tulostaa parittomille syötteille annetun sanan oikealle puolelle kaksi välilyöntiä, hymiön ja"
                    + " rivinvaihton.");
        }
        if (!rivit[0].equals(":):):):):):)")) {
            fail("Ensimmäinen rivisi on väärä syötteellä \"Matti\" - pitäisi olla \":):):):):):)\"");
        }
        if (!rivit[1].equals(":) Matti  :)")) {
            fail("Toinen rivisi on väärä syötteellä \"Matti\" - pitäisi olla \":) Matti  :)\"");
        }
        if (!rivit[2].equals(":):):):):):)")) {
            fail("Kolmas rivisi on väärä syötteellä \"Matti\" - pitäisi olla \":):):):):):)\"");
        }
    }

    private void kutsu(String mj) throws Throwable {
        String v = "Ongelman aiheutti "
                + "tulostaHymioityna(\"" + mj + "\");";
        String metodi = "tulostaHymioityna";
        klass.staticMethod(metodi).returningVoid().taking(String.class).withNiceError(v).invoke(mj);
    }
}