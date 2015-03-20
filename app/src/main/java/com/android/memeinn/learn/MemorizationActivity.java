package com.android.memeinn.learn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.memeinn.ChapterActivity;
import com.android.memeinn.QuizActivity;
import com.android.memeinn.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Activity for the learning system. Used to learn/memorize words.
 */
public class MemorizationActivity extends ActionBarActivity {

    private String vocabType = "";
    private String frequencyText = "";
    private String wordTableName = "";
    private Map<String, String> dict;
    private List<Entry<String, String>> indexedList;
    private int currPos;

    private TextView wordContentView;
    private TextView wordMeaningView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memorizationpage);

        wordContentView = (TextView) findViewById(R.id.wordContentView);
        wordMeaningView = (TextView) findViewById(R.id.wordMeaningView);

        Intent intent = getIntent();
        vocabType = intent.getStringExtra(ChapterActivity.EXTRA_MESSAGE_VOCAB_TYPE);
        Log.d("myapp", "Memorization.vocabType = " + vocabType);
        frequencyText = intent.getStringExtra(ChapterActivity.EXTRA_MESSAGE_FREQUENCY);
        wordTableName = intent.getStringExtra(ChapterActivity.EXTRA_MESSAGE_TABLE_NAME);

        initDict();
    }

    /**
     * Callback function for the Next button. Update interface to display
     * the next word in the list.
     * @param view Button The button clicked.
     */
    public void onClickNext(View view) {
        if (currPos < dict.size()) {
            currPos ++;
            if (currPos == dict.size()) {
                currPos = 0;
            }
            updateMemorizationView();
        }
    }

    /**
     * Callback function for the Previous button. Update interface to display
     * the previous word in the list.
     * @param view Button The button clicked.
     */
    public void onClickPrev(View view) {
        if (currPos >= 0) {
            currPos --;
            if (currPos < 0) {
                currPos = dict.size() - 1;
            }
            updateMemorizationView();
        }
    }

    /**
     * Fetch vocabulary set from Parse database and parse the set into an
     * ArrayList to enable easy display of the words and meanings.
     */
    private void initDict() {
        this.dict = new LinkedHashMap<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(wordTableName);
        query.setLimit(1000);
        query.orderByAscending("word");
        //query.whereStartsWith("word", frequencyText.toLowerCase());

        //query based on word frequency
        //System.out.println("vocabType" + vocabType);
        //System.out.println("frequencyText" + frequencyText);
        //System.out.println("wordTableName" + wordTableName);
        if(frequencyText.toLowerCase().equals("high frequency")){
            query.whereGreaterThan("frequency", 4);
        }
        else if(frequencyText.toLowerCase().equals("medium frequency")){
            query.whereGreaterThanOrEqualTo("frequency", 3);
            query.whereLessThanOrEqualTo("frequency", 4);
        }
        else{
            query.whereLessThanOrEqualTo("frequency", 2);
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> wordList, ParseException e) {
                if (e == null) {
                    // Query success
                    for (ParseObject word : wordList) {
                        dict.put(word.getString("word"), word.getString("definition"));
                    }
                    indexedList = new ArrayList<>(dict.entrySet());
                    initMemorizationView();
                } else {
                    e.printStackTrace();
                    //Log.e("GetWordError", e.toString());
                }
            }
        });
        currPos = 0;
    }

    /**
     * Initialize the interface with the first word in the vocab list.
     */
    private void initMemorizationView() {
        getEntryWithPos(0);
    }

    /**
     * Helper function triggered when clicked the next or previous button.
     */
    private void updateMemorizationView() {
        getEntryWithPos(currPos);
    }

    /**
     * Method that updates the interface to display the vocab at the given position.
     * @param pos int The position of the word in the word array.
     */
    private void getEntryWithPos(int pos) {
        Map.Entry<String, String> entry = indexedList.get(pos);
        String wordContent = entry.getKey();
        String wordMeaning = entry.getValue();
        this.wordContentView.setText(wordContent);
        this.wordMeaningView.setText(wordMeaning);
    }

    /**
     * Callback to start an Intent to open Quiz activity.
     * @param quizBtn Button The button clicked.
     */
    public void startQuiz(View quizBtn) {
        Intent quizIntent = new Intent(this, QuizActivity.class);
        quizIntent.putExtra(ChapterActivity.EXTRA_MESSAGE_FREQUENCY, frequencyText.toLowerCase());
        quizIntent.putExtra(ChapterActivity.EXTRA_MESSAGE_VOCAB_TYPE, vocabType);
        quizIntent.putExtra(ChapterActivity.EXTRA_MESSAGE_TABLE_NAME, wordTableName);
        startActivity(quizIntent);
    }
}
