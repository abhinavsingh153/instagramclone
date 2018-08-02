/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

import static com.google.android.gms.analytics.internal.zzy.i;
import static com.google.android.gms.analytics.internal.zzy.k;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {


    TextView changeSignUpModeText;
    Button signupButton;
    Boolean signUpModeActive = true;
    EditText passwordEditText;

    public void showUserList (){

        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

            signup(v);
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.changeSignUpModeTextView) {

            if (signUpModeActive) {
                signUpModeActive = false;
                changeSignUpModeText.setText("or , sign up");
                signupButton.setText("Sign in");
            } else {
                signUpModeActive = true;
                signupButton.setText("Sign up");
                changeSignUpModeText.setText("or, sign in");


            }


        } else if(v.getId()== R.id.backgroundrelativelayout || v.getId()== R.id.instalogo) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);
        }

    }

    public void signup(View view) {


        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);


        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {

            Toast.makeText(this, "A username and password are required . ", Toast.LENGTH_SHORT).show();

        } else {

            if (signUpModeActive) {

                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            Log.i("Sign up : ", "Successful");
                            showUserList();

                        } else {

                            Log.i("Sign up : ", "unSuccessful");
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {

                            showUserList();

                            Log.i("Login", "Successful");

                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        setTitle("Instagram");

        signupButton = (Button) findViewById(R.id.signUpbutton);

        RelativeLayout backGroundRelativeLayout =(RelativeLayout) findViewById(R.id.backgroundrelativelayout);
        ImageView instalogo = (ImageView) findViewById(R.id.instalogo);

        backGroundRelativeLayout.setOnClickListener(this);
        instalogo.setOnClickListener(this);

        changeSignUpModeText = (TextView) findViewById(R.id.changeSignUpModeTextView);
        changeSignUpModeText.setOnClickListener(this);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setOnKeyListener(this);

        //checking if the user is already logged in as the app opens and shows the userList activity as teh app opens
        if(ParseUser.getCurrentUser() != null){

            showUserList();
        }


        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }


}