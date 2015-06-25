import fi.helsinki.cs.tmc.edutestutils.Points;
import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;


public class MuuntajaTest<_Muuntaja, _Muunnos>  {
    @Test
    @Points("109.1")
    public void testaaMuunnosLuokanOlemassaOlo() throws Throwable {
        luoMuunnos('a', 'b');
    }

    @Test
    @Points("109.1")
    public void testaaMuunnoksia() throws Throwable {
        testaaMuunnosta('a', 'o', "halapakka");
        testaaMuunnosta('!', '#', "#!/bin/bash");
        testaaMuunnosta('U', 'u', "UU");
    }

    @Test
    @Points("109.2")
    public void testaaMuuntajaLuokanOlemassaOlo() throws Throwable {
        luoMuuntaja();
    }

    @Test
    @Points("109.2")
    public void testaaMuuntajaa() throws Throwable {
        Map<Character, Character> muunnokset =
                new HashMap<Character, Character>();
        muunnokset.put('A', 'I');
        muunnokset.put('i', 'u');
        muunnokset.put('e', 'a');
        muunnokset.put('!', '?');
        testaaMuuntajaa(muunnokset, "Arkkitehti!");
    }

    
    private void testaaMuunnosta(char merkki1, char merkki2, String merkkijono) throws Throwable {
        String muunnettu = muunnaMuunnoksella(merkkijono, merkki1, merkki2);
        String vastaus = merkkijono.replace(merkki1, merkki2);

        if (!muunnettu.equals(vastaus)) {
            fail("Oliolle Muunnos('" + merkki1 + "', '" + merkki2 +
                    "') kutsuttiin metodia muunna(\"" + merkkijono +
                    "\") ja tulokseksi palautettiin \"" + muunnettu +
                    "\", vaikka tuloksen olisi pitänyt olla \"" + vastaus + "\".");
        }
    }

    private String muunnaMuunnoksella(String merkkijono, char m1, char m2) throws Throwable {
        String klassName = "Muunnos";
        Reflex.ClassRef<Object> klass = Reflex.reflect(klassName);     
        String metodi = "muunna";
        
        Object olio = klass.constructor().taking(char.class, char.class).invoke(m1, m2);
      
      
        assertTrue("tee luokalle " + klassName + " metodi public String " + metodi + "(String merkkijono) ", klass.method(olio, metodi)
                .returning(String.class).taking(String.class).isPublic());
        
        String e = "koodi m = Muunnos('"+m1+"','"+m2+"'); m.muunna(\""+merkkijono+"\"); aiheutti poikkeuksen";
        

        return klass.method(olio, metodi)
                .returning(String.class).taking(String.class).withNiceError(e).invoke(merkkijono);
    }

    private Object luoMuunnos(char merkki1, char merkki2) throws Throwable {
        String klassName = "Muunnos";
        Reflex.ClassRef<Object> klass = Reflex.reflect(klassName);
        assertTrue("Luokan " + klassName + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassName + " {...\n}", klass.isPublic());
        
        Reflex.MethodRef2<Object, Object, Character, Character> ctor = klass.constructor().taking(char.class, char.class).withNiceError();
        assertTrue("Määrittele luokalle " + klassName + " julkinen konstruktori: public " + klassName + "(char merkki1, char merkki2)", ctor.isPublic());
        return ctor.invoke(merkki1, merkki2);
    }

    private void testaaMuuntajaa(Map<Character, Character> muunnokset, String merkkijono) throws Throwable {
        _Muuntaja muuntaja = luoMuuntaja();

        Iterator<Map.Entry<Character, Character>> it = muunnokset.entrySet().iterator();
        String vastaus = merkkijono;
        String muunnoksetTekstina = "";
        while (it.hasNext()) {
            Map.Entry<Character, Character> entry = it.next();
            char merkki1 = entry.getKey();
            char merkki2 = entry.getValue();

            lisaaMuunnos(muuntaja, merkki1, merkki2);

            vastaus = vastaus.replace(merkki1, merkki2);

            muunnoksetTekstina += "('" + merkki1 + "' -> '" + merkki2 + "'), ";
        }

        String muunnettu = muunnaMuuntajalla(muuntaja, merkkijono);

        if (!muunnettu.equals(vastaus)) {
            fail("Muuntajalle annettiin muunnokset " + muunnoksetTekstina +
                    " kutsuttiin metodia muunna(\"" + merkkijono +
                    "\") ja tulokseksi palautettiin \"" + muunnettu +
                    "\", vaikka tuloksen olisi pitänyt olla \"" + vastaus + "\".");
        }
    }

    private _Muuntaja luoMuuntaja() throws Throwable {
        String klassName = "Muuntaja";
        Reflex.ClassRef<_Muuntaja> klass = Reflex.reflect(klassName);
        assertTrue("Luokan " + klassName + " pitää olla julkinen, eli se tulee määritellä\npublic class " + klassName + " {...\n}", klass.isPublic());
                
        Reflex.MethodRef0<_Muuntaja, _Muuntaja> ctor = klass.constructor().takingNoParams().withNiceError();
        assertTrue("Määrittele luokalle " + klassName + " julkinen konstruktori: public " + klassName + "()", ctor.isPublic());
        return ctor.invoke();
    }

    private void lisaaMuunnos(_Muuntaja mtaja, char m1, char m2) throws Throwable {
        Reflex.ClassRef<_Muuntaja> _MuuntajaRef = Reflex.reflect("Muuntaja");

        Reflex.ClassRef<_Muunnos> _MuunnosRef = Reflex.reflect("Muunnos");
        
        _Muunnos mnos = _MuunnosRef.constructor().taking(char.class, char.class).invoke(m1, m2);
        
        Class<_Muunnos> c = _MuunnosRef.cls();

        String v = "\nMuuntaja m = new Muuntaja(); Muunnos mnt = new Muunnos('"+m1+"','"+m2+"'); m.lisaaMuunnos(mnt);";
        
        _MuuntajaRef.method(mtaja, "lisaaMuunnos").returningVoid().taking(c).withNiceError(v).invoke(mnos);
        
        
    }

    private String muunnaMuuntajalla(Object muuntaja, String merkkijono) {
        Method metodi;
        try {
            metodi = muuntaja.getClass().getMethod("muunna", String.class);
        } catch (Exception e) {
            fail("Muuntaja-luokalla ei ole metodia: public String muunna(String merkkijono).");
            return null;
        }

        if (!metodi.getReturnType().equals(String.class)) {
            fail("Muuntaja-luokan metodin muunna(String merkkijono) täytyy palauttaa merkkijono.");
            return null;
        }

        try {
            return (String) metodi.invoke(muuntaja, merkkijono);
        } catch (Exception e) {
            fail("Muuntaja-luokan muunna-metodissa tapahtui poikkeus: " + e.toString());
            return null;
        }
    }
}
