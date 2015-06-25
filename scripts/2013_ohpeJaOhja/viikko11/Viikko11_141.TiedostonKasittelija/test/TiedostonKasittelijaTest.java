
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TiedostonKasittelijaTest {

    Random arpa = new Random();

    @Points("141.1")
    @Test
    public void lukeeRivit1() throws FileNotFoundException {
        ArrayList<String> tekstit = new ArrayList<String>();
        tekstit.add("eka");
        tekstit.add("toka");
        rivienLuku(tekstit, "src/koesyote1.txt", "test/tmp/koesyote1.txt");
    }

    @Points("141.1")
    @Test
    public void lukeeRivit2() throws FileNotFoundException {
        ArrayList<String> tekstit = new ArrayList<String>();
        tekstit.add("yy");
        tekstit.add("kaa");
        tekstit.add("koo nee vii");
        rivienLuku(tekstit, "src/koesyote2.txt", "test/tmp/koesyote2.txt");
    }

    @Points("141.2")
    @Test
    public void tallentaaRivin1() throws FileNotFoundException, IOException {
        rivinTallennus(new TiedostonKasittelija(), "eka koerivi");
    }

    @Points("141.2")
    @Test
    public void tallentaaRivin2() throws FileNotFoundException, IOException {
        rivinTallennus(new TiedostonKasittelija(), "toinen koerivi");
    }

    @Points("141.2")
    @Test
    public void tallentaaRivin3() throws FileNotFoundException, IOException {
        rivinTallennus(new TiedostonKasittelija(), "kolmas koerivi");
    }

    @Points("141.3")
    @Test
    public void tallentaaRivit1() throws FileNotFoundException, IOException {
        ArrayList<String> tekstit = new ArrayList<String>();
        tekstit.add("eins");
        tekstit.add("zwei");
        tekstit.add("drei");

        rivienTallennus(new TiedostonKasittelija(), tekstit);
    }

    @Points("141.3")
    @Test
    public void tallentaaRivit2() throws FileNotFoundException, IOException {
        ArrayList<String> tekstit = new ArrayList<String>();
        tekstit.add("yy");
        tekstit.add("kaa");
        tekstit.add("koo");
        tekstit.add("nee");
        tekstit.add("vii");

        rivienTallennus(new TiedostonKasittelija(), tekstit);
    }

    private List<String> lue(String tiedosto) throws FileNotFoundException {
        Scanner s = new Scanner(new File(tiedosto));
        ArrayList<String> rivit = new ArrayList<String>();

        while (s.hasNextLine()) {
            rivit.add(s.nextLine());
        }
        return rivit;
    }

    private void rivinTallennus(TiedostonKasittelija t, String teksti) throws IOException, FileNotFoundException {
        int arvottu = arpa.nextInt(100000);
        String tdsto = "test/tmp/tmp" + arvottu + ".txt";
        t.tallenna(tdsto, teksti);

        File tied = new File(tdsto);

        String k = "TiedostonKasittelija t = new TiedostonKasittelija();\n"
                + "t.tallenna(\"" + tdsto + "\"," + teksti + ");\n";

        assertTrue("Tiedostoa ei luoda kun suoritetaan koodi\n" + k, tied.exists());

        List<String> rivit = lue(tdsto);
        tied.delete();
        assertEquals("Luodussa tiedostossa väärä määrä rivejä kun suoritetaan koodi\n"
                + k, 1, rivit.size());
        assertEquals("Tiedoston sisältö ei oikea kun suoritetaan koodi\n"
                + k, teksti, rivit.get(0));

    }

    private void rivienTallennus(TiedostonKasittelija t, ArrayList<String> tekstit) throws IOException, FileNotFoundException {
        int arvottu = arpa.nextInt(100000);
        String tdsto = "test/tmp/tmp" + arvottu + ".txt";
        t.tallenna(tdsto, tekstit);

        File tied = new File(tdsto);

        String k = "TiedostonKasittelija t = new TiedostonKasittelija();\n"
                + "List<String> tekstit = new ArrayList<String>();\n";

        for (String teksti : tekstit) {
            k += "tekstit.add(\"" + teksti + "\");\n";
        }

        k += "t.tallenna(\"" + tdsto + "\",tekstit);\n";

        assertTrue("Tiedostoa ei luoda kun suoritetaan koodi\n" + k, tied.exists());

        List<String> rivit = lue(tdsto);
        tied.delete();

        assertEquals("Luodussa tiedostossa väärä määrä rivejä kun suoritetaan koodi\n"
                + k, tekstit.size(), rivit.size());
        assertEquals("Tiedoston sisältö ei oikea kun suoritetaan koodi\n"
                + k, tekstit, rivit);
    }

    private void rivienLuku(ArrayList<String> tekstit, String tiedosto, String td) throws FileNotFoundException {
        String sisalto = "";
        for (String teksti : tekstit) {
            sisalto += teksti + "\n";
        }

        TiedostonKasittelija t = new TiedostonKasittelija();
        ArrayList<String> luettu = t.lue(td);

        String k = "Tiedostonkasittelija t = new Tiedostonkasittelija();\n"
                + "t.lue(\"" + tiedosto + "\");";

        assertEquals("Tiedoston " + tiedosto + "sisältö:\n" + sisalto + "\n"
                + "Luettujen rivien määrä väärä koodilla\n"
                + k, tekstit.size(), luettu.size());

        assertEquals("Tiedoston " + tiedosto + "sisältö:\n" + sisalto + "\n"
                + "Palautettu lista väärä koodilla\n"
                + k, tekstit, luettu);
    }
}
