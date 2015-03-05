package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class VocabActivity extends Activity{

    public final static String EXTRA_MESSAGE = "vocab.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabpage);
        //Parse.initialize(this, "l5qhJIZRq3vPDrHTmyzPu3z6IwMjukw7M3h9A8CZ", "iLgCs4Z7I71j1L9DIWrjwjkCZ02yc6KuDsYVO60e");
    }

    public void gotoGREChapList(View view) {
        Intent GREIntent = new Intent(this, ChapterActivity.class);
        EditText editText = (EditText) findViewById(R.id.gre);
        String message = editText.getText().toString();
        GREIntent.putExtra(EXTRA_MESSAGE, message);
        startActivity(GREIntent);
    }

}
