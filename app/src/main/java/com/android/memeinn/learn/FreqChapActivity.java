package com.android.memeinn.learn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.memeinn.Global;
import com.android.memeinn.R;

/**
 * Activity for the learning system. Used to learn/memorize words.
 */
public class FreqChapActivity extends Activity {




    private String vocabType = "";//the type of vocabulary
    private String vocabFrequency = "";
    private TextView vocabTypeView;
    private String vocabTableName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freqchaplist);

        Intent intent = getIntent();
        vocabType = intent.getStringExtra(Global.EXTRA_MESSAGE_VOCABTYPE);
        vocabFrequency = intent.getStringExtra(Global.EXTRA_MESSAGE_FREQUENCY);
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
        MemorizationIntent.putExtra(Global.EXTRA_MESSAGE_TABLENAME, vocabTableName);
        MemorizationIntent.putExtra(Global.EXTRA_MESSAGE_VOCABTYPE, vocabType);
        MemorizationIntent.putExtra(Global.EXTRA_MESSAGE_FREQUENCY, vocabFrequency);
        String chapterText = ((Button) view).getText().toString();
        MemorizationIntent.putExtra(Global.EXTRA_MESSAGE_CHAPTER, chapterText);
        startActivity(MemorizationIntent);
    }
}



