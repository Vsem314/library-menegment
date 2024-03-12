package com.library;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        try {
            // Графический интерфейс
            LibraryUI.start(library);
        } catch (IOException e) {
            // Если Lanterna не работает, используем консольный интерфейс
            System.err.println("Ошибка графического интерфейса: " + e.getMessage());
            new ConsoleLibrary(library).start();
        }

        library.saveData();
    }
}