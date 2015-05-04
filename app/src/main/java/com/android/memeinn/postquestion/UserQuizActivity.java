package com.android.memeinn.postquestion;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.memeinn.R;
import com.android.memeinn.Utility;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that handles logics dealing with quizzes.
 */
public class UserQuizActivity extends Activity {

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
        extraString = intent.getStringExtra(ShowUserQuizActivity.EXTRA_MESSAGE);

        questionTitle = (TextView) findViewById(R.id.questionT);
        option1 = (Button) findViewById(R.id.option0);
        option2 = (Button) findViewById(R.id.option1);
        option3 = (Button) findViewById(R.id.option2);
        option4 = (Button) findViewById(R.id.option3);

        options = new ArrayList<Button>();
        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(option4);

        initQuestions();
    }

    /**
     * click to view the next question
     * @param view
     */
    public void choiceClicked(View view) {
        if (currPos+1< questions.size()) {
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
            currAnswer = questions.get(currPos).getString("CurrentAnswer");
            Button btn = options.get(currAnswer.charAt(0) - 'A');
            String answerContent = btn.getText().toString();
            Utility.warningDialog(this, "Answer",
                    "Correct answer for " + questions.get(currPos).getString("questionTitle") + " is " + answerContent,
                     "You have done all the quiz" ,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
        }
    }

    /**
     * initialize the quiz question
     */
    private void initQuestions() {
        try {
            this.questions = new ArrayList<ParseObject>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("QuizQuestions");
            query.orderByAscending("createdAt");

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> questionList, ParseException e) {
                    if (e == null) {
                        // Query success
                        for (ParseObject question : questionList) {
                            if (((boolean) question.get("accepted") == true) && (question.get("VocabType").equals(extraString))) {
                                questions.add(question);
                            }
                        }
                        if (questions.size() > 0) {
                            System.out.print("questionsize"+questions.size());
                            displayContentWithPos(0);
                        } else {
                            printEmptyMessage();
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
            currPos = 0;
        }catch (IndexOutOfBoundsException e) {
            printEmptyMessage();
        }
    }

    /**
     * display the quiz question by pos
     * @param pos
     */
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

    /**
     * when no quiz anymore, return message to close
     */
    private void printEmptyMessage() {
        Utility.warningDialog(this, "No quiz avaliable ", "Please come and post your DIY quiz", "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }
}
