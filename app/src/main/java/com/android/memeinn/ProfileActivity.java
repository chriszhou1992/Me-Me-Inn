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
    public void gotoVocab(View view) {
        Intent vocabIntent = new Intent(this, VocabActivity.class);
        vocabIntent.setFlags(0);//flag: 0 for review, 1 for learn
        startActivity(vocabIntent);
    }

    /**
     * Callback function for Friends button. Triggers an Intent to go to the FriendActivity.
     * @param view The Button that is clicked.
     */
    public void onFriends(View view) {
        Intent Friends = new Intent(this, FriendActivity.class);
        startActivity(Friends);
    }

}
