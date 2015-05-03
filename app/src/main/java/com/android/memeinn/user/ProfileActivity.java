package com.android.memeinn.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.memeinn.R;
import com.android.memeinn.learn.VocabActivity;
import com.android.memeinn.friend.FriendActivity;
import com.android.memeinn.postquestion.AddPostActivity;
import com.android.memeinn.postquestion.CheckPostActivity;
import com.parse.ParseUser;

/**
 * Activity for displaying the profile (user statistics, words to review etc.)
 * of a user.
 */
public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        TextView userNameView = (TextView)findViewById(R.id.name);
        ParseUser u = ParseUser.getCurrentUser();
        userNameView.setText(u.getUsername());

        //hide check new post option for non-admin users
        Button checkpost = (Button)findViewById(R.id.checknewpost);
        String userID = u.getObjectId();
        //Log.d("profile page: ", adminID);
        if(!userID.equals("Jj6H7TVnGH")){
            Log.d("profile page", " user is not admin");
            checkpost.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Callback function for Review button. Triggers an Intent to go to the vocab list to
     * select the category of words to review.
     * @param view The Button that is clicked.
     */
    public void gotoReview(View view) {
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

    /**
     * Callback function for Check Post button(available to admin user only). Triggers an Intent to go to the CheckPostActivity.
     * @param view The Button that is clicked.
     */
    public void gotoCheckPost(View view) {
        Intent CheckPost = new Intent(this, CheckPostActivity.class);
        startActivity(CheckPost);
    }

    /**
     * Callback function for Add Post button. Triggers an Intent to go to the AddPostActivity.
     * @param view The Button that is clicked.
     */
    public void gotoAddPost(View view) {
        Intent AddPost = new Intent(this, AddPostActivity.class);
        startActivity(AddPost);
    }

    /**
     * Callback function for Log Out button. Triggers an Intent to go to the LoginActivity.
     * @param view The Button that is clicked.
     */
    public void onLogOut(View view){
        ParseUser.logOut();
        Intent LogOut = new Intent(this, LoginActivity.class);
        startActivity(LogOut);
    }

}
