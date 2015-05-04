package com.android.memeinn.learn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.memeinn.Global;
import com.android.memeinn.MainActivity;
import com.android.memeinn.R;

/**
 * Activity to display the quiz result.
 */
public class QuizResultActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizresult);
        ViewGroup gr = (ViewGroup)getWindow().getDecorView().findViewById(android.R.id.content);

        View v = findViewById(R.id.backButton);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int score = extras.getInt(Global.EXTRA_MESSAGE_QUIZSCORE);
            String res = "Your quiz score is " + score;
            TextView quizScore = (TextView) findViewById(R.id.testScore);
            quizScore.setText(res);
        }
    }

    /**
     * Event callback triggering intent to go back to MainActivity.
     * @param view
     */
    public void goBack(View view){
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }
}