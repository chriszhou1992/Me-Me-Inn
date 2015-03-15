package com.android.memeinn;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.parse.ParseUser;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends Activity{

    public static final String ACTION_ADD_FRIEND = "com.android.memeinn.ACTION_ADD_FRIEND";
    public static final String ACTION_FRIEND_RESPONSE = "com.android.memeinn.ACTION_FRIEND_RESPONSE";
    public static final String REQUESTFRIEND_FROMID = "REQUESTFRIEND_FROMID";
    public static final String REQUESTFRIEND_TOID = "REQUESTFRIEND_TOID";
    public static final String MESSAGE_CONTENT = "MESSAGE_CONTENT";
    public static final String SELECT_CASE = "SELECT_CASE";
    public static final int REQUEST_ACCEPT = 1000;

    private RequestFriendSession mCheckSession;

    private final ServiceConnection mRequestServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    private final ServiceConnection mResponseServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        Firebase.goOnline();
        updateUserOnlinePresence();
    }

    private void updateUserOnlinePresence() {
        Firebase connectedRef = FirebaseSingleton.getInstance("/.info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                if (connected) {
                    String username = ParseUser.getCurrentUser().getUsername();

                    //change user status to online in Firebase
                    Firebase onlineUsersRef = FirebaseSingleton.getInstance("onlineUsers/")
                            .child(username);
                    onlineUsersRef.setValue(Boolean.TRUE);
                    //mark the user as offline when user loses network
                    onlineUsersRef.onDisconnect().removeValue();

                    Firebase userRef = FirebaseSingleton.getInstance("users")
                            .child(username);
                    //log this connection to this user's online presence data
                    Firebase userOnlineRef = userRef.child("isOnline");
                    userOnlineRef.setValue(Boolean.TRUE);

                    //add listener to remove this connection when disconnected
                    userOnlineRef.onDisconnect().setValue(Boolean.FALSE);
                    //update last time online when disconnected
                    userRef.child("lastOnline").onDisconnect().setValue(ServerValue.TIMESTAMP);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("myapp", "Listener cancelled at .info/connected");
            }
        });

        // register broadcastreceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ADD_FRIEND);
        filter.addAction(ACTION_FRIEND_RESPONSE);
        registerReceiver(mReceiver, filter);

        Intent serviceIntent = new Intent(this, CheckRequestService.class);
        bindService(serviceIntent, mRequestServiceConnection, BIND_AUTO_CREATE);

        Intent serviceIntent1 = new Intent(this, CheckResponseService.class);
        bindService(serviceIntent1, mResponseServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ACCEPT) {
            if (resultCode == RESULT_OK) { // accept
                mCheckSession.setIsAccepted(true);
                final ParseUser currentUser = ParseUser.getCurrentUser();
                ParseQuery<ParseUser> query2 = ParseUser.getQuery();
                query2.getInBackground(mCheckSession.getFromUserID(), new GetCallback<ParseUser>() {
                    public void done(ParseUser fromUser, ParseException e) {
                        if (e == null) {
                            ParseRelation<ParseUser> relation = currentUser.getRelation("Relationship");
                            relation.add(fromUser);
                            currentUser.saveInBackground();
                        } else {
                            // something went wrong
                        }
                    }
                });
            }
            else
                mCheckSession.setIsRejected(true);

            mCheckSession.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final ParseUser currentUser = ParseUser.getCurrentUser();
            String action = intent.getAction();
            if (action.equalsIgnoreCase(ACTION_ADD_FRIEND)) {
                String fromUserID = intent.getStringExtra(REQUESTFRIEND_FROMID);
                if (currentUser != null) {
                    ParseQuery<RequestFriendSession> query = ParseQuery.getQuery(RequestFriendSession.CLASS_NAME);
                    query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_TOUSERID, currentUser.getObjectId());
                    query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_FROMUSERID, fromUserID);
                    query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_ISACCEPTED, false);
                    query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_ISREJECTED, false);
                    query.getFirstInBackground(new GetCallback<RequestFriendSession>() {
                        public void done(RequestFriendSession session, ParseException e) {
                            if (session == null) {
                                // nothing
                            }
                            else {
                                mCheckSession = session;
                                Intent i = new Intent(MainActivity.this, ShowMsgActivity.class);
                                i.putExtra(MESSAGE_CONTENT, session.getFromUserName() + " send request for friends.");
                                i.putExtra(SELECT_CASE, true);
                                startActivityForResult(i, REQUEST_ACCEPT);
                            }
                        }
                    });
                }
            }
            else if (action.equalsIgnoreCase(ACTION_FRIEND_RESPONSE)) {
                final String toUserID = intent.getStringExtra(REQUESTFRIEND_TOID);
                if (currentUser != null) {
                    ParseQuery<RequestFriendSession> query = ParseQuery.getQuery(RequestFriendSession.CLASS_NAME);
                    query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_FROMUSERID, currentUser.getObjectId());
                    query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_TOUSERID, toUserID);
                    query.getFirstInBackground(new GetCallback<RequestFriendSession>() {
                        public void done(final RequestFriendSession session, ParseException e) {
                            if (session == null) {
                                // nothing
                            }
                            else {
                                final boolean bAccept = session.getIsAccepted();
                                final boolean bReject = session.getIsRejected();

                                if (bAccept || bReject) {
                                    String strMsg = "";
                                    if (bAccept)
                                        strMsg = session.getToUserName() + " accept your request.";
                                    if (bReject)
                                        strMsg = session.getToUserName() + " reject your request.";

                                    Intent i = new Intent(MainActivity.this, ShowMsgActivity.class);
                                    i.putExtra(MESSAGE_CONTENT, strMsg);
                                    startActivity(i);

                                    if (bAccept) {
                                        ParseQuery<ParseUser> query2 = ParseUser.getQuery();
                                        query2.getInBackground(toUserID, new GetCallback<ParseUser>() {
                                            public void done(ParseUser toUser, ParseException e) {
                                                if (e == null) {
                                                    ParseRelation<ParseUser> relation = currentUser.getRelation("Relationship");
                                                    relation.add(toUser);
                                                    currentUser.saveInBackground();
                                                } else {
                                                    // something went wrong
                                                }
                                            }
                                        });
                                    }

                                    session.deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                //Toast.makeText(MainActivity.this, "Successfully delete session.", Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                //
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        }
    };

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        Firebase.goOffline();
    }
}
