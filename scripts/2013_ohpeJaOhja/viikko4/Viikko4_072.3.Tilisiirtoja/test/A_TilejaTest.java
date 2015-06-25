
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Method;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
@Points("72.3")
public class A_TilejaTest<_Tileja, _Tili> {

    String metodinNimi = "tilisiirto";

    String klassName = "Tileja";
    
    Reflex.ClassRef<_Tili> _TiliClassRef;
    Reflex.ClassRef<_Tileja> _TilejaClassRef;

    Method metodi;
    
    @Before
    public void setUp() {
        // Alustettava täällä luultavasti PowerMockin takia.
        _TiliClassRef = Reflex.reflect("Tili");
        _TilejaClassRef = Reflex.reflect("Tileja");
    }
    
    @Test
    public void metodiOlemassa() throws Throwable {
        // Tässä riittäisi myös käyttää Class[Ref]<Tili> ja Class[Ref]<Tileja> suoraan.
        
        assertTrue("tee luokalle " + klassName + " metodi public static void " + metodinNimi + "(Tili mista, Tili minne, double paljonko) ", 
                _TilejaClassRef.staticMethod(metodinNimi).returningVoid().
                                                 taking(ReflectionUtils.findClass("Tili"),ReflectionUtils.findClass("Tili"), double.class).isPublic());
        
        String v = "\nVirheen aiheuttanut koodi "
                + "Tili t1 = new Tili(\"Matti\",10.0); "
                + "Tili t2 = new Tili(\"Pekka\",0.0);"
                + "tilisiirto(t1, t2, 5.0); ";
        
        _Tili t1 = _TiliClassRef.constructor().taking(String.class, double.class).invoke("Matti", 10.0);
        _Tili t2 = _TiliClassRef.constructor().taking(String.class, double.class).invoke("Pekka", 0.0);
        
        Class<_Tili> c = _TiliClassRef.cls();
        
        _TilejaClassRef
                .staticMethod(metodinNimi)
                .returningVoid()
                .taking(c, c, double.class)
                .withNiceError(v)
                .invoke(t1, t2, 5.0);

    }

    @Test
    public void testaa() throws Exception {
        Tili eka = new Tili("eka", 1000);
        Tili toka = new Tili("toka", 0);
        double summa = 100;

        String virhe = "tee ohjelmaasi metodi public static void tilisiirto(Tili mista, Tili minne, double paljonko);";

        try {
            kutsu(eka, toka, summa);
        } catch (Throwable e) {
            if (e.getMessage() != null && (e.getMessage().contains("puuttuu") || e.getMessage().contains("missing"))) {
                fail(virhe);
            } else {
                String msg = "paina nappia show backtrace, virheen syy löytyy hieman alempaa kohdasta \" Caused by\"\n"
                        + "klikkaamalla pääset suoraan virheen aiheuttaneelle koodiriville";

                throw new Exception(msg, e);
            }
            assertTrue(virhe, metodi.toString().contains("static"));
            assertTrue(virhe, metodi.toString().contains("void"));
            assertTrue(virhe, metodi.toString().contains("public"));
        }

        assertEquals("Luotiin tili jossa rahaa 1000, siirrettäessä siltä 100, saldo ", 900, eka.saldo(), 0.1);
        assertEquals("Luotiin tili jossa rahaa 0, siirrettäessä sinne 100, saldo ", 100, toka.saldo(), 0.1);
    }
    
    private void kutsu(Tili milta, Tili mille, double summa) throws Throwable {
        metodi = ReflectionUtils.requireMethod(Tileja.class, metodinNimi, Tili.class, Tili.class, double.class);
        metodi.invoke(null, milta, mille, summa);
    }
}
