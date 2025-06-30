package com.library;

public class Book {
    private String id;
    private String title;
    private String author;
    private int year;
    private int price;


    public Book(String id, String title, String author, int year, int price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
    }

    // Геттеры и сеттеры
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
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Название: " + title + ", Автор: " + author + ", Год: " + year + "Цена" + price;
    }
}