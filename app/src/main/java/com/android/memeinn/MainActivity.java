package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.GetCallback;
import com.parse.ParseException;


public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("GRE");
        query.getInBackground("CMXG79JFqT", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Log.d("MyApp", "get the object " + object.get("word"));
                } else {
                    // something went wrong
                    Log.d("MyApp", "Error: " + e.getMessage());
                }
            }
        });
    }


    public void gotoVocab(View view) {
        Intent vocabIntent = new Intent(this, VocabActivity.class);
        vocabIntent.setFlags(1);//flag: 0 for review, 1 for learn
        startActivity(vocabIntent);
    }

    public void gotoProfile(View view) {
        Intent profIntent = new Intent(this, ProfileActivity.class);
        startActivity(profIntent);
    }
}
