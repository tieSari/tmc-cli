
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import fi.helsinki.cs.tmc.edutestutils.Reflex.MethodRef1;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("128")
public class TulostajaTest {

    String klassName = "Tulostaja";
    Reflex.ClassRef<Object> classRef;

    @Before
    public void setUp() {
        classRef = Reflex.reflect(klassName);
    }

    @Test
    public void luokkaOlemassa() {
        classRef = Reflex.reflect(klassName);

        assertTrue("Luokan " + s(klassName) + " pitää olla julkinen, eli se tulee määritellä\n"
                + "public class " + s(klassName) + " {...\n}", classRef.isPublic());
    }

    @Test
    public void eiYlimaaraisiaMuuttujia() {
        saniteettitarkastus(klassName, 10, "");
    }

    @Test
    public void onKonstruktori() throws Throwable {
        MethodRef1<Object, Object, String> ctor = classRef.constructor().taking(String.class).withNiceError();
        assertTrue("Määrittele luokalle " + s(klassName) + " julkinen konstruktori: \n"
                + "public " + s(klassName) + "(String tiedostonNimi)", ctor.isPublic());
        String v = "virheen aiheutti koodi new Tulostaja( \"src/testitiedosto.txt\" );\n";
        ctor.withNiceError(v).invoke("testitiedosto.txt");
    }

    public Object luo(String t) throws Throwable {
        return classRef.constructor().taking(String.class).invoke(t);
    }

    @Test
    public void onMetodiTulostaRivitJoilla() throws Throwable {

        Object o = luo("testitiedosto.txt");

        assertTrue("Lisää luokalle Tulostaja metodi public void tulostaRivitJoilla(String sana)",
                classRef.method(o, "tulostaRivitJoilla").returningVoid().taking(String.class).isPublic());

        String k = "Tulostaja t = new Tulostaja( \"src/testitiedosto.txt\" );\n"
                + "t.tulostaRivitJoilla(\"vanha\");";

        classRef.method(o, "tulostaRivitJoilla").returningVoid().taking(String.class).withNiceError(k).invoke("vanha");
    }

    @Test
    public void tulostaRivitJoillaVanha() throws Throwable {
        MockInOut io = new MockInOut("");
        Object o = luo("testitiedosto.txt");

        String k = "Tulostaja t = new Tulostaja( \"src/testitiedosto.txt\" );\n"
                + "t.tulostaRivitJoilla(\"vanha\");\n";

        classRef.method(o, "tulostaRivitJoilla").returningVoid().taking(String.class).withNiceError(k).invoke("vanha");

        String out = io.getOutput();

        assertFalse("Olisi pitänyt tulostaa 2 riviä koodilla" + k + "et tulostanut mitään!" + out, out.isEmpty());
        
        assertEquals("Olisi pitänyt tulostaa 2 riviä koodilla" + k + "tuloste oli\n" + out, 2, out.split("\n").length);

        assertTrue("Ensimmäisen tulostetun rivin olisi pitänyt olla\n"
                + "\"Siinä vanha Väinämöinen\"\n koodilla" + k + "tuloste oli\n" + out, out.split("\n")[0].startsWith("Siin"));
        assertTrue("Toisen tulostetun rivin olisi pitänyt olla\n"
                + "\"Sanoi vanha Väinämöinen\"\n koodilla" + k + "tuloste oli\n" + out, out.split("\n")[1].startsWith("Sanoi"));
    }

    @Test
    public void tulostaRivitJoillaTuli() throws Throwable {
        MockInOut io = new MockInOut("");
        Object o = luo("testitiedosto.txt");

        String k = "Tulostaja t = new Tulostaja( \"src/testitiedosto.txt\" );\n"
                + "t.tulostaRivitJoilla(\"tuli\");\n";

        classRef.method(o, "tulostaRivitJoilla").returningVoid().taking(String.class).withNiceError(k).invoke("tuli");

        String out = io.getOutput();

        assertEquals("Olisi pitänyt tulostaa 1 rivi koodilla" + k + "tuloste oli\n" + out, 1, out.split("\n").length);
        assertTrue("Tulostetun rivin olisi pitänyt olla\n"
                + "\"Niin tuli kevätkäkönen\"\n koodilla" + k + "tuloste oli\n" + out, out.split("\n")[0].startsWith("Niin tuli"));

    }

    @Test
    public void tulostaKaikkiRivit() throws Throwable {
        MockInOut io = new MockInOut("");
        Object o = luo("testitiedosto.txt");

        String k = "Tulostaja t = new Tulostaja( \"src/testitiedosto.txt\" );\n"
                + "t.tulostaRivitJoilla(\"\");\n";

        classRef.method(o, "tulostaRivitJoilla").returningVoid().taking(String.class).withNiceError(k).invoke("");

        String out = io.getOutput();

        assertEquals("Olisi pitänyt tulostaa 7 riviä koodilla" + k + "tuloste oli\n" + out, 7, out.split("\n").length);
        assertTrue("Ensimmäisen tulostetun rivin olisi pitänyt olla\n"
                + "\"Siinä vanha Väinämöinen\"\n koodilla" + k + "tuloste oli\n" + out, out.split("\n")[0].startsWith("Siin"));
        assertTrue("Viimeisen tulostetun rivin olisi pitänyt olla\n"
                + "\"Sanoi vanha Väinämöinen\"\n koodilla" + k + "tuloste oli\n" + out, out.split("\n")[6].startsWith("Sanoi"));
    }

    @Test
    public void lisaTesti() throws Throwable {
        MockInOut io = new MockInOut("");

        File file = File.createTempFile("temp", "txt");

        FileWriter writer = new FileWriter(file);
        writer.append("sana 1\nsana 2\nkoe\n");
        writer.flush();
        writer.close();

        Object o = luo(file.getAbsolutePath());


        classRef.method(o, "tulostaRivitJoilla").returningVoid().taking(String.class).invoke("sana");

        String out = io.getOutput();

        assertEquals("Kutsuttiin t.tulostaRivitJoilla(\"sana\"); kun tiedoston sisältö oli\n"
                + "----\n"
                + "sana 1\nsana 2\nkoe\n"
                + "----\n"
                + "Olisi pitänyt tulostaa 2 riviä. Tuloste oli\n" + out, 2, out.split("\n").length);

        assertTrue("Kutsuttiin t.tulostaRivitJoilla(\"sana\"); kun tiedoston sisältö oli\n"
                + "----\n"
                + "sana 1\nsana 2\nkoe\n"
                + "----\n"
                + "Ensimmäisen tulostetun rivin olisi pitänyt olla \"sana1\n. \n"
                + "Tuloste oli\n" + out, out.split("\n")[0].contains("sana 1"));
    }

    @Test
    public void kalevalaOlut() throws Throwable {
        MockInOut io = new MockInOut("");
        Object o = luo("kalevala.txt");

        String k = "Tulostaja t = new Tulostaja( \"src/kalevala.txt\" );\n"
                + "t.tulostaRivitJoilla(\"olut\");\n";

        classRef.method(o, "tulostaRivitJoilla").returningVoid().taking(String.class).withNiceError(k).invoke("olut");

        String out = io.getOutput();

        assertEquals("Olisi pitänyt tulostaa 41 riviä koodilla" + k + "tuloste oli\n" + out, 41, out.split("\n").length);
        assertTrue("Ensimmäisen tulostetun rivin olisi pitänyt olla\n"
                + "\"Kun ei tuotane olutta\"\n koodilla" + k + "tuloste oli\n" + out, out.split("\n")[0].contains("Kun ei tuotane olutta"));
    }
    
    @Test
    public void kalevalaMaito() throws Throwable {
        MockInOut io = new MockInOut("");
        Object o = luo("kalevala.txt");

        String k = "Tulostaja t = new Tulostaja( \"src/kalevala.txt\" );\n"
                + "t.tulostaRivitJoilla(\"maito\");\n";

        classRef.method(o, "tulostaRivitJoilla").returningVoid().taking(String.class).withNiceError(k).invoke("maito");

        String out = io.getOutput();

        assertEquals("Olisi pitänyt tulostaa 24 riviä koodilla" + k + "tuloste oli\n" + out, 24, out.split("\n").length);
        assertTrue("Ensimmäisen tulostetun rivin olisi pitänyt olla\n"
                + "\"maitopartana pahaisna\"\n koodilla" + k + "tuloste oli\n" + out, out.split("\n")[0].contains("maitopartana pahaisna"));
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
