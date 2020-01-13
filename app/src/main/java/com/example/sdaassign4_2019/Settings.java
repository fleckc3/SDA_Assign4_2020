package com.example.sdaassign4_2019;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {

    //Static variables declared for use by sharedpreferences
    private static  final String USER_NAME_KEY = "USER_NAME_KEY";
    private static  final String USER_EMAIL_KEY = "USER_EMAIL_KEY";
    private static  final String USER_ID_KEY = "USER_ID_KEY";

    //textview variable holders initialized
    private TextView userName, userEmail, userId;



    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //shared preference instance used to save user data input
        final SharedPreferences prefs = getActivity().getPreferences(MODE_PRIVATE);

         // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_settings, container, false);

        //Borrower Name - on first create default values are blank
        userName = root.findViewById(R.id.userName);
        userName.setText(prefs.getString(USER_NAME_KEY, ""));

        //Borrower email
        userEmail = root.findViewById(R.id.email);
        userEmail.setText(prefs.getString(USER_EMAIL_KEY, ""));

        //Borrower id
        userId = root.findViewById(R.id.borrowerID);
        userId.setText(prefs.getString(USER_ID_KEY, ""));

        //save button for user data
        final Button saveUserData = root.findViewById(R.id.button);
        saveUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //text that tells what user to check if input is faulty
                String snackMessage = "";

                //check user name input
                String userInputName;
                boolean checkName;
                Log.i(TAG, "borrower name textview empty: " + userName.getText().toString().isEmpty());

                //check if input field is null or blank
                if (userName.getText().toString().isEmpty() || userName.getText().toString().equals(" ")) {
                    //if passes this test, input is blank
                    checkName = false;
                    String nameMessage = " Borrower Name. ";
                    snackMessage = snackMessage + nameMessage;
                } else {
                    //set the input in userInputName variable if it passes the 1st check
                    userInputName = userName.getText().toString();

                    Log.i(TAG, "value of userInputName variable: " + userInputName);

                    //check if input is the same as what is saved in shared preferences
                    if (userInputName != prefs.getString(USER_NAME_KEY, "")) {

                        //if not same then it sets the input to be saved by shared preferences
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(USER_NAME_KEY, userInputName);
                        editor.apply();

                        userName.setText(userInputName);
                    }
                    checkName = true;
                }
                Log.i(TAG, "snackMessage = " + snackMessage);

                if(checkName == false){
                    Snackbar snackbar = Snackbar.make(v, "" + snackMessage, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        return root;
    }





}
