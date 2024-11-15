package com.example.exam14noviembre;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private int pages;
    private String isbn;

    public Book(int id, String title, String author, String publisher, int pages, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.pages = pages;
        this.isbn = isbn;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getPages() {
        return pages;
    }

    public String getIsbn() {
        return isbn;
    }

    @Override
    public String toString() {
        return title;
    }
}