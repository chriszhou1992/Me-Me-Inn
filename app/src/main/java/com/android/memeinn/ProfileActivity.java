package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;


public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        TextView userNameView = (TextView)findViewById(R.id.name);
        ParseUser u = ParseUser.getCurrentUser();
        userNameView.setText(u.getUsername());
    }

    /**
     * Callback function for Review button. Triggers an Intent to go to the ReviewActivity.
     * @param view The Button that is clicked.
     */
    public void gotoReview(View view) {
        Intent reviewIntent = new Intent(this, ReviewActivity.class);
        startActivity(reviewIntent);
    }

    /**
     * Callback function for Frineds button. Triggers an Intent to go to the FriendActivity.
     * @param view The Button that is clicked.
     */
    public void onFriends(View view) {
        Intent Friends = new Intent(this, FriendActivity.class);
        startActivity(Friends);
    }

}
