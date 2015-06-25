import java.util.Scanner;

public class Palindromi {

    public static boolean palindromi(String merkkijono) {
        // kirjoita koodia tähän
        return false;
    }

    public static void main(String[] args) {
        Scanner lukija = new Scanner(System.in);
        
        System.out.println("Anna merkkijono: ");
        String merkkijono = lukija.nextLine();    
        if (palindromi(merkkijono)) {
            System.out.println("Merkkijono on palindromi!");
        } else {
            System.out.println("Merkkijono ei ole palindromi!");
        }
    }
}
