package com.android.memeinn.postquestion;

/**
 * Created by qingsongqi on 4/9/15.
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.android.memeinn.R;

import com.android.memeinn.Utility;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class CheckPostActivity extends Activity{

    public static final String EXTRA_MESSAGE = "vocab.MESSAGE";
    //public int flag;//0 for review mode, 1 for learning mode
    private ArrayList<ParseObject> questions;

    private TextView vocabType;
    private TextView questionTitle;
    private TextView option1, option2, option3, option4;

    private int currPos;
    private int defaultColor1, defaultColor2, defaultColor3, defaultColor4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkquestion);
        Intent intent = getIntent();

        vocabType = (TextView) findViewById(R.id.vocabType);
        questionTitle = (TextView) findViewById(R.id.questionTitle);
        option1 = (TextView) findViewById(R.id.reviewquestioncontent1);
        option2 = (TextView) findViewById(R.id.reviewquestioncontent2);
        option3 = (TextView) findViewById(R.id.reviewquestioncontent3);
        option4 = (TextView) findViewById(R.id.reviewquestioncontent4);

        defaultColor1 = option1.getCurrentTextColor();
        defaultColor2 = option2.getCurrentTextColor();
        defaultColor3 = option3.getCurrentTextColor();
        defaultColor4 = option4.getCurrentTextColor();

        initQuestions();
    }

    public void onClickAccept(View view) {
        if (currPos < questions.size()) {
            ParseObject o = questions.get(currPos);
            String objectId = o.getObjectId();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("QuizQuestions");
            query.getInBackground(objectId, new GetCallback<ParseObject>() {
                public void done(ParseObject question, ParseException e) {
                    if (e == null) {
                        question.put("accepted", true);
                        question.saveInBackground();
                    } else {
                        Log.d("onClickAccept", "error accept");
                    }
                }
            });
            currPos ++;
            displayContentWithPos(currPos);
        } else {
            printEmptyMessage();
        }
    }

    public void onClickDecline(View view) {
        if (currPos < questions.size()) {
            ParseObject o = questions.get(currPos);
            String objectId = o.getObjectId();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("QuizQuestions");
            query.getInBackground(objectId, new GetCallback<ParseObject>() {
                public void done(ParseObject question, ParseException e) {
                    if (e == null) {
                        question.deleteInBackground();
                    } else {
                        Log.d("onClickDecline", "error decline");
                    }
                }
            });
            currPos ++;
            displayContentWithPos(currPos);
        } else {
            printEmptyMessage();
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
                        if ((boolean) question.get("accepted") == false) {
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
            resetTextField();

            ParseObject question = questions.get(pos);
            vocabType.setText((String) question.get("VocabType"));
            questionTitle.setText((String) question.get("questionTitle"));
            option1.setText((String) question.get("answerA"));
            option2.setText((String) question.get("answerB"));
            option3.setText((String) question.get("answerC"));
            option4.setText((String) question.get("answerD"));

            String answer = (String) question.get("CurrentAnswer");
            switch (answer) {
                case "A":
                    option1.setTextColor(Color.RED);
                    break;
                case "B":
                    option2.setTextColor(Color.RED);
                    break;
                case "C":
                    option3.setTextColor(Color.RED);
                    break;
                case "D":
                    option4.setTextColor(Color.RED);
                    break;
            }
        } catch (IndexOutOfBoundsException e) {
            printEmptyMessage();
        }
    }

    private void resetTextField() {
        vocabType.setText("");
        questionTitle.setText("");
        option1.setText("");
        option2.setText("");
        option3.setText("");
        option4.setText("");
        option1.setTextColor(defaultColor1);
        option2.setTextColor(defaultColor2);
        option3.setTextColor(defaultColor3);
        option4.setTextColor(defaultColor4);
    }

    private void printEmptyMessage() {
        Utility.warningDialog(this, "Reminder ", "No More Quiz to Check", "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        //Toast.makeText(this, "There is no more question to show!", Toast.LENGTH_SHORT).show();
    }

}
