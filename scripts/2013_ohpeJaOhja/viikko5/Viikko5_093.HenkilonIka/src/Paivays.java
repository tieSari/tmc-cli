
public class Paivays {

    private int paiva;
    private int kuukausi;
    private int vuosi;

    public Paivays(int pv, int kk, int vv) {
        this.paiva = pv;
        this.kuukausi = kk;
        this.vuosi = vv;
    }

    public String toString() {
        return this.paiva + "." + this.kuukausi + "." + this.vuosi;
    }

    public boolean aiemmin(Paivays verrattava) {
        // ensin verrataan vuosia
        if (this.vuosi < verrattava.vuosi) {
            return true;
        }

        // jos vuodet ovat samat, verrataan kuukausia
        if (this.vuosi == verrattava.vuosi && this.kuukausi < verrattava.kuukausi) {
            return true;
        }

        // vuodet ja kuukaudet samoja, verrataan päivää
        if (this.vuosi == verrattava.vuosi && this.kuukausi == verrattava.kuukausi
                && this.paiva < verrattava.paiva) {
            return true;
        }

        return false;
    }

    /*
     * tehtävässä 92 lisättiin luokalle metodi erotusVuosissa 
     * Kannattaa kopioida 92:sta metodi tänne, se helpottaa tehtävän tekemistä oleellisesti!
     */
    public int erotusVuosissa(Paivays verrattava) {
        return -99;
    }

  
}