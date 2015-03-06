package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class VocabActivity extends Activity{

    public final static String EXTRA_MESSAGE = "vocab.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabpage);
        //Parse.initialize(this, "l5qhJIZRq3vPDrHTmyzPu3z6IwMjukw7M3h9A8CZ", "iLgCs4Z7I71j1L9DIWrjwjkCZ02yc6KuDsYVO60e");
    }

    /**
     * triggers intent to quiz activity
     * @param view
     */
    public void goToQuiz(View view) {
        Intent quizAct = new Intent(this, QuizActivity.class);
        startActivity(quizAct);
    }

    /**
     * triggers intent to review activity
     * @param view
     */
    public void goToReview(View view) {
        Intent memoAct = new Intent(this, VocabMemoActivity.class);
        startActivity(memoAct);
    }

    public void gotoChapList(View view) {
        Intent goToChapIntent = new Intent(this, ChapterActivity.class);
        String vocabType = ((Button) /*findViewById(R.id.gre)*/view).getText().toString();
        goToChapIntent.putExtra(EXTRA_MESSAGE, vocabType);
        startActivity(goToChapIntent);
    }
}
