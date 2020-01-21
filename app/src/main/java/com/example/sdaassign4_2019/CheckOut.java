package com.example.sdaassign4_2019;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class CheckOut extends AppCompatActivity {
    private static final String TAG = "CheckOut";
    TextView mDisplaySummary;
    TextView confirmBookName;
    TextView bookAvailable;
    Button sendOrder;
    Button selectDate;
    Calendar mDateAndTime = Calendar.getInstance();
    public String title;

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
        String checkOutDate = "Check out date set for: " + SelectedDate;

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String borrowerName = prefs.getString("USER_NAME_KEY", "");
        String borrowerId = prefs.getString("USER_ID_KEY", "");
        String bookName = title;

        mDisplaySummary.setText("Borrower name: " + borrowerName + "\n" + "Borrower ID: " + borrowerId + "\n" + "Book title: " + bookName);
    }

    private void updateDB() {
        
    }
}
