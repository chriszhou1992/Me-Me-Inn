package com.android.memeinn;

import android.app.Activity;
import android.os.Bundle;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by bchen11 on 3/9/15.
 * disregared, just for testing purpose
 */
public class testConnection extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

    }
    public static void main(String[] arg ){
       // Firebase.setAndroidContext(this);


        Firebase myFirebaseRef = new Firebase("https://me-me-inn.firebaseio.com/");


        myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
            }

            @Override public void onCancelled(FirebaseError error) { }

        });

    }
}
