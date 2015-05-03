package com.android.memeinn;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import com.android.memeinn.user.FacebookLoginActivity;
import com.facebook.AccessToken;
import com.facebook.FacebookActivity;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Yifan on 5/2/15.
 */
public class FacebookLoginActivityTest extends ActivityInstrumentationTestCase2<FacebookLoginActivity> {

    public FacebookLoginActivityTest() {
        super(FacebookLoginActivity.class);
    }

    @Override
    public void setUp() throws Exception {
            super.setUp();
            // Espresso will not launch our activity for us, we must launch it via getActivity().
            getActivity();
    }

    public void testFacebookProfileName(){
        try {
            Thread.sleep(2000);
            AccessToken at = getActivity().getAccessToken();
            String graphPath = "/"+at.getUserId()+"/user";
            GraphRequest graphRequest = new GraphRequest(at, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {

                    String tempS = "Facebook In Game Name:\n";
                    try {
                        JSONArray jsonArray = graphResponse.getJSONObject().getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++){
                            String name = jsonArray.getJSONObject(i).getString("name");
                            tempS += name+"\n";
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }

                }

            });
            onView(withId(R.id.facebook_text)).perform();
        }catch(InterruptedException| NullPointerException e){
            e.printStackTrace();
        }
    }

    public void testFacebookInvitableFriendList(){
        try {
            Thread.sleep(2000);
            AccessToken at = getActivity().getAccessToken();
            String graphPath = "/"+at.getUserId()+"/invitable_friends";
            GraphRequest graphRequest = new GraphRequest(at, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {

                    String tempS = "Facebook In Game Invitable Friends List:\n";
                    try {
                        JSONArray jsonArray = graphResponse.getJSONObject().getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++){
                            String name = jsonArray.getJSONObject(i).getString("name");
                            tempS += name+"\n";
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }

                }

            });
            onView(withId(R.id.facebookinvitefriends)).perform();
        }catch(InterruptedException| NullPointerException e){
            e.printStackTrace();
        }
    }

    public void testFacebookFriendsList(){
        try {
            Thread.sleep(2000);
            AccessToken at = getActivity().getAccessToken();
            String graphPath = "/"+at.getUserId()+"/friends";
            GraphRequest graphRequest = new GraphRequest(at, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {

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

                }

            });
            onView(withId(R.id.friends)).perform();
        }catch(InterruptedException| NullPointerException e){
            e.printStackTrace();
        }
    }

    public void testFacebookInviteFriends(){
        try {
            Thread.sleep(2000);
            AccessToken at = getActivity().getAccessToken();
            String graphPath = "/"+at.getUserId()+"/invite";
            GraphRequest graphRequest = new GraphRequest(at, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {

                    try {
                        JSONArray jsonArray = graphResponse.getJSONObject().getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++){
                            String name = jsonArray.getJSONObject(i).getString("name");
                            Assert.assertNull(name);
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }

                }

            });
            onView(withId(R.id.facebookinvitefriends)).perform();
        }catch(InterruptedException| NullPointerException e){
            e.printStackTrace();
        }
    }

}
