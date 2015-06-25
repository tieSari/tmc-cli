public class Testiohjelma {
    public static void main(String[] args) {

        HirsipuuLogiikka l = new HirsipuuLogiikka("kissa");
        System.out.println("Sana on: "+l.salattuSana());

        System.out.println("Arvataan: A, D, S, F, D");
        l.arvaaKirjain("A");
        l.arvaaKirjain("D");
        l.arvaaKirjain("S");
        l.arvaaKirjain("F");
        l.arvaaKirjain("D");
        System.out.println("Arvatut kirjaimet: "+l.arvatutKirjaimet());
        System.out.println("Virheiden lukumäärä: "+l.virheidenLukumaara());
        System.out.println("Sana on: "+l.salattuSana());


    }
}
