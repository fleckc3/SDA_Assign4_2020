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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * LibraryViewAdapter receives the data from the firebase data based on Book object.
 * Book object is initialised and passed to recyclerView vie the BookList fragment.
 * This class was adapted from the Assignement 4 project zip folder.
 *
 * references:
 *         - SDA course text
 *         - https://developer.android.com/docs
 *         - https://firebase.google.com/docs
 *
 * @Author Colin Fleck - colin.fleck3@mail.dcu.ie
 * @version 1
 */
public class LibraryViewAdapter extends RecyclerView.Adapter<LibraryViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    //context and book data object variables declared
    private Context mNewContext;
    private ArrayList<Book> bookData;

    //context and book data object initiliased by data sent from BookList.class
    LibraryViewAdapter(Context mNewContext, ArrayList<Book> bookData) {
        this.mNewContext = mNewContext;
        this.bookData = bookData;
        Log.i(TAG, "LibraryViewAdapter: " + bookData);
    }

    /**
     * This method creates an individual viewHolder for each object passed
     * into the recyclerView
     * @param viewGroup creates the view container
     * @param i determines how many views to make
     * @return the view created for each object
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    /**
     * This method binds the data to the viewHolders
     * @param viewHolder holds the object data for each entry passed into the recyclerView
     * @param position gets the position of the data in the object
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: was called");

        //gets the title and author of book and sets into the appropriate textViews
        viewHolder.titleText.setText(bookData.get(position).getTitle());
        viewHolder.authorText.setText(bookData.get(position).getAuthor());

        //gets the url for the image from firebase storage and loads it into the imageView
        Glide.with(viewHolder.imageItem.getContext())
                .load(bookData.get(position).getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imageItem);

        //checkout onclick listener method
        viewHolder.checkOut.setOnClickListener(new View.OnClickListener() {

            /**
             * onClick method opens the checkout activity if specific validation is met.
             * @param v is the check out button clicked
             */
            @Override
            public void onClick(View v){

                //get shared preferences for whole application
                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mNewContext);

                //String variable used to see if there is user name saved in shared pref
                String checkString = prefs.getString("USER_NAME_KEY", "");
                Log.i(TAG, "onClick: " + checkString);

                //check to see if checkString is not empty
                if (!checkString.isEmpty()){

                    //creates intent to open the checkout activity. Followed by the bundle passed to target class
                    //ref: https://www.dev2qa.com/passing-data-between-activities-android-tutorial/
                    Intent myOrder = new Intent(mNewContext, CheckOut.class);
                    Bundle extras = new Bundle();

                    //gets title and if book is available to checkout
                    String title = bookData.get(position).getTitle();
                    boolean availability = bookData.get(position).getAvailable();
                    Log.i(TAG, "onClick: " + title);
                    Log.i(TAG, "onClick: " + availability);

                    //add title and availability to bundle
                    // https://zocada.com/using-intents-extras-pass-data-activities-android-beginners-guide/
                    extras.putString("title", title);
                    extras.putBoolean("availability", availability);

                    //alerts user of the book being selected to checkout
                    Toast.makeText(mNewContext, bookData.get(position).getTitle(), Toast.LENGTH_SHORT).show();

                    //add title and book availability bundle to intent
                    myOrder.putExtras(extras);
                    mNewContext.startActivity(myOrder);
                } else {
                    //alerts user that no user data has been saved
                    Snackbar snackbar = Snackbar.make(v, mNewContext.getResources().getString(R.string.enter_user_details_snackmessage), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    /**
     * Gets the count of how many objects in bookdata object
     * @return the count
     */
    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: " + bookData.size());
        return bookData.size();
    }

    /**
     * view holder class for recycler_list_item.xml
     * declares UI elements to be used and initialized in each
     * viewholder with data from DB.
     */
    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageItem;
        TextView authorText;
        TextView titleText;
        Button checkOut;
        RelativeLayout itemParentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            //grab the image, the text and the layout id's
            imageItem = itemView.findViewById(R.id.bookImage);
            authorText = itemView.findViewById(R.id.authorText);
            titleText = itemView.findViewById(R.id.bookTitle);
            checkOut = itemView.findViewById(R.id.out_button);
            itemParentLayout = itemView.findViewById(R.id.listItemLayout);

        }
    }
}


