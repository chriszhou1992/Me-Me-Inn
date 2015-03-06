package com.android.memeinn;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ReviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);

        Log.d("Myapp", "ReviewActivity");

        ParseUser u = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GRE");
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
    }
}
