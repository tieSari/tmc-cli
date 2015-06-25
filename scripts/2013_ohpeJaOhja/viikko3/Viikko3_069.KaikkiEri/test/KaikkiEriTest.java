
import org.junit.Test;
import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.ArrayList;
import java.util.Arrays;

@Points("69")
public class KaikkiEriTest {

    public void t0(ArrayList<Integer> luvut) {
        ArrayList<Integer> pre = new ArrayList<Integer>(luvut);
        assertFalse("parametrin ollessa lista " + luvut + " tulisi palauttaa false", KaikkiEri.kaikkiEri(luvut));
        tarkistaListanMuokkaamattomuus(pre, luvut);
    }

    public void t1(ArrayList<Integer> luvut) {
        ArrayList<Integer> pre = new ArrayList<Integer>(luvut);
        assertTrue("parametrin ollessa lista " + luvut + " tulisi palauttaa true", KaikkiEri.kaikkiEri(luvut));
        tarkistaListanMuokkaamattomuus(pre, luvut);
    }
    
    public void tarkistaListanMuokkaamattomuus(ArrayList<Integer> pre, ArrayList<Integer> post) {
        assertTrue("Algoritmisi antaa oikean tuloksen, mutta muuttaa syötteenä annettavaa listaa! Lista oli ennen "
                + "suoritusta " + pre + " ja suorituksen jälkeen se oli " + post + ". Voit luultavasti korjata "
                + "ongelman luomalla kaikkiEri-metodissa kopion syötteenä annetusta listasta ja käyttämällä sitä.",
                pre.equals(post));
    }

    @Test
    public void yhdenOn() {
        t1(new ArrayList<Integer>(Arrays.asList(1)));
    }

    @Test
    public void kahdenOn() {
        t1(new ArrayList<Integer>(Arrays.asList(1, 2)));
    }

    @Test
    public void kahdenEi() {
        t0(new ArrayList<Integer>(Arrays.asList(5, 5)));
    }

    @Test
    public void viidenOn() {
        t1(new ArrayList<Integer>(Arrays.asList(1, 2, 5, 4, 3)));
    }

    @Test
    public void viidenEi1() {
        t0(new ArrayList<Integer>(Arrays.asList(1, 4, 5, 2, 1)));
    }

    @Test
    public void viidenEi2() {
        t0(new ArrayList<Integer>(Arrays.asList(6, 1, 5, 2, 1)));
    }

    @Test
    public void kuudenEi() {
        t0(new ArrayList<Integer>(Arrays.asList(6, 1, 5, 2, 5, 7)));
    }

    @Test
    public void kahdeksanOn() {
        t1(new ArrayList<Integer>(Arrays.asList(6, 1, 5, 2, 8, 7, 3, 10)));
    }

    @Test
    public void kahdeksanEi1() {
        t0(new ArrayList<Integer>(Arrays.asList(6, 1, 5, 2, 8, 6, 3, 10)));
    }

    @Test
    public void kahdeksanEi2() {
        t0(new ArrayList<Integer>(Arrays.asList(6, 1, 5, 2, 8, 10, 3, 6)));
    }

    @Test
    public void kahdeksanEi3() {
        t0(new ArrayList<Integer>(Arrays.asList(5, 1, 6, 2, 8, 7, 3, 6)));
    }
    
    @Test
    public void yhdenksanEi1() {
        t0(new ArrayList<Integer>(Arrays.asList(11,6, 1, 5, 2, 8, 6, 3, 7)));
    }

    @Test
    public void yhdeksanEi2() {
        t0(new ArrayList<Integer>(Arrays.asList(5, 1, 7, 2, 8, 11, 3, 6, 6)));
    }
}