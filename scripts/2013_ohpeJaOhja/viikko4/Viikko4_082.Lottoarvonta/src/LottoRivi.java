import java.util.ArrayList;
import java.util.Random;

public class LottoRivi {
    private ArrayList<Integer> numerot;

    public LottoRivi() {
        // Alustetaan lista numeroille
        this.numerot = new ArrayList<Integer>();
        // Arvo numerot heti LottoRivin luomisen yhteydessä
        this.arvoNumerot();
    }

    public ArrayList<Integer> numerot() {
        return this.numerot;
    }

    public void arvoNumerot() {
        // Kirjoita numeroiden arvonta tänne käyttämällä metodia sisaltaaNumeron()
    }

    public boolean sisaltaaNumeron(int numero) {
        // Testaa tässä onko numero jo arvottujen numeroiden joukossa
        return true;
    }
}
