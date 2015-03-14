package com.android.memeinn;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CheckResponseService extends Service {
    public static final String MY_SERVICE = "CheckResponseService";

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

    private void CheckParseDB() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            ParseQuery<RequestFriendSession> query = ParseQuery.getQuery(RequestFriendSession.CLASS_NAME);
            query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_FROMUSERID, currentUser.getObjectId());
            query.whereEqualTo(RequestFriendSession.REQUEST_FIELD_ISFROMCHECKED, false);
            query.findInBackground(new FindCallback<RequestFriendSession>() {

                @Override
                public void done(List<RequestFriendSession> sessions, ParseException e) {
                    // TODO Auto-generated method stub
                    for (RequestFriendSession session : sessions) {
                        boolean bAccept = session.getIsAccepted();
                        boolean bReject = session.getIsRejected();
                        if (bAccept || bReject) {
                            session.setIsFromChecked(true);
                            session.saveInBackground(new SaveCallback() {

                                @Override
                                public void done(ParseException arg0) {
                                    // TODO Auto-generated method stub
                                }
                            });

                            final Intent intent = new Intent(MainActivity.ACTION_FRIEND_RESPONSE);
                            intent.putExtra(MainActivity.REQUESTFRIEND_TOID, session.getToUserID());
                            sendBroadcast(intent);
                        }
                    }
                }
            });
        }
    }
}