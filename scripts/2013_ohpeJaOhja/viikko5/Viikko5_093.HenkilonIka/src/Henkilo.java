import java.util.Calendar;

public class Henkilo {
    private String nimi;
    private Paivays syntymapaiva;
    
    public Henkilo(String nimi, int pp, int kk, int vv) {
        this.nimi = nimi;
        this.syntymapaiva = new Paivays(pp, kk, vv);
    }
    
    
    public int ika() {
        // laske henkilön ikä syntymäpäivän ja tämän päivän perusteella
        // tämä päivä saadaan selville seuraavasti
        // Calendar.getInstance().get(Calendar.DATE);
        // Calendar.getInstance().get(Calendar.MONTH) + 1; // tammikuun numero on 0 joten lisätään 1
        // Calendar.getInstance().get(Calendar.YEAR);
        return 0;
    }
    
    public boolean vanhempiKuin(Henkilo verrattava) {
        // vertaa henkilöiden ikiä käyttäen henkilöiden syntymäpäivää
        return false;
    }
    
    public String getNimi() {
        return this.nimi;
    }
    
    public String toString() {
        return this.nimi + ", syntynyt " + this.syntymapaiva;
    }
}
