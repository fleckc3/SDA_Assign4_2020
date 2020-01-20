package com.example.sdaassign4_2019;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CheckOut extends AppCompatActivity {

    TextView mDisplaySummary;
    TextView confirmBookName;
    TextView bookAvailable;
    Calendar mDateAndTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        //set the toolbar we have overridden
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //recieves the intent data from the libraryViewAdapter
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        //Gets book title and sets in the TextView field
        String title = extras.getString("title");
        confirmBookName = findViewById(R.id.confirmName);
        confirmBookName.setText("Check out " + title);

        //Gets the boolean availability and uses it to let user know if book is available or not
        boolean available = extras.getBoolean("availability", true);
        bookAvailable = findViewById(R.id.availability);
        if(available){
            bookAvailable.setText("This book is available.");
        } else {
            bookAvailable.setText("This book is not available at this time.");
        }

        //find the summary textview
        mDisplaySummary = findViewById(R.id.orderSummary);
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

    private void updateDateAndTimeDisplay() {
        //date time year
        CharSequence currentTime = DateUtils.formatDateTime(this, mDateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);
        CharSequence SelectedDate = DateUtils.formatDateTime(this, mDateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);
        String finalSummary = SelectedDate + " current time is " + currentTime;
        mDisplaySummary.setText(finalSummary);
    }
}
