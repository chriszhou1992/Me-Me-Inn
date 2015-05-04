package com.android.memeinn.match;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.memeinn.FirebaseSingleton;
import com.android.memeinn.Global;
import com.android.memeinn.R;
import com.android.memeinn.Utility;
import com.android.memeinn.Question;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Collections;
import java.util.List;

/**
 * Intermediate Activity for selecting match topic.
 */
public class SelectMatchingTopicActivity  extends Activity {

    private static final int ROUNDS = 4;
    private static final int NUM_OF_OPTIONS = 4;

    Firebase matchRef;
    private String userChosen; //boolean indicating whether a topic choice has made
    private String oppoChosen;
    String oppoName;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userChosen = null;
        oppoChosen = null;

        setContentView(R.layout.selectcontesttopic);
        oppoName = getIntent().getStringExtra(Global.EXTRA_MESSAGE_OPPONAME);
        currentUsername = ParseUser.getCurrentUser().getUsername();

        matchRef = FirebaseSingleton.getInstance("matches/" + Utility.combineStringSorted
                (oppoName, currentUsername));
        matchRef.addChildEventListener(createListenerOpponentTopicChoice());

        ((TextView)findViewById(R.id.userName)).setText(currentUsername);
        ((TextView)findViewById(R.id.oppoName)).setText(oppoName);
    }

    /**
     * Creates an event listener for the opponent's topic choice in Firebase.
     * @return ChildEventListener The created event listener.
     */
    private ChildEventListener createListenerOpponentTopicChoice() {
        return new ChildEventListener() {
            private int childCount = 0;
            //2 users' entry and ROUNDS*2 questions entry
            private final int MAX_CHILD_COUNT = 2 + ROUNDS * 2;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                childCount++;
                if (childCount == MAX_CHILD_COUNT) {
                    Log.d("match", "childCount = " + childCount);
                    directToContestPage();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("childMap", dataSnapshot.toString());
                if (dataSnapshot.getKey().equals(oppoName)) {
                    //set opponent's topic
                    int oppoTopicID = dataSnapshot.getValue(Integer.class);
                    View target = findViewById(oppoTopicID);
                    target.setBackgroundColor(Color.BLUE);
                    oppoChosen = ((Button) target).getText().toString();
                    //directToContestPage();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("userList", "get chosen topic failed " + firebaseError.getMessage());
            }
        };
    }

    /**
     * Private database fetch function that grabs all data needed and compose out all quiz
     * questions in one shot.
     */
    private void generateMatchQuestions() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(userChosen);

        Log.d("match", "userchose = " + userChosen);
        int frequency = Utility.randomInt(1, 10);
        Log.d("match", "freq = " + frequency);
        query.whereGreaterThan("frequency", frequency);
        query.setLimit(100);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> wordList, ParseException e) {
                if (e == null) {
                    Log.d("MyApp", "get the object with size " + wordList.size());

                    Collections.shuffle(wordList); //shuffle the word list

                    String[] options = new String[NUM_OF_OPTIONS];
                    for (int i = 0; i < ROUNDS; i++) {
                        Utility.clearSession();

                        ParseObject word = wordList.get(i);
                        String w = word.getString("word");
                        int random;
                        for (int j = 0; j < NUM_OF_OPTIONS; j++) {
                            random = Utility.randomIntNonDuplicated(0, wordList.size() - 1, i);
                            options[j] = wordList.get(random).getString("definition").trim();
                        }

                        //pick where to place the correct answer
                        random = Utility.randomInt(0, NUM_OF_OPTIONS - 1);
                        Log.d("MyApp", "random = " + random);
                        options[random] = word.getString("definition");
                        Question q = new Question(w, options, random);
                        publishQuestion(q, i);
                    }//for
                } else {
                    Log.d("MyApp", "Oops, list too short with only size " + wordList.size());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Function used to publish a question to Firebase.
     * @param q Question The question to be published.
     * @param questionIndex int An integer indicating the current question index.
     */
    private void publishQuestion(Question q, int questionIndex) {
        int compare = currentUsername.compareToIgnoreCase(oppoName);
        if (compare < 0)
            matchRef.child("Q" + (2 * questionIndex + 1) ).setValue(q); //odd-indexed questions
        else
            matchRef.child("Q" + (2 * (questionIndex + 1)) ).setValue(q); //even-indexed questions
    }

    /**
     * Function that fires up an intent for ContestActivity. Called when all questions are
     * generated from both players to start the actual competition.
     */
    private void directToContestPage(){
        if (oppoChosen != null && userChosen != null){
            Intent matchStartIntent = new Intent(this, ContestActivity.class);

            matchStartIntent.putExtra(Global.EXTRA_MESSAGE_OPPONAME, oppoName);
            matchStartIntent.putExtra(Global.EXTRA_MESSAGE_USERSELECTEDTOPIC, userChosen);
            matchStartIntent.putExtra(Global.EXTRA_MESSAGE_OPPOSELECTEDTOPIC, oppoChosen);
            Log.d("match", "started");
            startActivity(matchStartIntent);
            finish();
        }
        Log.d("match", "directing");
    }

    /**
     * onClick event callback function used to publish the chosen topic to Firebase.
     * @param view The View clicked.
     */
    public void chooseTopic(View view) {
        if(userChosen == null){
            view.setBackgroundColor(Color.GREEN);
            userChosen = ((Button)view).getText().toString().toUpperCase();
            matchRef.child(currentUsername).setValue(view.getId());
            //create match questions from the selected topic
            generateMatchQuestions();
        }
    }
}
