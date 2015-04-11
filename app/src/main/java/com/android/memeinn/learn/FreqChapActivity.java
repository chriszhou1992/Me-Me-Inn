package com.android.memeinn.learn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.memeinn.R;

/**
 * Activity for the learning system. Used to learn/memorize words.
 */
public class FreqChapActivity extends Activity {

    public final static String EXTRA_MESSAGE_FREQUENCY = "chapter.vocab.frequency";
    public final static String EXTRA_MESSAGE_VOCAB_TYPE = "chapter.vocab.type";
    public final static String EXTRA_MESSAGE_TABLE_NAME = "chapter.vocab.table.name";
    //
    public final static String EXTRA_MESSAGE_CHAPTER = "chapter.vocab.chapter";


    private String vocabType = "";//the type of vocabulary
    private String vocabFrequency = "";
    private TextView vocabTypeView;
    private String vocabTableName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freqchaplist);

        Intent intent = getIntent();
        vocabType = intent.getStringExtra(ChapterActivity.EXTRA_MESSAGE_VOCAB_TYPE);
        vocabFrequency = intent.getStringExtra(ChapterActivity.EXTRA_MESSAGE_FREQUENCY);
        vocabTableName = "GRE";
        vocabTypeView = (TextView) findViewById(R.id.vocabTypeView);

        // Give TextView a correct type name
        vocabTypeView.setText(vocabType);

        Log.d("myapp", "Chapter.vocabType = " + vocabType);
    }

    /**
     * Callback function for click event on the chapter buttons. Return the chapter name to
     * help database retrieve relative word list.
     * @param view Button The chapter button clicked
     */
    public void onClick(View view) {
        Intent MemorizationIntent = new Intent(this, MemorizationActivity.class);
        //Intent QuizIntent = new Intent(this, QuizActivity.class);
        MemorizationIntent.putExtra(EXTRA_MESSAGE_TABLE_NAME, vocabTableName);
        MemorizationIntent.putExtra(EXTRA_MESSAGE_VOCAB_TYPE, vocabType);
        MemorizationIntent.putExtra(EXTRA_MESSAGE_FREQUENCY, vocabFrequency);
        String chapterText = ((Button) view).getText().toString();
        MemorizationIntent.putExtra(EXTRA_MESSAGE_CHAPTER, chapterText);
        startActivity(MemorizationIntent);
    }
}



