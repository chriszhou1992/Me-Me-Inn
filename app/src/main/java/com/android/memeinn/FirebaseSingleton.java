package com.android.memeinn;


import com.firebase.client.Firebase;

/**
 * Singleton Pattern class used to fetch references to Firebase.
 */
public class FirebaseSingleton {
    public static final String BASE_URL = "https://me-me-inn.firebaseio.com/";
    private static Firebase firebaseInstance = null;
    private FirebaseSingleton() {  }

    /**
     * Singleton getter for the Firebase instance.
     * @return Firebase A reference to the Singleton instance.
     */
    public static synchronized Firebase getInstance() {
        if (firebaseInstance == null)
            firebaseInstance = new Firebase(BASE_URL);
        return firebaseInstance;
    }

    /**
     * Overloaded getInstance(). Takes a String to return a Firebase instance to the
     * specified child path.
     * @param childPath String The child path.
     * @return Firebase A reference to the specified child path.
     */
    public static synchronized Firebase getInstance(String childPath) {
        if (firebaseInstance == null)
            firebaseInstance = new Firebase(BASE_URL);
        return firebaseInstance.child(childPath);
    }
}
