package com.library;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Library implements AutoCloseable {
    private List<Book> books;
    private Scanner scanner;
    private static final String DATA_FILE = "library_data.ser";

    public Library() {
        this.scanner = new Scanner(System.in);
        loadData();
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            books = (List<Book>) ois.readObject();
        } catch (FileNotFoundException e) {
            books = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка загрузки данных: " + e.getMessage());
            books = new ArrayList<>();
        }
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения данных: " + e.getMessage());
        }
    }

    public void start() {
        try {
            while (true) {
                printMenu();
                int choice = safeReadInt("Выберите действие", 0, 5);
                handleUserChoice(choice);
            }
        } finally {
            saveData();
        }
    }

    private void printMenu() {
        System.out.println("\n--- Управление библиотекой ---");
        System.out.println("1. Добавить книгу");
        System.out.println("2. Показать все книги");
        System.out.println("3. Найти книгу");
        System.out.println("4. Обновить книгу");
        System.out.println("5. Удалить книгу");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void handleUserChoice(int choice) {
        switch (choice) {
            case 1 -> addBook();
            case 2 -> showAllBooks();
            case 3 -> findBook();
            case 4 -> updateBook();
            case 5 -> deleteBook();
            case 0 -> {
                System.out.println("Выход из программы...");
                System.exit(0);
            }
        }
    }

    private void addBook() {
        System.out.println("\n--- Добавление новой книги ---");

        String id = readNonEmptyInput("Введите ID книги: ");
        if (isBookIdExists(id)) {
            System.out.println("Книга с таким ID уже существует!");
            return;
        }

        String title = readNonEmptyInput("Введите название книги: ");
        String author = readNonEmptyInput("Введите автора книги: ");
        int year = safeReadInt("Введите год издания: ", 1450, java.time.Year.now().getValue());
        double price = safeReadDouble("Введите стоимость книги: ", 0, Double.MAX_VALUE);

        Book book = new Book(id, title, author, year, price);
        books.add(book);
        System.out.println("Книга успешно добавлена!");
    }

    private boolean isBookIdExists(String id) {
        return books.stream().anyMatch(b -> b.getId().equalsIgnoreCase(id));
    }

    private String readNonEmptyInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Поле не может быть пустым!");
            }
        } while (input.isEmpty());
        return input;
    }

    private int safeReadInt(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Введите число от %d до %d!%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Введите целое число!");
            }
        }
    }

    private double safeReadDouble(String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Введите число от %.2f до %.2f!%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Введите число!");
            }
        }
    }

    private void showAllBooks() {
        System.out.println("\n--- Список всех книг ---");
        if (books.isEmpty()) {
            System.out.println("В библиотеке нет книг.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private void findBook() {
        System.out.println("\n--- Поиск книги ---");
        System.out.print("Введите ID, название или автора книги: ");
        String searchTerm = scanner.nextLine().trim().toLowerCase();

        List<Book> foundBooks = books.stream()
                .filter(b -> b.getId().toLowerCase().contains(searchTerm) ||
                        b.getTitle().toLowerCase().contains(searchTerm) ||
                        b.getAuthor().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());

        if (foundBooks.isEmpty()) {
            System.out.println("Книги не найдены.");
        } else {
            System.out.println("Найденные книги:");
            foundBooks.forEach(System.out::println);
        }
    }

    private void updateBook() {
        System.out.println("\n--- Обновление книги ---");
        String id = readNonEmptyInput("Введите ID книги для обновления: ");

        Book bookToUpdate = books.stream()
                .filter(b -> b.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);

        if (bookToUpdate == null) {
            System.out.println("Книга с ID " + id + " не найдена.");
            return;
        }

        System.out.println("Текущие данные книги:");
        System.out.println(bookToUpdate);

        System.out.print("Введите новое название (оставьте пустым, чтобы не менять): ");
        String newTitle = scanner.nextLine();
        if (!newTitle.isEmpty()) {
            bookToUpdate.setTitle(newTitle);
        }

        System.out.print("Введите нового автора (оставьте пустым, чтобы не менять): ");
        String newAuthor = scanner.nextLine();
        if (!newAuthor.isEmpty()) {
            bookToUpdate.setAuthor(newAuthor);
        }

        int newYear = safeReadInt("Введите новый год издания (0 - не изменять): ", 0, Integer.MAX_VALUE);
        if (newYear != 0) {
            bookToUpdate.setYear(newYear);
        }

        double newPrice = safeReadDouble("Введите новую стоимость (0 - не изменять): ", 0, Double.MAX_VALUE);
        if (newPrice != 0) {
            bookToUpdate.setPrice((int) newPrice);
        }

        System.out.println("Книга успешно обновлена!");
    }

    private void deleteBook() {
        System.out.println("\n--- Удаление книги ---");
        String id = readNonEmptyInput("Введите ID книги для удаления: ");

        boolean removed = books.removeIf(b -> b.getId().equalsIgnoreCase(id));

        if (removed) {
            System.out.println("Книга успешно удалена!");
        } else {
            System.out.println("Книга с ID " + id + " не найдена.");
        }
    }

    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
        saveData();
    }
}