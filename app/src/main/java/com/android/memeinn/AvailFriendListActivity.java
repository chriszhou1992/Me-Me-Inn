package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.parse.ParseUser;

import java.util.HashMap;

/**
 * Created by bchen11 on 3/13/15.
 * activity to see all the available friend to match with
 */
public class AvailFriendListActivity extends Activity {

    public Firebase usersRef; //reference object of Firebase
    public LinearLayout ll;
    public HashMap<String, Button> userBtnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.availablefriendlist);
        userBtnMap = new HashMap<>();
        usersRef = FirebaseSingleton.getInstance("users");

        ll = (LinearLayout) findViewById(R.id.buttonField);
        //thanks to http://stackoverflow.com/questions/7195056/how-do-i-programmatically-add-buttons-into-layout-one-by-one-in-several-lines

        Query onlineRef = usersRef.orderByChild("isOnline").limitToLast(50);
        onlineRef.addChildEventListener(new ChildEventListener() {
            private String currentUsername = ParseUser.getCurrentUser().getUsername();

            /**
             * Real-time function monitoring the available user list,
             * so fetching has 3 constraints:
             * 1. this user must be online(a child of "onLineUser")
             * 2. this user must not be in a game(not inside game mode)
             * 3. this user must be a friend of user(not implemented)
             * @param dataSnapshot
             */
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                Log.d("userList", "the checkout list " + dataSnapshot.getValue().toString());
                Log.d("userList", "listKey " + dataSnapshot.getKey());

                String username = dataSnapshot.getKey();
                //traverse the user list to fetch the available users
                HashMap userInfo = dataSnapshot.getValue(HashMap.class);

                //don't display the current user
                if (username.equals(currentUsername))
                    return;

                Button userBtn = createUserButton();
                userBtn.setText(username);
                setUserButtonOnlineStatus(userBtn, (Boolean)userInfo.get("isOnline"));
                userBtnMap.put(username, userBtn);
                ll.addView(userBtnMap.get(username));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
                String username = dataSnapshot.getKey();
                if (username.equals(currentUsername))
                    return;
                Button b = userBtnMap.get(username);
                setUserButtonOnlineStatus(b, dataSnapshot.child("isOnline")
                        .getValue(Boolean.class));
                b.setText(username);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Button b = userBtnMap.remove(dataSnapshot.getKey());
                ll.removeView(b);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildKey) {

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
     * Function that handles to logic of creating a button style suitable for representing
     * a user.
     * @return A reference to the newly created Button object.
     */
    private Button createUserButton() {
        Button userBtn = new Button(getApplicationContext());
        userBtn.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        userBtn.setGravity(Gravity.CENTER_HORIZONTAL);

        //send opponent user name to the contest page
        userBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startConstest(v);
            }
        });
        return userBtn;
    }
    /**
     * Function that changes the display of Button b depending on whether the user
     * that button represents is online or not. A Boolean metadata is also stored onto Button
     * b indicating whether the user is online or not.
     * @param b
     * @param isOnline
     */
    private void setUserButtonOnlineStatus(Button b, Boolean isOnline) {
        b.setTag(isOnline);
        if (isOnline)
            b.setBackgroundColor(Color.GREEN);
        else
            b.setBackgroundColor(Color.RED);
    }

    /**
     * Event for displaying friends, later when friendlist functionality is realized,
     * can use to filter the users instead of having everyone in the display
     * @param view
     */
    public void checkOutFriend(View view){
        ((TextView)findViewById(R.id.checkOutClicking)).setText("See available friends");
    }

    /**
     * onClick event to fire up an intent to the contest page.
     * @param view
     */
    public void startConstest(View view) {
        if (!(Boolean)view.getTag())
            return;
        String oppoName = ((Button)view).getText().toString();
        Log.d("avail", "chosen oppo name is " + oppoName);
        //Query findUser = myFirebaseRef.child("onLineUser")
        Intent contestIntent = new Intent(this, ContestActivity.class);
        contestIntent.putExtra("opponame", oppoName);
        startActivity(contestIntent);
    }

}
