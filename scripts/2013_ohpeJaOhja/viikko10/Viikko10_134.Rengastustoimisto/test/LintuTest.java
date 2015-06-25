
import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

@Points("134.1")
public class LintuTest {
    
    @Test
    public void LinnunEquals() {
        testaaEquals("Varis", "Corvus corone cornix", 1952, "Varis", "Corvus corone cornix", 1952, true);
        testaaEquals("Nebelkrähe", "Corvus corone cornix", 1952, "Varis", "Corvus corone cornix", 1952, true);
        testaaEquals("Lokki", "Larus", 2011, "Lokki", "Larus", 2011, true);
        testaaEquals("Lokki", "Larus", 2011, "Seagul", "Larus", 2011, true);
        testaaEquals("Lokki", "Larus", 2012, "Lokki", "Larus", 2011, false);
        testaaEquals("Lokki", "Larus", 2012, "Lokki", "Laruc", 2012, false);
        testaaEqualsParametrinTyyppi("Varis", "Corvus corone cornix", 1952, "Varis", "Corvus corone cornix", 1952, true);
        testaaEqualsParametrinTyyppi("Nebelkrähe", "Corvus corone cornix", 1952, "Varis", "Corvus corone cornix", 1952, true);
    }
    
    @Test
    public void LinnunHashCode() {
        testaaHash("Varis", "Corvus corone cornix", 1952, "Varis", "Corvus corone cornix", 1952);
        testaaHash("Nebelkrähe", "Corvus corone cornix", 1952, "Varis", "Corvus corone cornix", 1952);
        testaaHash("Lokki", "Larus", 2011, "Lokki", "Larus", 2011);
        testaaHash("Lokki", "Larus", 2011, "Seagul", "Larus", 2011);
        Lintu r1 = new Lintu("Nebelkrähe", "Corvus corone cornix", 1952);
        Lintu r2 = new Lintu("Seagul", "Larus", 2011);
        Lintu r3 = new Lintu("rusokottarainen", "Sturnus roseus", 2012);
        assertFalse("metodi hashCode näyttää palauttavan kaikille linnuille saman arvon " + r1.hashCode(),
                r1.hashCode() == r2.hashCode() && r2.hashCode() == r3.hashCode());
        
    }
    
    private void testaaEquals(String m1, String b1, int v1, String m2, String b2, int v2, boolean vast) {
        Lintu rb1 = new Lintu(m1, b1, v1);
        Lintu rb2 = new Lintu(m2, b2, v2);
        
        String v = "Lintu b1 = new Lintu(\"" + m1 + "\", \"" + b1 + "\", " + v1 + ");\n"
                + "Lintu b2 = new Lintu(\"" + m2 + "\", \"" + b2 + "\", " + v2 + ");\n"
                + "b1.equals(b2)";
        assertEquals(v, vast, rb1.equals(rb2));        
    }
    
    private void testaaEqualsParametrinTyyppi(String m1, String b1, int v1, String m2, String b2, int v2, boolean vast) {
        Object rb1 = new Lintu(m1, b1, v1);
        Object rb2 = new Lintu(m2, b2, v2);
        
        String v = "Luokan metodin equals parametrin tulee olla Object-tyyppinen!\n\n"
                + "Object b1 = new Lintu(\"" + m1 + "\", \"" + b1 + "\", " + v1 + ");\n"
                + "Object b2 = new Lintu(\"" + m2 + "\", \"" + b2 + "\", " + v2 + ");\n"
                + "b1.equals(b2)";
        assertEquals(v, vast, rb1.equals(rb2));        
    }    
    
    private void testaaHash(String m1, String b1, int v1, String m2, String b2, int v2) {
        Lintu rb1 = new Lintu(m1, b1, v1);
        Lintu rb2 = new Lintu(m2, b2, v2);
        
        String v = "Lintu b1 = new Lintu(\"" + m1 + "\", \"" + b1 + "\", " + v1 + ");\n"
                + "Lintu b2 = new Lintu(\"" + m2 + "\", \"" + b2 + "\", " + v2 + ");\n"
                + "b1.hashCode() == b2.HashCode()";
        assertEquals(v, true, rb1.hashCode() == rb2.hashCode());
    }
}
