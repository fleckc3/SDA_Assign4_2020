package com.example.sdaassign4_2019;

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

public class CheckOut extends AppCompatActivity {
    private static final String TAG = "CheckOut";
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
    public String title;
    String topicKey;
    String dateReq;


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);


        //set the toolbar we have overridden
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Enables the back button from this activity to the MainActivity
         * reference: https://www.youtube.com/watch?v=JkVdP-e9BCo */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //finds the send and date buttons
        sendOrder = findViewById(R.id.orderButton);
        selectDate = findViewById(R.id.date);

        //receives the intent data from the libraryViewAdapter
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        //Gets book title and sets in the TextView field
        title = extras.getString("title");
        Log.i(TAG, "onCreate: " + title);

        confirmBookName = findViewById(R.id.confirmName);
        confirmBookName.setText(getResources().getString(R.string.check_out_book) + title);

        //Gets the boolean availability and uses it to let user know if book is available or not
        boolean available = extras.getBoolean("availability", true);
        bookAvailable = findViewById(R.id.availability);
        if(available){
            //sets text that book is available
            bookAvailable.setText(getResources().getString(R.string.book_is_available));
        } else {
            //book not available so buttons are greyed out
            bookAvailable.setText(getResources().getString(R.string.book_not_available));
            sendOrder.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            sendOrder.setTextColor(Color.LTGRAY);
            sendOrder.setClickable(false);

            selectDate.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            selectDate.setTextColor(Color.LTGRAY);
            selectDate.setClickable(false);

        }

        //find the summary textview
        mDisplaySummary = findViewById(R.id.orderSummary);

        //Firebase databse instance initialised with reference to the child order
        db = FirebaseDatabase.getInstance().getReference().child("order");

        //send order onclick listener function
        sendOrder.setOnClickListener(new View.OnClickListener() {

            /**
             * OnClick function creates an order entry into the firebase database.
             * if no date is returned by the updateDateAndDisplay() then user is prompted
             * to select a date.
             * @param v is the send order button that is clicked
             */
            @Override
            public void onClick(final View v) {
                //checks for date selection and if not prompts user to select date
                if(updateDateAndTimeDisplay() == null){
                    Snackbar snackbar = Snackbar.make(v, "Please select a date to check the book out.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                //if sate selected then proceeds to build order and send to databse
                } else {
                    //Firebase DB reference to order with push() - creates automatic id in db
                    DatabaseReference newOrderRef = db.push();

                    //follwoing date manipulation lines of code get the current date and time to be used in 
                    SimpleDateFormat dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date getCurrentDate = new Date();
                    currentDate = dateTime.format(getCurrentDate);

                    newOrderRef.setValue((new Order(title, borrowerId, finalSelectedDate, currentDate, mReturnDate)),
                            new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if(databaseError !=  null){
                                Snackbar snackbar = Snackbar.make(v, "Order was not saved, please try again." + "\n" + databaseError.getMessage(), Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar.make(v, "Order made successfully.", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                sendOrder.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                                sendOrder.setTextColor(Color.LTGRAY);
                                sendOrder.setClickable(false);
                                mDisplaySummary.append("\n" + "Order successfully made.");
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * This method invokes an android date picker with listener.
     * @param v is the select date button that fires the method
     * Method was adapted from source SDA_2019 android course examples ViewGroup demo
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

                //If selected date is after or same as todays date, set date to mDateAndTime
                if(selected.after(current) || selected.equals(current)){
                    mDateAndTime.set(Calendar.YEAR, year);
                    mDateAndTime.set(Calendar.MONTH, monthOfYear);
                    mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    //method updates date data and formulates summary order message
                    updateDateAndTimeDisplay();

                  //if selected is before todays dte then user notified that date is in the past
                } else if(selected.before(current)) {
                    Toast.makeText(getApplicationContext(), dateFormat.format(selected) + " is in the past.", Toast.LENGTH_SHORT).show();
                }
            }
        };


        new DatePickerDialog(CheckOut.this, mDateListener,
                mDateAndTime.get(Calendar.YEAR),
                mDateAndTime.get(Calendar.MONTH),
                mDateAndTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * This method takes the date selected by the user and formulates the summary message
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
     * presses the sendOrder button
     */
    private CharSequence updateDateAndTimeDisplay() {
        //date time year
        CharSequence selectedDate = DateUtils.formatDateTime(this, mDateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);
        String stringDate = selectedDate.toString();
        Log.i(TAG, "updateDateAndTimeDisplay: " + stringDate);

        //
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String borrowerName = prefs.getString("USER_NAME_KEY", "");
        borrowerId = prefs.getString("USER_ID_KEY", "");
        String bookName = title;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date theSelectedDate = mDateAndTime.getTime();
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(theSelectedDate);
        Date selectedDateFormatted =newCalendar.getTime();
        finalSelectedDate = dateFormat.format(selectedDateFormatted);

        //add two weeks to selected date to get the return date
        int twoWeeks = 14;
        mDateAndTime.add(Calendar.DAY_OF_YEAR, twoWeeks);
        Date reDate = mDateAndTime.getTime();
        mReturnDate = dateFormat.format(reDate);
        Log.i(TAG, "onDateSet: " + mReturnDate);

        //order summary
        mDisplaySummary.setText("Borrower name: " + borrowerName + "\n" + "Borrower ID: " + borrowerId + "\n" + "Book title: " + bookName + "\n"  + "Date selected: " + finalSelectedDate + "\n" + "Date to return: " + mReturnDate + " (14 day return policy)");
        return selectedDate;
    }


}
