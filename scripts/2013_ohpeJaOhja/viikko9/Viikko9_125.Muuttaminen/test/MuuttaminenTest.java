
import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.ReflectionUtils;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

public class MuuttaminenTest<_Tavara, _Esine, _Laatikko, _Pakkaaja> {

    String tavaraString = "muuttaminen.domain.Tavara";
    String esineString = "muuttaminen.domain.Esine";
    String muuttolaatikkoString = "muuttaminen.domain.Muuttolaatikko";
    String pakkaajaString = "muuttaminen.logiikka.Pakkaaja";
    Reflex.ClassRef<_Esine> _EsineRef;
    Reflex.ClassRef<_Laatikko> _LaatikkoRef;
    Reflex.ClassRef<_Tavara> _TavaraRef;
    Reflex.ClassRef<_Pakkaaja> _PakkaajaRef;

    @Test
    @Points("125.1")
    public void onTavaraRajapinta() {
        Class clazz = null;
        try {
            clazz = ReflectionUtils.findClass(tavaraString);
        } catch (Throwable t) {
            fail("Tee pakkaus muuttaminen.domain ja sinne rajapinta Tavara");
        }

        if (clazz == null) {
            fail("Olethan luonut rajapinnan Tavara pakkaukseen muuttaminen.domain ja onhan sillä määre public?");
        }

        if (!clazz.isInterface()) {
            fail("Onhan luokka Tavara rajapinta?");
        }

        boolean loytyi = false;
        for (Method m : clazz.getMethods()) {
            if (!m.getReturnType().equals(int.class)) {
                continue;
            }

            if (!m.getName().equals("getTilavuus")) {
                continue;
            }

            loytyi = true;
        }

        if (!loytyi) {
            fail("Onhan rajapinnalla Tavara metodi int getTilavuus()?");
        }
    }

    @Test
    @Points("125.1")
    public void luokkaEsineJulkinen() {
        String klassName = "muuttaminen.domain.Esine";
        _EsineRef = Reflex.reflect(klassName);

        assertTrue("Luokan " + s(klassName) + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassName + " {...\n}", _EsineRef.isPublic());
    }

    @Test
    @Points("125.1")
    public void eiYlimaaraisiaMuuttujia() {
        String klassName = "muuttaminen.domain.Esine";
        _EsineRef = Reflex.reflect(klassName);

        saniteettitarkastus(klassName, 2, "nimen ja tilavuuden tallettavat oliomuuttujat");
    }

    @Test
    @Points("125.1")
    public void testaaEsineKonstruktori() throws Throwable {
        String klassName = "muuttaminen.domain.Esine";

        _EsineRef = Reflex.reflect(klassName);

        Reflex.MethodRef2<_Esine, _Esine, String, Integer> ctor = _EsineRef.constructor().taking(String.class, int.class).withNiceError();
        assertTrue("Määrittele luokalle " + s(klassName) + " julkinen konstruktori: \n"
                + "public " + s(klassName) + "(String, nimi, int tilavuus)", ctor.isPublic());
        String v = "virheen aiheutti koodi new Esine(\"Olutkori\", 10);";
        ctor.withNiceError(v).invoke("Olutkori", 10);
    }

    public _Esine newEsine(String n, int ti) throws Throwable {
        String klassName = "muuttaminen.domain.Esine";
        _EsineRef = Reflex.reflect(klassName);
        Reflex.MethodRef2<_Esine, _Esine, String, Integer> ctor = _EsineRef.constructor().taking(String.class, int.class).withNiceError();
        return ctor.invoke(n, ti);
    }

    @Test
    @Points("125.1")
    public void luokkaEsineToteuttaaRajapinnanTavara() {
        Class clazz = ReflectionUtils.findClass(esineString);


        boolean toteuttaaRajapinnan = false;
        Class kali = ReflectionUtils.findClass(tavaraString);
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(kali)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Toteuttaahan luokka Esine rajapinnan Tavara?");
        }
    }

    @Test
    @Points("125.1")
    public void esineenGetTilavuus() throws Throwable {
        String klassName = "muuttaminen.domain.Esine";
        _EsineRef = Reflex.reflect(klassName);

        _Esine esine = newEsine("Olutkori", 10);

        String v = "Esine esine = new Esine(\"Olutkori\", 10); \n"
                + "esine.getTilavuus();\n";

        assertTrue("Luokalla " + s(klassName) + " tulee olla metodi public int getTilavuus()",
                _EsineRef.method(esine, "getTilavuus").returning(int.class).takingNoParams().
                withNiceError("Virheen aiheutti koodi: \n" + v).isPublic());

        assertEquals(v, 10, (int) _EsineRef.method(esine, "getTilavuus").returning(int.class).takingNoParams().invoke());

        _Esine esine2 = newEsine("Tietokone", 3);

        String v2 = "Esine esine = new Esine(\"Tietokone\", 3); \n"
                + "esine.getTilavuus();\n";

        assertEquals(v2, 3, (int) _EsineRef.method(esine2, "getTilavuus").returning(int.class).takingNoParams().invoke());
    }

    @Test
    @Points("125.1")
    public void esineenGetNimi() throws Throwable {
        String klassName = "muuttaminen.domain.Esine";
        _EsineRef = Reflex.reflect(klassName);

        _Esine esine = newEsine("Olutkori", 10);

        String v = "Esine esine = new Esine(\"Olutkori\", 10); \n"
                + "esine.getNimi();\n";

        assertTrue("Luokalla " + s(klassName) + " tulee olla metodi public String getNimi()",
                _EsineRef.method(esine, "getNimi").returning(String.class).takingNoParams().
                withNiceError("Virheen aiheutti koodi: \n" + v).isPublic());

        assertEquals(v, "Olutkori", _EsineRef.method(esine, "getNimi").returning(String.class).takingNoParams().invoke());

        _Esine esine2 = newEsine("Tietokone", 3);

        String v2 = "Esine esine = new Esine(\"Tietokone\", 3); \n"
                + "esine.getNimi();\n";

        assertEquals(v2, "Tietokone", _EsineRef.method(esine2, "getNimi").returning(String.class).takingNoParams().invoke());
    }

    @Test
    @Points("125.1")
    public void esineenToString() throws Throwable {
        String klassName = "muuttaminen.domain.Esine";
        _EsineRef = Reflex.reflect(klassName);

        _Esine esine = newEsine("Olutkori", 10);
        assertFalse("Toteuta luokalle Esine tehtävänannon mukaan toimiva metodi toString", esine.toString().contains("@"));

        String v = "Esine esine = new Esine(\"Olutkori\", 10); \n"
                + "System.out.println(esine)\n";

        assertEquals(v, "Olutkori (10 dm^3)", esine.toString());

        _Esine esine2 = newEsine("Tietokone", 3);

        String v2 = "Esine esine = new Esine(\"Tietokone\", 3); \n"
                + "System.out.println(esine)\n";

        assertEquals(v2, "Tietokone (3 dm^3)", esine2.toString());
    }

    /*
     * 
     */
    @Test
    @Points("125.2")
    public void esineVertailtava() {

        Class clazz = ReflectionUtils.findClass(esineString);

        boolean toteuttaaRajapinnan = false;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(Comparable.class)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Toteuttaahan luokka Esine rajapinnan Comparable<Esine>?");
        }


        Method compareToMeto = null;
        try {
            compareToMeto = ReflectionUtils.requireMethod(clazz, "compareTo", clazz);
        } catch (Throwable t) {
        }

        if (compareToMeto == null) {
            try {
                compareToMeto = ReflectionUtils.requireMethod(clazz, "compareTo", ReflectionUtils.findClass(tavaraString));
            } catch (Throwable t) {
            }
        }
    }

    @Test
    @Points("125.2")
    public void esineenCompareTo() throws Throwable {
        String klassName = "muuttaminen.domain.Esine";
        _EsineRef = Reflex.reflect(klassName);

        _Esine esine = newEsine("Olutkori", 10);
        _Esine esine2 = newEsine("Tietokone", 3);

        String v = "Esine esine1 = new Esine(\"Olutkori\", 10); \n"
                + "Esine esine2 = new Esine(\"Tietokone\", 3);\n"
                + "esine1.compareTo(esine2);\n";

        assertTrue("Luokalla " + klassName + " tulee olla metodi public int compareTo(Esine verrattava)",
                _EsineRef.method(esine, "compareTo").returning(int.class).taking(_EsineRef.cls()).
                withNiceError("Virheen aiheutti koodi: \n" + v).isPublic());

        int x = (int) _EsineRef.method(esine, "compareTo").returning(int.class).taking(_EsineRef.cls()).invoke(esine2);

        assertTrue("Tuloksen olisi pitänyt olla positiivinen luku koodilla\n" + v + " palautit " + x, x > 0);

        v = "Esine esine1 = new Esine(\"Olutkori\", 10); \n"
                + "Esine esine2 = new Esine(\"Tietokone\", 3);\n"
                + "esine2.compareTo(esine1);\n";

        x = (int) _EsineRef.method(esine2, "compareTo").returning(int.class).taking(_EsineRef.cls()).invoke(esine);

        assertTrue("Tuloksen olisi pitänyt olla negatiivinen luku koodilla\n" + v + " "
                + "palautit " + x, x < 0);
    }

    @Test
    @Points("125.2")
    public void esineidenVertailuToimii() {

        Class clazz = ReflectionUtils.findClass(esineString);

        boolean toteuttaaRajapinnan = false;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.equals(Comparable.class)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Toteuttaahan luokka Esine rajapinnan Comparable?");
        }


        Method compareToMeto = null;
        try {
            compareToMeto = ReflectionUtils.requireMethod(clazz, "compareTo", clazz);
        } catch (Throwable t) {
        }

        if (compareToMeto == null) {
            try {
                compareToMeto = ReflectionUtils.requireMethod(clazz, "compareTo", ReflectionUtils.findClass(tavaraString));
            } catch (Throwable t) {
            }
        }


        Object esine = luoEsine("kivi", 5);
        Integer ret = null;
        try {
            ret = ReflectionUtils.invokeMethod(int.class, compareToMeto, esine, luoEsine("levy", 2));
        } catch (Throwable ex) {
            fail("Onnistuuhan luokan Esine compareTo-metodin kutsuminen?");
        }

        if (ret <= 0) {
            fail("Järjestäähän luokan Esine compareTo-metodi esineitä laskevaan järjestykseen?\n"
                    + "kokeile koodia\n"
                    + "Esine esine1 = new Esine(\"kivi\", 5); \n"
                    + "Esine esine2 = new Esine(\"levy\", 2); \n"
                    + "esine1.compareTo(esine2)");
        }


        try {
            ret = ReflectionUtils.invokeMethod(int.class, compareToMeto, esine, luoEsine("levy", 7));
        } catch (Throwable ex) {
            fail("Onnistuuhan luokan Esine compareTo-metodin kutsuminen?");
        }

        if (ret >= 0) {
            fail("Järjestäähän luokan Esine compareTo-metodi esineitä laskevaan järjestykseen?\n"
                    + "kokeile koodia\n"
                    + "Esine esine1 = new Esine(\"kivi\", 5); \n"
                    + "Esine esine2 = new Esine(\"levy\", 7); \n"
                    + "esine1.compareTo(esine2)");
        }

        try {
            ret = ReflectionUtils.invokeMethod(int.class, compareToMeto, esine, luoEsine("levy", 5));
        } catch (Throwable ex) {
            fail("Onnistuuhan luokan Esine compareTo-metodin kutsuminen?");
        }

        if (ret != 0) {
            fail("Järjestäähän luokan Esine compareTo-metodi olioita laskevaan järjestykseen siten, että samanpainoiset ovat samalla kohdalla?\n"
                    + "kokeile koodia\n"
                    + "Esine esine1 = new Esine(\"kivi\", 5); \n"
                    + "Esine esine2 = new Esine(\"levy\", 5); \n"
                    + "esine1.compareTo(esine2)");
        }
    }

    /*
     * 
     */
    @Test
    @Points("125.3")
    public void luokkaMuuttolaatikkoJulkinen() {
        String klassName = "muuttaminen.domain.Muuttolaatikko";
        _LaatikkoRef = Reflex.reflect(klassName);

        assertTrue("Luokan " + s(klassName) + " pitää olla julkinen, eli se tulee määritellä\n"
                + "public class " + s(klassName) + " {...\n}", _LaatikkoRef.isPublic());
    }

    @Test
    @Points("125.3")
    public void eiYlimaaraisiaMuuttujiaMuuttolaatikossa() {
        String klassName = "muuttaminen.domain.Muuttolaatikko";
        _LaatikkoRef = Reflex.reflect(klassName);

        saniteettitarkastus(klassName, 2, "maksimitilavuuden ja tavarat tallettavat oliomuuttujat");
    }

    @Test
    @Points("125.3")
    public void testaaMuuttlaatikonKonstruktori() throws Throwable {
        String klassName = "muuttaminen.domain.Muuttolaatikko";
        _LaatikkoRef = Reflex.reflect(klassName);

        Reflex.MethodRef1<_Laatikko, _Laatikko, Integer> ctor = _LaatikkoRef.constructor().taking(int.class).withNiceError();
        assertTrue("Määrittele luokalle " + s(klassName) + " julkinen konstruktori: \n"
                + "public " + s(klassName) + "(int maksimitilavuus)", ctor.isPublic());
        String v = "virheen aiheutti koodi new Muuttolaatikko(1000);";
        ctor.withNiceError(v).invoke(1000);
    }

    public _Laatikko newMuuttolaatikko(int ti) throws Throwable {
        String klassName = "muuttaminen.domain.Muuttolaatikko";
        _LaatikkoRef = Reflex.reflect(klassName);
        Reflex.MethodRef1<_Laatikko, _Laatikko, Integer> ctor = _LaatikkoRef.constructor().taking(int.class).withNiceError();
        return ctor.invoke(ti);
    }

    @Test
    @Points("125.3")
    public void muuttolaatikonMetodiLisaa() throws Throwable {
        String klassName = "muuttaminen.domain.Muuttolaatikko";

        _Tavara esine = (_Tavara) newEsine("Olutkori", 10);

        _LaatikkoRef = Reflex.reflect(klassName);

        _Laatikko laatikko = newMuuttolaatikko(1000);

        _TavaraRef = Reflex.reflect("muuttaminen.domain.Tavara");

        String v = "Muuttolaatikko laatikko = new Muuttolaatikko(1000);\n"
                + "Esine esine = new Esine(\"Olutkori\", 10);\n"
                + "laatikko.lisaaTavara( esine );\n";

        assertTrue("Luokalla " + s(klassName) + " tulee olla metodi "
                + "public boolean lisaaTavara(Tavara t)",
                _LaatikkoRef.method(laatikko, "lisaaTavara").returning(boolean.class).taking(_TavaraRef.cls()).
                withNiceError("Virheen aiheutti koodi: \n" + v).isPublic());

        assertEquals(v, true, _LaatikkoRef.method(laatikko, "lisaaTavara").
                returning(boolean.class).
                taking(_TavaraRef.cls()).
                withNiceError("Virheen aiheutti koodi: \n" + v).
                invoke(esine));

        laatikko = newMuuttolaatikko(1000);
        esine = (_Tavara) newEsine("kivi", 1001);

        v = "Muuttolaatikko laatikko = new Muuttolaatikko(1000);\n"
                + "Esine esine = new Esine(\"Kivi\", 1001); \n"
                + "laatikko.lisaaTavara( esine );\n";
        assertEquals(v, false, _LaatikkoRef.method(laatikko, "lisaaTavara").returning(boolean.class).taking(_TavaraRef.cls()).invoke(esine));

    }

    @Test
    @Points("125.3")
    public void MuuttolaatikkoOnTavara() {
        Class muuttolaatikkoClass = ReflectionUtils.findClass(muuttolaatikkoString);

        if (muuttolaatikkoClass == null) {
            fail("Olethan luonut luokan Muuttolaatikko pakkaukseen muuttaminen.domain ja onhan sillä määre public?");
        }

        boolean toteuttaaRajapinnan = false;
        Class tavaraIface = ReflectionUtils.findClass(tavaraString);
        for (Class iface : muuttolaatikkoClass.getInterfaces()) {
            if (iface.equals(tavaraIface)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Toteuttaahan luokka Muuttolaatikko rajapinnan Tavara?");
        }
    }

    @Test
    @Points("125.3")
    public void muuttolaatikonGetTilavuus() throws Throwable {
        String klassName = "muuttaminen.domain.Muuttolaatikko";

        _Tavara esine = (_Tavara) newEsine("Olutkori", 10);

        _LaatikkoRef = Reflex.reflect(klassName);

        _Laatikko laatikko = newMuuttolaatikko(1000);

        String v = "Muuttolaatikko laatikko = new Muuttolaatikko(1000);"
                + "laatikko.getTilavuus(  );\n";

        assertTrue("Luokalla " + klassName + " tulee olla metodi public int getTilavuus()",
                _LaatikkoRef.method(laatikko, "getTilavuus").returning(int.class).takingNoParams().
                withNiceError("Virheen aiheutti koodi: \n" + v).isPublic());

        assertEquals(v, 0, (int) _LaatikkoRef.method(laatikko, "getTilavuus").returning(int.class).takingNoParams().invoke());

        _Tavara esine2 = (_Tavara) newEsine("Tietokone", 3);

        _TavaraRef = Reflex.reflect("muuttaminen.domain.Tavara");

        _LaatikkoRef.method(laatikko, "lisaaTavara").returning(boolean.class).taking(_TavaraRef.cls()).invoke(esine);
        _LaatikkoRef.method(laatikko, "lisaaTavara").returning(boolean.class).taking(_TavaraRef.cls()).invoke(esine2);

        v = "Muuttolaatikko laatikko = new Muuttolaatikko(1000);\n"
                + "laatikko.lisaaTavara( new Esine(\"Olutkori\", 10); )\n"
                + "laatikko.lisaaTavara( new Esine(\"Kivi\", 5); )\n"
                + "laatikko.getTilavuus(  );\n";

        assertEquals(v, 13, (int) _LaatikkoRef.method(laatikko, "getTilavuus").returning(int.class).takingNoParams().invoke());
    }

    @Test
    @Points("125.3")
    public void onLuokkaMuuttolaatikko() {
        Class muuttolaatikkoClass = ReflectionUtils.findClass(muuttolaatikkoString);

        if (muuttolaatikkoClass == null) {
            fail("Olethan luonut luokan Muuttolaatikko pakkaukseen muuttaminen.domain ja onhan sillä määre public?");
        }

        boolean toteuttaaRajapinnan = false;
        Class tavaraIface = ReflectionUtils.findClass(tavaraString);
        for (Class iface : muuttolaatikkoClass.getInterfaces()) {
            if (iface.equals(tavaraIface)) {
                toteuttaaRajapinnan = true;
            }
        }

        if (!toteuttaaRajapinnan) {
            fail("Toteuttaahan luokka Muuttolaatikko rajapinnan Tavara?");
        }

        try {
            ReflectionUtils.requireConstructor(muuttolaatikkoClass, int.class);
        } catch (Throwable t) {
            fail("Onhan luokalla Muuttolaatikko konstruktori public Muuttolaatikko(int maksimitilavuus)?");
        }

        try {

            boolean onMetodi = false;
            for (Method m : muuttolaatikkoClass.getMethods()) {
                if (!m.getReturnType().equals(boolean.class)) {
                    continue;
                }

                if (!m.getName().equals("lisaaTavara")) {
                    continue;
                }

                if (!m.getParameterTypes()[0].equals(tavaraIface)) {
                    continue;
                }

                onMetodi = true;

            }

            if (!onMetodi) {
                fail("ei-onnaa");
            }
        } catch (Throwable t) {
            fail("Onhan luokalla Muuttolaatikko metodi public boolean lisaaTavara(Tavara tavara)?");
        }

        Field collectionField = null;
        for (Field f : muuttolaatikkoClass.getDeclaredFields()) {
            if (!f.getType().equals(List.class) && !f.getType().equals(ArrayList.class)) {
                continue;
            }

            f.setAccessible(true);
            collectionField = f;
            break;
        }

        if (collectionField == null) {
            fail("Muuttolaatikon tulee tallettaa tavarat listaan.");
        }

        Object muuttolaatikko = luoMuuttolaatikko(10);
        Object kivi = luoEsine("kivi", 5);
        lisaaMuuttolaatikkoon(muuttolaatikko, kivi);
        List data = null;
        try {
            data = (List) collectionField.get(muuttolaatikko);
        } catch (Throwable t) {
            fail("Muuttolaatikon tulee tallettaa tavarat listaan.");
        }

        boolean kiviLoytyi = false;
        for (Object obj : data) {
            if (obj == kivi) {
                kiviLoytyi = true;
                break;
            }
        }

        if (!kiviLoytyi) {
            fail("Tarkista että lisättävät esineet lisätään muuttolaatikon sisäiseen listaan.");
        }

        if (!lisaaMuuttolaatikkoon(luoMuuttolaatikko(10), luoEsine("kivi", 10))) {
            fail("Voihan muuttolaatikkoon lisätä tavaroita maksimitilavuuteen asti. Esimerkiksi jos muuttolaatikon tilavuus on 10, pitäisi tilavuudeltaan 10 olevan esineen mahtua sinne.");
        }

        if (lisaaMuuttolaatikkoon(luoMuuttolaatikko(10), luoEsine("kivi", 20))) {
            fail("Tarkista ettei liian isoja esineitä voi lisätä muuttolaatikkoon. Esimerkiksi jos muuttolaatikon tilavuus on 10, ei tilavuudeltaan 20 olevan esineen pitäisi mahtua sinne.");
        }


        Object laatikko = luoMuuttolaatikko(10);
        if (!lisaaMuuttolaatikkoon(laatikko, luoEsine("kivi", 5))) {
            fail("Tyhjään laatikkoon tulee pystyä lisäämään esineitä.");
        }

        if (lisaaMuuttolaatikkoon(laatikko, luoEsine("kivi", 8))) {
            fail("Tarkista ettei muuttolaatikkoon voi lisätä esineitä siten, että muuttolaatikon maksimitilavuus ylittyisi.");
        }

        if (!lisaaMuuttolaatikkoon(laatikko, luoEsine("kivi", 2))) {
            fail("Laatikkoon jossa on esine tulee pystyä lisäämään esineitä jos muuttolaatikon maksimitilavuus ei ylity.");
        }

        if (!lisaaMuuttolaatikkoon(laatikko, luoEsine("kivi", 3))) {
            fail("Laatikkoon jossa on esine tulee pystyä lisäämään esineitä jos muuttolaatikon maksimitilavuus ei ylity.");
        }

        if (lisaaMuuttolaatikkoon(laatikko, luoEsine("kivi", 3))) {
            fail("Tarkista että muuttolaatikkoon ei voi lisätä esineitä jos se on jo täynnä.");
        }
    }

    /*
     * 
     */
    @Test
    @Points("125.4")
    public void luokkaPakkaajaJulkinen() {
        String klassName = "muuttaminen.logiikka.Pakkaaja";
        _PakkaajaRef = Reflex.reflect(klassName);

        assertTrue("Luokan " + klassName + " pitää olla julkinen, eli se tulee määritellä\n"
                + "public class " + klassName + " {...\n}", _PakkaajaRef.isPublic());
    }

    @Test
    @Points("125.4")
    public void testaaPakkaajanKonstruktori() throws Throwable {
        String klassName = "muuttaminen.logiikka.Pakkaaja";
        _PakkaajaRef = Reflex.reflect(klassName);

        Reflex.MethodRef1<_Pakkaaja, _Pakkaaja, Integer> ctor = _PakkaajaRef.constructor().taking(int.class).withNiceError();
        assertTrue("Määrittele luokalle " + s(klassName) + " julkinen konstruktori: \n"
                + "public " + s(klassName) + "(int laatikkokoko)", ctor.isPublic());
        String v = "virheen aiheutti koodi new Pakkaaja(1000);";
        Object o = ctor.withNiceError(v).invoke(1000);
        o.toString();
    }

    public _Pakkaaja newPakkaaja(int ti) throws Throwable {
        String klassName = "muuttaminen.logiikka.Pakkaaja";
        _PakkaajaRef = Reflex.reflect(klassName);
        Reflex.MethodRef1<_Pakkaaja, _Pakkaaja, Integer> ctor = _PakkaajaRef.constructor().taking(int.class).withNiceError();
        return ctor.invoke(ti);
    }

    @Test
    @Points("125.4")
    public void pakkaajanMetodiPakkaaTavarat() throws Throwable {
        String klassName = "muuttaminen.logiikka.Pakkaaja";

        _Tavara esine1 = (_Tavara) newEsine("Olutkori", 10);
        _Tavara esine2 = (_Tavara) newEsine("Kivi", 5);
        _Tavara esine3 = (_Tavara) newEsine("Taulu", 20);

        List lista = new ArrayList<_Tavara>();
        lista.add(esine1);
        lista.add(esine2);
        lista.add(esine3);

        _LaatikkoRef = Reflex.reflect(klassName);

        _Pakkaaja pakaaja = newPakkaaja(1000);

        String v = "Pakkaaja pakkaaja = new Pakkaaja(1000);\n"
                + "List<Tavara> tavarat = new ArrayList<Tavara>;\n"
                + "tavarat.add( new Esine(\"Olutkori\", 10) );\n"
                + "tavarat.add( new Esine(\"Kivi\", 5) );\n"
                + "tavarat.add( new Esine(\"Taulu\", 20) );\n"
                + "pakkaaja.pakkaaTavarat( tavarat );\n";

        assertTrue("Luokalla " + s(klassName) + " tulee olla metodi "
                + "public List<Muuttolaatikko> pakkaaTavarat(List<Tavara> tavarat)",
                _PakkaajaRef.method(pakaaja, "pakkaaTavarat").returning(List.class).taking(List.class).
                withNiceError("Virheen aiheutti koodi: \n" + v).isPublic());

        _PakkaajaRef.method(pakaaja, "pakkaaTavarat").returning(List.class).taking(List.class).withNiceError("Virheen aiheutti koodi: \n" + v).invoke(lista);
    }

    @Test
    @Points("125.4")
    public void pakkaajaToimii() throws Throwable {
        Class muuttolaatikkoClass = ReflectionUtils.findClass(muuttolaatikkoString);
        Class pakkaajaClass = ReflectionUtils.findClass(pakkaajaString);

        if (pakkaajaClass == null) {
            fail("Olethan luonut luokan Pakkaaja pakkaukseen muuttaminen.logiikka ja onhan sillä määre public?");
        }

        Object pakkaaja = null;
        try {
            Constructor pakkaajaConst = ReflectionUtils.requireConstructor(pakkaajaClass, int.class);
            pakkaaja = ReflectionUtils.invokeConstructor(pakkaajaConst, 20);

            if (pakkaaja == null) {
                fail("ei-onnistu");
            }
        } catch (Throwable t) {
            fail("Onhan luokalla Pakkaaja konstruktori public Pakkaaja(int laatikoidenTilavuus)?");
        }

        Method pakkaaMetodi = null;
        try {
            for (Method m : pakkaajaClass.getMethods()) {
                if (!m.getReturnType().equals(ArrayList.class) && !m.getReturnType().equals(List.class)) {
                    continue;
                }

                if (!m.getName().equals("pakkaaTavarat")) {
                    continue;
                }

                if (!m.getParameterTypes()[0].equals(ArrayList.class) && !m.getParameterTypes()[0].equals(List.class)) {
                    continue;
                }

                pakkaaMetodi = m;
            }

            if (pakkaaMetodi == null) {
                fail("ei-onnaa");
            }
        } catch (Throwable t) {
            fail("Onhan luokalla Pakkaaja metodi public List<Muuttolaatikko> pakkaaTavarat(List<Tavara> tavarat)?");
        }

        List esineet = new ArrayList();

        String esineMj = "List<Tavara> tavarat = new ArrayList<Tavara>();\n";

        for (int i = 0; i < 5; i++) {
            int til = (int) (1 + (Math.random() * 10));
            Object e = luoEsine("kivi", til);
            esineMj += " tavarat.add( new Esine(\"kivi\", " + til + ") );\n";
            esineet.add(e);
        }

        String e = esineet.toString();

        List esineetkopio = new ArrayList(esineet);

        List laatikot = null;
        try {
            laatikot = (List) pakkaaMetodi.invoke(pakkaaja, esineet);
        } catch (Throwable t) {
            fail("Onnistuuhan laukkujen pakkaaminen? Testaa koodi\n"
                    + esineMj
                    + "Pakkaaja pakkaaja = new Pakkaaja(20);\n"
                    + "pakkaaja.pakkaaTavarat(tavarat);");
        }

        String koodi = ""
                + esineMj
                + "Pakkaaja pakkaaja = new Pakkaaja(20);\n"
                + "pakkaaja.pakkaaTavarat(tavarat);";

        Field collectionField = null;
        for (Field f : muuttolaatikkoClass.getDeclaredFields()) {
            if (!f.getType().equals(List.class) && !f.getType().equals(ArrayList.class)) {
                continue;
            }

            f.setAccessible(true);
            collectionField = f;
            break;
        }

        if (collectionField == null) {
            fail("Muuttolaatikon tulee tallettaa tavarat listaan. Virhe koodilla\n"
                    + koodi);
        }

        assertFalse("luokan Pakkaaja metodi pakkaaTavarat ei saa palauttaa null", laatikot == null);

        for (Object laatikko : laatikot) {
            List data = null;
            try {
                data = (List) collectionField.get(laatikko);
            } catch (Throwable t) {
                fail("Muuttolaatikon tulee tallettaa tavarat listaan. Tarkista koodi\n"
                        + koodi);
            }

            for (Object esine : data) {
                esineetkopio.remove(esine);
            }

            oikeanKokoinen(laatikko, 20);
        }

        if (!esineetkopio.isEmpty()) {
            fail("Pakattiinhan kaikki esineet laukkuihin? Tarkista koodi\n"
                    + koodi);
        }
    }

    private boolean lisaaMuuttolaatikkoon(Object muuttolaatikko, Object esine) {
        Method lisaaTavaraMeto = ReflectionUtils.requireMethod(ReflectionUtils.findClass(muuttolaatikkoString), "lisaaTavara", ReflectionUtils.findClass(tavaraString));

        try {
            return ReflectionUtils.invokeMethod(boolean.class, lisaaTavaraMeto, muuttolaatikko, esine);
        } catch (Throwable ex) {
            fail("Esineen lisääminen muuttolaatikkoon epäonnistui: " + esine);
        }
        return false;
    }

    private Object luoEsine(String nimi, int tilavuus) {
        Class clazz = ReflectionUtils.findClass(esineString);
        Object esine = null;
        try {
            esine = ReflectionUtils.invokeConstructor(ReflectionUtils.requireConstructor(clazz, String.class, int.class), nimi, tilavuus);
        } catch (Throwable ex) {
            fail("Esineen luominen epäonnistui kun nimi on " + nimi + " ja tilavuus on " + tilavuus);
        }

        return esine;
    }

    private Object luoMuuttolaatikko(int maksimitilavuus) {
        Class clazz = ReflectionUtils.findClass(muuttolaatikkoString);
        Object muuttolaatikko = null;
        try {
            muuttolaatikko = ReflectionUtils.invokeConstructor(ReflectionUtils.requireConstructor(clazz, int.class), maksimitilavuus);
        } catch (Throwable ex) {
            fail("Muuttolaatikon luominen epäonnistui kun maksimitilavuus on " + maksimitilavuus);
        }

        return muuttolaatikko;
    }

    private void saniteettitarkastus(String klassName, int n, String m) throws SecurityException {
        Field[] kentat = ReflectionUtils.findClass(klassName).getDeclaredFields();

        for (Field field : kentat) {
            assertFalse("et tarvitse \"stattisia muuttujia\", poista luokalta " + s(klassName) + " muuttuja " + kentta(field.toString(), s(klassName)), field.toString().contains("static") && !field.toString().contains("final"));
            assertTrue("luokan kaikkien oliomuuttujien näkyvyyden tulee olla private, muuta luokalta " + s(klassName) + " löytyi: " + kentta(field.toString(), klassName), field.toString().contains("private"));
        }

        if (kentat.length > 1) {
            int var = 0;
            for (Field field : kentat) {
                if (!field.toString().contains("final")) {
                    var++;
                }
            }
            assertTrue("et tarvitse " + s(klassName) + "-luokalle kuin " + m + ", poista ylimääräiset", var <= n);
        }
    }

    private String kentta(String toString, String klassName) {
        return toString.replace(klassName + ".", "").replace("java.lang.", "").replace("java.util.", "");
    }

    private String s(String klassName) {
        return klassName.substring(klassName.lastIndexOf(".") + 1);
    }

    private void oikeanKokoinen(Object laatikko, int koko) throws Throwable {
        _Laatikko lodju = (_Laatikko) laatikko;
        int alussa = getTilavuus(lodju);
        for (int i = 1; i <= koko - alussa; i++) {
            Object kilo = newEsine("kivi", 1);
            lisaaMuuttolaatikkoon(lodju, kilo);
            assertTrue("Kun luodaan new Pakkaaja(20); on pakkaajan käytettävä "
                    + "laatikoita joiden koko on 20\nKäytit laatikkoa jonka koko on "
                    + getTilavuus(lodju),
                    getTilavuus(lodju) == alussa + i);

        }
        int was = getTilavuus(lodju);
        while (true) {
            Object kilo = newEsine("kivi", 1);
            lisaaMuuttolaatikkoon(lodju, kilo);
            int now = getTilavuus(lodju);
            if (now == was) {
                break;
            }
            was = now;
        }
        assertTrue("Kun luodaan new Pakkaaja(20); on pakkaajan käytettävä "
                + "laatikoita joiden koko on 20\nKäytit laatikkoa jonka koko on "
                + getTilavuus(lodju),
                getTilavuus(lodju) == koko);
    }

    private int getTilavuus(_Laatikko laatikko) throws Throwable {
        String klassName = "muuttaminen.domain.Muuttolaatikko";
        _LaatikkoRef = Reflex.reflect(klassName);
        String v = "Muuttolaatikko laatikko = new Muuttolaatikko(1000);\n"
                + "laatikko.lisaaTavara( new Esine(\"kivi\",1));\n"
                + "laatikko.getTilavuus();\n";

        return _LaatikkoRef.method(laatikko, "getTilavuus").returning(int.class).takingNoParams().
                withNiceError("Virheen aiheutti koodi: \n" + v).invoke();

    }
}
