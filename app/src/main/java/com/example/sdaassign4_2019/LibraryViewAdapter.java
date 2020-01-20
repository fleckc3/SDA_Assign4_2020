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
import android.net.Uri;
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

/*
 * @author Chris Coughlan 2019
 */
public class LibraryViewAdapter extends RecyclerView.Adapter<LibraryViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private Context mNewContext;
    private ArrayList<Book> bookData;




    LibraryViewAdapter(Context mNewContext, ArrayList<Book> bookData) {
        this.mNewContext = mNewContext;
        this.bookData = bookData;
        Log.i(TAG, "LibraryViewAdapter: " + bookData);
    }

    //declare methods
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: was called");

        viewHolder.authorText.setText(bookData.get(position).getAuthor());
        viewHolder.titleText.setText(bookData.get(position).getTitle());
        //viewHolder.imageItem.setImageResource(mImageID.get(position));

        Glide.with(viewHolder.imageItem.getContext())
                .load(bookData.get(position).getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imageItem);


        //should check here to see if the book is available.
        viewHolder.checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mNewContext);

                String checkString = prefs.getString("USER_NAME_KEY", "");
                Log.i(TAG, "onClick: " + checkString);

                if (!checkString.isEmpty()){
                    Toast.makeText(mNewContext, bookData.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    //...

                    Intent myOrder = new Intent(mNewContext, CheckOut.class);
                    Bundle extras = new Bundle();

                    String title = bookData.get(position).getTitle();
                    // boolean available = bookData.get(position).getAvailability();
                    Log.i(TAG, "onClick: " + title);
                   // Log.i(TAG, "onClick: " + available);

                    boolean availableTest = true;

                    Log.i(TAG, "onClick: " + availableTest);
                    myOrder.putExtra("title", title);
                    myOrder.putExtra("availability", availableTest);
                    mNewContext.startActivity(myOrder);
                } else {
                    Snackbar snackbar = Snackbar.make(v, "Please go to the Settings tab and enter your Borrower details", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: " + bookData.size());
        return bookData.size();
    }

    //view holder class for recycler_list_item.xml
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


