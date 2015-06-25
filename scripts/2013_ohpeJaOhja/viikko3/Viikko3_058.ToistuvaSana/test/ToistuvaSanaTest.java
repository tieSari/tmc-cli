
import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.NoSuchElementException;
import org.junit.*;
import static org.junit.Assert.*;

@Points("58")
public class ToistuvaSanaTest {

    @Rule
    public MockStdio io = new MockStdio();

    @Test
    public void testi1() {
        String[] sanat = {"Java", "Java"};        
        testaa(sanat);
    }
    
    @Test
    public void testi2() {
        String[] sanat =  {"Ohjelmointi", "Matematiikka", "Ohjelmointi"};       
        testaa(sanat);
    }
    
    @Test
    public void testi3() {
        String[] sanat = {"Kent Beck", "Alan Turing", "Arto Vihavainen", "Ken Thompson", "Bill Gates", "Kent Beck" };       
        testaa(sanat);
    }
    
    @Test
    public void testi4() {
        String[] sanat = {"while", "if", "muuttuja", "print", "metodi", "sijoitus", "olio", "luokka", "lista", "taulukko", "pino", "puu", "metodi"};      
        testaa(sanat);
    }    

    private void testaa(String[] sanat) {
                String toisto = sanat[sanat.length-1];
                
        io.setSysIn(toS(sanat));
        try {
            ToistuvaSana.main(new String[0]);
        } catch (NoSuchElementException e) {
            fail("syötteellä " + toS2(sanat) + " pitäisi tulostaa \"Annoit uudestaan sanan "+toisto+"\"");
        }
        
        String out = io.getSysOut();
        assertTrue("syötteellä " + toS2(sanat) + " pitäisi tulostaa \"Annoit uudestaan sanan "+toisto+"\"", out.contains(toisto));
    }

    private static String toS(String[] sanat) {
        String s = "";

        for (String sana : sanat) {
            s += sana + "\n";
        }

        return s;
    }

    private static String toS2(String[] sanat) {
        String s = "";

        for (String sana : sanat) {
            s += sana + ", ";
        }

        return s.substring(0, s.length() - 2);
    }
}
