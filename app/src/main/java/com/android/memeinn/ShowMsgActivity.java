package com.android.memeinn;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ShowMsgActivity extends Activity {

    TextView txt_ok;
    TextView txt_cancel;
    TextView txt_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showmsg);

        // get the id and return the content
        txt_ok = (TextView) findViewById(R.id.txt_ok);
        txt_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                ShowMsgActivity.this.finish();
            }
        });

        txt_cancel = (TextView) findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // get the id and return the content
        txt_content = (TextView) findViewById(R.id.txt_content);
        String strContent = getIntent().getStringExtra(MainActivity.MESSAGE_CONTENT);
        if (!TextUtils.isEmpty(strContent))
            txt_content.setText(strContent);

        boolean bSelect = getIntent().getBooleanExtra(MainActivity.SELECT_CASE, false);
        if (bSelect) {
            txt_ok.setText("Accept");
            txt_cancel.setText("Reject");
        }
        else {
            txt_cancel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
