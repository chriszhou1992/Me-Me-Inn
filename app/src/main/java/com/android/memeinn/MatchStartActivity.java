package com.android.memeinn;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.parse.ParseUser;

import java.util.Timer;
import java.util.TimerTask;

public class MatchStartActivity extends Activity {

    //private Thread animationThread;
    private MatchStartAnimView animatedView;
    private String currentUsername;
    private String opponentName;

    private Timer timer;
    private TimerTask startContestTask;

    private DialogInterface.OnClickListener cancelClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.matchstart);

        currentUsername = ParseUser.getCurrentUser().getUsername();
        opponentName = "NPC";
        Intent i = getIntent();
        if (i != null) {
            String s = i.getStringExtra(Global.EXTRA_MESSAGE_OPPONAME);
            opponentName = s == null? "NPC" : s;
        }

        animatedView = new MatchStartAnimView(this, opponentName);
        setContentView(animatedView);

        //Listens for the result of the match request sent
        addMatchReqResultListener();
        setupCancelDialogOKListener();
        setupTimer();
    }

    /**
     * Setup the listener for exit confirmation dialogs. The listener will clear up match data
     * in the database and exit the activity.
     */
    private void setupCancelDialogOKListener() {
        cancelClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Firebase userRef = FirebaseSingleton.getInstance(currentUsername);
                userRef.child("isInMatch").setValue(Boolean.FALSE);
                Firebase matchRef = FirebaseSingleton.getInstance("matches/" + Utility
                        .combineStringSorted(currentUsername, opponentName));
                matchRef.removeValue();
                finish();
            }
        };
    }

    /**
     * Setup the Timer which counts down the time remaining before the opponent needs
     * to accept the match request. If the timer is finished, then display an exiting
     * dialog alert and exit the activity.
     */
    private void setupTimer() {
        timer = new Timer();
        TimerTask countDownTask = new TimerTask() {
            @Override
            public void run() {
                int countDown = animatedView.decrementCountdown();
                animatedView.postInvalidate();
                if (countDown <= 0) {
                    timer.cancel();
                    //Have to display warning dialog from UI thread since other threads
                    //cannot modify UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utility.warningDialog(MatchStartActivity.this, "Match Request Timeout",
                                    "Your opponent failed to respond in time. Try asking again.",
                                    "OK", cancelClickListener);
                        }
                    });
                }
            }
        };
        startContestTask = new TimerTask() {
            @Override
            public void run() {
                Intent contestIntent = new Intent(MatchStartActivity.this,
                        ContestActivity.class);
                contestIntent.putExtra(Global.EXTRA_MESSAGE_OPPONAME, opponentName);
                startActivity(contestIntent);
            }
        };
        timer.scheduleAtFixedRate(countDownTask, 1000, 1000);
    }

    /**
     * Real-Time Listener for the status of match request. If the match request
     * is accepted, then start contest activity with a 1 second delay. If the request
     * is rejected, then display an exit dialog alert and exit the activity.
     */
    private void addMatchReqResultListener() {
        final Firebase matchRef = FirebaseSingleton.getInstance("matches/" + Utility
                .combineStringSorted(opponentName, currentUsername));
        //indicate the current user is ready to go into match
        matchRef.child(currentUsername).setValue(Boolean.TRUE);

        //listen to the result of the match request
        ValueEventListener matchReqResultListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MeMeInnApp app = (MeMeInnApp) getApplicationContext();
                if (!dataSnapshot.exists()) {
                    timer.cancel();
                    matchRef.removeEventListener(this);

                    Utility.warningDialog(app.currentActivity, "Match Rejected",
                            "Your friend refused the match!", "OK", cancelClickListener);

                } else if (dataSnapshot.exists() && dataSnapshot.hasChild(opponentName)) {
                    /*Utility.warningDialog(app.currentActivity, "Match Accepted",
                            "Your friend accepted the match!");*/
                    animatedView.setOpponentStatus(true);
                    matchRef.removeEventListener(this);

                    timer.cancel();
                    timer = new Timer();
                    //start contest after 1 second
                    timer.schedule(startContestTask, 1000);
                    Log.d("match", "MATCH STARTED");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        matchRef.addValueEventListener(matchReqResultListener);
    }

    /**
     * Customize the Back button to trigger an exit confirmation dialog
     * before exiting the activity.
     */
    @Override
    public void onBackPressed() {
        Utility.warningDialog(MatchStartActivity.this, "Cancel Confirmation",
                "Exiting will cancel this match. Do you really want to cancel?", "Yes",
                cancelClickListener, "No", null);
    }

    /**
     * Additional clean ups when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    /*
    @Override
    public void run() {
        SurfaceHolder holder = animatedView.getHolder();
        while (!animatedView.isAnimationDone()) {
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                animatedView.draw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("thread", "running...");
        }
    }*/
}
