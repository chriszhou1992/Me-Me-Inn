package com.android.memeinn;

import android.annotation.TargetApi;
import android.util.Log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Created by bchen11 on 3/1/15.
 */
public class QuizActivity extends Activity {
    final int OPTIONS = 4;
    private int correct;
    private int score;
    private int rounds;
    final private int ROUNDS = 5;
    //private List<ParseObject> capL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizmain);
        correct = -1;
        score = 0;
        rounds = 0;

        generateQuizQuestion(getWindow().getCurrentFocus());
        rightChoice(getWindow().getDecorView().findViewById(android.R.id.content));
        // getWindow().getDecorView().findViewById(android.R.id.content) from stack
        //http://stackoverflow.com/questions/4486034/get-root-view-from-current-activity
    }

    public void goToNext(View view){
        TextView quizTop = (TextView) findViewById(R.id.name);
        quizTop.setText("Current Quiz Score: " + score);
        generateQuizQuestion(view);
    }

    public void goToResult(View view){
        Intent quizResult = new Intent(getApplicationContext(), QuizResult.class);
        quizResult.putExtra("score", score);
        startActivity(quizResult);
    }


    /**
     * set the touch listener on the button
     * @param view
     */
    @TargetApi(16)
    public void rightChoice(View view){
        for(int i = 0; i < OPTIONS; i++){
            String packageName = getPackageName();
            int resId = getResources().getIdentifier("button"+i, "id", packageName);
            Button opt = (Button)findViewById(resId);
            opt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                //thanks to http://stackoverflow.com/questions/11779082/listener-for-pressing-and-releasing-a-button
                public boolean onTouch(View v, MotionEvent event) {


                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            String packageName = getPackageName();
                            int rightChoiceId = getResources().getIdentifier("button"+correct, "id", packageName);
                            Log.d("MyApp", "correct is " + correct);
                            if(rightChoiceId == v.getId()){
                                v.setBackground(getResources().getDrawable(R.drawable.round_button_correct));
                                score++;
                            }
                            else
                                v.setBackground(getResources().getDrawable(R.drawable.round_button_incorrect));
                            return true; // if you want to handle the touch event
                        case MotionEvent.ACTION_UP:
                            v.setBackground(getResources().getDrawable(R.drawable.round_button));
                            if(rounds >= ROUNDS)
                                goToResult(getWindow().getDecorView().findViewById(android.R.id.content));
                            else
                                goToNext(getWindow().getDecorView().findViewById(android.R.id.content));
                            return true; // if you want to handle the touch event
                    }
                    return false;
                }
            });

        }

    }

    private void sameWordForReview(ParseObject word){
        ParseObject reviewIdx = new ParseObject("UserReviewList");
        reviewIdx.put("wordid", word.getObjectId());
        reviewIdx.put("userid", ParseUser.getCurrentUser().getObjectId());
        reviewIdx.saveInBackground();
        Log.d("MyApp", "word "+ reviewIdx.getObjectId() + " saved");
    }


    /**
     * generate a new quiz question
     * @param view
     */
    private void generateQuizQuestion(View view){
        rounds++; //each generation is a round
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GRE");
        query.whereStartsWith("Word", "a");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> capList, ParseException e) {
                if (e == null && capList.size() >= 4) {
                    Log.d("MyApp", "get the object with size " + capList.size());
                    Collections.shuffle(capList); //shuffle the word list
                    TextView testWord =(TextView)findViewById(R.id.testWord);
                    String currentWord = capList.get(0).getString("Word");
                    sameWordForReview(capList.get(0));
                    testWord.setText(currentWord); //get the current word

                    ArrayList<Integer> order = new ArrayList<>();
                    for(int i = 0; i < OPTIONS; i++) {
                        order.add(i);
                    }
                    Collections.shuffle(order);

                    for(int i = 0; i < OPTIONS; i++){
                        String packageName = getPackageName();
                        int resId = getResources().getIdentifier("button"+i, "id", packageName);
                        Button opt = (Button)findViewById(resId);
                        opt.setText(capList.get(order.get(i)).getString("Definition"));
                        if(order.get(i) == 0) // the definition is the correct one, as index 0 store the current testing word
                            correct = i;
                    }
                } else {
                    Log.d("MyApp", "Oops, list too short with only size " + capList.size());

                    Log.d("MyApp", "Error: " + e.getMessage());
                }
            }
        });

    }



}
