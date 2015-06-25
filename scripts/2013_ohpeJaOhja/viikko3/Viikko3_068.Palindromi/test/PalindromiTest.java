import org.junit.Test;
import static org.junit.Assert.*;
import fi.helsinki.cs.tmc.edutestutils.Points;

@Points("68")
public class PalindromiTest {
    public void t0(String s) {
        assertFalse("Merkkijono \""+s+"\" ei ole palindromi! Ohjelmasi väitti että on!",
                    Palindromi.palindromi(s));
    }

    public void t1(String s) {
        assertTrue("Merkkijono \""+s+"\" on palindromi! Ohjelmasi väitti että ei ole!",
                   Palindromi.palindromi(s));
    }

    @Test
    public void tyhjaOn() {
        t1("");
    }

    @Test
    public void yhdenMittainenOn() {
        t1("a");
    }

    @Test
    public void kaksiEriKirjaintaEi() {
        t0("ax");
    }

    @Test
    public void ekaJaVikaSama() {
        t0("axda");
    }

    @Test
    public void abcEiOle() {
        t0("abc");
    }

    @Test
    public void abaOn() {
        t1("aba");
    }

    @Test
    public void parillisenPituinenJokaOn() {
        t1("abccba");
    }

    @Test
    public void parillisenPituinenJokaEiOle() {
        t0("abcc");
    }

    @Test
    public void toinenParillisenPituinenJokaEiOle() {
        t0("abca");
    }

    @Test
    public void saippuakauppiasOn() {
        t1("saippuakauppias");
    }
}