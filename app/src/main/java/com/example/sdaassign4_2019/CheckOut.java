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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static java.time.LocalDate.parse;

/**
 * <pre>Checkout class defines a view where user can select a date to order a specific book.
 * This view opens from the book recyclerView when a user clicks on the checkout button.
 * Data about that book is passed here to be used in the order details upon checkout.
 * A date selector provides the rest of the data for the order. The order is then sent to
 * firebase database with relevant data.
 *
 * references:
 *        - https://developer.android.com/docs
 *        - https://firebase.google.com/docs
 *
 * @author Colin Fleck - colin.fleck3@mail.dcu.ie
 * @version 1</pre>
 */
public class CheckOut extends AppCompatActivity {
    private static final String TAG = "CheckOut";

    //various variables used throughout class
    private DatabaseReference db;
    TextView mDisplaySummary;
    TextView confirmBookName;
    TextView bookAvailable;
    Button sendOrder;
    Button selectDate;
    Calendar mDateAndTime = Calendar.getInstance();
    String currentDate;
    String mReturnDate;
    String borrowerId;
    String finalSelectedDate;
    String summary;
    boolean dateSelectedCheck;
    boolean sendOrderCheck = false;
    public String title;

    //keys for onRestorInstanceState()
    String summaryKey = "SUMMARY_KEY";
    String selectedDateKey = "SELECTED_DATE_KEY";
    String borrowerIdKey = "BORROWER_ID_KEY";
    String returnDateKey = "RETURN_DATE_KEY";
    String currentDateKey = "CURRENT_DAY_KEY";
    String dateSelectedCheckKey = "SELECTED_BOOLEAN_KEY";
    String titleKey = "TITLE_KEY";
    String sendOrderCheckKey = "SEND_ORDER_KEY";

    /**
     *<pre>This onCreate method declares the textviews and buttons used to select a date and
     * create an order that is saved in the DB. This view recieies data from the recyclerView
     * according to which book item is chosen to checkout.
     * @param savedInstanceState passes saved bundle. </pre>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        /* boolean check to see if date has been selected.
         * Initialised to false when it is created as this view is used
         * each time a book is selected to be ordered. This is important so that
         * the send order button can't be fired using a date that might have been chosen
         * while creating an order for another book using this same view
         */
        dateSelectedCheck = false;

        //set the toolbar we have overridden
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Enables the back button from this activity to the MainActivity
         * reference: https://www.youtube.com/watch?v=JkVdP-e9BCo */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //finds the send and date buttons
        sendOrder = findViewById(R.id.orderButton);
        selectDate = findViewById(R.id.date);

        //declares summary textview
        mDisplaySummary = findViewById(R.id.orderSummary);

        //receives the intent data from the libraryViewAdapter
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        //Gets book title and sets in the TextView field
        title = extras.getString("title");
        Log.i(TAG, "onCreate: " + title);

        //intitlaizes title in the textview
        confirmBookName = findViewById(R.id.confirmName);

        //reference: https://stackoverflow.com/questions/7493287/android-how-do-i-get-string-from-resources-using-its-name
        confirmBookName.setText(getResources().getString(R.string.check_out_book) + title);

        //Gets the boolean availability and uses it to let user know if book is available or not
        boolean available = extras.getBoolean("availability", true);
        bookAvailable = findViewById(R.id.availability);
        if(available){
            //sets text that book is available
            bookAvailable.setText(getResources().getString(R.string.book_is_available));
        } else {
            //book not available so buttons are grayed out
            bookAvailable.setText(getResources().getString(R.string.book_not_available));
            sendOrder.setBackgroundColor(Color.WHITE);
            sendOrder.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            sendOrder.setTextColor(Color.LTGRAY);

            //onClickListener overrides setClickable(false) so had to change it setEnabled(false)
            //reference: https://stackoverflow.com/questions/6812730/setenabled-setclickable-not-working/8241321
            sendOrder.setEnabled(false);

            //select date grayed out
            selectDate.setBackgroundColor(Color.WHITE);
            selectDate.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            selectDate.setTextColor(Color.LTGRAY);
            selectDate.setClickable(false);
        }

        //Firebase database instance initialised with reference to the child order
        db = FirebaseDatabase.getInstance().getReference().child("order");

        //send order onclick listener function
        sendOrder.setOnClickListener(new View.OnClickListener() {

            /**
             * OnClick function creates an order entry into the firebase database.
             * if no date is returned by the updateDateAndDisplay() then user is prompted
             * to select a date.
             *
             * @param v is the send order button that is clicked
             */
            @Override
            public void onClick(final View v) {
                //checks for date selection and if not prompts user to select date
                if (!dateSelectedCheck) {
                    Snackbar snackbar = Snackbar.make(v, getResources().getString(R.string.Select_book_date), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //if sate selected then proceeds to build order and send to database
                } else {
                    //Firebase DB reference to order with push() - creates automatic id in db
                    DatabaseReference newOrderRef = db.push();

                    /* following date manipulation lines of code get the current date and time to be used in */
                    SimpleDateFormat dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date getCurrentDate = new Date();
                    currentDate = dateTime.format(getCurrentDate);

                    Log.i(TAG, "selected date: " + finalSelectedDate);
                    Log.i(TAG, "return date: " + mReturnDate);
                    Log.i(TAG, "currentDate: " + currentDate );


                    //sets the values to be pushed by the newOrderRef db reference
                    newOrderRef.setValue((new Order(title, borrowerId, finalSelectedDate, currentDate, mReturnDate)),
                    new DatabaseReference.CompletionListener() {
                        /**
                         * <pre>onComplete listener checks to make the data was successful inserted into
                         * the DB.
                         * @param databaseError provides DB error occurred while trying to complete.
                         * @param databaseReference provides the reference to the db being listened to.</pre>
                         */
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            //if there is db error then alert user
                            if (databaseError != null) {
                                Snackbar snackbar = Snackbar.make(v, getResources().getString(R.string.order_not_saved) + "\n" + databaseError.getMessage(), Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                //alerts that order was successful
                                Snackbar snackbar = Snackbar.make(v, getResources().getString(R.string.order_made_success), Snackbar.LENGTH_LONG);
                                snackbar.show();

                                //grays out send order button
                                //ref: https://stackoverflow.com/questions/8743120/how-to-grey-out-a-button
                                sendOrder.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                                sendOrder.setTextColor(Color.LTGRAY);
                                sendOrder.setClickable(false);

                                //displays order success message in order summary
                                mDisplaySummary.append("\n" + getResources().getString(R.string.order_success_textview));

                                //boolean used for onRestoreInstanceState() to check the state of the send order button
                                sendOrderCheck = true;
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * <pre>This method invokes an android date picker with listener.
     * @param v is the select date button that fires the method.
     * Method was adapted from source SDA_2019 android course examples ViewGroup demo.</pre>
     */
    public void onDateClicked(View v) {
        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {

            /**
             * onDateSet method get the date selected by user and then additional conversions
             * and manipulations are used to check the dates
             * @param view shows the datePicker widget
             * @param year sets the year
             * @param monthOfYear sets the month
             * @param dayOfMonth sets the day
             */
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                /* Following Date and calendar conversion was adapted from the following references:
                 * https://stackoverflow.com/questions/36265860/java-date-parse-exception
                 * https://attacomsian.com/blog/java-convert-string-to-date
                 * https://www.javatpoint.com/java-get-current-date
                 *
                 * Following lines of code checks the date selected then compares it against
                 * today's date to see if it is in the past.
                 */
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Calendar check = Calendar.getInstance();
                check.set(Calendar.YEAR, year);
                check.set(Calendar.MONTH, monthOfYear);
                check.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date selected = check.getTime();
                Date current = new Date();

                String selectedDateString = dateFormat.format(selected);
                String currentString = dateFormat.format(current);

                //reference: https://examples.javacodegeeks.com/core-java/text/parseexception/java-text-parseexception-how-to-solve-parseexception/
                try {
                    /* formatted and updated dates without the time component so they can be compared
                     * correctly in the if statement below */
                    selected = dateFormat.parse(selectedDateString);
                    current = dateFormat.parse(currentString);
                    Log.i(TAG, "onDateSet: " + selected + " " + current);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                /* If selected date is after or same as today's date, set date to mDateAndTime
                 * reference: https://stackoverflow.com/questions/19109960/how-to-check-if-a-date-is-greater-than-another-in-java
                 */
                if(selected.after(current) || selected.equals(current)){
                    mDateAndTime.set(Calendar.YEAR, year);
                    mDateAndTime.set(Calendar.MONTH, monthOfYear);
                    mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    //method updates date data and formulates summary order message
                    updateDateAndTimeDisplay();

                  //if selected is before today's dte then user notified that date is in the past
                } else if(selected.before(current)) {
                    Toast toast = Toast.makeText(getApplicationContext(), dateFormat.format(selected) + getResources().getString(R.string.date_in_past), Toast.LENGTH_SHORT);
                    View sbView = toast.getView();
                    sbView.setBackgroundColor(Color.parseColor("#CCFF90"));
                    toast.show();
                }
            }
        };

        //brings up the calendar widget
        new DatePickerDialog(CheckOut.this, mDateListener,
                mDateAndTime.get(Calendar.YEAR),
                mDateAndTime.get(Calendar.MONTH),
                mDateAndTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * <pre>This method takes the date selected by the user and formulates the summary message
     * based off what this date. A two week checkout maximum is set so a return date is
     * calculated from the selected date. SharedPreferences is invoked to get user name
     * and is for the summary message as well.
     *
     * This method is adapted from the Assignment 4 zip folder. In addition the following referneces were used
     * for Date manipulation:
     *                  * https://stackoverflow.com/questions/36265860/java-date-parse-exception
     *                  * https://attacomsian.com/blog/java-convert-string-to-date
     *                  * https://www.javatpoint.com/java-get-current-date
     * @return selectedDate is used in a check statement for the onClick method called when a user
     * presses the sendOrder button.</pre>
     */
    private void updateDateAndTimeDisplay() {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String borrowerName = prefs.getString("USER_NAME_KEY", "");
        borrowerId = prefs.getString("USER_ID_KEY", "");
        String bookName = title;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date theSelectedDate = mDateAndTime.getTime();
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(theSelectedDate);
        Date selectedDateFormatted = newCalendar.getTime();
        finalSelectedDate = dateFormat.format(selectedDateFormatted);
        Log.i(TAG, "updateDateAndTimeDisplay: " + finalSelectedDate);

        //add two weeks to selected date to get the return date
        //reference: https://stackoverflow.com/questions/23307324/how-do-i-add-2-weeks-to-a-date-in-java/38959426
        int twoWeeks = 14;
        mDateAndTime.add(Calendar.DAY_OF_YEAR, twoWeeks);
        Date reDate = mDateAndTime.getTime();
        mReturnDate = dateFormat.format(reDate);
        Log.i(TAG, "onDateSet: " + mReturnDate);

        //order summary text
        summary = getResources().getString(R.string.borrower_name_summaryMessage) + borrowerName + "\n"
                + getResources().getString(R.string.borrower_id_summaryMessage) + borrowerId + "\n"
                + getResources().getString(R.string.book_title_summaryMessage) + bookName + "\n"
                + getResources().getString(R.string.date_selected_summaryMessage) + finalSelectedDate + "\n"
                + getResources().getString(R.string.return_date_summaryMessage) + mReturnDate
                + getResources().getString(R.string.return_policy_summaryMessage);
        mDisplaySummary.setText(summary);

        //boolean value helps with onclick check for the sendOrder button
        dateSelectedCheck = true;
    }

    /**
     * <pre>This method saves the summary message in case of screen rotation.
     * @param outState is loaded with summary message.</pre>
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Saving needed data to persist through a screen rotation, onPause(), and onStop()
        outState.putString(summaryKey, summary);
        outState.putString(selectedDateKey, finalSelectedDate);
        outState.putBoolean(dateSelectedCheckKey, dateSelectedCheck);
        outState.putString(borrowerIdKey, borrowerId);
        outState.putString(returnDateKey, mReturnDate);
        outState.putString(currentDateKey, currentDate);
        outState.putString(titleKey, title);
        outState.putBoolean(sendOrderCheckKey, sendOrderCheck);
        Log.i(TAG, "onSaveInstanceState: " + outState);
    }

    /**
     * <pre>This method used to get onSaveInstanceState after view restored from screen rotation.
     * This is because of the fragment this activity was started from.
     * reference: https://stackoverflow.com/questions/24075154/how-to-get-data-from-bundle-of-onsaveinstancestate-in-android
     *
     * @param savedInstanceState gets the bundle saved from the onSaveInstanceState().</pre>
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Read values from the savedInstanceState object using their keys
        String restoreSummary = savedInstanceState.getString(summaryKey);
        String restoreSelectedDate = savedInstanceState.getString(selectedDateKey);
        String restoreBorrowerId = savedInstanceState.getString(borrowerIdKey);
        String restoreTitle = savedInstanceState.getString(titleKey);
        String restoreCurrentDate = savedInstanceState.getString(currentDateKey);
        String restoreReturnDate = savedInstanceState.getString(returnDateKey);
        boolean restoreDateIsSelected = savedInstanceState.getBoolean(dateSelectedCheckKey);
        boolean restoreSendOrderButton = savedInstanceState.getBoolean(sendOrderCheckKey);

        //order data restored
        dateSelectedCheck = restoreDateIsSelected;
        mReturnDate = restoreReturnDate;
        currentDate = restoreCurrentDate;
        borrowerId = restoreBorrowerId;
        title = restoreTitle;
        finalSelectedDate = restoreSelectedDate;

        //Summary message and send order button restored to what they were before
        mDisplaySummary.setText(restoreSummary);
        sendOrderCheck = restoreSendOrderButton;

        //greys out send order button if the boolean passed to savedInstanceState is true
        if(sendOrderCheck){
            //grays out send order button
            //ref: https://stackoverflow.com/questions/8743120/how-to-grey-out-a-button
            sendOrder.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            sendOrder.setTextColor(Color.LTGRAY);
            sendOrder.setClickable(false);
        }
    }
}
