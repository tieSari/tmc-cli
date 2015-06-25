
import fi.helsinki.cs.tmc.edutestutils.Points;
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

@Points("72.1")
//@RunWith(PowerMockRunner.class)
@PrepareForTest({Tileja.class, Tili.class})
public class TilejaTest {

    @Rule
    public PowerMockRule p = new PowerMockRule();

    @Test
    public void testaa() throws Exception {
        Tili tiliMock = createMock(Tili.class);

        expectNew(Tili.class, EasyMock.anyObject(), EasyMock.eq(100.0)).andReturn(tiliMock);

        tiliMock.pano(20.0);
        replay(tiliMock, Tili.class);

        try {
            Tileja.main(new String[0]);
            verify(tiliMock, Tili.class);

        } catch (Throwable t) {
            String virhe = t.getMessage();
            if (virhe.contains("pano")) {
                fail("luo tili ja kutsu tilille metodia pano parametrilla 20");
            } else if (virhe.contains("constructor")) {
                fail("laita tilin luomisessa konstruktorin parametriksi 100.0");
            }
            fail("odottamaton tilanne:\n" + virhe);
        }
    }

    @Test
    public void testaaToString() throws Exception {
        MockInOut mio = new MockInOut("");

        Tileja.main(new String[0]);

        String out = mio.getOutput();
        assertTrue("ohjelman tulee tulostaa tili, eli kutsua System.out.println(tili); tili on tässä tilimuuttjan nimi", out.contains("120.0"));
        mio.close();

    }
}
