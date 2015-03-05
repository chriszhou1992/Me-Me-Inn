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

//        Parse.initialize(this, "l5qhJIZRq3vPDrHTmyzPu3z6IwMjukw7M3h9A8CZ",
//                "iLgCs4Z7I71j1L9DIWrjwjkCZ02yc6KuDsYVO60e");

        wordContentView = (TextView) findViewById(R.id.wordContentView);
        wordMeaningView = (TextView) findViewById(R.id.wordMeaningView);

        Intent intent = getIntent();
        vocabType = intent.getStringExtra(ChapterActivity.EXTRA_MESSAGE_VOCAB_TYPE);
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
        if (currPos > 0) {
            currPos --;
            if (currPos == 0) {
                currPos = dict.size() - 1;
            }
            updateMemorizationView();
        }
    }

    private void initDict() {
        this.dict = new LinkedHashMap<String, String>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(vocabType);
        query.whereStartsWith("word", firstLetter.toLowerCase());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> wordList, ParseException e) {
                if (e == null) {
                    // Query success
                    for (ParseObject word : wordList) {
                        dict.put(word.getString("word"), word.getString("definition"));
                    }
                    indexedList = new ArrayList<Map.Entry<String, String>>(dict.entrySet());
                } else {
                    Log.e("GetWordError", e.toString());
                }
            }
        });
        currPos = 0;
    }

    private void initMemorizationView() {
        getEntryWithPos(1);
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
