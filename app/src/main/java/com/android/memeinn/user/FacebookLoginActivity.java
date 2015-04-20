package com.android.memeinn.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.android.memeinn.R;

/**
 * Created by yifan on 4/20/15.
 */
public class FacebookLoginActivity extends FragmentActivity {
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.facebookdisplay);
    }
}
