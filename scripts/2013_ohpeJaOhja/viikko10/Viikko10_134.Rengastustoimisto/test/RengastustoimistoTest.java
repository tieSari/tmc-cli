
import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("134.2")
public class RengastustoimistoTest {

    String klassName = "Rengastustoimisto";
    Reflex.ClassRef<Object> klass;

    @Before
    public void setUp() {
        klass = Reflex.reflect(klassName);
    }

    @Test
    public void luokkaJulkinen() {
        klass = Reflex.reflect(klassName);
        assertTrue("Luokan " + klassName + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassName + " {...\n}", klass.isPublic());
    }

    @Test
    public void eiYlimaaraisiaMuuttujia() {
        saniteettitarkastus(klassName, 2, "havainnot tallentavan Map<Lintu, List<String>>-tyyppisen oliomuuttujan");
    }

    @Test
    public void onHashMap() {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();
        assertFalse("Lisää luokalle " + klassName + " Map<Lintu, List<String>>-tyyppinen oliomuuttuja", kentat.length < 1);
        assertTrue("Luokalla " + klassName + " tulee olla ainoastaan Map<Lintu, List<String>>-tyyppinen oliomuuttuja"
                + "poista ylimääräiset", kentat.length == 1);
        
        assertTrue("Luokalla " + klassName + " tulee olla Map<Lintu, List<String>>-tyyppinen oliomuuttuja " + kentat[0].toString(), kentat[0].toString().contains("Map"));

    }

    @Test
    public void tyhjaKonstruktori() throws Throwable {
        klass = Reflex.reflect(klassName);
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        assertTrue("Määrittele luokalle " + klassName + " julkinen konstruktori: public " + klassName + "()", ctor.isPublic());
        String v = "virheen aiheutti koodi new Rengastustoimisto();";
        ctor.withNiceError(v).invoke();
    }

    @Test
    public void havaitseMetodi() throws Throwable {
        String metodi = "havaitse";

        Object olio = luo();

        assertTrue("tee luokalle " + klassName + " metodi public void " + metodi + "(Lintu lintu, String paikka) ",
                klass.method(olio, metodi)
                .returningVoid().taking(Lintu.class, String.class).isPublic());

        String v = "\nVirheen aiheuttanut koodi\n "
                + "Rengastustoimisto rt = new Rengastustoimisto();\n"
                + "rt.havaitse( new Lintu(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n";

        klass.method(olio, metodi)
                .returningVoid().taking(Lintu.class, String.class).
                withNiceError(v).invoke(new Lintu("Nebelkrähe", "Corvus corone cornix", 2000), "Berlin");

        v = "\nVirheen aiheuttanut koodi\n "
                + "Rengastustoimisto rt = new Rengastustoimisto();\n"
                + "rt.havaitse( new Lintu(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.havaitse( new Lintu(\"Harmaalokki\", \"Larus argentatus\", 2012), \"Kumpula\");\n";

        klass.method(olio, metodi)
                .returningVoid().taking(Lintu.class, String.class).
                withNiceError(v).invoke(new Lintu("Harmaalokki", "Larus argentatus", 2012), "Kumpula");

        v = "\nVirheen aiheuttanut koodi\n "
                + "Rengastustoimisto rt = new Rengastustoimisto();\n"
                + "rt.havaitse( new Lintu(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.havaitse( new Lintu(\"Harmaalokki\", \"Larus argentatus\", 2012), \"Kumpula\");\n"
                + "rt.havaitse( new Lintu(\"Varis\", \"Corvus corone cornix\", 2000), \"Arabia\");\n";

        klass.method(olio, metodi)
                .returningVoid().taking(Lintu.class, String.class).
                withNiceError(v).invoke(new Lintu("Varis", "Corvus corone cornix", 2000), "Arabia");
    }

    @Test
    public void havainnotMetodi() throws Throwable {
        String metodi = "havainnot";

        Object olio = luo();

        assertTrue("tee luokalle " + klassName + " metodi public void " + metodi + "(Lintu lintu) ",
                klass.method(olio, metodi)
                .returningVoid().taking(Lintu.class).isPublic());

        String v = "\nVirheen aiheuttanut koodi\n "
                + "Rengastustoimisto rt = new Rengastustoimisto();\n"
                + "rt.havainnot(new Lintu(\"Nebelkrähe\", \"Corvus corone cornix\", 2000));\n";

        klass.method(olio, metodi)
                .returningVoid().
                taking(Lintu.class).
                withNiceError(v).invoke(new Lintu("Nebelkrähe", "Corvus corone cornix", 2000));
    }

    @Test
    public void havavainnointiToimii1() throws Throwable {
        MockInOut mio = new MockInOut("");

        Object olio = luo();

        String v = "\n"
                + "Rengastustoimisto rt = new Rengastustoimisto();\n"
                + "rt.havaitse( new Lintu(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.havainnot( new Lintu(\"Nebelkrähe\", \"Corvus corone cornix\", 2000) )";

        havaitse(olio, "Nebelkrähe", "Corvus corone cornix", 2000, "Berlin", v);
        havainnot(olio, v, new Lintu("Nebelkrähe", "Corvus corone cornix", 2000));
        String out = mio.getOutput();

        assertFalse("Olisi pitänyt tulostaa 2 riviä koodilla\n" + v
                + "\nNyt ei tulostunut mitään\n", out.isEmpty());

        assertTrue("Olisi pitänyt tulostaa 2 riviä koodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n").length > 1);

        assertTrue("Ensimmäisen rivin olisi pitänyt olla \"Corvus corone cornix (2000) havaintoja: 1"
                + "\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n")[0].contains("Corvus corone cornix (2000)"));
        assertTrue("Ensimmäisen rivin olisi pitänyt olla \"Corvus corone cornix (2000) havaintoja: 1"
                + "\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n")[0].contains("havaintoja: 1"));
 
        assertTrue("Toisen rivin olisi pitänyt olla \"Berlin\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n")[1].contains("Berlin"));
    }

    @Test
    public void havavainnointiToimii2() throws Throwable {
        MockInOut mio = new MockInOut("");

        Object olio = luo();

        String v = "\n"
                + "Rengastustoimisto rt = new Rengastustoimisto();\n"
                + "rt.havaitse( new Lintu(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.havaitse( new Lintu(\"Harmaalokki\", \"Larus argentatus\", 2012), \"Kumpula\");\n"
                + "rt.havainnot( new Lintu(\"Nebelkrähe\", \"Corvus corone cornix\", 2000) )";

        havaitse(olio, "Nebelkrähe", "Corvus corone cornix", 2000, "Berlin", v);
        havaitse(olio, "Harmaalokki", "Larus argentatus", 2012, "Kumpula", v);

        havainnot(olio, v, new Lintu("Nebelkrähe", "Corvus corone cornix", 2000));

        String out = mio.getOutput();

        assertTrue("Olisi pitänyt tulostaa 2 riviä koodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n").length > 1);

        assertTrue("Ensimmäisen rivin olisi pitänyt olla \"Corvus corone cornix (2000) havaintoja: 1"
                + "\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n")[0].contains("Corvus corone cornix (2000)"));
        assertTrue("Ensimmäisen rivin olisi pitänyt olla \"Corvus corone cornix (2000) havaintoja: 1"
                + "\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n")[0].contains("havaintoja: 1"));
        assertTrue("Toisen rivin olisi pitänyt olla \"Berlin\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n")[1].contains("Berlin"));

        mio = new MockInOut("");
        havainnot(olio, v, new Lintu("Harmaalokki", "Larus argentatus", 2012));

        v = "\nVirheen aiheuttanut koodi\n "
                + "Rengastustoimisto rt = new Rengastustoimisto();\n"
                + "rt.havaitse( new Lintu(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.havaitse( new Lintu(\"Harmaalokki\", \"Larus argentatus\", 2012), \"Kumpula\");\n"
                + "rt.havainnot( new Lintu(\"Harmaalokki\", \"Larus argentatus\", 2012) )";

        havainnot(olio, v, new Lintu("Harmaalokki", "Larus argentatus", 2012));

        out = mio.getOutput();

        assertTrue("Olisi pitänyt tulostaa 2 riviä koodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n").length > 1);

        assertTrue("Ensimmäisen rivin olisi pitänyt olla \"Larus argentatus (2012) havaintoja: 1"
                + "\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n")[0].contains("Larus argentatus (2012) havaintoja: 1"));
        assertTrue("Ensimmäisen rivin olisi pitänyt olla \"Larus argentatus (2012)"
                + "\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n")[0].contains("havaintoja: 1"));
                
        assertTrue("Toisen rivin olisi pitänyt olla \"Kumpula\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n")[1].contains("Kumpula"));
    }

    @Test
    public void havavainnointiToimii3() throws Throwable {
        MockInOut mio = new MockInOut("");

        Object olio = luo();

        String v = "\n"
                + "Rengastustoimisto rt = new Rengastustoimisto();\n"
                + "rt.havaitse( new Lintu(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.havaitse( new Lintu(\"Harmaalokki\", \"Larus argentatus\", 2012), \"Kumpula\");\n"
                + "rt.havaitse( new Lintu(\"Varis\", \"Corvus corone cornix\", 2000), \"Arabia\");\n"
                + "rt.havaitse( new Lintu(\"lokki\", \"Larus argentatus\", 2012), \"Korso\");\n"
                + "rt.havaitse( new Lintu(\"Varis\", \"Corvus corone cornix\", 2000), \"Kamppi\");\n"
                + "rt.havainnot( new Lintu(\"Varis\", \"Corvus corone cornix\", 2000) )\n";

        havaitse(olio, "Nebelkrähe", "Corvus corone cornix", 2000, "Berlin", v);
        havaitse(olio, "Harmaalokki", "Larus argentatus", 2012, "Kumpula", v);
        havaitse(olio, "Varis", "Corvus corone cornix", 2000, "Arabia", v);
        havaitse(olio, "lokki", "Larus argentatus", 2012, "Korso", v);
        havaitse(olio, "Varis", "Corvus corone cornix", 2000, "Kamppi", v);

        havainnot(olio, v, new Lintu("Nebelkrähe", "Corvus corone cornix", 2000));

        String out = mio.getOutput();

        assertTrue("Olisi pitänyt tulostaa 4 riviä koodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n").length > 1);

        assertTrue("Ensimmäisen rivin olisi pitänyt olla \"Corvus corone cornix (2000) havaintoja: 3"
                + "\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n")[0].contains("havaintoja: 3"));
        assertTrue("Ensimmäisen rivin olisi pitänyt olla \"Corvus corone cornix (2000) havaintoja: 3"
                + "\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n")[0].contains("Corvus corone cornix (2000)"));
              
        assertTrue("Tulostuksen olisi pitänyt sisältää \"Berlin\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.contains("Berlin"));
        assertTrue("Tulostuksen olisi pitänyt sisältää \"Arabia\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.contains("Arabia"));
        assertTrue("Tulostuksen olisi pitänyt sisältää \"Kumpula\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.contains("Kamppi"));

        
        mio = new MockInOut("");

        v = "\nVirheen aiheuttanut koodi\n "
                + "Rengastustoimisto rt = new Rengastustoimisto();\n"
                + "rt.havaitse( new Lintu(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.havaitse( new Lintu(\"Harmaalokki\", \"Larus argentatus\", 2012), \"Kumpula\");\n"
                + "rt.havaitse( new Lintu(\"Varis\", \"Corvus corone cornix\", 2000), \"Arabia\");\n"
                + "rt.havaitse( new Lintu(\"lokki\", \"Larus argentatus\", 2012), \"Korso\");\n"
                + "rt.havaitse( new Lintu(\"Varis\", \"Corvus corone cornix\", 2000), \"Kamppi\");\n"
                + "rt.havainnot( new Lintu(\"Harmaalokki\", \"Larus argentatus\", 2012) );";

        havainnot(olio, v, new Lintu("Harmaalokki", "Larus argentatus", 2012));

        out = mio.getOutput();

        assertTrue("Olisi pitänyt tulostaa 2 riviä koodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n").length > 1);

        assertTrue("Ensimmäisen rivin olisi pitänyt olla \"Larus argentatus (2012) havaintoja: 2"
                + "\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n")[0].contains("Larus argentatus (2012)") && out.split("\n")[0].contains("havaintoja: 2"));
        assertTrue("Tulostuksen olisi pitänyt sisältää \"Korso\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.contains("Korso"));
        assertTrue("Tulostuksen olisi pitänyt sisältää \"Kumpula\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.contains("Kumpula"));
    }

    @Test
    public void havavainnointiToimii4() throws Throwable {
        MockInOut mio = new MockInOut("");

        Object olio = luo();

        String v = "\n"
                + "Rengastustoimisto rt = new Rengastustoimisto();\n"
                + "rt.havaitse( new Lintu(\"Nebelkrähe\", \"Corvus corone cornix\", 2000), \"Berlin\");\n"
                + "rt.havainnot( new Lintu(\"Varsi\", \"Corvus corone cornix\", 2012) )";

        havaitse(olio, "Nebelkrähe", "Corvus corone cornix", 2000, "Berlin", v);
        havainnot(olio, v, new Lintu("Varis", "Corvus corone cornix", 2012));
        String out = mio.getOutput();

        assertFalse("Olisi pitänyt tulostaa 1 rivi koodilla\n" + v
                + "\nNyt ei tulostunut mitään\n", out.isEmpty());

        assertTrue("Olisi pitänyt tulostaa 1 rivi koodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n").length == 1);

        assertTrue("Tulostetun rivin olisi pitänyt olla \"Corvus corone cornix (2012) havaintoja: 0"
                + "\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n")[0].contains("Corvus corone cornix (2012)"));
        assertTrue("Tulostetun rivin olisi pitänyt olla \"Corvus corone cornix (2012) havaintoja: 0"
                + "\"\nkoodilla\n" + v
                + "\ntulostui:\n" + out, out.split("\n")[0].contains("havaintoja: 0"));
    }
    
    /*
     * 
     */
    public void havainnot(Object o, String v, Lintu l) throws Throwable {
        klass.method(o, "havainnot")
                .returningVoid().
                taking(Lintu.class).
                withNiceError(v).invoke(l);
    }

    public void havaitse(Object olio, String n, String l, int v, String h, String vi) throws Throwable {
        klass.method(olio, "havaitse")
                .returningVoid().taking(Lintu.class, String.class).
                withNiceError(vi).invoke(new Lintu(n, l, v), h);
    }

    public Object luo() throws Throwable {
        klass = Reflex.reflect(klassName);
        Reflex.MethodRef0<Object, Object> ctor = klass.constructor().takingNoParams().withNiceError();
        return ctor.invoke();
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("et tarvitse \"stattisia muuttujia\", poista luokalta " + klassName + " muuttuja " + kentta(field.toString(), klassName), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("luokan kaikkien oliomuuttujien näkyvyyden tulee olla private, muuta luokalta " + klassName + " löytyi: " + kentta(field.toString(), klassName), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }
            assertTrue("et tarvitse " + klassName + "-luokalle kuin " + m + ", poista ylimääräiset", var <= n);
        }
    }

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "");
    }
}
