package com.android.memeinn.definitionAPI;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.android.memeinn.R;


/**
 * Created by yifan on 4/7/15.
 */
public class DefinitionActivity extends Activity {

    private String word;
    private EditText etResponse;
    private EditText etWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.definitionpull);
        word = "ubiquitous";

        // get reference to the views
        etWord = (EditText) findViewById(R.id.etWord);
        etResponse = (EditText) findViewById(R.id.etResponse);
        etWord.setText(word);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        Context context = getBaseContext();
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(connMgr, context, etResponse);
        httpAsyncTask.execute("https://montanaflynn-dictionary.p.mashape.com/define?word=" + word);
    }
}
