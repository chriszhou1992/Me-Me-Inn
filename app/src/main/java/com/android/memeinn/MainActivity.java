package com.android.memeinn;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends Activity {

    public static final String ACTION_ADD_FRIEND = "com.android.memeinn.ACTION_ADD_FRIEND";
    public static final String ACTION_FRIEND_RESPONSE = "com.android.memeinn.ACTION_FRIEND_RESPONSE";
    public static final String REQUESTFRIEND_FROMID = "REQUESTFRIEND_FROMID";
    public static final String REQUESTFRIEND_TOID = "REQUESTFRIEND_TOID";
    public static final String MESSAGE_CONTENT = "MESSAGE_CONTENT";
    public static final String SELECT_CASE = "SELECT_CASE";
    public static final int REQUEST_ACCEPT = 1000;

    private RequestFriendSession mCheckSession;
    private final BroadcastReceiver mReceiver = createMsgReceiver();

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

    /**
     * Method that handles the back-end setup after the user logged in. Writes to database
     * user's online presence and sets up listeners for user-related events.
     */
    private void updateUserOnlinePresence() {
        Firebase connectedRef = FirebaseSingleton.getInstance("/.info/connected");
        connectedRef.addValueEventListener(createConnectionListener());

        Firebase userRef = FirebaseSingleton.getInstance("users")
                .child(ParseUser.getCurrentUser().getUsername());

        //User is not in match
        Firebase userInMatchRef = userRef.child("isInMatch");
        userInMatchRef.setValue(Boolean.FALSE);

        Firebase matchRequestRef = userRef.child("matchRequests");
        matchRequestRef.addChildEventListener(createMatchRequestListener());
    }

    /**
     * Creates an event listener that listens for changes in network connection.
     * @return ValueEventListener A listener and callbacks for changes in network state.
     */
    private ValueEventListener createConnectionListener() {
        return new ValueEventListener() {
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
        };
    }

    /**
     * Creates an event listener that listens for requests from other users.
     * @return ChildEventListener A listener and callbacks for receiving match requests.
     */
    private ChildEventListener createMatchRequestListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String requestUser = dataSnapshot.getKey();
                DialogInterface.OnClickListener acceptListener =
                        new DialogInterface.OnClickListener() {
                    String currentUsername = ParseUser.getCurrentUser().getUsername();
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Firebase userRef = FirebaseSingleton.getInstance("users")
                                .child(currentUsername);
                        //indicate current user to be in match
                        userRef.child("isInMatch").setValue(Boolean.TRUE);
                        //remove that request from request queue
                        userRef.child("matchRequests/" + requestUser).removeValue();

                        //create a match entry
                        Firebase matchRef = FirebaseSingleton.getInstance("matches");
                        matchRef.child(Utility.combineStringSorted(requestUser,
                                currentUsername)).child(currentUsername).setValue(Boolean.TRUE);
                    }
                };
                DialogInterface.OnClickListener rejectListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Firebase userRef = FirebaseSingleton.getInstance("users")
                                        .child(ParseUser.getCurrentUser().getUsername());
                                //remove that request from request queue
                                userRef.child("matchRequests/" + requestUser).removeValue();
                            }
                        };
                MeMeInnApp app = (MeMeInnApp)getApplicationContext();
                Utility.warningDialog(app.currentActivity, "Match Request Received",
                        "User " + requestUser + " wants to start a match with you.", "Accept Match",
                        acceptListener, "Reject Match", rejectListener);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
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
            } else
                mCheckSession.setIsRejected(true);

            mCheckSession.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }

    /**
     * Start an Intent to go to the vocabulary page.
     * @param view
     */
    public void gotoVocab(View view) {
        Intent vocabIntent = new Intent(this, VocabActivity.class);
        startActivity(vocabIntent);
    }

    /**
     * Start an Intent to go the user profile activity.
     * @param view
     */
    public void gotoProfile(View view) {
        Intent profIntent = new Intent(this, ProfileActivity.class);
        startActivity(profIntent);
    }

    /**
     * Start an Intent to go to the interface that allows starting a match with friends.
     * @param view
     */
    public void goMatchWithFriend(View view) {
        Intent availFriend = new Intent(this, AvailFriendListActivity.class);
        startActivity(availFriend);
    }

    /**
     * Method invoked when the activity is destroyed. Does some clean ups.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        Firebase.goOffline();
    }

     private BroadcastReceiver createMsgReceiver() {
         return new BroadcastReceiver() {
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
                                 } else {
                                     mCheckSession = session;
                                     Intent i = new Intent(MainActivity.this, ShowMsgActivity.class);
                                     i.putExtra(MESSAGE_CONTENT, session.getFromUserName() + " send request for friends.");
                                     i.putExtra(SELECT_CASE, true);
                                     startActivityForResult(i, REQUEST_ACCEPT);
                                 }
                             }
                         });
                     }
                 } else if (action.equalsIgnoreCase(ACTION_FRIEND_RESPONSE)) {
                     final String toUserID = intent.getStringExtra(REQUESTFRIEND_TOID);
                     if (currentUser != null) {
                         ParseQuery<RequestFriendSession> query = ParseQuery.getQuery(RequestFriendSession.CLASS_NAME);
                         query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_FROMUSERID, currentUser.getObjectId());
                         query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_TOUSERID, toUserID);
                         query.getFirstInBackground(new GetCallback<RequestFriendSession>() {
                             public void done(final RequestFriendSession session, ParseException e) {
                                 if (session == null) {
                                     // nothing
                                 } else {
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
                                                 } else {
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
     }
}   //end class