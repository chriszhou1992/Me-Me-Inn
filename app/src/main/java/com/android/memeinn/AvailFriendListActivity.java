package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by bchen11 on 3/13/15.
 * activity to see all the available friend to match with
 */
public class AvailFriendListActivity extends Activity {

    public Firebase myFirebaseRef; //reference object of fireB
    public LinearLayout ll;
    public Activity self;
    public ArrayList<Button> defButtons;
    //private HashMap<String, Object> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.availablefriendlist);
        self = new Activity();
        defButtons = new ArrayList<>();
        defButtons.add(new Button(this));
        defButtons.get(0).getContext(); //have a default button and use its context
        myFirebaseRef = new Firebase("https://me-me-inn.firebaseio.com/");
        Log.d("userList", "go to the callback");

        ll = (LinearLayout) findViewById(R.id.buttonField);
        //thanks to http://stackoverflow.com/questions/7195056/how-do-i-programmatically-add-buttons-into-layout-one-by-one-in-several-lines
       // Query findUserList =  myFirebaseRef.child("onlineUser").orderByValue();

        myFirebaseRef.child("onLineUser").addValueEventListener(new ValueEventListener() {
            /**
             * realtime function monitoring the available user list,
             * so fetching has 3 constraints:
             * 1. this user must be online(a child of "onLineUser")
             * 2. this user must not be in a game(not inside game mode)
             * 3. this user must be a friend of user(not implemented)
             * @param snapshot
             */
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("userList", "the checkout list " + snapshot.getValue().toString());
                //traverse the user list to fetch the available users
                Iterator<DataSnapshot> userEntry = snapshot.getChildren().iterator();

                int i = 1;
                //traverse through each data point to set up the available user button list
                for (;userEntry.hasNext();){
                    HashMap<String, Object> ds =(HashMap<String, Object>)userEntry.next().getValue();
//                    Button btnTag = new Button(self);
//                    //TODO: how to set a button context
                    defButtons.add(new Button(defButtons.get(0).getContext()));
                    defButtons.get(i).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    defButtons.get(i).setText(ds.get("username").toString());
                    defButtons.get(i).setGravity(Gravity.CENTER_HORIZONTAL);
                    //send opponent user name to the contest page
                    defButtons.get(i).setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {
                            startConstest(v);
                        }
                    });
                    ll.addView(defButtons.get(i));
                    i++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("userList", "list fail " + firebaseError.getMessage());
            }
        });

        //thanks to http://stackoverflow.com/questions/1066589/iterate-through-a-hashmap

        //TODO: How to set the buttons in the center? Currently just work around by set layout to the right


    }

    /**
     * event for displaying friends, later when friendlist functionality is realized,
     * can use to filter the users instead of having everyone in the display
     * @param view
     */
    public void checkOutFriend(View view){

        ((TextView)findViewById(R.id.checkOutClicking)).setText("See available friends");
    }

    /**
     * on click event to intent to the contest page
     * @param view
     */
    public void startConstest(View view){
        String oppoName = ((Button)view).getText().toString();
        Log.d("avail", "chosen oppo name is " + oppoName);
        //Query findUser = myFirebaseRef.child("onLineUser")
        Intent contestIntent = new Intent(this, ContestActivity.class);
        contestIntent.putExtra("opponame", oppoName);
        startActivity(contestIntent);
    }

}
