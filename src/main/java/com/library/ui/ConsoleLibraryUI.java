package com.library.ui;

import com.library.Book;
import com.library.Library;
import java.time.Year;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ConsoleLibraryUI extends com.library.ui.BaseLibraryUI {
    private final Scanner scanner;
    private boolean running;

    public ConsoleLibraryUI(Library library) {
        super(library);
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    @Override
    public void start() {
        System.out.println("=== LIBRARY MANAGEMENT SYSTEM ===");
        while (running) {
            printMainMenu();
            handleMainMenuInput();
        }
    }

    @Override
    public void shutdown() {
        scanner.close();
        library.saveData();
        System.out.println("Program terminated.");
    }

    private void printMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Manage Books");
        System.out.println("2. Search Books");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
        System.out.print("Select option: ");
    }

    private void handleMainMenuInput() {
        try {
            int choice = readIntInput(0, 3);
            switch (choice) {
                case 1 -> showBookManagementMenu();
                case 2 -> showSearchMenu();
                case 3 -> showStatistics();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice, try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: enter number 0-3");
            scanner.nextLine();
        }
    }

    private void showBookManagementMenu() {
        while (true) {
            System.out.println("\nBook Management:");
            System.out.println("1. Add Book");
            System.out.println("2. List All Books");
            System.out.println("3. Edit Book");
            System.out.println("4. Delete Book");
            System.out.println("0. Back");
            System.out.print("Select option: ");

            try {
                int choice = readIntInput(0, 4);
                if (choice == 0) break;
                switch (choice) {
                    case 1 -> addBookViaConsole();
                    case 2 -> displayAllBooks();
                    case 3 -> editBook();
                    case 4 -> deleteBook();
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: enter number 0-4");
                scanner.nextLine();
            }
        }
    }

    private void showSearchMenu() {
        System.out.println("\nSearch Books:");
        System.out.print("Enter search query (title, author, genre or ID): ");
        String query = scanner.nextLine();

        List<Book> results = library.searchBooks(query);
        if (results.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.println("\nSearch Results:");
            results.forEach(book -> System.out.println("- " + book));
        }
    }

    private void showStatistics() {
        List<Book> books = library.getAllBooks();
        System.out.println("\nLibrary Statistics:");
        System.out.println("- Total books: " + books.size());
        System.out.println("- Available: " + books.stream().filter(Book::isAvailable).count());
        System.out.println("- Checked out: " + books.stream().filter(b -> !b.isAvailable()).count());
        System.out.printf("- Average price: %.2f\n",
                books.stream().mapToDouble(Book::getPrice).average().orElse(0));
    }

    private void addBookViaConsole() {
        System.out.println("\nAdd New Book:");
        String id = readNonEmptyInput("Enter book ID: ");
        String title = readNonEmptyInput("Enter title: ");
        String author = readNonEmptyInput("Enter author: ");
        int year = readYearInput();
        double price = readPriceInput();
        String genre = readNonEmptyInput("Enter genre: ");

        try {
            Book book = new Book(id, title, author, year, price, genre);
            library.addBook(book);
            System.out.println("Book added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void displayAllBooks() {
        List<Book> books = library.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books in library.");
            return;
        }
        System.out.println("\nAll Books:");
        books.forEach(book -> System.out.println("- " + book));
        System.out.println("Total books: " + books.size());
    }

    private void editBook() {
        System.out.println("\nEdit Book:");
        String id = readNonEmptyInput("Enter book ID to edit: ");
        Book book = library.getBookById(id);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.println("Current book data:");
        System.out.println(book);

        System.out.println("\nEnter new data (leave empty to keep current):");
        String newTitle = readOptionalInput("Title", book.getTitle());
        String newAuthor = readOptionalInput("Author", book.getAuthor());
        int newYear = readOptionalYearInput(book.getYear());
        double newPrice = readOptionalPriceInput(book.getPrice());
        String newGenre = readOptionalInput("Genre", book.getGenre());

        try {
            Book updatedBook = new Book(
                    book.getId(),
                    newTitle,
                    newAuthor,
                    newYear,
                    newPrice,
                    newGenre
            );
            updatedBook.setAvailable(book.isAvailable());
            library.updateBook(id, updatedBook);
            System.out.println("Book updated successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteBook() {
        System.out.println("\nDelete Book:");
        String id = readNonEmptyInput("Enter book ID to delete: ");
        if (library.deleteBook(id)) {
            System.out.println("Book deleted successfully!");
        } else {
            System.out.println("Book not found.");
        }
    }

    // Helper methods for input
    private int readIntInput(int min, int max) throws InputMismatchException {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.printf("Enter number %d-%d: ", min, max);
            } catch (InputMismatchException e) {
                scanner.nextLine();
                throw e;
            }
        }
    }

    private String readNonEmptyInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Field cannot be empty!");
            }
        } while (input.isEmpty());
        return input;
    }

    private String readOptionalInput(String fieldName, String currentValue) {
        System.out.printf("%s [%s]: ", fieldName, currentValue);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? currentValue : input;
    }

    private int readYearInput() {
        System.out.print("Publication year: ");
        while (true) {
            try {
                return readIntInput(1450, Year.now().getValue() + 1);
            } catch (InputMismatchException e) {
                System.out.print("Enter valid year: ");
                scanner.nextLine();
            }
        }
    }

    private int readOptionalYearInput(int currentValue) {
        System.out.printf("Publication year [%d]: ", currentValue);
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) return currentValue;
                int year = Integer.parseInt(input);
                if (year >= 1450 && year <= Year.now().getValue() + 1) {
                    return year;
                }
                System.out.print("Enter valid year (1450-current year+1): ");
            } catch (NumberFormatException e) {
                System.out.print("Enter number: ");
            }
        }
    }

    private double readPriceInput() {
        System.out.print("Price: ");
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                double price = Double.parseDouble(input);
                if (price >= 0 && price <= 1_000_000) {
                    return price;
                }
                System.out.print("Enter valid price (0-1,000,000): ");
            } catch (NumberFormatException e) {
                System.out.print("Enter number: ");
            }
        }
    }

    private double readOptionalPriceInput(double currentValue) {
        System.out.printf("Price [%.2f]: ", currentValue);
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) return currentValue;
                double price = Double.parseDouble(input);
                if (price >= 0 && price <= 1_000_000) {
                    return price;
                }
                System.out.print("Enter valid price (0-1,000,000): ");
            } catch (NumberFormatException e) {
                System.out.print("Enter number: ");
            }
        }
    }
}