/* 
 * ÄLÄ KOSKE TÄÄLLÄ OLEVAAN KOODIIN!
 */

public class Tili {

    private double saldo;
    private String omistaja;

    public Tili(String omistaja, double saldo) {
        this.saldo = saldo;
        this.omistaja = omistaja;
    }
  
    public void pano(double maara) {
        saldo+=maara;
    }

    public void otto(double maara){
        saldo-=maara;
    }    
    
    public double saldo() {
        return saldo;
    }

    @Override
    public String toString() {
        return omistaja + " saldo: "+saldo;
    }
}
