package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by lindsey on 15-02-26.
 */
public class ChapterActivity extends Activity{

    private String vocabType = "";//the type of vocabulary
    private TextView vocabTypeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapterlist);

        Intent intent = getIntent();
        vocabType = intent.getStringExtra(VocabActivity.EXTRA_MESSAGE);
        //do something with Parse

        vocabTypeView = (TextView) findViewById(R.id.vocabType);
        // Give TextView a correct type name
        vocabTypeView.setText(vocabType);
    }
}
