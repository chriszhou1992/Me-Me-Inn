package com.android.memeinn.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.memeinn.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yifan on 4/20/15.
 */
public class FacebookLoginActivity extends FragmentActivity {
    private TextView mTextDetails;
    private CallbackManager mCallbackManager;
    private LoginManager loginManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private AccessToken accessToken;
    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("MeMeInn", "onSuccess");
            AccessToken accessToken = loginResult.getAccessToken();
            settingTrackersAndView();
        }

        @Override
        public void onCancel() {
            Log.d("MeMeInn", "onCancel");
            mTextDetails.setText("User cancelled login.");
        }

        @Override
        public void onError(FacebookException e) {
            mTextDetails.setText("Facebook callback error:"+e.toString());
            Log.d("MeMeInn", "onError " + e);
        }
    };

    private void settingTrackersAndView(){
        setupTokenTracker();
        setupProfileTracker();

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();

        Profile profile = Profile.getCurrentProfile();
        mTextDetails = (TextView)findViewById(R.id.facebook_text);
        mTextDetails.setText(constructWelcomeMessage(profile));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebookdisplay);
        if((accessToken = AccessToken.getCurrentAccessToken()) == null) {
            mCallbackManager = CallbackManager.Factory.create();
            List<String> permissions = Arrays.asList("public_profile", "user_friends");
            loginManager = LoginManager.getInstance();

            loginManager.logInWithReadPermissions(this, permissions);
            loginManager.registerCallback(mCallbackManager, mFacebookCallback);

        } else{
            settingTrackersAndView();
        }

    }

    private String constructWelcomeMessage(Profile profile) {
        StringBuffer stringBuffer = new StringBuffer();
        if (profile != null) {
            stringBuffer.append("Welcome " + profile.getName());
        }
        return stringBuffer.toString();
    }

    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("MeMeInn", "" + currentAccessToken);
            }
        };
    }

    private void setupProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.d("MeMeInn", "" + currentProfile);
                mTextDetails.setText(constructWelcomeMessage(currentProfile));
            }
        };
    }


    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        String newText = constructWelcomeMessage(profile);
        if(mTextDetails!=null && newText!=null)
            mTextDetails.setText(newText);
    }

    @Override
    public void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
