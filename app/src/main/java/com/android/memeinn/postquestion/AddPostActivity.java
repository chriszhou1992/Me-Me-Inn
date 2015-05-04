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

import com.android.memeinn.R;

public class AddPostActivity extends Activity {

    public static final String EXTRA_MESSAGE = "vocab.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpostvocablist);
    }

    /**
     * Choose the Vocab list to post question
     * @param view
     */
    public void gotoPostQuestion(View view) {
        Intent goToPost = new Intent(this, PostQuestionActivity.class);
        String vocabType = ((Button) /*findViewById(R.id.gre)*/view).getText().toString();
        Log.d("MyApp", "VocabType" + vocabType);
        goToPost.putExtra(EXTRA_MESSAGE, vocabType);
        startActivity(goToPost);
    }

}
