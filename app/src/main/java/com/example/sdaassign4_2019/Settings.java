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

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.preference.PreferenceManager;
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
import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * This fragment class provides three textviews where a user must enter their details
 * in order to check a book out. The user details are validated and saved to the apps
 * SharedPreferences.
 *
 * Adapted from the Assignment 4 project zip folder prov
 *
 * @author Colin Fleck - colin.fleck3@mail.dcu.ie
 * @version 1
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

    /**
     * onCreateView inflates our fragment view
     * @param inflater sets the fragment layout
     * @param container sets the container for the view
     * @param savedInstanceState gets the saved data called by the onSaveInstanceState()
     * @return the completed fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //shared preference instance used to save user data input
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

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
            /**
             * onClick method called to save data to shared preferences. Has to pass
             * input validation to be saved properly. user notified by which section textview
             * there is an error in.
             * @param v is the save user info button
             */
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


                //check user email input
                String userInputEmail;
                String validateEmail;
                boolean checkEmail;

                if(userEmail.getText().toString().isEmpty() || userEmail.getText().toString().equals(" ")) {
                    checkEmail = false;
                    String emailMessage = " Email Address. ";
                    snackMessage = snackMessage + emailMessage;
                } else {
                    validateEmail = userEmail.getText().toString().trim();
                    Log.i(TAG, "check email validate function result: " + isValidEmail(validateEmail));
                    if (isValidEmail(validateEmail)) {
                        checkEmail = true;
                        userInputEmail = userEmail.getText().toString();
                        if (userInputEmail != prefs.getString(USER_EMAIL_KEY, "")) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(USER_EMAIL_KEY, userInputEmail);
                            editor.apply();

                            userEmail.setText(userInputEmail);
                        }

                    } else {
                        checkEmail = false;
                        String emailMessage = " Email Address. ";
                        snackMessage = snackMessage + emailMessage;
                    }
                }

                String userInputId;
                boolean checkId;
                if(userId.getText().toString().isEmpty() || userId.getText().toString().equals(" ")){
                    checkId = false;
                    String idMessage = " Borrower ID ";
                    snackMessage = snackMessage + idMessage;
                } else {
                    userInputId = userId.getText().toString();
                    if(userInputId != prefs.getString(USER_ID_KEY, "")){
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(USER_ID_KEY, userInputId);
                        editor.apply();

                        userId.setText(userInputId);
                    }
                    checkId = true;
                }

                Log.i(TAG, "current snackMessage value: " + snackMessage);


                if(checkName == false || checkEmail == false || checkId == false){
                    Snackbar snackbar = Snackbar.make(v, "" + snackMessage, Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView tv = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
                    Spannable inputToCheck = new SpannableString("Please check the following: " + "\n" + snackMessage);
                    inputToCheck.setSpan(new ForegroundColorSpan(Color.RED), 28, inputToCheck.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(inputToCheck);
                    snackbar.show();

                } else {
                    Snackbar snackbar = Snackbar.make(v,"Details saved successfully", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });

        final Button clearData = root.findViewById(R.id.clearButton);
        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(USER_NAME_KEY, "");
                editor.putString(USER_EMAIL_KEY, "");
                editor.putString(USER_ID_KEY,"");
                editor.apply();

                userName.setText("");
                userEmail.setText("");
                userId.setText("");
            }
        });


        return root;
    }

    public final static boolean isValidEmail(String email) {
        boolean validate;
        String checkEmail = email;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(checkEmail.matches(emailPattern)) {
            validate = true;
        } else {
            validate = false;
        }
        return validate;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(USER_NAME_KEY, userName.getText().toString());
        outState.putString(USER_EMAIL_KEY, userEmail.getText().toString());
        outState.putString(USER_ID_KEY, userId.getText().toString());
        Log.i(TAG, "onSaveInstanceState: " + outState);
    }





}
