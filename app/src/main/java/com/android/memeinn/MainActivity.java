package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.client.Firebase;
import com.parse.ParseUser;


public class MainActivity extends Activity{

    public Firebase myFirebaseRef; //reference object of fireB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        //test firbase functionality
        myFirebaseRef = new Firebase("https://me-me-inn.firebaseio.com/");
        myFirebaseRef.child("onLineUser").child(ParseUser.getCurrentUser().getUsername())
                .setValue(ParseUser.getCurrentUser());

//        ParseQuery<ParseObject> query = ParseQuery.getQuery("GRE");
//        query.getInBackground("CMXG79JFqT", new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                if (e == null) {
//                    Log.d("MyApp", "get the object " + object.get("word"));
//                } else {
//                    // something went wrong
//                    Log.d("MyApp", "Error: " + e.getMessage());
//                }
//            }
//        });
    }


    public void gotoVocab(View view) {
        Intent vocabIntent = new Intent(this, VocabActivity.class);
        startActivity(vocabIntent);
    }

    public void gotoProfile(View view) {
        Intent profIntent = new Intent(this, ProfileActivity.class);
        startActivity(profIntent);
    }

    /**
     * TODO:temporary direct to the contest, need a transition page for match with friend
     * @param view
     */
    public void goMatchWithFriend(View view){
        Intent availFriend = new Intent(this, AvailFriendListActivity.class);
        //contestIntent.putExtra("userId", ParseUser.getCurrentUser().getObjectId());
        myFirebaseRef.child("onLineUser").child(ParseUser.getCurrentUser().getUsername())
                .setValue(ParseUser.getCurrentUser());
//        FirebaseObj fireRef = new FirebaseObj(myFirebaseRef);
//        availFriend.putExtra("fireRef", fireRef);
        startActivity(availFriend);


    }
}
