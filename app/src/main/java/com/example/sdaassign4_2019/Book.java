package com.example.sdaassign4_2019;

public class Book {

    public Book(){

    }
    private String title;
    private String author;
    private String imageUrl;
    private boolean available;


    public Book(String title, String author, String imageUrl, Boolean available){
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.available = available;

    }


    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean getAvailable() { return available; }

    public void setAvailable(boolean available) { this.available = available; }




}
