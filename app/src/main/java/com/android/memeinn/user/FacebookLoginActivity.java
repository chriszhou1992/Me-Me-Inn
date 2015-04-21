package com.android.memeinn.user;

import android.app.Dialog;
import android.content.Intent;
import android.media.tv.TvInputService;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.memeinn.MainActivity;
import com.android.memeinn.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.internal.WebDialog;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.GameRequestDialog;

import java.util.Arrays;
import java.util.List;


import bolts.AppLinks;

/**
 * Created by yifan on 4/20/15.
 */
public class FacebookLoginActivity extends FragmentActivity {
    GameRequestDialog requestDialog;
    CallbackManager gameCallbackManager;


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
        if(mTextDetails!=null)
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



        gameCallbackManager = CallbackManager.Factory.create();
        requestDialog = new GameRequestDialog(this);
        requestDialog.registerCallback(gameCallbackManager, new FacebookCallback<GameRequestDialog.Result>() {
            public void onSuccess(GameRequestDialog.Result result) {
                String id = result.getRequestId();
            }

            public void onCancel() {}

            public void onError(FacebookException error) {}
        });

    }


    public void onClickRequestButton(View view) {
        GameRequestContent content = new GameRequestContent.Builder()
                .setMessage("Come play this level with me")
                .build();
        requestDialog.show(content);
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
        gameCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void gotoMainActivity(View view){
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }

}
