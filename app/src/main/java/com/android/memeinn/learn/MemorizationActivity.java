package com.android.memeinn.learn;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.memeinn.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MemorizationActivity extends ActionBarActivity {

    private String vocabType = "";
    private String frequencyText = "";
    private String wordTableName = "";
    private Map<String, String> dict;
    private List<Map.Entry<String, String>> indexedList;
    private int currPos;

    private TextView wordContentView;
    private TextView wordMeaningView;

    TextToSpeech ttobj;
    private EditText write;

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

        write = (EditText)findViewById(R.id.hidden_edit_view);
        ttobj=new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR){
                            ttobj.setLanguage(Locale.US);
                        }
                    }
                });


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

    private String getWord(int pos) {
        Map.Entry<String, String> entry = indexedList.get(pos);
        String wordContent = entry.getKey();
        return wordContent;
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

    @Override
    public void onPause(){
        if(ttobj !=null){
            ttobj.stop();
            ttobj.shutdown();
        }
        super.onPause();
    }

    public void speakText(View view){
        String toSpeak = getWord(currPos);
        Toast.makeText(getApplicationContext(), toSpeak,
                Toast.LENGTH_SHORT).show();
        ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

    }


}
