package com.android.memeinn;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;

import com.parse.*;

public class MemorizationActivity extends ActionBarActivity {

    private String vocabType = "";
    private String firstLetter = "";
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
        firstLetter = intent.getStringExtra(ChapterActivity.EXTRA_MESSAGE_FIRST_LETTER);
        initDict();
    }

    public void onClickNext(View view) {
        if (currPos < dict.size()) {
            currPos ++;
            if (currPos == dict.size()) {
                currPos = 0;
            }
            updateMemorizationView();
        }
    }

    public void onClickPrev(View view) {
        if (currPos >= 0) {
            currPos --;
            if (currPos < 0) {
                currPos = dict.size() - 1;
            }
            updateMemorizationView();
        }
    }

    private void initDict() {
        this.dict = new LinkedHashMap<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(vocabType);
        query.whereStartsWith("word", firstLetter.toLowerCase());
        Log.d("MyAPP", firstLetter.toLowerCase());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> wordList, ParseException e) {
                if (e == null) {
                    // Query success
                    for (ParseObject word : wordList) {
                        Log.d("MyApp", "1");
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

    private void initMemorizationView() {
        getEntryWithPos(0);
    }

    private void updateMemorizationView() {
        getEntryWithPos(currPos);
    }

    private void getEntryWithPos(int pos) {
        Map.Entry<String, String> entry = indexedList.get(pos);
        String wordContent = entry.getKey();
        String wordMeaning = entry.getValue();
        this.wordContentView.setText(wordContent);
        this.wordMeaningView.setText(wordMeaning);
    }

}
