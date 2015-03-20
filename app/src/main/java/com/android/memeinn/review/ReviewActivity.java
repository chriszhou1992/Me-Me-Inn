package com.android.memeinn.review;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.memeinn.R;
import com.android.memeinn.VocabActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

/**
 * Activity that handles the display of words to review.
 */
public class ReviewActivity extends Activity {

    private String vocabType = "";  //the type of vocabulary

    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);

        //Initialize gesture detector
        gestureDetector = new GestureDetectorCompat(this, createGestureListener());

        Intent intent = getIntent();
        vocabType = intent.getStringExtra(VocabActivity.EXTRA_MESSAGE);

        ParseUser u = ParseUser.getCurrentUser();
        String relationName = "UserReviewList" + vocabType;
        ParseRelation relation = u.getRelation(relationName);
        ParseQuery query = relation.getQuery();

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
                    }//for
                    table.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return gestureDetector.onTouchEvent(event);
                        }
                    });
                } else {
                    e.printStackTrace();
                }
            }
        });

        Log.d("Myapp", "ReviewActivity.vocabType = " + vocabType);
    }


    /**
     * Create listener for gestures.
     * @return SimpleOnGestureListener A simple gesture listener where selected gestures
     * can be overridden to provide customized functionality.
     */
    private GestureDetector.SimpleOnGestureListener createGestureListener() {
        return new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent event) {
                Log.d("gestures", "onDown: " + event.toString());
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent event) {
                Log.d("gestures", "onLongPress: " + event.toString());
                Intent detailedVocab = new Intent(ReviewActivity.this,
                    ReviewVocabDetailedActivity.class);
                detailedVocab.putExtra(VocabActivity.EXTRA_MESSAGE, vocabType);
                startActivity(detailedVocab);
                return true;
            }
        };
    }

    /**
     * Link gesture detector to touch events.
     * @param me MotionEvent
     * @return Boolean
     */
    /*@Override
    public boolean onTouchEvent(MotionEvent me) {
        Log.d("gestures", "GGG");
        return gestureDetector.onTouchEvent(me);
    }*/
}
