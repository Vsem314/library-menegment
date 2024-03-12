package com.library;


import java.util.Scanner;

public class ConsoleLibrary {
    private final Library library;
    private final Scanner scanner;

    public ConsoleLibrary(Library library) {
        this.library = library;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== Консольный интерфейс библиотеки ===");
            System.out.println("1. Управление книгами");
            System.out.println("2. Выход");
            System.out.print("Выберите: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                library.start();
            } else if (choice == 2) {
                System.out.println("Выход...");
                break;
            }
        }
    }
}
