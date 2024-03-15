package com.library;

import java.io.Serializable;
import java.time.Year;
import java.util.Objects;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String author;
    private int year;
    private double price;
    private String genre;
    private boolean available;

    public Book(String id, String title, String author, int year, double price, String genre) {
        setId(id);
        setTitle(title);
        setAuthor(author);
        setYear(year);
        setPrice(price);
        setGenre(genre);
        this.available = true;
    }

    // Getters and setters with validation
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public String getGenre() { return genre; }
    public boolean isAvailable() { return available; }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be empty");
        }
        if (!id.matches("^[A-Za-z0-9-]{3,20}$")) {
            throw new IllegalArgumentException("ID must be 3-20 characters (letters, digits, hyphens)");
        }
        this.id = id;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("Title too long (max 100 characters)");
        }
        this.title = title.trim();
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        if (author.length() > 50) {
            throw new IllegalArgumentException("Author name too long (max 50 characters)");
        }
        this.author = author.trim();
    }

    public void setYear(int year) {
        int currentYear = Year.now().getValue();
        if (year < 1450 || year > currentYear + 1) {
            throw new IllegalArgumentException("Year must be between 1450 and " + (currentYear + 1));
        }
        this.year = year;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (price > 1_000_000) {
            throw new IllegalArgumentException("Price too high (max 1,000,000)");
        }
        this.price = Math.round(price * 100) / 100.0;
    }

    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be empty");
        }
        if (genre.length() > 30) {
            throw new IllegalArgumentException("Genre too long (max 30 characters)");
        }
        this.genre = genre.trim();
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%d) | Genre: %s | Price: %.2f | %s",
                title, author, year, genre, price, available ? "Available" : "Checked out");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}