
public class HirsipuuLogiikka {

    private String sana;
    private String arvatutKirjaimet;
    private int virheidenLukumaara;

    public HirsipuuLogiikka(String sana) {
        this.sana = sana.toUpperCase();
        this.arvatutKirjaimet = "";
        this.virheidenLukumaara = 0;
    }

    public int virheidenLukumaara() {
        return this.virheidenLukumaara;
    }

    public String arvatutKirjaimet() {
        return this.arvatutKirjaimet;
    }

    public int virheitaHavioon() {
        return 12;
    }

    public void arvaaKirjain(String kirjain) {
        // Ohjelmoi tänne toiminnallisuus kirjaimen arvaamiseksi.

        // Arvattua kirjainta ei saa arvata uudestaan. Jos arvattava sana 
        // ei sisällä kirjainta, virheiden lukumäärän tulee kasvaa.
        // Lisää arvattu kirjain arvattuihin kirjaimiin

    }

    public String salattuSana() {
        // Ohjelmoi tänne toiminnallisuus salatun sanan luomiseksi
        // ja antamiseksi. 

        // Luo uusi salattu sana this.sana-merkkijonoa kirjain kirjaimelta 
        // läpikäyden. Jos kirjain on arvatuissa kirjaimissa, voit lisätä sen 
        // sellaisenaan. Jos ei, lisää uuteen sanaan merkki "_".

        // Muista varmistaa ettet ohjelmoi päättymätöntä toistolauseketta!

        // palauta lopuksi salattua sanaa kuvaava merkkijono.

        // HUOM: merkin sisältymistä merkkijonoon ei voi suoraan verrata contains-metodilla
        // vaan on käytettävä indexOf-metodia tai muutettava merkki merkkijonoksi esim. seuraavasti
        // ""+ merkki
        
        return "";
    }

}
