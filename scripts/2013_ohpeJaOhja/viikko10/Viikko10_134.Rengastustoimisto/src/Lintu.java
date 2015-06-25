
public class Lintu {

    private String nimi;
    private String latinankielinenNimi;
    private int rengastusvuosi;

    public Lintu(String nimi, String latinankielinenNimi, int rengastusvuosi) {
        this.nimi = nimi;
        this.latinankielinenNimi = latinankielinenNimi;
        this.rengastusvuosi = rengastusvuosi;
    }
    
    
    @Override
    public String toString() {
        return this.latinankielinenNimi + " (" + this.rengastusvuosi + ")";
    }
}


