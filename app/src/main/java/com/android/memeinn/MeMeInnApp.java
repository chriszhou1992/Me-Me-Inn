package com.android.memeinn;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.firebase.client.Firebase;
import com.parse.Parse;

/**
 * Custom Application child class for the app in order to fix
 * the screen orientation to portrait.
 */
public class MeMeInnApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // register to be informed of activities starting up
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity,
                                          Bundle savedInstanceState) {

                //Initialize Parse API to initialize connection to cloud
                Parse.initialize(activity, "l5qhJIZRq3vPDrHTmyzPu3z6IwMjukw7M3h9A8CZ",
                        "iLgCs4Z7I71j1L9DIWrjwjkCZ02yc6KuDsYVO60e");

                // new activity created; force its orientation to portrait
                activity.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                Firebase.setAndroidContext(activity);

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
