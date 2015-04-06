package com.android.memeinn.match;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.memeinn.Global;
import com.android.memeinn.R;
import com.parse.ParseUser;

/**
 * Main contest activity
 */
public class ContestActivity extends Activity {
    private String currentUsername;
    private String opponentName;
    private TextView randomScore;

    public ContestActivity() {

    }

    /**
     * Setup function for the activity.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contest);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            opponentName = extras.getString(Global.EXTRA_MESSAGE_OPPONAME);
            TextView oppoNameView = (TextView) findViewById(R.id.opponent);
            oppoNameView.setText(opponentName);

            currentUsername = ParseUser.getCurrentUser().getUsername();
            TextView userNameView = (TextView) findViewById(R.id.user);
            userNameView.setText(currentUsername);
        }
    }
}
