package com.android.memeinn.user;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yifan on 4/20/15.
 */
public class FacebookLoginActivity extends FragmentActivity {
    private TextView invitableFriendsList;
    private TextView in_game_friendsList;
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
            AccessToken at = loginResult.getAccessToken();
            settingTrackersAndView();
            displayInvitableFriendsList(at);
            displayFriendsList(at);
        }

        @Override
        public void onCancel() {
            gotoMainActivity(null);
        }

        @Override
        public void onError(FacebookException e) {
            gotoMainActivity(null);
        }
    };

    /**
     * Setting up trackers in case of profile/token change,
     * update view according to profile change
     */
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

    /**
     * On activity create, login the facebook user
     * setting up access token according to user login.
     * If user already logged in, retrieve the old accessToken
     * Displaying in/out of game friend lists for the current logged in user.
     * @param savedInstanceState
     */
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
            displayInvitableFriendsList(accessToken);
            displayFriendsList(accessToken);
        }
    }

    /**
     * Function for displaying the not in game friends list,
     * using graph API to retrieve the list
     *
     * GraphRequest(AccessToken, String, Bundle, HttpMethod, Callback)
     * accessToken	The access token to use, or null
     * graphPath	The graph path to retrieve, create, or delete
     * parameters	Additional parameters to pass along with the Graph API request; parameters must be Strings, Numbers, Bitmaps, Dates, or Byte arrays.
     * httpMethod	The HttpMethod to use for the request, or null for default (HttpMethod.GET)
     *
     * @param at
     */
    public void displayInvitableFriendsList(AccessToken at){
        String graphPath = "/"+at.getUserId()+"/invitable_friends";
        GraphRequest graphRequest = new GraphRequest(at, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                invitableFriendsList = (TextView)findViewById(R.id.invitablefriendslist);
                String tempS = "Facebook Not In Game Friends List:\n";
                try {
                    JSONArray jsonArray = graphResponse.getJSONObject().getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        String name = jsonArray.getJSONObject(i).getString("name");
                        tempS += name+"\n";
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                invitableFriendsList.setText(tempS);
            }

        });
        graphRequest.executeAsync();
    }

    /**
     * Function for displaying the in game friends list,
     * using graph API to retrieve the list
     * @param at
     */
    public void displayFriendsList(AccessToken at){
        String graphPath = "/"+at.getUserId()+"/friends";
        GraphRequest graphRequest = new GraphRequest(at, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                in_game_friendsList = (TextView)findViewById(R.id.in_game_friendslist);
                String tempS = "Facebook In Game Friends List:\n";
                try {
                    JSONArray jsonArray = graphResponse.getJSONObject().getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        String name = jsonArray.getJSONObject(i).getString("name");
                        tempS += name+"\n";
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                in_game_friendsList.setText(tempS);
            }

        });
        graphRequest.executeAsync();
    }

    /**
     * While clicking on the invite friend button
     * Load the invite friend Fragment into view
     */
    public void onClickRequestButton(View view) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        FriendsInvitesFragment f1 = new FriendsInvitesFragment();
        ft.add(R.id.facebookinvitefriends, f1);
        ft.commit();
    }


    /**
     * Display welcome message to specific logged in facebook user
     * @param profile
     * @return string of the welcome message
     */
    private String constructWelcomeMessage(Profile profile) {
        StringBuffer stringBuffer = new StringBuffer();
        if (profile != null) {
            stringBuffer.append("Welcome " + profile.getName());
        }
        return stringBuffer.toString();
    }

    /**
     * Setting up on facebook token change tracker
     */
    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("MeMeInn", "" + currentAccessToken);
            }
        };
    }

    /**
     * Setting up on facebook profile change tracker
     */
    private void setupProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Log.d("MeMeInn", "" + currentProfile);
                mTextDetails.setText(constructWelcomeMessage(currentProfile));
            }
        };
    }


    /**
     * On app resume, resume the profile and view text
     */
    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        String newText = constructWelcomeMessage(profile);
        if(mTextDetails!=null && newText!=null)
            mTextDetails.setText(newText);
    }

    /**
     * On app stop, stop both facebook trackers
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mTokenTracker != null)
            mTokenTracker.stopTracking();
        if (mProfileTracker != null)
            mProfileTracker.stopTracking();
    }

    /**
     * on Call back activity, use call back manager to handle result
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mCallbackManager!=null)
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        //gameCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Go to the main menu while clicking
     */
    public void gotoMainActivity(View view){
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }

    /**
     * Go to the main menu while clicking logout, also void the accessToken
     */
    public void gotoLogoutAndMainActivity(View view){
        LoginManager.getInstance().logOut();
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }

    /**
     * Get private accessToken for this session
     * @return
     */
    public AccessToken getAccessToken(){
        return accessToken;
    }

}
