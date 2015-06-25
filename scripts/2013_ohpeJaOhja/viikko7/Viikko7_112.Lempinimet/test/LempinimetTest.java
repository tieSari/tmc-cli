import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.*;
import static org.junit.Assert.*;

import static org.powermock.api.easymock.PowerMock.*;
import static org.easymock.EasyMock.expect;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.HashMap;
import org.powermock.modules.junit4.rule.PowerMockRule;

@Points("112")
@PrepareForTest({HashMap.class, Lempinimet.class})
public class LempinimetTest {

    @Rule public PowerMockRule p = new PowerMockRule();

    @Test public void testaaLuonti() throws Exception {

        HashMap hm = new HashMap();
        expectNew(HashMap.class).andReturn(hm);
        replayAll();

        try {
            Lempinimet.main(new String[0]);
        } catch (AssertionError e) {
            fail("apua "+e);
        } catch (Throwable t) {
            fail("Jokin meni pieleen kun main-metodia ajettiin: "+t);
        }

        try {
            verifyAll();
        } catch (AssertionError e) {
            if (e.getMessage().contains("HashMap()")) {
                fail("Et tainnut luoda yhtään HashMap-oliota! Lisätietoja:\n"+e);
            }
            fail("Jokin meni pieleen: "+e);
        }

    }

    @Test public void testaaKaikki() throws Exception {
        HashMap hm = createMock(HashMap.class);
        expectNew(HashMap.class).andReturn(hm);

        expect(hm.put("matti", "mage")).andReturn(null);
        expect(hm.put("mikael", "mixu")).andReturn(null);
        expect(hm.put("arto", "arppa")).andReturn(null);

        //expect(hm.toString()).andReturn(null);
        expect(hm.get("mikael")).andReturn(null);

        replayAll();

        try {
            Lempinimet.main(new String[0]);
        } catch (AssertionError e) {
            if (e.getMessage().contains("HashMap.put")) {
                fail("Teit väärän put-kutsun HashMapille! Tarkista tehtävänanto! Lisätietoja:\n"+e);
            }
            if (e.getMessage().contains("HashMap.get")) {
                fail("Teit väärän get-kutsun HashMapille! Tarkista tehtävänanto! Lisätietoja:\n"+e);
            }
            fail("apua: "+e);
        } catch (Throwable t) {
            fail("Jokin meni pieleen kun main-metodia ajettiin: "+t);
        }

        try {
            verifyAll();
        } catch (AssertionError e) {
            if (e.getMessage().contains("HashMap()")) {
                fail("Et tainnut luoda yhtään HashMap-oliota! Lisätietoja:\n"+e);
            }
            if (e.getMessage().contains("HashMap.put")) {
                fail("Et tehnyt oikeita put-kutsuja HashMapille! Lisätietoja:\n"+e);
            }
            if (e.getMessage().contains("HashMap.get")) {
                fail("Et tehnyt oikeita get-kutsuja HashMapille! Lisätietoja:\n"+e);
            }
            fail("Jokin meni pieleen: "+e);
        }
    }

}
