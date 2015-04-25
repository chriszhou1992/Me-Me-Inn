package com.android.memeinn.postquestion;

/**
 * Created by qingsongqi on 4/9/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.memeinn.MainActivity;
import com.android.memeinn.Utility;
import com.android.memeinn.learn.ChapterActivity;
import com.android.memeinn.R;
import com.android.memeinn.learn.MemorizationActivity;
import com.android.memeinn.learn.QuizActivity;
import com.android.memeinn.review.ReviewActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.ParseObject;

public class PostQuestion extends Activity{

    String extraString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postquestion);
        Intent intent = getIntent();
        extraString = intent.getStringExtra(AddPostActivity.EXTRA_MESSAGE);
    }

    public void onClickSubmit(View view) {
        EditText questionTitle = (EditText) findViewById(R.id.qtitle);
        EditText option1 = (EditText) findViewById(R.id.option1text);
        EditText option2 = (EditText) findViewById(R.id.option2text);
        EditText option3 = (EditText) findViewById(R.id.option3text);
        EditText option4 = (EditText) findViewById(R.id.option4text);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.option_group);

        String title = questionTitle.getText().toString();
        String q1 = option1.getText().toString();
        String q2 = option2.getText().toString();
        String q3 = option3.getText().toString();
        String q4 = option4.getText().toString();
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButtonID);
        int idx = radioGroup.indexOfChild(radioButton);
        char answer = (char) ('A' + idx);

        if (title.equals("") || q1.equals("") ||
                q2.equals("") || q3.equals("") || q4.equals("")) {
            Utility.warningDialog(this, "Bad Input", "title/question answer can not be empty.");
            return;
        }

//        ParseUser user = new ParseUser();
//        user.setUsername(username);
//        user.setPassword(pass);
        ParseObject question = new ParseObject("QuizQuestions");
        question.put("questionTitle", title);
        question.put("answerA", q1);
        question.put("answerB", q2);
        question.put("answerC", q3);
        question.put("answerD", q4);
        question.put("CurrentAnswer", String.valueOf(answer));
        question.put("accepted", false);
        question.put("VocabType", extraString);

        question.saveInBackground();
        Toast.makeText(this, "Question saved successfully!", Toast.LENGTH_SHORT).show();

        questionTitle.setText("");
        option1.setText("");
        option2.setText("");
        option3.setText("");
        option4.setText("");
        radioGroup.clearCheck();
    }

}
