import java.util.ArrayList;
import java.util.Collections;

public class JoukkoYhdistaminen {
    public static void main(String[] args) {
        //Alustetaan listat
        //Allaolevia lukuja saa muuttaa testatessa
        ArrayList<Integer> lista1 = new ArrayList<Integer>();
        ArrayList<Integer> lista2 = new ArrayList<Integer>();

        Collections.addAll(lista1, 4, 3);


        Collections.addAll(lista2, 5, 10, 4, 3, 7);


        //Toteuta metodi yhdista ja testaa sen toimintaa eri listoilla
        //joukkoYhdista(lista1, lista2);
        System.out.println(lista1);
        System.out.println(lista2);
    }
    
}
