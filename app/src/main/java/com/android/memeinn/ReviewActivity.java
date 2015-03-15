package com.android.memeinn;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private Button hide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewchart);

        wordContentView = (TextView) findViewById(R.id.wordContentView);
        wordMeaningView = (TextView) findViewById(R.id.wordMeaningView);
        hide = (Button) findViewById(R.id.checkMeaning);

        Intent intent = getIntent();
        vocabType = intent.getStringExtra(VocabActivity.EXTRA_MESSAGE);

        Log.d("Myapp", "ReviewActivity.vocabType = " + vocabType);

        wordList = new ArrayList<>();
        initList();
        currPos = 0;

    }

    /*Assumption:
    * User will always click on the circle to show meaning if he/she doesn't remember,
    * and will then provide feedback(IDK).
    * However the user can click on "I know" without discovering the word meaning and go straight to next word*/

    //disappear the circle and show word meaning hidden underneath
    public void showMeaning(View view){
        view.setVisibility(View.GONE);
    }

    public void hideMeaning(){
        hide.setVisibility(View.VISIBLE);
    }

    //user nailed the word
    //remove the word from review list table
    public void knowWord(View view){
        ParseUser u = ParseUser.getCurrentUser();
        String relationName = "reviewUsers"/*+vocabType*/;
        ParseRelation relation = u.getRelation(relationName);
        ParseObject word = wordList.get(currPos);
        relation.remove(word);
        //this somehow didn't remove the word from DB successfully
        onClickNext(view);
    }

    public void onClickNext(View view) {
        if (currPos < wordList.size()) {
            currPos ++;
            if (currPos == wordList.size()) {
                currPos = 0;
            }
            updateReviewView();
        }
    }

    //parse the vocabulary set and print the words and meanings
    private void initList() {
        ParseUser u = ParseUser.getCurrentUser();
        String relationName = "UserReviewList"/*+vocabType*/;
        ParseRelation relation = u.getRelation(relationName);
        ParseQuery query = relation.getQuery();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if ((e == null) && (!list.isEmpty())) {
                    Log.d("MyApp", "get review word objects");
                    System.out.println("UserReviewList of this user has " + list.size() + " tuples");
                    for (int i = 0; i < list.size(); i++) {
                        ParseObject word = list.get(i);
                        ReviewActivity.this.wordList.add(word);
                    }
                    initReviewView();
                } else if(list.isEmpty()){
                    System.out.println("UserReviewList is empty!");
                } else {
                    Log.d("MyApp", "Error from retrieving review words: " + e.getMessage());
                }
            }
        });
    }

    //initialize the memorization view page
    private void initReviewView() {
        setWordDisplayWithPos(0);
    }

    // helper function when click the next or previous button
    private void updateReviewView() {
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
