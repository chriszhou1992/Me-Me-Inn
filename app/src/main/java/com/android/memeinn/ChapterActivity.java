package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity for ChapterList GUI.
 */
public class ChapterActivity extends Activity{

    public final static String EXTRA_MESSAGE_FIRST_LETTER = "chapter.first.letter";
    public final static String EXTRA_MESSAGE_VOCAB_TYPE = "chapter.vocab.type";
    private String vocabType = "";//the type of vocabulary
    private TextView vocabTypeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapterlist);

        Intent intent = getIntent();
        vocabType = intent.getStringExtra(VocabActivity.EXTRA_MESSAGE);

        vocabTypeView = (TextView) findViewById(R.id.vocabTypeView);
        // Give TextView a correct type name
        vocabTypeView.setText(vocabType);

        Log.d("myapp", "Chapter.vocabType = " + vocabType);
    }

    /**
     * Callback function for click event on the chapter buttons.Return the chapter name to
     * help database retrieve relative word list.
     * @param view Button The chapter button clicked
     */
    public void onClick(View view) {
        Intent MemorizationIntent = new Intent(this, MemorizationActivity.class);
        MemorizationIntent.putExtra(EXTRA_MESSAGE_VOCAB_TYPE, vocabType);
        String firstLetter = ((Button) view).getText().toString();
        MemorizationIntent.putExtra(EXTRA_MESSAGE_FIRST_LETTER, firstLetter);
        startActivity(MemorizationIntent);
    }

}
