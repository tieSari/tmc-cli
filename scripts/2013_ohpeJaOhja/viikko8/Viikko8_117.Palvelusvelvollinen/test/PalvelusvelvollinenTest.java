
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import fi.helsinki.cs.tmc.edutestutils.Reflex.ClassRef;
import fi.helsinki.cs.tmc.edutestutils.Reflex.MethodRef0;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

public class PalvelusvelvollinenTest {

    @Points("117.1")
    @Test
    public void testaaSivaria() throws Throwable {
        testaaPalvelusvelvollista("Sivari", 362, false);
    }

    @Points("117.2")
    @Test
    public void testaaAsevelvollista() throws Throwable {
        testaaPalvelusvelvollista("Asevelvollinen", 180, true);
        testaaPalvelusvelvollista("Asevelvollinen", 270, true);
        testaaPalvelusvelvollista("Asevelvollinen", 362, true);
    }

    private <T> void testaaPalvelusvelvollista(String luokka, int alkuTj, boolean tjKonstruktoriin) throws Throwable {
        ClassRef<T> classRef;

        classRef = (ClassRef<T>) Reflex.reflect(luokka);
        assertTrue("Luokan " + luokka + " pitää olla julkinen, eli se tulee määritellä\npublic class " + luokka + " {...\n}", classRef.isPublic());

        String v = "";

        T velvollinen;
        if (tjKonstruktoriin) {
            v = "Tee luokalle " + luokka + " konstruktori public " + luokka + "(int tj)";
            assertTrue(v, classRef.constructor().taking(int.class).isPublic());
            v = "Tarkista koodi " + luokka + " v = new " + luokka + "(" + alkuTj + "); ";
            velvollinen = classRef.constructor().taking(int.class).withNiceError(v).invoke(alkuTj);
        } else {
            v = "Tee luokalle " + luokka + " konstruktori public " + luokka + "()";
            assertTrue(v, classRef.constructor().takingNoParams().isPublic());
            v = "Tarkista koodi " + luokka + " v = new " + luokka + "(); ";
            velvollinen = classRef.constructor().takingNoParams().withNiceError(v).invoke();
        }

        if (!Palvelusvelvollinen.class.isAssignableFrom(
                classRef.getReferencedClass())) {
            Assert.fail("Luokka " + luokka + " ei toteutta rajapintaa Palvelusvelvollinen.");
            return;
        }

        MethodRef0<T, Integer> getTjMethod;
        getTjMethod = classRef.method(velvollinen, "getTJ").
                returning(int.class).takingNoParams();

        assertTrue("Luokka " + luokka + " ei sisällä metodia: public int getTJ()",
                getTjMethod.isPublic());

        assertEquals(v + "v.getTj(); ", alkuTj, (int) getTjMethod.withNiceError(v).invoke());


        MethodRef0<T, Void> palveleMethod;
        palveleMethod = classRef.method(velvollinen, "palvele").
                returningVoid().takingNoParams();

        assertTrue("Luokka " + luokka + " ei sisällä metodia: public void palvele()",
                palveleMethod.isPublic());

        v += "v.palvele(); ";

        palveleMethod.withNiceError(v).invoke() ;

        v += "v.getTj(); ";
        
        
        
        assertEquals(v, alkuTj-1, (int)getTjMethod.withNiceError(v).invoke() );
        
        Integer tj;

        for (int i = 1; i < alkuTj; i++) {
            try {
                tj = getTjMethod.invoke();
            } catch (Throwable t) {
                Assert.fail("Luokassa " + luokka + " metodin getTJ() kutsumisessa "
                        + "tapahtui poikkeus: " + t.toString());
                return;
            }

            Assert.assertTrue(
                    luokka + "-luokan TJ täytyy olla " + i + " päivän palveluksen jälkeen " + (alkuTj - i) + ", palautit: " + tj,
                    tj == (alkuTj - i));

            try {
                palveleMethod.invoke();
            } catch (Throwable t) {
                Assert.fail("Luokan " + luokka + " metodin palvele() kutsumisessa "
                        + "tapahtui poikkeus: " + t.toString());
                return;
            }
        }


        try {
            tj = getTjMethod.invoke();
        } catch (Throwable t) {
            Assert.fail("Luokassa " + luokka + " metodin getTJ() kutsumisessa "
                    + "tapahtui poikkeus: " + t.toString());
            return;
        }

        Assert.assertTrue(
                luokka + "-luokan TJ täytyy olla koko palveluksen jälkeen nolla, palautit: " + tj,
                tj == 0);

        try {
            palveleMethod.invoke();
        } catch (Throwable t) {
            Assert.fail("Luokan " + luokka + " metodin palvele() kutsumisessa "
                    + "tapahtui poikkeus: " + t.toString());
            return;
        }

        try {
            tj = getTjMethod.invoke();
        } catch (Throwable t) {
            Assert.fail("Luokassa " + luokka + " metodin getTJ() kutsumisessa "
                    + "tapahtui poikkeus: " + t.toString());
            return;
        }

        Assert.assertTrue(
                luokka + "-luokan TJ:n täytyy pysyä nollassa palveluksen loputtua, vaikka palvele() metodia kutsutaan, palautit: " + tj,
                tj == 0);
    }
}
