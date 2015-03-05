package com.android.memeinn;

import com.facebook.Session;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabpage);
    /*
        //Initialize Parse API to initialize connection to cloud
        Parse.initialize(this, "l5qhJIZRq3vPDrHTmyzPu3z6IwMjukw7M3h9A8CZ",
                "iLgCs4Z7I71j1L9DIWrjwjkCZ02yc6KuDsYVO60e");

        //Fix the issue Android changes the font of password fields into monospace
        EditText passField = (EditText) findViewById(R.id.pword);
        passField.setTypeface(Typeface.DEFAULT);
        */
    }


    /**
     * Callback function for the Login Button. Synchronously signs in the user.
     * @param view
     */
    public void logIn(View view) {
        EditText usernameField = (EditText) findViewById(R.id.uname);
        EditText passField = (EditText) findViewById(R.id.pword);

        /*ParseUser.logInInBackground(usernameField.getText().toString(),
                passField.getText().toString(), new LogInCallback() {

            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainIntent);
                } else {
                    //failed
                    Log.d("APP", e.getMessage());
                    Utility.warningDialog(LoginActivity.this, "Login Failed", e.getMessage());
                }
            }
        });*/

        try {
            ParseUser.logIn(usernameField.getText().toString(), passField.getText().toString());
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        } catch (ParseException e) {
            Log.d("MyApp", e.getMessage());
            Utility.warningDialog(LoginActivity.this, "Login Failed", e.getMessage());
        }
    }

    /**
     * Callback function for signUp button. Triggers an Intent to go to the SignUpActivity.
     * @param view
     */
    public void gotoSignUp(View view) {
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpIntent);
    }

    /**
     * Function that uses Parse API to trigger Facebook Single-Signon Dialog.
     * @param view
     */
    public void FBSignOn(View view) {
        Log.d("MyApp", "Inn");
        //FB Single-Signon dialog
        ParseFacebookUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                Log.d("MyApp", "AAA");
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
