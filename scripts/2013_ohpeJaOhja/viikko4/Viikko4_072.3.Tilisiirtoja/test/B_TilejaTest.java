
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.Rule;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.expect;
import org.junit.Before;
import static org.powermock.api.easymock.PowerMock.*;
import org.powermock.reflect.exceptions.MethodNotFoundException;

@Points("72.3")
@PrepareForTest({Tileja.class, Tili.class})
public class B_TilejaTest<_Tileja, _Tili> {

    @Rule
    public PowerMockRule p = new PowerMockRule();
    
    String metodinNimi = "tilisiirto";

    String klassName = "Tileja";
    
    Method metodi;    

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

    @Test
    public void testaaToString() throws Exception {
        Tili a = createMock(Tili.class);
        Tili b = createMock(Tili.class);
        Tili c = createMock(Tili.class);

        expectNew(Tili.class, EasyMock.anyObject(), EasyMock.eq(100.0)).andReturn(a);
        expectNew(Tili.class, EasyMock.anyObject(), EasyMock.eq(0.0)).andReturn(b);
        expectNew(Tili.class, EasyMock.anyObject(), EasyMock.eq(0.0)).andReturn(c);

        mockStaticPartial(Tileja.class, metodinNimi);
        reset(Tileja.class);

        metodi = ReflectionUtils.requireMethod(Tileja.class, metodinNimi, Tili.class, Tili.class, double.class);

        metodi.invoke(
                null, a, b, 50.0);
        metodi.invoke(
                null, b, c, 25.0);

        replay(Tileja.class);
        replay(a, Tili.class);
        replay(b, Tili.class);
        replay(c, Tili.class);

        try {
            Tileja.main(new String[0]);
            verifyAll();
        } catch (Throwable e) {
            fail("Luo pääohjelmassa kolme tiliä, alkusaldot 100, 0 ja 0. Siirrä metodin \"tilisiirto\" avulla ensin 50 ensimmäiseltä toiselle tilille ja sen jälkeen 25 toiselta kolmannelle");
        }
    }
    
    private void kutsu(Tili milta, Tili mille, double summa) throws Throwable {
        metodi = ReflectionUtils.requireMethod(Tileja.class, metodinNimi, Tili.class, Tili.class, double.class);
        metodi.invoke(null, milta, mille, summa);
    }
}
