package com.test.readmelater.models;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by audreyeso on 8/9/16.
 */
@Parcel
public class Book {


    String title;
    String author;
    String isbn;
    String bookImage;
    long id;
    ArrayList<Book> bookArrayList;

    public Book() {

    }

    public Book(String title, String author, String bookImage, long id) {
        this.title = title;
        this.author = author;
        this.bookImage = bookImage;
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

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public ArrayList<Book> getBookArrayList() {
        return bookArrayList;
    }

    public void setBookArrayList(ArrayList<Book> bookArrayList) {
        this.bookArrayList = bookArrayList;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
