package com.android.memeinn;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

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
     *init firebase, get the opponent user name and set a pair relation in the firebase to
     * indicate that the two users are in a game
     * @param savedInstanceState
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
