package com.android.memeinn;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends Activity {

    private String vocabType = "";//the type of vocabulary
    private ArrayList<ParseObject> wordList;
    private int currPos;

    private TextView wordContentView;
    private TextView wordMeaningView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewchart);

        Intent intent = getIntent();
        vocabType = intent.getStringExtra(VocabActivity.EXTRA_MESSAGE);

        Log.d("Myapp", "ReviewActivity.vocabType = " + vocabType);

        initList();
        currPos = 0;

        /*
        ParseUser u = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(vocabType);
        query.whereEqualTo("reviewUsers", u);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                if (e == null) {
                    TableLayout table = (TableLayout) findViewById(R.id.reviewTable);

                    TableRow.LayoutParams wordColLayout = new TableRow.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                    wordColLayout.span = 1;

                    TableRow.LayoutParams definitionColLayout = new TableRow.LayoutParams(
                            1,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                    definitionColLayout.span = 2;

                    for (ParseObject obj : results) {
                        TableRow tr = new TableRow(ReviewActivity.this);

                        TextView wordField = new TextView(ReviewActivity.this);
                        wordField.setText(obj.getString("word"));
                        wordField.setLayoutParams(wordColLayout);
                        wordField.setTextColor(Color.WHITE);
                        wordField.setGravity(Gravity.START);
                        wordField.setPadding(0,0,4,0);

                        TextView defField = new TextView(ReviewActivity.this);
                        defField.setText(obj.getString("definition"));
                        defField.setLayoutParams(definitionColLayout);
                        defField.setGravity(Gravity.START);
                        defField.setTextColor(Color.WHITE);
                        defField.setPadding(1,1,1,1);

                        tr.addView(wordField);
                        tr.addView(defField);
                        table.addView(tr);

                        View separator = new View(ReviewActivity.this);
                        separator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup
                                .LayoutParams.MATCH_PARENT, 2));
                        separator.setBackgroundColor(Color.WHITE);
                        table.addView(separator);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        */
    }

    //disappear the circle and show word meaning hidden underneath
    public void showMeaning(View view){
        LinearLayout mLayout = (LinearLayout) view;
        mLayout.setVisibility(View.GONE);
    }

    //user nailed the word
    //remove the word from review list table
    public void knowWord(View view){
        ParseUser u = ParseUser.getCurrentUser();
        String relationName = "reviewUsers"/*+vocabType*/;
        ParseRelation relation = u.getRelation(relationName);
        ParseObject word = wordList.get(currPos);
        relation.remove(word);
    }

    public void onClickNext(View view) {
        if (currPos < wordList.size()) {
            currPos ++;
            if (currPos == wordList.size()) {
                currPos = 0;
            }
            updateMemorizationView();
        }
    }

    /*
    //control the Button Previous
    public void onClickPrev(View view) {
        if (currPos >= 0) {
            currPos --;
            if (currPos < 0) {
                currPos = wordList.size() - 1;
            }
            updateMemorizationView();
        }
    }
    */

    //parse the vocabulary set and print the words and meanings
    private void initList() {
        ParseUser u = ParseUser.getCurrentUser();
        String relationName = "reviewUsers"/*+vocabType*/;
        ParseRelation relation = u.getRelation(relationName);
        ParseQuery query = relation.getQuery();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> wordList, ParseException e) {
                if (e == null) {
                    Log.d("MyApp", "get review word objects");
                    initMemorizationView();
                } else {
                    Log.d("MyApp", "Error from retrieving review words: " + e.getMessage());
                }
            }
        });
    }

    //initialize the memorization view page
    private void initMemorizationView() {
        setWordDisplayWithPos(0);
    }

    // helper function when click the next or previous button
    private void updateMemorizationView() {
        setWordDisplayWithPos(currPos);
    }

    //get the current word position to control the sequence
    private void setWordDisplayWithPos(int pos) {
        ParseObject word = wordList.get(pos);
        String wordContent = word.getString("word");
        String wordMeaning = word.getString("definition");
        this.wordContentView.setText(wordContent);
        this.wordMeaningView.setText(wordMeaning);
    }

    public void backToProfile(View view) {
        Intent profIntent = new Intent(this, ProfileActivity.class);
        startActivity(profIntent);
    }
}
