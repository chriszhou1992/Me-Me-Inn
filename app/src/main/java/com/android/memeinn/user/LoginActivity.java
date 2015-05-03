package com.android.memeinn.user;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

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
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Fix the issue Android changes the font of password fields into monospace
        EditText passField = (EditText) findViewById(R.id.pword);
        passField.setTypeface(Typeface.DEFAULT);

        progressBar = (ProgressBar) findViewById(R.id.spinner);
    }


    /**
     * Callback function for the Login Button. Synchronously signs in the user.
     * @param view
     */
    public void logIn(View view) {
        //prevent multiple login clicks
        if (progressBar.getVisibility() == View.VISIBLE)
            return;
        progressBar.setVisibility(View.VISIBLE);

        EditText usernameField = (EditText) findViewById(R.id.uname);
        EditText passField = (EditText) findViewById(R.id.pword);

        //Use AsyncTask to enable built-in Espresso support for testing async operations
        AsyncTask<String, Void, String> loginTask = createLoginAsyncTask();
        loginTask.execute(usernameField.getText().toString(), passField.getText().toString());
    }

    /**
     * Private function that creates an async task for login.
     * @return AsyncTask
     */
    private AsyncTask<String, Void, String> createLoginAsyncTask() {
        return new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    ParseUser.logIn(params[0], params[1]);
                } catch (ParseException e) {
                    return e.getMessage();
                }
                return null;
            }

            /**
             * Called on UI thread to perform the UI updates after the login operation finishes
             * in the backend.
             * @param errorMsg The returned string for a possible login error. A value of null
             *                 indicates login success.
             */
            @Override
            protected void onPostExecute(String errorMsg) {
                progressBar.setVisibility(View.GONE);
                if (errorMsg == null) { //login success then launch main activity
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Log.d("MyApp", errorMsg);
                    Utility.warningDialog(LoginActivity.this, "Login Failed", errorMsg);
                }
            }
        };
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
        //progressDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);

        List<String> permissions = Arrays.asList("public_profile","email","user_friends");
        // NOTE: for extended permissions, like "user_about_me", your app must be reviewed by the Facebook team
        // (https://developers.facebook.com/docs/facebook-login/permissions/)

        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions
         ,new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {

                if (user == null) {
                    //Log.d("MemeIn", "Uh oh. The user cancelled the Facebook login.");
                    //failed
                     //Log.d("APP", err.getMessage());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    Utility.warningDialog(LoginActivity.this, "Login Failed", err.getMessage());
                } else if (user.isNew()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    //Log.d("MemeIn", "User signed up and logged in through Facebook!");
                } else {
                    //Log.d("MemeIn", "User logged in through Facebook!");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

          }
          );
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
