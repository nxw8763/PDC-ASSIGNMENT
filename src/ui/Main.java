package ui;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {
            initializeDatabase();
        } catch (IOException e) {
            System.out.println("Failed to initialise database files.");
            e.printStackTrace();
            return;
        }

        System.out.println("==== Service Desk System ====");

        Scanner scanner = new Scanner(System.in);
        LoginUI loginUI = new LoginUI();

        while (true) {

            System.out.println("Do you wish to log in:\t(y/n)");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("y")) {

                loginUI.start(scanner);

            } else if (input.equalsIgnoreCase("n")) {

                System.out.println("Goodbye.");
                break;

            } else {

                System.out.println("Please type y or n!");

            }
        }

        scanner.close();
    }

    private static void initializeDatabase() throws IOException {

        File dataDir = new File("data");

        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        extractIfMissing("users.txt");
        extractIfMissing("tickets.txt");
        extractIfMissing("categories.txt");
    }

    private static void extractIfMissing(String fileName) throws IOException {

        File outputFile = new File("data/" + fileName);

        if (!outputFile.exists()) {

            InputStream in = Main.class.getResourceAsStream("/data/" + fileName);

            if (in == null) {
                throw new RuntimeException("Missing resource: /data/" + fileName);
            }

            Files.copy(in, outputFile.toPath());
            in.close();
        }
    }
}