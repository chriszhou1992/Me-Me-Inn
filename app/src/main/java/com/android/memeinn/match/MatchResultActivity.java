package com.android.memeinn.match;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.memeinn.Global;
import com.android.memeinn.MainActivity;
import com.android.memeinn.R;

public class MatchResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchresult);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int score = extras.getInt(Global.EXTRA_MESSAGE_MATCHSCORE);
            String res = "Your quiz score is " + Math.abs(score);
            TextView matchScore = (TextView) findViewById(R.id.matchScore);
            matchScore.setText(res);

            int matchResult = extras.getInt(Global.EXTRA_MESSAGE_MATCHRESULT);
            Log.d("matchresult", "" + matchResult);
            TextView matchResultTextView = (TextView) findViewById(R.id.resultSentence);
            if (matchResult > 0)
                matchResultTextView.setText("Congratulations! You won!");
            else if (matchResult < 0)
                matchResultTextView.setText("Awww! You lost!");
            else
                matchResultTextView.setText("Not bad! Its a tie!");
        }
    }

    /**
     * OnClick callback function that fires up an intent to go back to MainActivity.
     * @param view Button The button that is clicked.
     */
    public void goBack(View view){
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
