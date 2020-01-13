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



        return root;
    }



    

}
