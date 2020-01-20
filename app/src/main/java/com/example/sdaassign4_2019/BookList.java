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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Images used are sourced from Public Domain Day 2019.
 * by Duke Law School's Center for the Study of the Public Domain
 * is licensed under a Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * A simple {@link Fragment} subclass.
 * @author Chris Coughlan
 */
public class BookList extends Fragment {
    private static final String TAG = "BookList";
    public  final ArrayList<Book> bookData = new ArrayList<>();
    DatabaseReference db;


    public BookList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_book_list, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.bookView_view);

        final LibraryViewAdapter recyclerViewAdapter = new LibraryViewAdapter(getContext(), bookData);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        db = FirebaseDatabase.getInstance().getReference().child("book");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    String author = snapshot.getValue(Book.class).getAuthor();
                    String title = snapshot.getValue(Book.class).getTitle();
                    String url = snapshot.getValue(Book.class).getImageUrl();
                    boolean available = snapshot.getValue(Book.class).getAvailability();
                    bookData.add(new Book(author, title, url, available));

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

