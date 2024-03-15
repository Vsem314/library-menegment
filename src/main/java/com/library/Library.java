package com.library;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class Library {
    private static final Logger logger = Logger.getLogger(Library.class.getName());
    private static final String DATA_FILE = "library_data.ser";
    private final List<Book> books;

    public Library() {
        this.books = new ArrayList<>();
        loadData();
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            logger.info("Data file not found. New file will be created.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                List<Book> loadedBooks = (List<Book>) obj;
                books.addAll(loadedBooks);
                logger.info("Data loaded successfully. Loaded books: " + loadedBooks.size());
            }
        } catch (InvalidClassException e) {
            logger.warning("Incompatible data version: " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("Error loading data: " + e.getMessage());
        }
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(books);
            logger.info("Data saved successfully. Saved books: " + books.size());
        } catch (IOException e) {
            logger.severe("Error saving data: " + e.getMessage());
        }
    }

    public void addBook(Book book) {
        Objects.requireNonNull(book, "Book cannot be null");
        if (getBookById(book.getId()) != null) {
            throw new IllegalArgumentException("Book with ID " + book.getId() + " already exists");
        }
        books.add(book);
        logger.info("Added new book: " + book.getId());
    }

    public List<Book> getAllBooks() {
        return Collections.unmodifiableList(books);
    }

    public Book getBookById(String id) {
        return books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String lowerQuery = query.toLowerCase();
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lowerQuery) ||
                        b.getAuthor().toLowerCase().contains(lowerQuery) ||
                        b.getGenre().toLowerCase().contains(lowerQuery) ||
                        b.getId().toLowerCase().contains(lowerQuery))
                .toList();
    }

    public void updateBook(String id, Book newData) {
        Book book = getBookById(id);
        if (book == null) {
            throw new IllegalArgumentException("Book with ID " + id + " not found");
        }
        if (newData.getTitle() != null) book.setTitle(newData.getTitle());
        if (newData.getAuthor() != null) book.setAuthor(newData.getAuthor());
        if (newData.getYear() > 0) book.setYear(newData.getYear());
        if (newData.getPrice() >= 0) book.setPrice(newData.getPrice());
        if (newData.getGenre() != null) book.setGenre(newData.getGenre());
        book.setAvailable(newData.isAvailable());
        logger.info("Updated book: " + id);
    }

    public boolean deleteBook(String id) {
        boolean removed = books.removeIf(b -> b.getId().equals(id));
        if (removed) {
            logger.info("Deleted book: " + id);
        }
        return removed;
    }

    public boolean isIdUnique(String id) {
        return books.stream().noneMatch(b -> b.getId().equals(id));
    }
}