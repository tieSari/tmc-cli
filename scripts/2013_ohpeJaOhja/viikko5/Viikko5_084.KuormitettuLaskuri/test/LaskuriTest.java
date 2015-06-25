import fi.helsinki.cs.tmc.edutestutils.Points;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LaskuriTest {

    String name = "Laskuri";

    Class l;

    MethodSignature arvo = new MethodSignature(Integer.TYPE, "arvo");
    MethodSignature lisaa = new MethodSignature(Void.TYPE, "lisaa");
    MethodSignature vahenna = new MethodSignature(Void.TYPE, "vahenna");

    MethodSignature lisaa2 = new MethodSignature(Void.TYPE, "lisaa", Integer.TYPE);
    MethodSignature vahenna2 = new MethodSignature(Void.TYPE, "vahenna", Integer.TYPE);

    ConstructorSignature cib = new ConstructorSignature(Integer.TYPE, Boolean.TYPE);
    ConstructorSignature ci = new ConstructorSignature(Integer.TYPE);
    ConstructorSignature cb = new ConstructorSignature(Boolean.TYPE);
    ConstructorSignature c = new ConstructorSignature();

    @Before
    public void haeLuokka() {
        l = Utils.getClass(name);
    }

    @Points("84.1")
    @Test
    public void testaaKonstruointi() {
        cib.invokeIn(l, 10, true);
        cib.invokeIn(l, 2, false);
    }

    @Points("84.1")
    @Test
    public void testaaArvo() {

        Object o = cib.invokeIn(l,10,false);
        Integer i = -9000;
        i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(10, false);\nSystem.out.println(l.arvo());\n",
                     10,(int)i);

        o = cib.invokeIn(l,2,true);
        i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(2, true);\nSystem.out.println(l.arvo());\n",
                     2,(int)i);

    }

    @Points("84.1")
    @Test public void testaaLisaa() {

        Object o = cib.invokeIn(l,5,false);
        lisaa.invokeIn(l,o);
        int i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(5, false);\nl.lisaa();\nSystem.out.println(l.arvo());\n",
                     6,(int)i);

        
        lisaa.invokeIn(l,o);
        i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(5, false);\nl.lisaa();\nl.lisaa();\nSystem.out.println(l.arvo());\n",
                     7,(int)i);


    }

    @Points("84.1")
    @Test public void testaaVahenna() {

        Object o = cib.invokeIn(l,900,false);
        vahenna.invokeIn(l,o);
        int i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(900, false);\nl.vahenna();\nSystem.out.println(l.arvo());\n",
                     899,(int)i);

        
        vahenna.invokeIn(l,o);
        i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(900, false);\nl.vahenna();\nl.vahenna();\nSystem.out.println(l.arvo());\n",
                     898,(int)i);
    }

    @Points("84.1")
    @Test public void testaaVahennaEiTarkistusta() {
        Object o = cib.invokeIn(l,2,false);
        vahenna.invokeIn(l,o);
        int i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(2, false);\nl.vahenna();\nSystem.out.println(l.arvo());\n",
                     1,(int)i);

        
        vahenna.invokeIn(l,o);
        i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(2, false);\nl.vahenna();\nl.vahenna();\nSystem.out.println(l.arvo());\n",
                     0,(int)i);

        vahenna.invokeIn(l,o);
        i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(2, false);\nl.vahenna();\nl.vahenna();\nl.vahenna();\nSystem.out.println(l.arvo());\n",
                     -1,(int)i);

    }

    @Points("84.1")
    @Test public  void testaaVahennaTarkistus() {
        Object o = cib.invokeIn(l,2,true);
        vahenna.invokeIn(l,o);
        int i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(2, true);\nl.vahenna();\nSystem.out.println(l.arvo());\n",
                     1,(int)i);

        
        vahenna.invokeIn(l,o);
        i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(2, true);\nl.vahenna();\nl.vahenna();\nSystem.out.println(l.arvo());\n",
                     0,(int)i);

        vahenna.invokeIn(l,o);
        i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(2, true);\nl.vahenna();\nl.vahenna();\nl.vahenna();\nSystem.out.println(l.arvo());\n",
                     0,(int)i);
    }

    @Points("84.1")
    @Test
    public void testaaKonstruktorit() {

        Object o = ci.invokeIn(l, 11);
        int i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(11);\nSystem.out.println(l.arvo());\n",
                     11,i);

        o = c.invokeIn(l);
        i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri();\nSystem.out.println(l.arvo());\n",
                     0,i);

        o = cb.invokeIn(l,true);
        vahenna.invokeIn(l,o);
        i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(true);\nl.vahenna();\nSystem.out.println(l.arvo());\n",
                     0,i);

    }

    @Points("84.2")
    @Test public void testaaParametrillinenLisaa() {

        Object o = cib.invokeIn(l,5,false);
        lisaa2.invokeIn(l,o, 2);
        int i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. "
                + "Kokeile tätä:\nLaskuri l = new Laskuri(5, false);\nl.lisaa(2);\nSystem.out.println(l.arvo());\n",
                     7,(int)i);

        
        lisaa2.invokeIn(l,o,4);
        i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\n"
                + "Laskuri l = new Laskuri(5, false);\nl.lisaa(2);\nl.lisaa(4);\nSystem.out.println(l.arvo());\n",
                     11,(int)i);
    }
    
    @Points("84.2")
    @Test public void testaaParametrillinenLisaaNegatiivisella() {

        Object o = cib.invokeIn(l,5,false);
        lisaa2.invokeIn(l,o, -2);
        int i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Negatiivisen lisäyksen ei pitäisi muuttaa laskurin arvoa "
                + "Kokeile tätä:\nLaskuri l = new Laskuri(5, false);\nl.lisaa(-2);\nSystem.out.println(l.arvo());\n",
                     5,(int)i);
    }
    
    @Points("84.2")
    @Test public void testaaParametrillinenVahennys() {

        Object o = cib.invokeIn(l,900,false);
        vahenna2.invokeIn(l,o,7);
        int i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(900, false);\nl.vahenna(7);\nSystem.out.println(l.arvo());\n",
                     893,(int)i);

        
        vahenna2.invokeIn(l,o,3);
        i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(900, false);\nl.vahenna(7);\nl.vahenna(3);\nSystem.out.println(l.arvo());\n",
                     890,(int)i);
    }
    
    @Points("84.2")
    @Test public void testaaParametrillinenVahennysNegatiivisella() {

        Object o = cib.invokeIn(l,900,false);
        vahenna2.invokeIn(l,o,-55);
        int i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Negatiivisen vähennyksen ei pitäisi muuttaa laskurin arvoa "
                + " Kokeile tätä:\nLaskuri l = new Laskuri(900, false);\nl.vahenna(-55);\nSystem.out.println(l.arvo());\n",
                     900,(int)i);

    }    
    
    @Points("84.2")
    @Test public  void testaaParametrillinenVahennaTarkistus() {
        Object o = cib.invokeIn(l,2,true);
        vahenna2.invokeIn(l,o,4);
        int i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(2, true);\nl.vahenna(4);\nSystem.out.println(l.arvo());\n",
                     0,(int)i);

    }
    
    @Points("84.2")
    @Test public void testaaParametrillinenVahennaEiTarkistusta() {
        Object o = cib.invokeIn(l,2,false);
        vahenna2.invokeIn(l,o,5);
        int i = (Integer) arvo.invokeIn(l,o);
        assertEquals("Palautit väärän arvon. Kokeile tätä:\nLaskuri l = new Laskuri(2, false);\nl.vahenna(5);\nSystem.out.println(l.arvo());\n",
                     -3,(int)i);
    }    
}
