
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Field;
import org.junit.*;
import static org.junit.Assert.*;

@Points("89")
public class KelloTest {

    String klassName = "Kello";

    @Test
    public void eiYlimaaraisiaMuuttujia(){
        saniteettitarkastus(3, "runkoon valmiiksi määritellyt kolme YlhaaltaRajoitettuLaskuri-tyyppistä oliomuuttujaa");
    }      
    
    @Test
    public void eiKaiPoikkeusta() throws Throwable {
        Reflex.ClassRef<Object> klass;

        klass = Reflex.reflect(klassName);

        assertTrue("Luokan " + klassName + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassName + " {...\n}", klass.isPublic());

        Reflex.MethodRef3<Object, Object, Integer, Integer, Integer> ctor = klass.constructor().taking(int.class, int.class, int.class).withNiceError();
        assertTrue("Määrittele luokalle " + klassName + " julkinen konstruktori: public " + klassName + "(int tunnitAlussa, int minuutitAlussa, int sekunnitAlussa)", ctor.isPublic());
        Object olio = ctor.invoke(23, 59, 55);

        String v = "Kello k = new Kello(23,29,55); k.etene(); System.out.print(k)";
        klass.method(olio, "etene").returningVoid().takingNoParams().withNiceError(v).invoke();
        klass.method(olio, "toString").returning(String.class).takingNoParams().withNiceError(v).invoke();
    }

    @Test
    public void tarkistaVuorokaudenVaihtuminen() {
        Kello kello = new Kello(23, 59, 58);
        String tulos;

        String a = "23:59:58";
        int et = 0;

        tulos = "23:59:58";
        assertTrue("Kun kello oli alussa " + a + ", " + et + " sekunnin etenemisen jälkeen odotettiin aikaa " + tulos
                + ", mutta kellon toString palautti ajan: " + kello, kello.toString().contains(tulos));

        kello.etene();
        et++;

        tulos = "23:59:59";
        assertTrue("Kun kello oli alussa " + a + ", " + et + " sekunnin etenemisen jälkeen odotettiin aikaa " + tulos
                + ", mutta kellon toString palautti ajan: " + kello, kello.toString().contains(tulos));

        kello.etene();
        et++;

        tulos = "00:00:00";
        assertTrue("Kun kello oli alussa " + a + ", " + et + " sekunnin etenemisen jälkeen odotettiin aikaa " + tulos
                + ", mutta kellon toString palautti ajan: " + kello, kello.toString().contains(tulos));

        kello.etene();
        et++;

        tulos = "00:00:01";
        assertTrue("Kun kello oli alussa " + a + ", " + et + " sekunnin etenemisen jälkeen odotettiin aikaa " + tulos
                + ", mutta kellon toString palautti ajan: " + kello, kello.toString().contains(tulos));
    }

    @Test
    public void tarkistaMinuutinVaihtuminen() {
        Kello kello = new Kello(00, 17, 58);
        String tulos;
        String a = "00:17:58";
        int et = 0;


        tulos = "00:17:58";
        assertTrue("Kun kello oli alussa " + a + ", " + et + " sekunnin etenemisen jälkeen odotettiin aikaa " + tulos
                + ", mutta kellon toString palautti ajan: " + kello, kello.toString().contains(tulos));

        kello.etene();
        et++;

        tulos = "00:17:59";
        assertTrue("Kun kello oli alussa " + a + ", " + et + " sekunnin etenemisen jälkeen odotettiin aikaa " + tulos
                + ", mutta kellon toString palautti ajan: " + kello, kello.toString().contains(tulos));

        kello.etene();
        et++;

        tulos = "00:18:00";
        assertTrue("Kun kello oli alussa " + a + ", " + et + " sekunnin etenemisen jälkeen odotettiin aikaa " + tulos
                + ", mutta kellon toString palautti ajan: " + kello, kello.toString().contains(tulos));

        kello.etene();
        et++;

        tulos = "00:18:01";
        assertTrue("Kun kello oli alussa " + a + ", " + et + " sekunnin etenemisen jälkeen odotettiin aikaa " + tulos
                + ", mutta kellon toString palautti ajan: " + kello, kello.toString().contains(tulos));
    }

    @Test
    public void tarkistaTunninVaihtuminen() {
        Kello kello = new Kello(11, 59, 59);
        String tulos;
        String a = "11:59:59";
        int et = 0;

        tulos = "11:59:59";
        assertTrue("Kun kello oli alussa " + a + ", " + et + " sekunnin etenemisen jälkeen odotettiin aikaa " + tulos
                + ", mutta kellon toString palautti ajan: " + kello, kello.toString().contains(tulos));

        kello.etene();
        et++;

        tulos = "12:00:00";
        assertTrue("Kun kello oli alussa " + a + ", " + et + " sekunnin etenemisen jälkeen odotettiin aikaa " + tulos
                + ", mutta kellon toString palautti ajan: " + kello, kello.toString().contains(tulos));

        kello.etene();
        et++;

        tulos = "12:00:01";
        assertTrue("Kun kello oli alussa " + a + ", " + et + " sekunnin etenemisen jälkeen odotettiin aikaa " + tulos
                + ", mutta kellon toString palautti ajan: " + kello, kello.toString().contains(tulos));

        kello.etene();
        et++;

        tulos = "12:00:02";
        assertTrue("Kun kello oli alussa " + a + ", " + et + " sekunnin etenemisen jälkeen odotettiin aikaa " + tulos
                + ", mutta kellon toString palautti ajan: " + kello, kello.toString().contains(tulos));
    }

    private void saniteettitarkastus(int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("et tarvitse \"stattisia muuttujia\", poista " + kentta(field.toString()), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("luokan kaikkien oliomuuttujien näkyvyyden tulee olla private, muuta löytyi: " + kentta(field.toString()), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }
            assertTrue("et tarvitse " + klassName + "-luokalle kuin " + m + ", poista ylimääräiset", var <= n);
        }
    }

    private String kentta(String toString) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "");
    }
}
