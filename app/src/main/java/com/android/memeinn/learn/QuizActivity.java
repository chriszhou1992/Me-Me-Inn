package com.android.memeinn.learn;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.memeinn.Global;
import com.android.memeinn.Question;
import com.android.memeinn.R;
import com.android.memeinn.Utility;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Activity that handles logics dealing with quizzes.
 */
public class QuizActivity extends Activity {
    private int score;
    private int rounds;
    private final int ROUNDS = 5;

    private Queue<Question> questionQueue;
    private String vocabCategory;
    private String wordTableName;
    private String wordFrequency;
    //private String firstLetter;
    private ArrayList<ParseObject> wordList;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizmain);
        score = 0;
        rounds = 0;

        spinner = (ProgressBar) findViewById(R.id.spinner);
        //store pointers to elements on GUI to make displaying questions more efficient
        questionTextView = (TextView)findViewById(R.id.testWord);

        optionBtns = new ArrayList<>();
        for (int i = 0; i < NUM_OF_OPTIONS; i++)
            optionBtns.add(i, (Button)findViewById(BTN_IDS[i]));

        initQuizVocabSet();
        initAnimations();

        wordList = new ArrayList<>();
        questionQueue = new LinkedList<>();
        //one DB fetch to generate all quiz questions needed
        generateQuizQuestions();
    }

    private void initQuizVocabSet() {
        vocabCategory = "GRE";
        wordFrequency = "High Frequency";
        wordTableName = "GRE";
        Intent i = getIntent();
        if (i != null) {
            //String s = i.getStringExtra(ChapterActivity.EXTRA_MESSAGE_VOCAB_TYPE);
            wordFrequency = i.getStringExtra(Global.EXTRA_MESSAGE_FREQUENCY);
            wordTableName = i.getStringExtra(Global.EXTRA_MESSAGE_TABLENAME);
            //vocabCategory = s == null? "GRE" : s;
            vocabCategory = i.getStringExtra(Global.EXTRA_MESSAGE_VOCABTYPE);
            //firstLetter = s == null? "a" : s;
        }
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
    }

    /**
     * Private database fetch function that grabs all data needed and compose out all quiz
     * questions in one shot.
     */
    private void generateQuizQuestions() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(wordTableName);
        //query.whereStartsWith("word", firstLetter);

        System.out.println("Quiz ChapterActivity.EXTRA_MESSAGE_FREQUENCY" + wordFrequency);
        if(wordFrequency.toLowerCase().equals("high frequency")){
            query.whereGreaterThan("frequency", 4);
        }
        else if(wordFrequency.toLowerCase().equals("medium frequency")){
            query.whereGreaterThanOrEqualTo("frequency", 3);
            query.whereLessThanOrEqualTo("frequency", 4);
        }
        else{
            query.whereLessThanOrEqualTo("frequency", 2);
        }

        //show an icon of loading spinner
        spinner.setVisibility(View.VISIBLE);
        //disable buttons
        setOptionsClickable(false);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> wordList, ParseException e) {
                spinner.setVisibility(View.GONE);
                if (e == null && wordList.size() >= ROUNDS) {
                    Collections.shuffle(wordList); //shuffle the word list

                    String[] options = new String[NUM_OF_OPTIONS];
                    for (int i = 0; i < ROUNDS; i++) {
                        Utility.clearSession();

                        ParseObject word = wordList.get(i);
                        QuizActivity.this.wordList.add(word);

                        String w = word.getString("word");
                        int random;
                        for (int j = 0; j < NUM_OF_OPTIONS; j++) {
                            random = Utility.randomIntNonDuplicated(0, wordList.size() - 1, i);
                            options[j] = wordList.get(random).getString("definition").trim();
                        }

                        //pick where to place the correct answer
                        random = Utility.randomInt(0, NUM_OF_OPTIONS - 1);
                        options[random] = word.getString("definition");
                        Question q = new Question(w, options, random);
                        questionQueue.add(q);
                    }//for

                    nextQuestion(); //display first question
                } else {
                    Log.d("MyApp", "Oops, list too short with only size " + wordList.size());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Private function used to populate the interface with the next question in quiz.
     */
    private void nextQuestion() {
        rounds++;
        Question q = questionQueue.poll();

        //change interface
        if (q == null) {    //no more questions, quiz ended
            goToResult();
            return;
        } else {
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
     * onClick() event handler for answer buttons on the interface. Checks whether the answer
     * is correct and changes the button background accordingly.
     * @param view The button that is clicked.
     */
    public void choiceClicked(View view) {
        ParseObject vocab = wordList.get(rounds - 1);
        if ((boolean)view.getTag()) {
            score++;
            changeAnswerBtnBackground(view, 1);
        } else {
            changeAnswerBtnBackground(view, -1);
            //if answered incorrectly, add this word to user's review list
            /*from lingzi- I reversed this relationship so that each word is
            associated with the current user*/
            ParseUser u = ParseUser.getCurrentUser();
            String relationName = "UserReviewList" + vocabCategory;
            ParseRelation<ParseObject> rel = u.getRelation(relationName);
            rel.add(vocab);
            u.saveInBackground();
        }
        //disable further button clicking
        setOptionsClickable(false);

        animatedView.startAnimation(out);
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
        quizResult.putExtra(Global.EXTRA_MESSAGE_QUIZSCORE, score);
        quizResult.putExtra(Global.EXTRA_MESSAGE_VOCABTABLE, wordTableName);
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

    /**
     * Getter for current score.
     * @return int The current quiz score.
     */
    public int getScore(){
        return score;
    }

    /**
     * Getter for NUM_OF_OPTIONS constant.
     * @return int The number of choices each quiz question has.
     */
    public int getNUM_OF_OPTIONS(){
        return NUM_OF_OPTIONS;
    }

    /**
     * Getter for quiz option Buttons. Used in testing.
     * @param idx int The index of the choice button.
     * @return Button A reference to that button.
     */
    public Button getButton(int idx){
        return optionBtns.get(idx);
    }

    /**
     * Getter for BTN_IDS constant array. Used in testing.
     * @param idx int The index of the choice button id needed.
     * @return int The ID of the needed choice button.
     */
    public int getButtonId(int idx){
        return BTN_IDS[idx];
    }


}
