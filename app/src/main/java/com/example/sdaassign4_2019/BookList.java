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

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


/**
 * Images used are sourced from Public Domain Day 2019.
 * by Duke Law School's Center for the Study of the Public Domain
 * is licensed under a Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *  - Images are stored on the Firebase DB and retrieved here in this fragment view
 *  - This Fragment class was adapted from the Assignment 4 project zip file
 * A simple {@link Fragment} subclass.
 * @author Colin Fleck - colin.fleck3@mail.dcu.ie
 * @version 1
 * @since
 */
public class BookList extends Fragment {
    private static final String TAG = "BookList";

    //List that is filled with data retrieved from Firebase and passed to recyclerViewAdapter
    public  final ArrayList<Book> bookData = new ArrayList<>();
    //DB reference for firebase
    DatabaseReference db;

    public BookList() {
        // Required empty public constructor
    }

    /**
     * onCreateView method builds and inflates this fragment
     * @param inflater sets the layout
     * @param container creates a container for the fragment
     * @param savedInstanceState
     * @return the fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_book_list, container, false);

        //recyclerView declared
        RecyclerView recyclerView = root.findViewById(R.id.bookView_view);

        //RecyclerViewAdapter declared with context and the bookData list
        final LibraryViewAdapter recyclerViewAdapter = new LibraryViewAdapter(getContext(), bookData);

        //Data sent to the LibraryViewAdapter.java file
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        /* Firebase reference to the book data and listener that updates the
         * bookData list with the current data in the database
         */
        db = FirebaseDatabase.getInstance().getReference().child("book");
        db.addValueEventListener(new ValueEventListener() {

            /**
             * OnDataChange method checks the database for any new data and updates
             * the bookDaata object with the current data
             * @param dataSnapshot takes a json snapshot of firebase db ref to book
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //loops though the data in DB with ref book
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    //grabs the author, title, imageUrl, availability via help from the JOVO Book.class
                    String author = snapshot.getValue(Book.class).getAuthor();
                    String title = snapshot.getValue(Book.class).getTitle();
                    String url = snapshot.getValue(Book.class).getImageUrl();
                    boolean available = snapshot.getValue(Book.class).getAvailable();
                    Log.i(TAG, "boolean: " + available);

                    //add the data from the database and put into the bookData list object
                    bookData.add(new Book(title, author, url, available));
                }
                //Notify the recyclerView that new data has been set
                recyclerViewAdapter.notifyDataSetChanged();
            }

            /**
             * method alerts user with db error if there was a
             * problem receiving the data from the db
             * @param databaseError
             */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //alert user with db error
            }
        });

        return root;
    }


}

