package com.example.sdaassign4_2019;

public class Book {

    public Book(){

    }
    private String title;
    private String author;
    private String imageUrl;
    private boolean availability;

    public Book(String title, String author, String imageUrl, Boolean availability){
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
    }


    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public String getImageUrl() { return imageUrl; }

    public boolean getAvailability() { return availability; }

    public void setAvailability(boolean availability) { this.availability = availability; }




}
