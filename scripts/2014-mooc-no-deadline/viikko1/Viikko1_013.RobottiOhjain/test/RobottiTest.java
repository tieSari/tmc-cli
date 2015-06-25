
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import java.awt.Point;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.junit.*;
import robotti.Ohjain;

@Points("13")
public class RobottiTest {

    @Test
    public void ohjainKaynnistetaanAlussaJaSammutetaanLopussa() throws Exception {
        ReflectionUtils.newInstanceOfClass(Paaohjelma.class);
        ReflectionUtils.newInstanceOfClass(Ohjain.class);
        testMode();
        Paaohjelma.main(new String[]{});
        List<String> komennot = getKomennot();

        Assert.assertNotNull("Varmista, että robotti sammutetaan lopuksi komennolla \"Ohjain.sammuta();\".", komennot);
        Assert.assertTrue("Kun ohjelma suoritettiin, komentoja annettiin " + komennot.size() + ". Robotti tulee ainakin käynnistää ja sammuttaa.", komennot.size() >= 2);

        Assert.assertTrue("Ensimmäisen ohjaimelle annettavan komennon tulee olla \"Ohjain.kaynnista();\".", komennot.get(0).equals("kaynnista"));
        Assert.assertTrue("Viimeisen ohjaimelle annettavan komennon tulee olla \"Ohjain.sammuta();\".", komennot.get(komennot.size() - 1).equals("sammuta"));

        int kaynnistyksia = 0;
        int sammutuksia = 0;
        for (String komento : komennot) {
            if (komento.equals("kaynnista")) {
                kaynnistyksia++;
            }
            if (komento.equals("sammuta")) {
                sammutuksia++;
            }
        }

        Assert.assertTrue("Ylimääräiset käynnistykset ja sammutukset vievät sähköä!\nKäynnistä robotti vain alussa ja sammuta se lopussa!", kaynnistyksia == 1 && sammutuksia == 1);
    }

    @Test
    public void piirretaanIsoSuorakulmio() throws Exception {
        ohjainKaynnistetaanAlussaJaSammutetaanLopussa();
        List<Point> sijainnit = getSijainnit();
        Assert.assertTrue("Robotti tarvitsee isomman suorakulmion kulkemiseen ainakin 24 askelta.", sijainnit.size() >= 24);
        sijainnit = new ArrayList<Point>(new HashSet<Point>(sijainnit));
        sijainnit.retainAll(isoSuorakulmio());
        Assert.assertTrue("Varmista, että robotin kulkeman isomman suorakulmion korkeus on 7 askelta ja leveys 5 askelta.", sijainnit.size() == isoSuorakulmio().size());
        Assert.assertTrue("Varmista, että robotin kulkeman isomman suorakulmion korkeus on 5 askelta ja leveys 7 askelta.", sijainnit.size() == isoSuorakulmio().size());
    }


    @Test
    public void piirretaanPieniSuorakulmio() throws Exception {
        piirretaanIsoSuorakulmio();

        List<Point> sijainnit = getSijainnit();

        sijainnit = new ArrayList<Point>(new HashSet<Point>(sijainnit));
        sijainnit.retainAll(pieniSuorakulmio());

        Assert.assertTrue("Varmista, että robotin kulkeman pienemmän suorakulmion korkeus on 2 askelta ja leveys 3 askelta, ja että pieni suorakulmio on isomman suorakulmion vasemmassa yläkulmassa.", sijainnit.size() == pieniSuorakulmio().size());
    }

    @Test
    public void piirretaanVainSuorakulmiot() throws Exception {
        piirretaanPieniSuorakulmio();

        List<Point> sijainnit = getSijainnit();

        sijainnit = new ArrayList<Point>(new HashSet<Point>(sijainnit));
        sijainnit.removeAll(pieniSuorakulmio());
        sijainnit.removeAll(isoSuorakulmio());

        for (Point point : sijainnit) {
            System.out.println(point);
        }

        Assert.assertTrue("Varmista, että robotti tekee vain pienen ja ison suorakulmion.", sijainnit.isEmpty());
    }

    @Test
    public void lopetetaanVasempaanYlakulmaan() throws Exception {
        piirretaanVainSuorakulmiot();

        List<Point> sijainnit = getSijainnit();
        Point vasenYlakulma = new Point(1, 6);
        Assert.assertTrue("Varmista, että robotti pysähtyy suorakulmioiden vasempaan yläkulmaan.", sijainnit.get(sijainnit.size() - 1).equals(vasenYlakulma));
    }

    private List<Point> isoSuorakulmio() {
        List<Point> suorakulmio = new ArrayList<Point>();
        for (int i = 1; i <= 8; i++) {
            suorakulmio.add(new Point(i, 1));
            suorakulmio.add(new Point(i, 6));
        }

        for (int i = 1; i <= 5; i++) {
            suorakulmio.add(new Point(1, i));
            suorakulmio.add(new Point(8, i));
        }

        suorakulmio = new ArrayList<Point>(new HashSet<Point>(suorakulmio));
        return suorakulmio;
    }

    private List<Point> pieniSuorakulmio() {
        List<Point> suorakulmio = new ArrayList<Point>();
        for (int i = 1; i <= 3; i++) {
            suorakulmio.add(new Point(i, 4));
            suorakulmio.add(new Point(i, 6));
        }

        for (int i = 4; i <= 5; i++) {
            suorakulmio.add(new Point(1, i));
            suorakulmio.add(new Point(4, i));
        }

        suorakulmio = new ArrayList<Point>(new HashSet<Point>(suorakulmio));
        return suorakulmio;
    }

    private List<Point> getSijainnit() {
        try {
            Method m = Ohjain.class.getDeclaredMethod("getSijainnit");
            m.setAccessible(true);
            return (List<Point>) m.invoke(null);
        } catch (Throwable e) {
        }
        return null;
    }

    private List<String> getKomennot() {
        try {
            Method m = Ohjain.class.getDeclaredMethod("getKomennot");
            m.setAccessible(true);
            return (List<String>) m.invoke(null);
        } catch (Throwable e) {
        }
        return null;
    }

    private void testMode() {
        try {
            Method m = Ohjain.class.getDeclaredMethod("testMode");
            m.setAccessible(true);
            m.invoke(null);
        } catch (Throwable e) {
            throw new Error("Jotain meni pieleen!", e);
        }
    }
}
