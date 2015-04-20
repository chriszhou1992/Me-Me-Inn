package com.android.memeinn.user;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.memeinn.MainActivity;
import com.android.memeinn.R;
import com.android.memeinn.Utility;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

/**
 * Default activity of the application. Handles login.
 */
public class LoginActivity extends ActionBarActivity {
    private Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        //Fix the issue Android changes the font of password fields into monospace
        EditText passField = (EditText) findViewById(R.id.pword);
        passField.setTypeface(Typeface.DEFAULT);

        // Check if there is a currently logged in user
        // and it's linked to a Facebook account.
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            // Go to the Main Activity
            showMainActivity();
        }
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
            //Intent mainIntent = new Intent(this, MainActivity.class);
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
        finish();   //destroy activity to prevent going back to sign-in screen
    }


    public void onLoginClick(View v) {
        progressDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);

        List<String> permissions = Arrays.asList("public_profile", "email", "user_friends");
        // NOTE: for extended permissions, like "user_about_me", your app must be reviewed by the Facebook team
        // (https://developers.facebook.com/docs/facebook-login/permissions/)

        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                progressDialog.dismiss();
                if (user == null) {
                    //Log.d("MemeIn", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    //Log.d("MemeIn", "User signed up and logged in through Facebook!");
                    showMainActivity();
                } else {
                    //Log.d("MemeIn", "User logged in through Facebook!");
                    showMainActivity();
                }
            }
        });
    }
    private void showMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
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
