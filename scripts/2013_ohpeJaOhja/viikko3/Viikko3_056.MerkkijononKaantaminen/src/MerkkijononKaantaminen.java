import java.util.Scanner;

public class MerkkijononKaantaminen {
    public static String kaanna(String merkkijono) {
        // Kirjoita koodia tähän.
        // Metodin on siis palautettava käännetty merkkijono returnilla!
        return "";
    }

    public static void main(String[] args) {
        Scanner lukija = new Scanner(System.in);
        System.out.print("Anna merkkijono: ");
        String merkkijono = lukija.nextLine();
        System.out.println("Väärinpäin: " + kaanna(merkkijono));
    }
}
