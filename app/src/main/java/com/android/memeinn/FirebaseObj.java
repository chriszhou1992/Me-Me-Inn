package com.android.memeinn;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.Firebase;

/**
 * Created by bchen11 on 3/12/15.
 * thanks to http://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
 */

// simple class just uses parcelable as a wrapper to transport the firebase reference
public class FirebaseObj implements Parcelable {
    public Firebase fire; //reference to the firebase

    /* everything below here is for implementing Parcelable */
    public FirebaseObj(Firebase fire){
        this.fire = fire;
    }

    // 99.9% of the time you can just ignore this
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {
        //out.writeInt(mData);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<FirebaseObj> CREATOR = new Parcelable.Creator<FirebaseObj>() {
        public FirebaseObj createFromParcel(Parcel in) {
            return new FirebaseObj(in);
        }

        public FirebaseObj[] newArray(int size) {
            return new FirebaseObj[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private FirebaseObj(Parcel in) {
        //fireRef = in.readInt();
    }
}