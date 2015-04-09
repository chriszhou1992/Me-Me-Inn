package com.android.memeinn.match;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.memeinn.FirebaseSingleton;
import com.android.memeinn.Global;
import com.android.memeinn.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.parse.ParseUser;

/**
 * intermediate class for selecting competition topic
 * Created by bchen11 on 3/31/15.
 */
public class SelectMatchingTopicActivity  extends Activity {
    Firebase usersRef;
    private String userChosen; //boolean indicating whether a topic choice has made
    private String oppoChosen;
    String oppoName;
    private String currentUsername;
    /**
     * start creating
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userChosen = "";
        oppoChosen = "";

        setContentView(R.layout.selectcontesttopic);
        usersRef = FirebaseSingleton.getInstance("users");
        oppoName = getIntent().getStringExtra(Global.EXTRA_MESSAGE_OPPONAME);
        usersRef.child(oppoName).addChildEventListener(createListenerOpponentTopicChoice());

        currentUsername = ParseUser.getCurrentUser().getUsername();

        ((TextView)findViewById(R.id.userName)).setText(currentUsername);
        ((TextView)findViewById(R.id.oppoName)).setText(oppoName);


    }

    private void directToContestPage(){
        if(!oppoChosen.equals("") && !userChosen.equals("")){
            Intent matchStartIntent = new Intent(this, ContestActivity.class);

            matchStartIntent.putExtra(Global.EXTRA_MESSAGE_OPPONAME, oppoName);
            matchStartIntent.putExtra(Global.EXTRA_MESSAGE_USERSELECTEDTOPIC, userChosen);
            matchStartIntent.putExtra(Global.EXTRA_MESSAGE_OPPOSELECTEDTOPIC, oppoChosen);

            startActivity(matchStartIntent);
        }
    }

    private ChildEventListener createListenerOpponentTopicChoice() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String childName = dataSnapshot.getKey();
                //traverse the user list to fetch the available users
                Log.d("childMap", dataSnapshot.toString());
                if(childName.equals("selectedTopic")){
//                    if(!oppoChosen){
//                        oppoChosen = true;
//                        Log.d("childMap", "this is the garbage value");
//                        return;
//                    }
                    View target = findViewById((int) ((long) dataSnapshot.getValue()));
                    target.setBackgroundColor(Color.BLUE);
                    oppoChosen =((Button)target).getText().toString();
                    Log.d("childMap", "oppochosen is " + oppoChosen);

                    directToContestPage();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("userList", "get chosen topic failed " + firebaseError.getMessage());
            }
        };

    }


    public void chooseTopic(View view) {
        if(userChosen.equals("")){
            usersRef.child(currentUsername).child("selectedTopic").setValue
//                    (getResources().getResourceEntryName(view.getId()));
                    (view.getId()); //use id as reference
            view.setBackgroundColor(Color.GREEN);
            userChosen = ((Button)view).getText().toString();
            directToContestPage();
        }

    }

}
