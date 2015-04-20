package com.android.memeinn.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.android.memeinn.R;

/**
 * Created by yifan on 4/20/15.
 */

public class FacebookLoginActivity extends Activity {
    private FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebookLogin);
        mFragmentManager = getSupportFragmentManager();
    }
}
