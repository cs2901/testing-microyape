import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter text (in english) to translate: ");
        String text = scanner.nextLine();
        scanner.close();

        System.out.println("Translating...");
        Dictionary dic = new Dictionary(text);

        for (HashMap.Entry<String, String> entry : dic.translate().entrySet()) {
            System.out.println("Translation to " + entry.getKey() + ": " + entry.getValue());
        }
    }
}
