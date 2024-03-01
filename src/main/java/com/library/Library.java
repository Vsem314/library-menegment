package com.library;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Library {
    private List<Book> books;
    private Scanner scanner;

    public Library() {
        books = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n--- Управление библиотекой ---");
            System.out.println("1. Добавить книгу");
            System.out.println("2. Показать все книги");
            System.out.println("3. Найти книгу");
            System.out.println("4. Обновить книгу");
            System.out.println("5. Удалить книгу");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // очистка буфера

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    showAllBooks();
                    break;
                case 3:
                    findBook();
                    break;
                case 4:
                    updateBook();
                    break;
                case 5:
                    deleteBook();
                    break;
                case 0:
                    System.out.println("Выход из программы...");
                    return;
                default:
                    System.out.println("Неверный выбор, попробуйте еще раз.");
            }
        }
    }

    private void addBook() {
        System.out.println("\n--- Добавление новой книги ---");
        System.out.print("Введите ID книги: ");
        String id = scanner.nextLine();

        System.out.print("Введите название книги: ");
        String title = scanner.nextLine();

        System.out.print("Введите автора книги: ");
        String author = scanner.nextLine();

        System.out.print("Введите год издания: ");
        int year = scanner.nextInt();
        scanner.nextLine(); // очистка буфера

        Book book = new Book(id, title, author, year);
        books.add(book);
        System.out.println("Книга успешно добавлена!");
    }

    private void showAllBooks() {
        System.out.println("\n--- Список всех книг ---");
        if (books.isEmpty()) {
            System.out.println("В библиотеке нет книг.");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    private void findBook() {
        System.out.println("\n--- Поиск книги ---");
        System.out.print("Введите ID, название или автора книги: ");
        String searchTerm = scanner.nextLine().toLowerCase();

        List<Book> foundBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getId().toLowerCase().contains(searchTerm) ||
                    book.getTitle().toLowerCase().contains(searchTerm) ||
                    book.getAuthor().toLowerCase().contains(searchTerm)) {
                foundBooks.add(book);
            }
        }

        if (foundBooks.isEmpty()) {
            System.out.println("Книги не найдены.");
        } else {
            System.out.println("Найденные книги:");
            for (Book book : foundBooks) {
                System.out.println(book);
            }
        }
    }

    private void updateBook() {
        System.out.println("\n--- Обновление книги ---");
        System.out.print("Введите ID книги для обновления: ");
        String id = scanner.nextLine();

        Book bookToUpdate = null;
        for (Book book : books) {
            if (book.getId().equals(id)) {
                bookToUpdate = book;
                break;
            }
        }

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

        System.out.print("Введите новый год издания (оставьте 0, чтобы не менять): ");
        int newYear = scanner.nextInt();
        scanner.nextLine(); // очистка буфера
        if (newYear != 0) {
            bookToUpdate.setYear(newYear);
        }

        System.out.println("Книга успешно обновлена!");
    }

    private void deleteBook() {
        System.out.println("\n--- Удаление книги ---");
        System.out.print("Введите ID книги для удаления: ");
        String id = scanner.nextLine();

        Book bookToRemove = null;
        for (Book book : books) {
            if (book.getId().equals(id)) {
                bookToRemove = book;
                break;
            }
        }

        if (bookToRemove == null) {
            System.out.println("Книга с ID " + id + " не найдена.");
            return;
        }

        books.remove(bookToRemove);
        System.out.println("Книга успешно удалена!");
    }
}
