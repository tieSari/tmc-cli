import fi.helsinki.cs.tmc.edutestutils.Points;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import java.util.ArrayList;

@Points("63")
public class LukujenSummaTest {
    public ArrayList<Integer> a(int... i ) {
        ArrayList<Integer> al = new ArrayList<Integer>();
        for (int j : i) {
            al.add(j);
        }
        return al;
    }

    @Test
    public void testaaYhdenKokoisenListanSumma() {
        assertEquals("Et laske listan [1] summaa oikein.",
                1, LukujenSumma.summa(a(1)));
        assertEquals("Et laske listan [2] summaa oikein.",
                2, LukujenSumma.summa(a(2)));
        assertEquals("Et laske listan [3] summaa oikein.",
                3, LukujenSumma.summa(a(3)));
    }

    @Test
    public  void testaaIsommanListanSumma() {
        assertEquals("Et laske listan [1,2,3] summaa oikein.",
                6, LukujenSumma.summa(a(1, 2, 3)));
        assertEquals("Et laske listan [2,2,2,2,2,2,2] summaa oikein.",
                14, LukujenSumma.summa(a(2, 2, 2, 2, 2, 2, 2)));
        assertEquals("Et laske listan [-1,1,-2,2,-3,3] summaa oikein.",
                0, LukujenSumma.summa(a(-1, 1, -2, 2, -3, 3)));
    }
}
