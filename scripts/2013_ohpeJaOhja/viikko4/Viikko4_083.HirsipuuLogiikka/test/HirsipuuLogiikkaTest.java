
import fi.helsinki.cs.tmc.edutestutils.Points;
import static org.junit.Assert.*;
import org.junit.Test;

public class HirsipuuLogiikkaTest {

    @Test
    public void testVirheidenLukumaaraAluksiNolla() {
        HirsipuuLogiikka instance = new HirsipuuLogiikka("PORKKANA");
        assertEquals("Virheiden lukumäärän tulisi olla aluksi 0", 0, instance.virheidenLukumaara());
    }

    @Test
    public void testArvatutKirjaimetAluksiTyhja() {
        HirsipuuLogiikka instance = new HirsipuuLogiikka("PORKKANA");
        assertEquals("Arvattujen kirjainten tulee olla aluksi tyhjä.", "", instance.arvatutKirjaimet());
    }

    @Test
    @Points("83.1")
    public void testArvaaKirjainOikeallaVaarienMaaraEiKasva() {
        String kirjain = "O";
        HirsipuuLogiikka instance = new HirsipuuLogiikka("MOOC");
        instance.arvaaKirjain(kirjain);
        assertTrue("Kun arvaa oikein, väärien arvausten määrän ei tule kasvaa.", instance.virheidenLukumaara() == 0);
    }

    @Test
    @Points("83.1")
    public void testArvaaKirjainVaarallaVaarienMaaraKasvaa() {
        String kirjain = "A";
        HirsipuuLogiikka instance = new HirsipuuLogiikka("MOOC");
        instance.arvaaKirjain(kirjain);
        assertTrue("Kun arvaa väärin, väärien arvausten määrän tulee kasvaa yhdellä.", instance.virheidenLukumaara() == 1);
    }

    @Test
    @Points("83.1")
    public void testArvaaKirjainArvattuLisataanArvattuihin() {
        String kirjain = "A";
        HirsipuuLogiikka instance = new HirsipuuLogiikka("MOOC");
        instance.arvaaKirjain(kirjain);
        assertTrue("Arvattu kirjain tulee lisätä arvattuihin kirjaimiin.", instance.arvatutKirjaimet().equals(kirjain));
    }

    @Test
    @Points("83.1")
    public void testArvaaKirjainArvattuLisataanArvattuihinVaikkaOikein() {
        String kirjain = "O";
        HirsipuuLogiikka instance = new HirsipuuLogiikka("MOOC");
        instance.arvaaKirjain(kirjain);
        assertTrue("Arvattu kirjain tulee lisätä arvattuihin kirjaimiin.", instance.arvatutKirjaimet().equals(kirjain));
    }

    @Test
    @Points("83.1")
    public void testArvaaKirjainArvattuLisataanArvattuihinVainKerran() {
        String kirjain = "A";
        HirsipuuLogiikka instance = new HirsipuuLogiikka("MOOC");
        instance.arvaaKirjain(kirjain);
        instance.arvaaKirjain(kirjain);
        assertTrue("Sama kirjain tulee lisätä arvattuihin kirjaimiin vain kerran.", instance.arvatutKirjaimet().equals(kirjain));
    }

    @Test
    @Points("83.1")
    public void testVirheidenMaaraEiKasvaKunArvataanJoArvattuaSanaa() {
        String kirjain = "A";
        HirsipuuLogiikka instance = new HirsipuuLogiikka("MOOC");
        instance.arvaaKirjain(kirjain);
        instance.arvaaKirjain(kirjain);
        assertTrue("Väärien arvausten määrän ei tule kasvaa kun arvataan jo arvattua kirjainta.", instance.virheidenLukumaara() == 1);
    }

    @Test
    @Points("83.1")
    public void testArvatutKasvaaKunArvataanKaksiKirjainta() {
        HirsipuuLogiikka instance = new HirsipuuLogiikka("MOOC");
        instance.arvaaKirjain("A");
        instance.arvaaKirjain("B");

        boolean ok = instance.arvatutKirjaimet().length() == 2
                && instance.arvatutKirjaimet().contains("A")
                && instance.arvatutKirjaimet().contains("B");

        assertTrue("Arvattujen kirjainten pitäisi sisältää kaksi kirjainta kun arvattu kahdella kirjaimella.", ok);
    }

    @Test
    @Points("83.2")
    public void testAnnaSalattuSanaPitka() {
        HirsipuuLogiikka instance = new HirsipuuLogiikka("PORKKANA");
        String expResult = "________";
        String result = instance.salattuSana().replaceAll("\\s+", "");
        assertEquals("Kahdeksan merkin pituisen sanan pitäisi olla salattuna ________", expResult, result);
    }

    @Test
    @Points("83.2")
    public void testAnnaSalattuSanaLyhyt() {
        HirsipuuLogiikka instance = new HirsipuuLogiikka("MOOC");
        String expResult = "____";
        String result = instance.salattuSana().replaceAll("\\s+", "");
        assertEquals("Neljän merkin pituisen sanan pitäisi olla salattuna ____", expResult, result);
    }

    @Test
    @Points("83.2")
    public void testSalattuSanaMuuttuuKunArvattuOikein() {
        HirsipuuLogiikka instance = new HirsipuuLogiikka("MOOC");
        instance.arvaaKirjain("O");
        String expResult = "_OO_";
        String result = instance.salattuSana().replaceAll("\\s+", "");
        assertEquals("Kun sanasta MOOC on arvattu kirjain O, tulisi salatun sanan olla _OO_.", expResult, result);
    }
}
