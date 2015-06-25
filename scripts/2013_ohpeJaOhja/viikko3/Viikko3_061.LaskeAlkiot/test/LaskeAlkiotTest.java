import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import java.lang.reflect.Method;

@Points("61")
public class LaskeAlkiotTest {
    
    Method m;

    @Before
    public void haeMetodi() {
        String v = "Et ole lisännyt metodia public static int laskeAlkiot(ArrayList<String> lista) !";
        try {
            m = ReflectionUtils.requireMethod(LaskeAlkiot.class, "laskeAlkiot", ArrayList.class);

        } catch (Throwable t) {
            fail(v+" Lisätietoja:\n"+t);
        }
        assertTrue(v, int.class == m.getReturnType());
    }

    public ArrayList a(Object... i ) {
        ArrayList al = new ArrayList();
        for (Object j : i) {
            al.add(j);
        }
        return al;
    }

    public void test(int len, ArrayList in) {
        String s = in.toString();
        try {
            assertEquals("Listan "+s+" alkioitten laskeminen ei onnistunut!",
                         len,
                         (int) ReflectionUtils.invokeMethod(Integer.TYPE, m, null, in));
        } catch (Throwable t) {
            fail("Jokin meni pieleen kun kutsuttiin laskeAlkiot listalle "+s+". \nLisätietoja: "+t);
        }
    }

    @Test
    public void testaaYhdenPituinen() {
        test(1,a("XXX"));
        test(1,a("s"));
    }

    @Test
    public void testaaNollanPituinen() {
        test(0,a());
    }

    @Test
    public void testaaPitempi() {
        test(3,a("X","Y","Z"));
        test(8,a("a","b","c","d","e","f","g","h"));
    }
}