package com.android.memeinn.postquestion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.memeinn.R;

public class ShowUserQuizActivity extends Activity{

    public static final String EXTRA_MESSAGE = "vocab.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usercreatedquizlist);

    }

    /**
     * interface to into the quiz screen
     * @param view
     */
    public void gotoUserQ(View view) {
        Intent goToUserQuiz = new Intent(this, UserQuizActivity.class);
        String vocabType = ((Button) /*findViewById(R.id.gre)*/view).getText().toString();
        Log.d("MyApp", "VocabType" + vocabType);
        goToUserQuiz.putExtra(EXTRA_MESSAGE, vocabType);
        startActivity(goToUserQuiz);
    }
}
