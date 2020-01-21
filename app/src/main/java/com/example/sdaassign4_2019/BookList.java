package com.example.sdaassign4_2019;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Images used are sourced from Public Domain Day 2019.
 * by Duke Law School's Center for the Study of the Public Domain
 * is licensed under a Creative Commons Attribution-ShareAlike 3.0 Unported License.
 *  - Images are stored on the Firebase DB and retrieved here in this fragment view
 *  - This Fragment class was adapted from the Assignment 4 project zip file
 * A simple {@link Fragment} subclass.
 * @author Colin Fleck
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
     * @return
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

        //Firebase reference to the book data
        db = FirebaseDatabase.getInstance().getReference().child("book");
        //Listener that updates the bookData list with the current data in the database
        db.addValueEventListener(new ValueEventListener() {
            /**
             * OnDataChange method checks the database for any new data and updates
             * the bookDaata object with the current data
             * @param dataSnapshot
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
                    //Notify the recyclerView that new data has been set

                }
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return root;
    }


}

