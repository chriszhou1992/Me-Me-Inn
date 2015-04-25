package com.android.memeinn.postquestion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.memeinn.R;

public class ShowUserQuiz extends Activity{

    public static final String EXTRA_MESSAGE = "vocab.MESSAGE";
    //public int flag;//0 for review mode, 1 for learning mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usercreatedquizlist);
        Intent intent = getIntent();

    }

    public void gotoUserQ(View view) {
        Intent goToUserQuiz = new Intent(this, UserQuiz.class);
        String vocabType = ((Button) /*findViewById(R.id.gre)*/view).getText().toString();
        Log.d("MyApp", "VocabType" + vocabType);
        goToUserQuiz.putExtra(EXTRA_MESSAGE, vocabType);
        startActivity(goToUserQuiz);
    }
}
