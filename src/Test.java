import java.util.Scanner;


public class Test {
    public static void main(String[] args) {
        ACPSystem system = new ACPSystem();
        CommandProcessor processor = system.getCommandProcessor();

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                processor.processLine(line);
            }
        }
    }
}

