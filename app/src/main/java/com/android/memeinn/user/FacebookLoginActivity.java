package com.android.memeinn.user;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.memeinn.MainActivity;
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
import com.facebook.share.widget.GameRequestDialog;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yifan on 4/20/15.
 */
public class FacebookLoginActivity extends FragmentActivity {
    private GameRequestDialog requestDialog;
    private CallbackManager gameCallbackManager;
    FragmentManager fm = getFragmentManager();
    FragmentTransaction ft=fm.beginTransaction();

    private TextView mTextDetails;
    private TextView mFriendsList;
    private CallbackManager mCallbackManager;
    private LoginManager loginManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private AccessToken accessToken;
    private Dialog progressDialog;
    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("MeMeInn", "onSuccess");
            AccessToken accessToken = loginResult.getAccessToken();
            settingTrackersAndView();
        }

        @Override
        public void onCancel() {
            progressDialog = ProgressDialog.show(FacebookLoginActivity.this, "", "cancelling login...", true);
            gotoMainActivity(null);
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog = ProgressDialog.show(FacebookLoginActivity.this, "", "loggin error...", true);
            gotoMainActivity(null);
            progressDialog.dismiss();
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


        /*
        gameCallbackManager = CallbackManager.Factory.create();
        requestDialog = new GameRequestDialog(this);
        requestDialog.registerCallback(gameCallbackManager, new FacebookCallback<GameRequestDialog.Result>() {
            public void onSuccess(GameRequestDialog.Result result) {
                String id = result.getRequestId();
            }

            public void onCancel() {}

            public void onError(FacebookException error) {}
        });*/

        fm = getFragmentManager();
        ft=fm.beginTransaction();

    }


    public void onClickRequestButton(View view) {
//        Intent mainIntent = new Intent(getApplicationContext(), FriendsInvitesFragment.class);
//        startActivity(mainIntent);
//
//        GameRequestContent content = new GameRequestContent.Builder()
//                .setMessage("Come play this level with me").
//                .build();
//        requestDialog.show(content);


        /*
        GraphRequest(AccessToken, String, Bundle, HttpMethod, Callback)
        accessToken	The access token to use, or null
        graphPath	The graph path to retrieve, create, or delete
        parameters	Additional parameters to pass along with the Graph API request; parameters must be Strings, Numbers, Bitmaps, Dates, or Byte arrays.
        httpMethod	The HttpMethod to use for the request, or null for default (HttpMethod.GET)
        */

        /*
        String graphPath = "/"+accessToken.getUserId()+"/invitable_friends";
        GraphRequest graphRequest = new GraphRequest(accessToken, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                mFriendsList = (TextView)findViewById(R.id.facebook_friendslist);
                mFriendsList.setText(graphResponse.toString());
            }

        });*/


//        new GraphRequest(
//                session,
//                "/{user-id}/invitable_friends",
//                null,
//                HttpMethod.GET,
//                new GraphRequest.GraphJSONObjectCallback() {
//
//                }
//        ).executeAsync();

        FriendsInvitesFragment f1 = new FriendsInvitesFragment();
        ft.add(R.id.facebookinvitefriends, f1);
        //ft.add(f1,R.id.facebookinvitefriends);
        ft.commit();
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
        if(mCallbackManager!=null)
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        //gameCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void gotoMainActivity(View view){
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }

    public void gotoLogoutAndMainActivity(View view){
        LoginManager.getInstance().logOut();
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }

}
