package com.android.memeinn.friend;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import com.android.memeinn.MainActivity;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CheckRequestService extends Service {
    public static final String MY_SERVICE = "CheckRequestService";

    private boolean mBInited = false;
    private Timer mCheckTimer = null;

    public class LocalBinder extends Binder {
        public final Service service;

        public LocalBinder(Service service) {
            this.service = service;
        }
    }

    private final IBinder mBinder = new LocalBinder(this);

    @Override
    public IBinder onBind(Intent intent) {
        if (!mBInited) {
            mBInited = true;
            startTimer();
        }

        return mBinder;
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mBInited) {
            mBInited = true;
            startTimer();
        }

        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    // check database
    private void startTimer() {
        if (mCheckTimer != null) {
            mCheckTimer.cancel();
            mCheckTimer = null;
        }

        if (mCheckTimer == null) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    CheckParseDB();
                }
            };

            mCheckTimer = new Timer();
            mCheckTimer.schedule(task, 0, 5 * 1000); // every 5 seconds
        }
    }

    // parse database and return content
    private void CheckParseDB() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            ParseQuery<RequestFriendSession> query = ParseQuery.getQuery(RequestFriendSession.CLASS_NAME);
            String curUser = currentUser.getObjectId();
//	    	query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_TOUSERID, currentUser.getObjectId());
            query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_TOUSERID, curUser);
            query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_ISACCEPTED, false);
            query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_ISREJECTED, false);
            query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_ISTOCHECKED, false);
            query.getFirstInBackground(new GetCallback<RequestFriendSession>() {
                public void done(final RequestFriendSession session, ParseException e) {
                    if (session == null) {
                        // nothing
                    }
                    else {
                        session.setIsToChecked(true);
                        session.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException arg0) {
                                // TODO Auto-generated method stub
                            }
                        });

                        final Intent intent = new Intent(MainActivity.ACTION_ADD_FRIEND);
                        intent.putExtra(MainActivity.REQUESTFRIEND_FROMID, session.getFromUserID());
                        sendBroadcast(intent);
                    }
                }
            });
        }
    }
}