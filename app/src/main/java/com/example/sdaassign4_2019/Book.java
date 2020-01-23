package com.example.sdaassign4_2019;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Book POJO class used to set and get book details to be used in recyclerView
 *
 * references:
 *        - https://developer.android.com/docs
 *        - https://firebase.google.com/docs
 *
 * @author Colin Fleck - colin.fleck3@mail.dcu.ie
 * @version 1
 */
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
