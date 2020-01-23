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

                //boolean check to see if there is an issue with name
                boolean checkName;
                Log.i(TAG, "borrower name textview empty: " + userName.getText().toString().isEmpty());

                //check if name input field is null or blank
                if (userName.getText().toString().isEmpty() || userName.getText().toString().equals(" ")) {
                    //if passes this test, input is blank
                    checkName = false;
                    String nameMessage = getResources().getString(R.string.borrower_name_for_snack);
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

                        //sets text in th textView field for confirmation
                        userName.setText(userInputName);
                    }

                    //boolean check for snackMessage
                    checkName = true;
                }
                Log.i(TAG, "snackMessage = " + snackMessage);

                //check user email input
                String userInputEmail;
                String validateEmail;

                //boolean check if there is an issue with email
                boolean checkEmail;

                //checks if email field is empty or filled with spaces, if so an alert is added to the snack message
                if(userEmail.getText().toString().isEmpty() || userEmail.getText().toString().equals(" ")) {
                    checkEmail = false;
                    String emailMessage = getResources().getString(R.string.email_for_snackMessage);
                    snackMessage = snackMessage + emailMessage;
                } else {
                    //once email textview has something in it, a finction is called to validate if it the email is in a proper email format
                    validateEmail = userEmail.getText().toString().trim();
                    Log.i(TAG, "check email validate function result: " + isValidEmail(validateEmail));

                    //emailValidate function returns a boolean based off the user input. if true sets the email to shared preferences
                    if (isValidEmail(validateEmail)) {
                        checkEmail = true;
                        userInputEmail = userEmail.getText().toString();
                        //additional check to see if an email been saved prior is the same as the new one being entered
                        if (userInputEmail != prefs.getString(USER_EMAIL_KEY, "")) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(USER_EMAIL_KEY, userInputEmail);
                            editor.apply();

                            //sets email in textview for confirmation
                            userEmail.setText(userInputEmail);
                        }
                    //if isValidEmail() returns false, email alert added to snack message to let user know
                    } else {
                        checkEmail = false;
                        String emailMessage = getResources().getString(R.string.email_for_snackMessage);
                        snackMessage = snackMessage + emailMessage;
                    }
                }

               //check user id input
                String userInputId;
                //boolean check to notify if there is an issue with the user id
                boolean checkId;

                //checks if user id is blank or null and alerts user if so
                if(userId.getText().toString().isEmpty() || userId.getText().toString().equals(" ")){
                    checkId = false;
                    String idMessage = getResources().getString(R.string.borrowerId_for_snackMessage);
                    snackMessage = snackMessage + idMessage;
                } else {
                    userInputId = userId.getText().toString();

                    //if user ID is new and not blank then it is added to shared prefs
                    if(userInputId != prefs.getString(USER_ID_KEY, "")){
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(USER_ID_KEY, userInputId);
                        editor.apply();

                        //id set in texview for confirmation
                        userId.setText(userInputId);
                    }
                    checkId = true;
                }

                Log.i(TAG, "current snackMessage value: " + snackMessage);

                //Checks to see if any of the textviews for name,email, or id.
                if(checkName == false || checkEmail == false || checkId == false){

                    // if so the the snack message alerts users to which section there is an error in
                    Snackbar snackbar = Snackbar.make(v, "" + snackMessage, Snackbar.LENGTH_LONG);

                    /* following lines change color of text to red in the snackbar message to emphasize which textview has an issue
                     * references:
                     *      - https://inneka.com/programming/android/how-to-set-support-library-snackbar-text-color-to-something-other-than-androidtextcolor/
                     *      - https://stackoverflow.com/questions/3282940/set-color-of-textview-span-in-android
                     */
                    View sbView = snackbar.getView();
                    TextView tv = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
                    Spannable inputToCheck = new SpannableString(getResources().getString(R.string.snackMessage_text) + "\n" + snackMessage);
                    inputToCheck.setSpan(new ForegroundColorSpan(Color.RED), 28, inputToCheck.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(inputToCheck);
                    snackbar.show();

                } else {
                    //if no errors in textviews user is notified of details being saved
                    Snackbar snackbar = Snackbar.make(v, getResources().getString(R.string.user_details_saved_correctly), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        //clear data button resets the three textviews to default values in order to restart or change information captured
        final Button clearData = root.findViewById(R.id.clearButton);
        clearData.setOnClickListener(new View.OnClickListener() {
            /**
             * onclick function called when clear data button clicked. clears the shared prefs
             * back to their default states
             *
             * @param v is the cleaer data button clicked
             */
            @Override
            public void onClick(View v) {

                //restore shared prefs to default
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(USER_NAME_KEY, "");
                editor.putString(USER_EMAIL_KEY, "");
                editor.putString(USER_ID_KEY,"");
                editor.apply();

                //updates textViews to confirm clear
                userName.setText("");
                userEmail.setText("");
                userId.setText("");
            }
        });

        return root;
    }

    /**
     *  isValidEmail checks the user email input to make sure it is in proper email format
     *  This method was adapted from the following ref:
     *      - https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
     *      -
     * @param email is passed from the email texview by the user input
     * @return true if the string is in proper email format
     */
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

    /**
     * onSaveInstanceState method saves the user input in the textviews
     * in order for them to still be seen when the app is paused or rotated.
     * @param outState bundle holds the user input data to be carried over when oncreate called again
     */
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(USER_NAME_KEY, userName.getText().toString());
        outState.putString(USER_EMAIL_KEY, userEmail.getText().toString());
        outState.putString(USER_ID_KEY, userId.getText().toString());
        Log.i(TAG, "onSaveInstanceState: " + outState);
    }





}
