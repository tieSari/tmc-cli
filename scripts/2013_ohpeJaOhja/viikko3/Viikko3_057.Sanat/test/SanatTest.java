import fi.helsinki.cs.tmc.edutestutils.MockStdio;
import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.*;
import static org.junit.Assert.*;

@Points("57")
public class SanatTest {
    @Rule
    public MockStdio io = new MockStdio();
    
    @Test
    public void testi1() {
        String[] sanat = {"Java"};
        io.setSysIn(toS(sanat) + "\n");
        Sanat.main(new String[0]);
        String out = io.getSysOut();
        for (String sana : sanat) {
            assertTrue("syötteellä "+toS2(sanat)+" pitäisi tulostaa "+sana,out.contains(sana));
        }    
    }
    
    @Test
    public void testi2() {
        String[] sanat = {"Ohjelmointi", "Matematiikka"};
        io.setSysIn(toS(sanat) + "\n");
        Sanat.main(new String[0]);
        String out = io.getSysOut();
        for (String sana : sanat) {
            assertTrue("syötteellä "+toS2(sanat)+" pitäisi tulostaa "+sana,out.contains(sana));
        }    
    }    
    
    @Test
    public void testi3() {
        String[] sanat = {"Kent Beck", "Alan Turing", "Arto Vihavainen", "Ken Thompson", "Bill Gates" };
        io.setSysIn(toS(sanat) + "\n");
        Sanat.main(new String[0]);
        String out = io.getSysOut();
        for (String sana : sanat) {
            assertTrue("syötteellä "+toS2(sanat)+" pitäisi tulostaa "+sana,out.contains(sana));
        }    
    }  
    
    @Test
    public void testi4() {
        String[] sanat = {"while", "if", "muuttuja", "print", "metodi", "sijoitus", "olio", "luokka", "lista", "taulukko", "pino", "puu"};
        io.setSysIn(toS(sanat) + "\n");
        Sanat.main(new String[0]);
        String out = io.getSysOut();
        for (String sana : sanat) {
            assertTrue("syötteellä "+toS2(sanat)+" pitäisi tulostaa "+sana,out.contains(sana));
        }   
        assertTrue("syötteellä "+toS2(sanat)+" pitäisi tulostua "+sanat.length+" riviä.", out.replaceAll("\r\n", "\n").split("\n").length >= sanat.length);
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
        
        return s+"\"\"";
    }    
}
