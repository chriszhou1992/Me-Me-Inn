package com.android.memeinn.postquestion;

/**
 * Created by qingsongqi on 4/9/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.memeinn.learn.ChapterActivity;
import com.android.memeinn.R;
import com.android.memeinn.learn.MemorizationActivity;
import com.android.memeinn.learn.QuizActivity;
import com.android.memeinn.review.ReviewActivity;

public class AddPostActivity extends Activity {

    public static final String EXTRA_MESSAGE = "vocab.MESSAGE";
    //public int flag;//0 for review mode, 1 for learning mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpostvocablist);
        Intent intent = getIntent();
        //flag = intent.getFlags();
    }

    public void gotoPostQuestion(View view) {
        Intent goToPost = new Intent(this, PostQuestion.class);
        String vocabType = ((Button) /*findViewById(R.id.gre)*/view).getText().toString();
        Log.d("MyApp", "VocabType" + vocabType);
        goToPost.putExtra(EXTRA_MESSAGE, vocabType);
        startActivity(goToPost);
    }

}
