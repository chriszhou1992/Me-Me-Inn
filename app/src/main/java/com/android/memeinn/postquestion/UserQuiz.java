package com.android.memeinn.postquestion;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.android.memeinn.Question;
import com.android.memeinn.R;
import com.android.memeinn.Utility;
import com.parse.FindCallback;
import com.parse.GetCallback;
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
public class UserQuiz extends Activity {

    String extraString = "";
    private ArrayList<ParseObject> questions;

    private TextView questionTitle;
    private Button option1, option2, option3, option4;
    private int currPos;
    private String currAnswer = "";
    private ArrayList<Button> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userquizmain);
        Intent intent = getIntent();
        extraString = intent.getStringExtra(ShowUserQuiz.EXTRA_MESSAGE);

        questionTitle = (TextView) findViewById(R.id.testWord);
        option1 = (Button) findViewById(R.id.option0);
        option2 = (Button) findViewById(R.id.option1);
        option3 = (Button) findViewById(R.id.option2);
        option4 = (Button) findViewById(R.id.option3);

        options = new ArrayList<Button>();
        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(option4);

        questionTitle.setText(questionTitle + " (" + extraString + ")");

        initQuestions();
    }

    public void choiceClicked(View view) {
        if (currPos < questions.size()) {
            currAnswer = questions.get(currPos).getString("CurrentAnswer");
            Button btn = options.get(currAnswer.charAt(0) - 'A');
            String answerContent = btn.getText().toString();
            Utility.warningDialog(this, "Answer",
                    "Correct answer for " + questions.get(currPos).getString("questionTitle") + " is " + answerContent,
                    "next", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    currPos ++;
                    displayContentWithPos(currPos);
                }
            });
        } else {
            Utility.warningDialog(this, "End of quiz", "You have done all the quiz", "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
    }

    private void initQuestions() {
        this.questions = new ArrayList<ParseObject>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuizQuestions");
        query.orderByAscending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> questionList, ParseException e) {
                if (e == null) {
                    // Query success
                    for (ParseObject question : questionList) {
                        if ((boolean) question.get("accepted") == true) {
                            questions.add(question);
                        }
                    }
                    displayContentWithPos(0);
                } else {
                    e.printStackTrace();
                }
            }
        });
        currPos = 0;
    }

    private void displayContentWithPos(int pos) {
        try {
            ParseObject question = questions.get(pos);
            questionTitle.setText((String) question.get("questionTitle"));
            option1.setText((String) question.get("answerA"));
            option2.setText((String) question.get("answerB"));
            option3.setText((String) question.get("answerC"));
            option4.setText((String) question.get("answerD"));

            currAnswer = (String) question.get("CurrentAnswer");
        } catch (IndexOutOfBoundsException e) {
        }
    }
}
