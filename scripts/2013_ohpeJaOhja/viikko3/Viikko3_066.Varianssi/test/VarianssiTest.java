
import fi.helsinki.cs.tmc.edutestutils.Points;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.rule.PowerMockRule;
import static org.powermock.api.easymock.PowerMock.*;

@Points("66")
@PrepareForTest(Varianssi.class)
//@RunWith(PowerMockRunner.class)
public class VarianssiTest {

    @Rule
    public PowerMockRule p = new PowerMockRule();

    public ArrayList<Integer> a(int... i) {
        ArrayList<Integer> al = new ArrayList<Integer>();
        for (int j : i) {
            al.add(j);
        }
        return al;
    }

    @Test
    public void testaaKahdenKokoisenListanVarianssi() {
        assertEquals("Et laske listan [1,1] varianssia oikein.",
                0, Varianssi.varianssi(a(1, 1)), 0.0001);
        assertEquals("Et laske listan [2,2] varianssia oikein.",
                0, Varianssi.varianssi(a(2, 2)), 0.0001);
        assertEquals("Et laske listan [1,3] varianssia oikein.",
                2, Varianssi.varianssi(a(1, 3)), 0.0001);
    }

    @Test
    public void testaaIsommanListanVarianssi() {
        assertEquals("Et laske listan [1,2,3] varianssia oikein.",
                1, Varianssi.varianssi(a(1, 2, 3)), 0.0001);
        assertEquals("Et laske listan [2,2,2,2,2,2,2] varianssia oikein.",
                0, Varianssi.varianssi(a(2, 2, 2, 2, 2, 2, 2)), 0.0001);
        assertEquals("Et laske listan [-1,1,-2,2,-3,3] varianssia oikein.",
                5.6, Varianssi.varianssi(a(-1, 1, -2, 2, -3, 3)), 0.0001);
        assertEquals("Et laske listan [1,1,1,1,2,2,2] varianssia oikein.",
                0.29, Varianssi.varianssi(a(1, 1, 1, 1, 2, 2, 2)), 0.01);
    }

    @Test
    public void testaaApumetodinKaytto() {
        mockStaticPartial(Varianssi.class, "keskiarvo");
        reset(Varianssi.class);

        ArrayList<Integer> luvut = new ArrayList<Integer>();
        luvut.add(1);
        luvut.add(3);

        Varianssi.keskiarvo(luvut);
        expectLastCall().andReturn(2);

        replay(Varianssi.class);       

        try {
            Varianssi.varianssi(luvut);
            verifyAll();
        } catch (AssertionError e) {
            fail("metodin varianssi tulee laskea parametrinaan saamansa listan "
                    + "alkioiden keskiarvo metodin keskiarvo avulla\n"
                    + "metodia keskiarvo tulee kutsua vain kertaalleen\n"
                    + "Kun kutsuttiin metodia varianssi listalle [1,3] " + e);
        }

    }

    @Test
    public void testaaApumetodinKaytto2() {
        mockStaticPartial(Varianssi.class, "keskiarvo");
        reset(Varianssi.class);

        ArrayList<Integer> luvut = new ArrayList<Integer>();
        luvut.add(2);
        luvut.add(9);
        luvut.add(1);
        luvut.add(3);
        luvut.add(5);

        Varianssi.keskiarvo(luvut);
        expectLastCall().andReturn(4);

        replay(Varianssi.class);
      
        try {
            Varianssi.varianssi(luvut);    
            verifyAll();
        } catch (Throwable e) {
            fail("metodin varianssi tulee laskea parametrinaan saamansa listan "
                    + "alkioiden keskiarvo metodin keskiarvo avulla\n"
                    + "metodia keskiarvo tulee kutsua vain kertaalleen\n"
                    + "Kun kutsuttiin metodia varianssi listalle [2,9,1,3,5] " + e);
        } 

    }
}
