package com.android.memeinn;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

    }

    public void signUp(View view) {
        EditText usernameField = (EditText) findViewById(R.id.uname);
        EditText passField = (EditText) findViewById(R.id.pword);
        EditText passVerifyField = (EditText) findViewById(R.id.pword2);

        String username = usernameField.getText().toString();
        String pass = passField.getText().toString();
        String verifyPass = passVerifyField.getText().toString();

        if (username.equals("") || pass.equals("") ||
                verifyPass.equals("") || !verifyPass.equals(pass)) {
            Utility.warningDialog(this, "Bad Input", "Username/Password is not in correct format.");
            return;
        }

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(pass);
        //user.setEmail("email@example.com");
        //user.put("phone", "650-555-0000");

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainIntent);
                    Log.d("MyApp", "Success");
                } else {
                    //failed
                    Log.d("MyApp", e.getMessage());
                    Utility.warningDialog(SignUpActivity.this, "SignUp Failed", e.getMessage());
                }
            }
        });
    }

    public void gotoLogin(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

}



