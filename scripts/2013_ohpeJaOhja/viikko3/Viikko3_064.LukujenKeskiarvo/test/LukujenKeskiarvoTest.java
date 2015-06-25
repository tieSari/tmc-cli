
import fi.helsinki.cs.tmc.edutestutils.Points;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import java.util.ArrayList;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import static org.powermock.api.easymock.PowerMock.*;
import org.powermock.modules.junit4.PowerMockRunner;

@Points("64")
@PrepareForTest(LukujenKeskiarvo.class)
//@RunWith(PowerMockRunner.class)
public class LukujenKeskiarvoTest {

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
    public void testaaYhdenKokoisenListanKeskiarvo() {
        assertEquals("Et laske listan [1] keskiarvoa oikein.", 1, LukujenKeskiarvo.keskiarvo(a(1)), 0.0001);
        assertEquals("Et laske listan [2] keskiarvoa oikein.", 2, LukujenKeskiarvo.keskiarvo(a(2)), 0.0001);
        assertEquals("Et laske listan [3] keskiarvoa oikein.", 3, LukujenKeskiarvo.keskiarvo(a(3)), 0.0001);
    }

    @Test
    public void testaaIsommanListanSumma() {
        assertEquals("Et laske listan [1,2,3] keskiarvoa oikein.",
                2, LukujenKeskiarvo.keskiarvo(a(1, 2, 3)), 0.0001);
        assertEquals("Et laske listan [2,2,2,2,2,2,2] keskiarvoa oikein.",
                2, LukujenKeskiarvo.keskiarvo(a(2, 2, 2, 2, 2, 2, 2)), 0.0001);
        assertEquals("Et laske listan [-1,1,-2,2,-3,3] keskiarvoa oikein.",
                0, LukujenKeskiarvo.keskiarvo(a(-1, 1, -2, 2, -3, 3)), 0.0001);
        assertEquals("Et laske listan [1,1,1,1,2,2,2] keskiarvoa oikein.",
                1.429, LukujenKeskiarvo.keskiarvo(a(1, 1, 1, 1, 2, 2, 2)), 0.001);
    }

    @Test
    public void testaaApumetodinKaytto() {
        mockStaticPartial(LukujenKeskiarvo.class, "summa");
        reset(LukujenKeskiarvo.class);

        ArrayList<Integer> luvut = new ArrayList<Integer>();
        luvut.add(1);
        luvut.add(3);


        LukujenKeskiarvo.summa(luvut);
        expectLastCall().andReturn(4);

        replay(LukujenKeskiarvo.class);

        LukujenKeskiarvo.keskiarvo(luvut);
        
        try {
            verifyAll();
        }  catch (AssertionError e) {
            fail("metodin keskiarvo tulee laskea parametrinaan saamansa listan "
                    + "alkioiden summa metodin summa avulla\n"
                    + "Kun kutsuttiin metodia keskiarvo listalle [1,3] " + e);
        }

    }
    
    @Test
    public void testaaApumetodinKaytto2() {
        mockStaticPartial(LukujenKeskiarvo.class, "summa");
        reset(LukujenKeskiarvo.class);

        ArrayList<Integer> luvut = new ArrayList<Integer>();
        luvut.add(1);
        luvut.add(9);
        luvut.add(3);
        luvut.add(4);

        LukujenKeskiarvo.summa(luvut);
        expectLastCall().andReturn(17);

        replay(LukujenKeskiarvo.class);

        LukujenKeskiarvo.keskiarvo(luvut);
        
        try {
            verifyAll();
        }  catch (AssertionError e) {
            fail("metodin keskiarvo tulee laskea parametrinaan saamansa listan "
                    + "alkioiden summa metodin summa avulla\n"
                    + "Kun kutsuttiin metodia keskiarvo listalle [1,9,3,4] " + e);
        }    
    }
}
 