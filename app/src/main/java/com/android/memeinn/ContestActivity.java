package com.android.memeinn;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.parse.ParseUser;

/**
 * Created by bchen11 on 3/11/15.
 * Main contest activity
 */
public class ContestActivity extends Activity {
    public Firebase myFirebaseRef; //reference object of fireB
    private String userId;
    private TextView randomScore;

    public ContestActivity() {

    }

    /**
     *init firebase, get the opponent user name and set a pair relation in the firebase to
     * indicate that the two users are in a game
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contest);

        //test firbase functionality
        myFirebaseRef = new Firebase("https://me-me-inn.firebaseio.com/");

        //randomScore = (TextView)findViewById(R.id.userScore);
        //cycleUpdate();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String opponame = extras.getString("opponame");
            TextView oppoNameView = (TextView) findViewById(R.id.opponent);
            oppoNameView.setText(opponame);

            String username = ParseUser.getCurrentUser().getUsername();
            TextView userNameView = (TextView) findViewById(R.id.user);
            userNameView.setText(username);
            //set the ingame pair to indicate the game in progress
            myFirebaseRef.child("GamingPair").child(username + " ~ "
                    + opponame).setValue(true);
        }


    }

    /**
     * test connection function, disregarded currently
     * @param view
     */
    public void startChange(View view){
        Thread change = new Thread(new Runnable() {
            @Override
            public void run() {
                changeValue();
            }
        });
        change.start();
    }

    /**
     * connection test function, disregard currently
     */
    private void cycleUpdate(){
        myFirebaseRef.child("onLineUser").child(ParseUser.getCurrentUser().getUsername())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Long randomInt = (Long)snapshot.getValue();
                        randomScore.setText("score is " + randomInt);
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });

    }

    /**
     * test connection function. disregard
     */
    private void changeValue()  {
        while(true){
            Log.d("fire", "value change cycle");

            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
//            myFirebaseRef.child("onLineUser").child(ParseUser.getCurrentUser().getUsername())
//                    .setValue(randomInt(1, 10));
        }

    }




}
