import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the dictionary");
        System.out.println("Available languages... ");

        for (String lang : Dictionary.getLanguages()) {
            System.out.println(lang);
        }

        Scanner scanner = new Scanner(System.in);
        String initialLang = "";
        String finalLang = "";

        System.out.print("Please enter the initial language. It will prompt for a new one if invalid\n> ");
        while (!Dictionary.getLanguages().contains(initialLang))
            initialLang = scanner.nextLine();

        System.out.print("Please enter the final language. It will prompt for a new one if invalid\n> ");
        while (!Dictionary.getLanguages().contains(finalLang))
            finalLang = scanner.nextLine();

        System.out.print("Please enter the text\n> ");
        String text = scanner.nextLine();

        scanner.close();

        Dictionary dic = new Dictionary(text);
        System.out.println("\nTranslation: " + dic.translate(initialLang, finalLang));
    }
}