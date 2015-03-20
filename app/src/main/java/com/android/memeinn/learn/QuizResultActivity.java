package com.android.memeinn.learn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.memeinn.MainActivity;
import com.android.memeinn.R;


public class QuizResultActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizresult);
        ViewGroup gr = (ViewGroup)getWindow().getDecorView().findViewById(android.R.id.content);
        Log.d("MyApp", "enter the group, gr count is " + gr);

        //for(int i = 0; i < gr.getChildCount(); i++){
            //View view = gr.getChildAt(i);
            //Log.d("MyApp", "view name " + getResources().getResourceEntryName(view.getId()));
        //}

        View v = findViewById(R.id.backButton);
        Log.d("MyApp", "view type is " + v.getClass().getName());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int score = extras.getInt("score");
            String res = "Your quiz score is " + score;
            TextView quizScore = (TextView) findViewById(R.id.testScore);
            quizScore.setText(res);
        }
    }


    public void goBack(View view){
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }
}
