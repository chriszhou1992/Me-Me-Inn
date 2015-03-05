package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by lindsey on 15-02-26.
 */
public class ChapterActivity extends Activity{

    public final static String EXTRA_MESSAGE = "vocab.MESSAGE";
    private String vocabType = "";//the type of vocabulary
    private TextView vocabTypeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapterlist);

        Intent intent = getIntent();
        vocabType = intent.getStringExtra(VocabActivity.EXTRA_MESSAGE);
        //do something with Parse

        vocabTypeView = (TextView) findViewById(R.id.vocabTypeView);
        // Give TextView a correct type name
        vocabTypeView.setText(vocabType);
    }

    public void onClickA(View view) {
        Intent MemorizationIntent = new Intent(this, MemorizationActivity.class);
        String ButtonText = ((Button) findViewById(R.id.buttonA)).getText().toString();
        MemorizationIntent.putExtra(EXTRA_MESSAGE, ButtonText);
        startActivity(MemorizationIntent);
    }

    public void onClickB(View view) {
        Intent MemorizationIntent = new Intent(this, MemorizationActivity.class);
        String ButtonText = ((Button) findViewById(R.id.buttonB)).getText().toString();
        MemorizationIntent.putExtra(EXTRA_MESSAGE, ButtonText);
        startActivity(MemorizationIntent);
    }

    public void onClickC(View view) {
        Intent MemorizationIntent = new Intent(this, MemorizationActivity.class);
        String ButtonText = ((Button) findViewById(R.id.buttonC)).getText().toString();
        MemorizationIntent.putExtra(EXTRA_MESSAGE, ButtonText);
        startActivity(MemorizationIntent);
    }

    public void onClickD(View view) {
        Intent MemorizationIntent = new Intent(this, MemorizationActivity.class);
        String ButtonText = ((Button) findViewById(R.id.buttonD)).getText().toString();
        MemorizationIntent.putExtra(EXTRA_MESSAGE, ButtonText);
        startActivity(MemorizationIntent);
    }
}
