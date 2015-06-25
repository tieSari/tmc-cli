
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.*;
import static org.junit.Assert.*;

@Points("71")
public class JoukkoYhdistaminenTest {

    Method m;

    @Before
    public void haeMetodi() {
        try {
            m = ReflectionUtils.requireMethod(JoukkoYhdistaminen.class, "joukkoYhdista", ArrayList.class, ArrayList.class);
        } catch (Throwable t) {
            fail("Et ole lisännyt metodia public static void joukkoYhdista(ArrayList<Integer> lista1, ArrayList<Integer> lista2)! Lisätietoja: " + t);
        }
        assertTrue("Et ole määritellyt metodia joukkoYhdista() staattiseksi!", m.toString().contains("static"));
        assertTrue("Et ole määritellyt metodia joukkoYhdista() void-tyyppiseksi!", m.toString().contains("void"));
    }

    public void tarkistaLista(ArrayList<Integer> lista1, ArrayList<Integer> lista2) {

        //Debuggausta varten
        ArrayList<Integer> origLista1 = new ArrayList<Integer>(lista1);
        ArrayList<Integer> origLista2 = new ArrayList<Integer>(lista2);
        Object[] params = {lista1, lista2};
        try {
            ReflectionUtils.invokeMethod(Void.TYPE, m, null, params);
        } catch (Throwable ex) {
            Logger.getLogger(JoukkoYhdistaminen.class.getName()).log(Level.SEVERE, null, ex);
        }

        ArrayList<Integer> yhdiste = new ArrayList<Integer>(lista1);
       
        ArrayList<Integer> yKopy = new ArrayList<Integer>(yhdiste);
        Collections.sort(yKopy);
        for (int i=0; i<yKopy.size()-1; i++) {
            if ( yKopy.get(i)==yKopy.get(i+1))
                 fail("Listojen " + origLista1 + " ja " + origLista2 + " yhdisteestä kahteen kertaan luku " + yKopy.get(i) + ".");
        }
        
        for (int luku : origLista1) {
            if (!yhdiste.contains(luku)) {

                fail("Listojen " + origLista1 + " ja " + origLista2 + " yhdisteestä puuttuu luku " + luku + ".");
            }
            yhdiste.remove(Integer.valueOf(luku));
            while( origLista2.contains(luku)) {
                origLista2.remove(Integer.valueOf(luku));
            }
        }

        for (int luku : origLista2) {
            if (!yhdiste.contains(luku)) {

                fail("Listojen " + origLista1 + " ja " + origLista2 + " yhdisteestä puuttuu luku " + luku + ".");
            }
            yhdiste.remove(Integer.valueOf(luku));
        }

        if (!yhdiste.isEmpty()) {
            fail("Listojen " + origLista1 + " ja " + origLista2 + " yhdisteessä seuraava(t) sinne kuulumattomat luvut " + yhdiste + ".");
        }

    }

    public void tarkistaLista2(ArrayList<Integer> lista1, ArrayList<Integer> lista2) {
        ArrayList<Integer> origLista1 = new ArrayList<Integer>(lista1);
        ArrayList<Integer> origLista2 = new ArrayList<Integer>(lista2);
        Object[] params = {lista1, lista2};

        try {
            ReflectionUtils.invokeMethod(Void.TYPE, m, this, params);
        } catch (Throwable ex) {
            Logger.getLogger(JoukkoYhdistaminenTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        ArrayList<Integer> yhdiste = new ArrayList<Integer>(lista1);
        lista1 = new ArrayList<Integer>(origLista1);

        //Tarkistetaan että lista sisältää kaikki alkiot
        for (Integer i : lista1) {
            if (!yhdiste.contains(i)) {
                fail("Listojen " + origLista1 + " ja " + origLista2 + " joukkoyhdiste ei sisältänyt lista1:n alkiota " + i);
            }
        }
        for (Integer i : lista2) {
            if (!yhdiste.contains(i)) {
                fail("Listojen " + origLista1 + " ja " + origLista2 + " joukkoyhdiste ei sisältänyt lista2:n alkiota " + i);
            }
        }
        //Tarkistetaan ettei lista sisällä duplikaatteja
        HashSet<Integer> apusetti = new HashSet<Integer>();
        for (Integer i : yhdiste) {
            if (!apusetti.add(i)) {
                fail("Listojen " + origLista1 + " ja " + origLista2 + " joukkoyhdiste sisälsi alkion " + i + " useamman kuin yhden kerran.");
            }
        }
    }

    @Test
    public void pienetListat1Test() {
        ArrayList<Integer> lista1 = new ArrayList<Integer>();
        lista1.add(10);
        lista1.add(11);

        ArrayList<Integer> lista2 = new ArrayList<Integer>();
        lista2.add(5);        

        tarkistaLista(lista1, lista2);
    }

    @Test
    public void pienetListat2Test() {
        ArrayList<Integer> lista1 = new ArrayList<Integer>();
        lista1.add(10);
        lista1.add(11);

        ArrayList<Integer> lista2 = new ArrayList<Integer>();
        lista2.add(5);
        lista2.add(11);

        tarkistaLista(lista1, lista2);
    }       
    
    @Test
    public void isommatListatTest() {
        ArrayList<Integer> lista1 = new ArrayList<Integer>();
        lista1.add(5);
        lista1.add(1);
        lista1.add(2);

        ArrayList<Integer> lista2 = new ArrayList<Integer>();
        lista2.add(5);
        lista2.add(1);
        lista2.add(2);
        lista2.add(1);
        lista2.add(40);

        tarkistaLista(lista1, lista2);
    }

    @Test
    public void erisuuretListatTest() {
        ArrayList<Integer> lista1 = new ArrayList<Integer>();
        lista1.add(10);
        lista1.add(11);
        lista1.add(12);
        lista1.add(13);

        ArrayList<Integer> lista2 = new ArrayList<Integer>();
        lista2.add(5);
        lista2.add(6);
        lista2.add(7);
        lista2.add(8);
        lista2.add(9);
        lista2.add(10);
        lista2.add(11);
        lista2.add(12);

        tarkistaLista(lista1, lista2);
    }

    @Test
    public void toinenListaTyhjaTest() {
        ArrayList<Integer> lista1 = new ArrayList<Integer>();
        lista1.add(10);
        lista1.add(11);
        lista1.add(12);
        lista1.add(13);

        ArrayList<Integer> lista2 = new ArrayList<Integer>();

        tarkistaLista(lista1, lista2);
    }

    @Test
    public void molemmatListatTyhjiaTest() {
        ArrayList<Integer> lista1 = new ArrayList<Integer>();

        ArrayList<Integer> lista2 = new ArrayList<Integer>();

        tarkistaLista(lista1, lista2);
    }
}
