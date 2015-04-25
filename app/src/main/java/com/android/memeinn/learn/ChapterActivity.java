package com.android.memeinn.learn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.memeinn.Global;
import com.android.memeinn.R;
import com.android.memeinn.VocabActivity;

/**
 * Activity for ChapterList GUI.
 */
public class ChapterActivity extends Activity{



    private String vocabType = "";//the type of vocabulary
    private TextView vocabTypeView;
    private String vocabTableName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapterlist);

        Intent intent = getIntent();
        vocabType = intent.getStringExtra(VocabActivity.EXTRA_MESSAGE);
        vocabTableName = "GRE";
        vocabTypeView = (TextView) findViewById(R.id.vocabTypeView);
        // Give TextView a correct type name
        vocabTypeView.setText(vocabType);

    }

    /**
     * Callback function for click event on the chapter buttons. Return the chapter name to
     * help database retrieve relative word list.
     * @param view Button The chapter button clicked
     */
    public void onClick(View view) {

        //modified here
        Intent freqChapIntent;
        freqChapIntent = new Intent(this, FreqChapActivity.class);
        //Intent QuizIntent = new Intent(this, QuizActivity.class);
        freqChapIntent.putExtra(Global.EXTRA_MESSAGE_TABLENAME, vocabTableName);
        freqChapIntent.putExtra(Global.EXTRA_MESSAGE_VOCABTYPE, vocabType);
        String frequencyText = ((Button) view).getText().toString();
        freqChapIntent.putExtra(Global.EXTRA_MESSAGE_FREQUENCY, frequencyText);
        startActivity(freqChapIntent);
    }

}
