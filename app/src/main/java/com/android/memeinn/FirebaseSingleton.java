package com.android.memeinn;


import com.firebase.client.Firebase;

public class FirebaseSingleton {
    public static final String BASE_URL = "https://me-me-inn.firebaseio.com/";
    private static Firebase firebaseInstance = null;
    private FirebaseSingleton() {  }

    public static synchronized Firebase getInstance() {
        if (firebaseInstance == null)
            firebaseInstance = new Firebase(BASE_URL);
        return firebaseInstance;
    }

    public static synchronized Firebase getInstance(String childPath) {
        if (firebaseInstance == null)
            firebaseInstance = new Firebase(BASE_URL);
        return firebaseInstance.child(childPath);
    }
}
