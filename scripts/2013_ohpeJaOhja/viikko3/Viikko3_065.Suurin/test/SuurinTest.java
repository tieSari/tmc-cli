import fi.helsinki.cs.tmc.edutestutils.Points;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import java.util.ArrayList;

@Points("65")
public class SuurinTest {
    
    public ArrayList<Integer> a(int... i ) {
        ArrayList<Integer> al = new ArrayList<Integer>();
        for (int j : i) {
            al.add(j);
        }
        return al;
    }

    @Test
    public void testaaYhdenKokoisenListanSuurin() {
        assertEquals("Et laske listan [1] suurinta oikein.",1,Suurin.suurin(a(1)));
        assertEquals("Et laske listan [2] suurinta oikein.",2,Suurin.suurin(a(2)));
        assertEquals("Et laske listan [3] suurinta oikein.",3,Suurin.suurin(a(3)));
    }

    @Test
    public  void testaaIsommanListanSumma() {
        assertEquals("Et laske listan [1,2,3] suurinta oikein.",
                     3,Suurin.suurin(a(1,2,3)));
        assertEquals("Et laske listan [3,2,1] suurinta oikein.",
                     3,Suurin.suurin(a(3,2,1)));
        assertEquals("Et laske listan [2,2,2,2,2,2,2] suurinta oikein.",
                     2,Suurin.suurin(a(2,2,2,2,2,2,2)));
        assertEquals("Et laske listan [-1,1,-2,2,-3,3] suurinta oikein.",
                     3,Suurin.suurin(a(-1,1,-2,2,-3,3)));
        assertEquals("Et laske listan [-2,-1,-3] suurinta oikein.",
                     -1,Suurin.suurin(a(-2,-1,-3)));
        assertEquals("Et laske listan [-9000,-9001] suurinta oikein.",
                     -9000,Suurin.suurin(a(-9000,-9001)));
    }

}
