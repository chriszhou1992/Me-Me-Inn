package com.android.memeinn;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.android.memeinn.friend.RequestFriendSession;
import com.firebase.client.Firebase;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseFacebookUtils;
/**
 * Custom Application class for the app to initialize Firebase and Parse
 * backend, force screen orientation, and keep track of the current foreground
 * activity.
 */
public class MeMeInnApp extends Application {
    public Activity currentActivity;    //tracks the activity on display in the foreground

    @Override
    public void onCreate() {
        super.onCreate();
        //only needs to be called once per application
        Firebase.setAndroidContext(this);
        //Initialize Parse API to initialize connection to cloud
        Parse.initialize(this, getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));
        ParseObject.registerSubclass(RequestFriendSession.class);
        ParseFacebookUtils.initialize("");

        // register to be informed of activities starting up
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity,
                                          Bundle savedInstanceState) {
                // new activity created; force its orientation to portrait
                activity.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                //Every activity must call onResumed() before going to the foreground
                currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (activity == currentActivity)
                    currentActivity = null;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (activity == currentActivity)
                    currentActivity = null;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (activity == currentActivity)
                    currentActivity = null;
            }
        });
    }
}
