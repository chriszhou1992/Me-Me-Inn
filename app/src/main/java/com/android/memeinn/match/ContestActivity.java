package com.android.memeinn.match;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.memeinn.FirebaseSingleton;
import com.android.memeinn.Global;
import com.android.memeinn.Question;
import com.android.memeinn.R;
import com.android.memeinn.Utility;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main contest activity
 */
public class ContestActivity extends Activity {
    private static final int TOTAL_QUESTION_COUNT = 8;
    private static final int BASE_CORRECT_SCORE = 10;   //base score earned for a correct answer

    private String currentUsername;
    private String opponentName;

    /*Timer Fields*/
    private TextView timeLeftTextView;
    private Timer timer;
    private Timer tempTimer;
    private Integer countDown;
    private TimerTask countDownTask;

    /*Question Logics*/
    private Firebase matchRef;
    private Queue<Question> questionQueue;
    private int rounds;

    /*Scording Logics*/
    private Integer oppoSelection;
    private Integer oppoScoreGain;
    private int userScore;
    private int oppoScore;
    private TextView userScoreTextView;
    private TextView oppoScoreTextView;

    /* GUI loading spinner */
    private ProgressBar spinner;

    private final int[] BTN_IDS = {R.id.option0, R.id.option1, R.id.option2, R.id.option3};
    public final int NUM_OF_OPTIONS = BTN_IDS.length;

    private ArrayList<Button> optionBtns;
    private TextView questionTextView;

    /* fade-out/fade-in animations */
    private View animatedView;
    final Animation out = new AlphaAnimation(1.0f, 0.0f);
    final Animation in = new AlphaAnimation(0.0f, 1.0f);

    /**
     * Setup function for the activity.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contest);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            opponentName = extras.getString(Global.EXTRA_MESSAGE_OPPONAME);
            TextView oppoNameView = (TextView) findViewById(R.id.opponent);
            oppoNameView.setText(opponentName);

            currentUsername = ParseUser.getCurrentUser().getUsername();
            TextView userNameView = (TextView) findViewById(R.id.user);
            userNameView.setText(currentUsername);
        }

        /* Score Logic */
        userScore = 0;
        oppoScore = 0;
        userScoreTextView = (TextView) findViewById(R.id.userScore);
        oppoScoreTextView = (TextView) findViewById(R.id.oppoScore);

        /* Question Logic */
        rounds = 0;
        questionQueue = new LinkedList<>();

        //store pointers to elements on GUI to make displaying questions more efficient
        questionTextView = (TextView)findViewById(R.id.testWord);
        optionBtns = new ArrayList<>();
        for (int i = 0; i < NUM_OF_OPTIONS; i++)
            optionBtns.add(i, (Button)findViewById(BTN_IDS[i]));

        initAnimations();

        matchRef = FirebaseSingleton.getInstance("matches/" + Utility.combineStringSorted
                (currentUsername, opponentName));
        matchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Q7") && dataSnapshot.hasChild("Q8")) {
                    HashMap data = dataSnapshot.getValue(HashMap.class);
                    for (int i = 1; i <= TOTAL_QUESTION_COUNT; i++) {
                        HashMap questionData = (HashMap) data.get("Q" + i);
                        Question q = new Question(questionData);
                        Log.d("contest", q.toString());
                        questionQueue.add(q);
                    }
                    //start match
                    setupTimer();
                    nextQuestion();
                    matchRef.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //end match if connection drops
        matchRef.onDisconnect().removeValue();

        addMatchStatusListener();
        /*Timer*/
        timeLeftTextView = (TextView) findViewById(R.id.timeLeft);
        countDown = 11;
        timeLeftTextView.setText(countDown.toString());

        oppoSelection = null;
        oppoScoreGain = null;
    }

    private void addMatchStatusListener() {
        matchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //display dialog if opponent exits
                    /*if (!ContestActivity.this.isFinishing())
                        Utility.warningDialog(ContestActivity.this, "Opponent Exited!",
                            "Your opponent exited the match!");*/
                    //force user to win since opponent exited
                    //oppoScore = -1;
                    //goToResult();
                    Firebase userInMatchRef = FirebaseSingleton.getInstance(currentUsername +
                            "/isInMatch");
                    userInMatchRef.setValue(false);
                    finish();
                }

                //opponent acted
                if (oppoSelection == null && dataSnapshot.hasChild(opponentName + rounds)) {
                    HashMap m = dataSnapshot.getValue(HashMap.class);
                    HashMap clickData = (HashMap)m.get(opponentName + rounds);
                    oppoSelection = (int)clickData.get("choiceClicked");
                    oppoScoreGain = (int)clickData.get("score");
                    //put in a negative if opponent didn't gain score
                    displayScore(0, oppoScoreGain == 0? -1 : oppoScoreGain);
                }

                //round ended
                if (dataSnapshot.hasChild(currentUsername + rounds) &&
                        dataSnapshot.hasChild(opponentName + rounds)) {

                    displayRoundResult();
                    //transition to next question in two seconds
                    tempTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    animatedView.startAnimation(out);
                                }
                            });
                        }
                    }, 2000);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * Setup the Timer which counts down the time remaining to answer the question.
     */
    private void setupTimer() {
        tempTimer = new Timer();
        timer = new Timer();
        Log.d("contest", "countdownTV = " + timeLeftTextView.getText());
        countDownTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("contest", "timer trigger");
                decrementCountDown();
                Log.d("contest", "timer = " + countDown);
            }
        };
        disableCountDownTimer(false);
        timer.scheduleAtFixedRate(countDownTask, 1000, 1000);
    }

    private void decrementCountDown() {
        if (countDown > 0)
            countDown--;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            if (timeLeftTextView.getCurrentTextColor() == Color.WHITE)
                timeLeftTextView.setText(countDown.toString());
            if (countDown == 0) {
                timeLeftTextView.setTextColor(Color.RED);
                Log.d("contest", "Choice Clicked(null)");
                choiceClicked(null);
            }
            }
        });
    }

    private void changeChoiceVisibility(int control) {
        if (control > 0) {
            for (Button b : optionBtns)
                b.setVisibility(View.VISIBLE);
        } else {
            for (Button b : optionBtns)
                b.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * Setup animation specs for fade-in/fade-out effects. Fade-In animation
     * should start after fade-out animation has finished.
     */
    private void initAnimations() {
        //fade-out/fade-in animations
        animatedView = findViewById(R.id.matchLayout);
        out.setDuration(1000);
        in.setDuration(1000);
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //when fade-out animation ended for previous question
                //start fade-in animation and advance to next question
                nextQuestion();
                animatedView.startAnimation(in);
                timeLeftTextView.setText("10");
                changeChoiceVisibility(-1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //1 second delay to display options
                tempTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                changeChoiceVisibility(1);
                                disableCountDownTimer(false);
                            }
                        });
                        countDown = 11;
                    }
                }, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * Private function used to populate the interface with the next question in quiz.
     */
    private void nextQuestion() {
        rounds++;
        Log.d("contest", "rounds = " + rounds);
        Question q = questionQueue.poll();

        Log.d("contest", "queue size = " + questionQueue.size());
        //change interface
        if (q == null) {    //no more questions, match ended
            goToResult();
            return;
        } else {
            //populate question and choices onto GUI
            questionTextView.setText(q.question);
            for (int i = 0; i < NUM_OF_OPTIONS; i++) {
                Button btn = optionBtns.get(i);
                btn.setText(q.options[i]);

                //store metadata on option buttons indicating the correct choice
                if (i == q.CORRECT_OPTION)
                    btn.setTag(true);
                else
                    btn.setTag(false);
            }
        }
        setOptionsClickable(true);

        //reset data and display colors for next round
        resetRoundData();
    }

    private void resetRoundData() {
        oppoScoreGain = null;
        oppoSelection = null;
        userScoreTextView.setTextColor(Color.WHITE);
        oppoScoreTextView.setTextColor(Color.WHITE);
        countDown = 11;
    }

    /**
     * Private helper function for controlling the change of the answer button backgrounds.
     * @param view The Button that needs a change in background.
     * @param control An integer specifying the which way the change should go.
     */
    @TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN)
    public void changeAnswerBtnBackground(View view, int control) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        boolean oldSDK = sdk < android.os.Build.VERSION_CODES.JELLY_BEAN;
        Resources res = getResources();

        Drawable d = res.getDrawable(R.drawable.round_button_correct);
        if (control < 0)
            d = res.getDrawable(R.drawable.round_button_incorrect);
        else if (control == 0)
            d = res.getDrawable(R.drawable.round_button);

        if (oldSDK)
            view.setBackgroundDrawable(d);
        else
            view.setBackground(d);
    }

    /**
     * Fire up intent to go to QuizResultActivity when quiz is finished.
     */
    private void goToResult(){
        Intent matchResult = new Intent(this, MatchResultActivity.class);
        matchResult.putExtra(Global.EXTRA_MESSAGE_MATCHSCORE, userScore);
        matchResult.putExtra(Global.EXTRA_MESSAGE_MATCHRESULT, userScore - oppoScore);
        startActivity(matchResult);
        matchRef.removeValue();
        finish();
    }

    /**
     * Private helper function that enables/disables the answer Buttons.
     * @param clickable Boolean flag indicating whether the answer Buttons should be enabled or
     *                  disabled.
     */
    private void setOptionsClickable(boolean clickable) {
        //disable/enable further button clicking
        for(Button b : optionBtns) {
            b.setClickable(clickable);
            if (clickable)  //rollback colors if re-enabling answer buttons
                changeAnswerBtnBackground(b, 0);
        }
    }

    private void displayScore(int userScoreGain, int oppoScoreGain) {
        if (userScoreGain > 0)
            userScore += userScoreGain;
        if (oppoScoreGain > 0)
            oppoScore += oppoScoreGain;
        if (userScoreGain != 0)
            displayScore(userScoreTextView, userScoreGain, userScore);
        if (oppoScoreGain != 0)
            displayScore(oppoScoreTextView, oppoScoreGain, oppoScore);
    }

    private void displayScore(final TextView scoreTextView, int scoreGain,
                              final Integer finalScore) {
        if (scoreGain < 0) {
            scoreTextView.setTextColor(Color.RED);
        } else {
            scoreTextView.setTextColor(Color.GREEN);
            scoreTextView.setText("+" + scoreGain);
            tempTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scoreTextView.setTextColor(Color.WHITE);
                            scoreTextView.setText(finalScore.toString());
                        }
                    });
                }
            }, 1000);
        }
    }

    private void disableCountDownTimer(boolean disable) {
        if (disable)
            timeLeftTextView.setTextColor(Color.LTGRAY);
        else
            timeLeftTextView.setTextColor(Color.WHITE);
    }
    /**
     * onClick() event handler for answer buttons on the interface. Checks whether the answer
     * is correct and changes the button background accordingly.
     * @param btn The button that is clicked.
     */
    public void choiceClicked(View btn) {
        disableCountDownTimer(true);
        countDown = 11;
        //disable further button clicking
        setOptionsClickable(false);

        HashMap<String, Integer> clickData = new HashMap<>();
        int score = 0;
        int i = 0;  //index of the button clicked

        if (btn != null) {
            if ((boolean) btn.getTag()) {
                score = countDown + BASE_CORRECT_SCORE;
                changeAnswerBtnBackground(btn, 1);
                displayScore(score, 0);
                Log.d("contest", "correct choice");
            } else {
                displayScore(-1, 0);
                changeAnswerBtnBackground(btn, -1);
                Log.d("contest", "wrong choice");
                //if answered incorrectly, add this word to user's review list
               /* ParseUser u = ParseUser.getCurrentUser();
                String relationName = "UserReviewList" + vocabCategory;
                ParseRelation<ParseObject> rel = u.getRelation(relationName);
                Log.d("quiz", relationName);
                rel.add(vocab);
                Log.d("quiz", vocab.getString("word"));
                u.saveInBackground();*/
            }

            for (; i < BTN_IDS.length; i++)
                if (BTN_IDS[i] == btn.getId())
                    break;
        } else {
            displayScore(-1, 0);
            i = -1;
        }

        clickData.put("score", score);
        clickData.put("choiceClicked", i);
        Firebase choiceRef = matchRef.child(currentUsername + rounds);
        choiceRef.setValue(clickData);
    }

    private void displayRoundResult() {
        if (oppoSelection == null)
            return;

        if (oppoSelection >= 0) {   //if opponent did select a choice
            Button b = optionBtns.get(oppoSelection);
            if (oppoScoreGain <= 0) //opponent selected correct choice
                changeAnswerBtnBackground(b, -1);
            else
                changeAnswerBtnBackground(b, 1);
        }
        displayCorrectAnswer();
    }

    private void displayCorrectAnswer() {
        Button b;
        for (int i = 0; i < optionBtns.size(); i++) {
            b = optionBtns.get(i);
            if ((boolean)b.getTag()) {
                changeAnswerBtnBackground(b, 1);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        tempTimer.cancel();
        matchRef.removeValue();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();/*
        Intent friendListIntent = new Intent(this, AvailFriendListActivity.class);
        startActivity(friendListIntent);
        finish();*/
    }
}
