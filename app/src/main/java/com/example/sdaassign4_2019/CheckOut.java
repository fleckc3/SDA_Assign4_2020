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
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);


        //set the toolbar we have overridden
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        db = FirebaseDatabase.getInstance().getReference().child("order");
        sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(updateDateAndTimeDisplay() == null){
                    Snackbar snackbar = Snackbar.make(v, "Please select a date to check the book out.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    DatabaseReference newOrderRef = db.push();
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
                            }
                        }
                    });



                }
            }
        });


    }

    //source SDA_2019 android course examples ViewGroup demo
    public void onDateClicked(View v) {

        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mDateAndTime.set(Calendar.YEAR, year);
                mDateAndTime.set(Calendar.MONTH, monthOfYear);
                mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateDateAndTimeDisplay();

            }
        };

        new DatePickerDialog(CheckOut.this, mDateListener,
                mDateAndTime.get(Calendar.YEAR),
                mDateAndTime.get(Calendar.MONTH),
                mDateAndTime.get(Calendar.DAY_OF_MONTH)).show();

    }

    private CharSequence updateDateAndTimeDisplay() {
        //date time year
        CharSequence selectedDate = DateUtils.formatDateTime(this, mDateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);
        String stringDate = selectedDate.toString();
        Log.i(TAG, "updateDateAndTimeDisplay: " + stringDate);

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

        int twoWeeks = 14;
        mDateAndTime.add(Calendar.DAY_OF_YEAR, twoWeeks);
        Date reDate = mDateAndTime.getTime();
        mReturnDate = dateFormat.format(reDate);
        Log.i(TAG, "onDateSet: " + mReturnDate);

        mDisplaySummary.setText("Borrower name: " + borrowerName + "\n" + "Borrower ID: " + borrowerId + "\n" + "Book title: " + bookName + "\n"  + "Date selected: " + finalSelectedDate + "\n" + "Date to return: " + mReturnDate);
        return selectedDate;
    }


}
