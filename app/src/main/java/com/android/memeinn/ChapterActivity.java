package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by lindsey on 15-02-26.
 */
public class ChapterActivity extends Activity{

    String vocab;//the type of vocabulary

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        vocab = intent.getStringExtra(VocabActivity.EXTRA_MESSAGE);

        //do something with Parse

        setContentView(R.layout.chapterlist);

    }
}
