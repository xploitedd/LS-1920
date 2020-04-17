package pt.isel.ls;

import java.util.Scanner;

public class ConsoleApplication implements Runnable {

    private static final Scanner SCANNER = new Scanner(System.in);
    private final AppProcessor processor;

    public ConsoleApplication(AppProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void run() {
        for ( ; ; ) {
            System.out.print("> ");
            String input = SCANNER.nextLine();
            processor.processInput(input);
        }
    }

}
