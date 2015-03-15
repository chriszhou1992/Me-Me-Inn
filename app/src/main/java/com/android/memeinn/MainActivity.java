package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.parse.ParseUser;


public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        updateUserOnlinePresence();
    }

    private void updateUserOnlinePresence() {
        Firebase connectedRef = FirebaseSingleton.getInstance("/.info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                if (connected) {    //change user status to online in Firebase
                    //get Firebase reference to the logged in user
                    Firebase userRef = FirebaseSingleton.getInstance("users")
                            .child(ParseUser.getCurrentUser().getUsername());
                    //log this connection to this user's online presence data
                    Firebase con = userRef.child("connections").push();
                    con.setValue(Boolean.TRUE);
                    //add listener to remove this connection when disconnected
                    con.onDisconnect().removeValue();
                    //update last time online when disconnected
                    userRef.child("lastOnline").onDisconnect().setValue(ServerValue.TIMESTAMP);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("myapp", "Listener cancelled at .info/connected");
            }
        });
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
        //myFirebaseRef.child("users").child(ParseUser.getCurrentUser().getUsername())
        //        .setValue(ParseUser.getCurrentUser());
//        FirebaseObj fireRef = new FirebaseObj(myFirebaseRef);
//        availFriend.putExtra("fireRef", fireRef);
        startActivity(availFriend);
    }
}
