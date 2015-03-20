package com.android.memeinn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InlinedApi")
public class FriendActivity extends Activity {

    private ListView lst_users;
    private LazyAdapter mAdapter;
    private ArrayList<ParseUser> mAllUsers = new ArrayList<ParseUser>();
    private ArrayList<ParseUser> mFriendUsers = new ArrayList<ParseUser>();
    private int mSelectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend);

        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", currentUser.getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> results, ParseException e) {
                // results has the list of users
                mAllUsers = (ArrayList<ParseUser>) results;
            }
        });

        // print the friend list
        lst_users = (ListView) findViewById(R.id.lst_users);
        ParseRelation<ParseUser> relation = currentUser.getRelation("Relationship");
        relation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> results, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    // results have all the Posts the current user liked.
                    for (ParseUser user : results)
                        mFriendUsers.add(user);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        mAdapter = new LazyAdapter();
        lst_users.setAdapter(mAdapter);

        // register broadcastreceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.ACTION_FRIEND_RESPONSE);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final ParseUser currentUser = ParseUser.getCurrentUser();
            String action = intent.getAction();
            if (action.equalsIgnoreCase(MainActivity.ACTION_FRIEND_RESPONSE)) {
                final String toUserID = intent.getStringExtra(MainActivity.REQUESTFRIEND_TOID);
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
                                if (bAccept) {
                                    new Handler().postDelayed(new Runnable() {

                                        @Override
                                        public void run() {
                                            ParseRelation<ParseUser> relation = currentUser.getRelation("Relationship");
                                            relation.getQuery().findInBackground(new FindCallback<ParseUser>() {
                                                public void done(List<ParseUser> results, ParseException e) {
                                                    if (e != null) {
                                                        // There was an error
                                                    } else {
                                                        // results have all the Posts the current user liked.
                                                        for (ParseUser user : results)
                                                            mFriendUsers.add(user);
                                                        mAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                            });
                                        }
                                    }, 3000);
                                }
                            }
                        }
                    });
                }
            }
        }
    };

    //add the just accepted frined to the friend list and print on the table
    public void onAddFriend(View view) {
        ArrayList<String> strUsersArray = new ArrayList<String>();
        for (ParseUser user : mAllUsers)
            strUsersArray.add(user.getUsername());
        String[] strUsers = strUsersArray.toArray(new String[strUsersArray.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo);
        builder.setTitle("Send request to");
        builder.setSingleChoiceItems(strUsers, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelectedIndex = which;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                ParseUser friendUser = mAllUsers.get(mSelectedIndex);

                ParseQuery<RequestFriendSession> query = ParseQuery.getQuery(RequestFriendSession.CLASS_NAME);
                query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_FROMUSERID, currentUser.getObjectId());
                query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_FROMUSERNAME, currentUser.getUsername());
                query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_TOUSERID, friendUser.getObjectId());
                query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_TOUSERNAME, friendUser.getUsername());
                query.findInBackground(new FindCallback<RequestFriendSession>() {

                    @Override
                    public void done(List<RequestFriendSession> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() > 0) {
                                //
                            }
                            else {
                                makeSessionToParse();
                            }
                        }
                        else {
                            Toast.makeText(FriendActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    // parse data to the database
    private void makeSessionToParse() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseUser friendUser = mAllUsers.get(mSelectedIndex);

        RequestFriendSession newSession = new RequestFriendSession();
        newSession.setFromUserID(currentUser.getObjectId());
        newSession.setFromUserName(currentUser.getUsername());
        newSession.setToUserID(friendUser.getObjectId());
        newSession.setToUserName(friendUser.getUsername());
        newSession.setIsAccepted(false);
        newSession.setIsRejected(false);
        newSession.setIsFromChecked(false);
        newSession.setIsToChecked(false);

        newSession.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //
                }
                else {
                    Toast.makeText(FriendActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     * list adapter, print data by adapter
     */
    public class LazyAdapter extends BaseAdapter {
        private LayoutInflater inflater = null;

        public LazyAdapter() {
            inflater = (LayoutInflater) FriendActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return mFriendUsers.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            ImageView img_contact;
            TextView txt_username;
        }

        @SuppressLint("InflateParams")
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder _holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_friend, null);
                _holder = new ViewHolder();
                _holder.txt_username = (TextView) convertView.findViewById(R.id.txt_username);
                _holder.img_contact = (ImageView) convertView.findViewById(R.id.img_contact);

                convertView.setTag(_holder);
            } else {
                _holder = (ViewHolder) convertView.getTag();
            }

            ParseUser data = mFriendUsers.get(position);
//			_holder.img_contact.setImageURI(data.photoUri);
            _holder.txt_username.setText(data.getUsername());

            return convertView;
        }
    }
}
