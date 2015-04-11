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
import com.android.memeinn.learn.QuizResultActivity;
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

        addListenerForOpponentSelection();
        /*Timer*/
        timeLeftTextView = (TextView) findViewById(R.id.timeLeft);
        countDown = 10;
        timeLeftTextView.setText(countDown.toString());

        oppoSelection = null;
    }

    private void addListenerForOpponentSelection() {
        for (int i = 1; i <= TOTAL_QUESTION_COUNT; i++) {
            Firebase oppoScoreRef = matchRef.child(opponentName + i);
            oppoScoreRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists())
                        return;
                    oppoSelection = 0;
                    int encoding = dataSnapshot.getValue(Integer.class);
                    while (encoding % 10 == 0) {
                        oppoSelection++;
                        encoding = encoding / 10;
                    }
                    if (encoding < 0)
                        oppoSelection = oppoSelection * -1;
                    //opponent chose wrong answer
                    displayScore(0, encoding); //encoding has the remaining countdown
                    Log.d("contest", "oppoSelected = " + oppoSelection);
                    Log.d("contest", "oppoCountDown = " + encoding);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
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
        timer.scheduleAtFixedRate(countDownTask, 1000, 1000);
    }

    private void decrementCountDown() {
        if (countDown > 0)
            countDown--;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timeLeftTextView.setText(countDown.toString());
                if (countDown == 0) {
                    countDown--;
                    Log.d("contest", "Choice Clicked(null)");
                    choiceClicked(null);
                }
            }
        });
    }

    /**
     * Setup animation specs for fade-in/fade-out effects. Fade-In animation
     * should start after fade-out animation has finished.
     */
    private void initAnimations() {
        //fade-out/fade-in animations
        animatedView = findViewById(R.id.quizLayout);
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
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                for (Button b : optionBtns) {
                    b.setVisibility(View.GONE);
                }
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
                                for (Button b : optionBtns) {
                                    b.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        countDown = 10;
                    }
                }, 1000);
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
        Intent quizResult = new Intent(getApplicationContext(), QuizResultActivity.class);
        //quizResult.putExtra("score", score);
        //quizResult.putExtra("vocabTableName", wordTableName);
        startActivity(quizResult);
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


    /**
     * onClick() event handler for answer buttons on the interface. Checks whether the answer
     * is correct and changes the button background accordingly.
     * @param btn The button that is clicked.
     */
    public void choiceClicked(View btn) {
        //disable further button clicking
        setOptionsClickable(false);
        int scoreEncoding = -1;

        if (btn != null) {
            //ParseObject vocab = wordList.get(rounds - 1);
            if ((boolean) btn.getTag()) {
                scoreEncoding = countDown + BASE_CORRECT_SCORE;
                changeAnswerBtnBackground(btn, 1);
                displayScore(scoreEncoding, 0);
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
            int i = 0;
            for (; i < BTN_IDS.length; i++)
                if (BTN_IDS[i] == btn.getId())
                    break;
            //encode
            scoreEncoding = scoreEncoding * (int) Math.pow(10, i);
        } else {
            displayScore(-1, 0);

            //encode
            scoreEncoding = -2;
        }

        Firebase choiceRef = matchRef.child(currentUsername + rounds);
        choiceRef.setValue(scoreEncoding);

        matchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(currentUsername + rounds) &&
                        dataSnapshot.hasChild(opponentName + rounds)) {
                    oppoSelection = 1;
                    HashMap m = dataSnapshot.getValue(HashMap.class);
                    int encoding = (int)m.get(opponentName + rounds);
                    while (encoding % 10 == 0) {
                        oppoSelection++;
                        encoding = encoding / 10;
                    }
                    if (encoding < 0)
                        oppoSelection = oppoSelection * -1;

                    if (encoding == -2)
                        oppoSelection = null;

                    tempTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (oppoSelection != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //displayRoundResult();
                                        displayRoundResult();
                                        animatedView.startAnimation(out);
                                    }
                                });
                            }
                        }
                    }, 1500);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void displayRoundResult() {
        if (oppoSelection == null) {
            return;
        }

        Button b = optionBtns.get(Math.abs(oppoSelection) - 1);
        if (oppoSelection < 0) {
            changeAnswerBtnBackground(b, -1);
            for (int i = 0; i < optionBtns.size(); i++) {
                b = optionBtns.get(i);
                if ((boolean)b.getTag())
                    changeAnswerBtnBackground(b, 1);
                Log.d("contest", "correct = " + i);
            }
        } else if (oppoSelection > 0)
            changeAnswerBtnBackground(b, 1);
        oppoSelection = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        matchRef.removeValue();
    }
}
