package com.library;

import java.util.regex.Pattern;

public class Book {
    // Добавляем статусы книги
    public enum Status { AVAILABLE, BORROWED, UNDER_REPAIR }

    private String id;
    private String title;
    private String author;
    private int year;
    private double price;
    private String category;
    private Status status;



    public Book(String id, String title, String author, int year, double price) {
        if (!Pattern.matches("^[A-Za-z0-9]{3,10}$", id)) {
            throw new IllegalArgumentException("ID должен содержать 3-10 букв/цифр");
        }
        if (year < 1450 || year > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("Некорректный год издания");
        }
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
        this.category = category;
        this.status = Status.AVAILABLE;
    }

    // Геттеры и сеттеры


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Название: " + title + ", Автор: " + author + ", Год: " + year + ", Цена: " + price;
    }
}