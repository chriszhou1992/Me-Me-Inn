package com.android.memeinn;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private TextView reviewProgressView;

    private int total;//total num of words to review
    private int count;//counting up the progress


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewchart);

        wordContentView = (TextView) findViewById(R.id.wordContentView);
        wordMeaningView = (TextView) findViewById(R.id.wordMeaningView);
        hide = (Button) findViewById(R.id.checkMeaning);
        reviewProgressView = (TextView) findViewById(R.id.reviewProgress);

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
        String relationName = "UserReviewList"+vocabType;
        ParseRelation relation = u.getRelation(relationName);
        ParseObject word = wordList.get(currPos);
        relation.remove(word);
        u.saveInBackground();
        Log.d("Myapp", word.getString("word")+" has been removed");
        //this somehow didn't remove the word from DB successfully
        onClickNext(view);
    }

    public void onClickNext(final View view) {
        count++;
        if (count < total) {
            currPos ++;
            updateReviewView();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Review finished!");
            alertDialog.setMessage("Congradulations! You have finished the review. Click OK to go back to Profile page.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    backToProfile();
                }
            });
            alertDialog.setIcon(R.drawable.img2);
            alertDialog.show();
        }
    }

    public void listEmpty(){
        System.out.println("UserReviewList is empty!");
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Review list is empty!");
        alertDialog.setMessage("You hav nothing to review yet. Try doing more quizes. Click OK to go back to Profile page.");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                backToProfile();
            }
        });
        alertDialog.setIcon(R.drawable.img2);
        alertDialog.show();
    }

    //parse the vocabulary set and print the words and meanings
    private void initList() {
        ParseUser u = ParseUser.getCurrentUser();
        String relationName = "UserReviewList"+vocabType;
        ParseRelation relation = u.getRelation(relationName);
        ParseQuery query = relation.getQuery();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if ((e == null) && (!list.isEmpty())) {
                    Log.d("MyApp", "get review word objects");
                    System.out.println("UserReviewList of this user has " + list.size() + " tuples");
                    total = list.size();

                    for (int i = 0; i < list.size(); i++) {
                        ParseObject word = list.get(i);
                        ReviewActivity.this.wordList.add(word);
                    }
                    initReviewView();
                } else if(list.isEmpty()){
                    listEmpty();
                } else {
                    Log.d("MyApp", "Error from retrieving review words: " + e.getMessage());
                }
            }
        });
    }

    //initialize the memorization view page
    private void initReviewView() {
        count = 0;
        setWordDisplayWithPos(0);
    }

    // helper function when click the next or previous button
    private void updateReviewView() {
        hideMeaning();
        setWordDisplayWithPos(currPos);
    }

    //get the current word position to control the sequence
    private void setWordDisplayWithPos(int pos) {
        ParseObject word = wordList.get(pos);
        String wordContent = word.getString("word");
        String wordMeaning = word.getString("definition");
        String progress = "You have mastered "+count+"/"+total;
        this.wordContentView.setText(wordContent);
        this.wordMeaningView.setText(wordMeaning);
        this.reviewProgressView.setText(progress);
    }

    public void backToProfile(View view) {
        Intent profIntent = new Intent(this, ProfileActivity.class);
        startActivity(profIntent);
    }

    public void backToProfile() {
        Intent profIntent = new Intent(this, ProfileActivity.class);
        startActivity(profIntent);
    }
}
