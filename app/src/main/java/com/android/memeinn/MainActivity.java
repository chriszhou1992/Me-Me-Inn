package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.parse.Parse;

/**
 * Created by bchen11 on 2/19/15.
 */
public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        //Parse.initialize(this, "l5qhJIZRq3vPDrHTmyzPu3z6IwMjukw7M3h9A8CZ", "iLgCs4Z7I71j1L9DIWrjwjkCZ02yc6KuDsYVO60e");
    }


    public void gotoVocab(View view) {
        Intent vocabIntent = new Intent(this, VocabActivity.class);
        startActivity(vocabIntent);
    }
}
