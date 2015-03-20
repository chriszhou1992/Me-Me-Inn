package com.android.memeinn;
import android.text.TextUtils;

import com.parse.ParseClassName;
import com.parse.ParseObject;


/*
 * An extension of ParseObject that makes
 * it more convenient to access information
 * about a given Meal
 */

@ParseClassName("RequestFriendSession")
public class RequestFriendSession extends ParseObject {

    public static String CLASS_NAME = "RequestFriendSession";

    /*
     * fields all functions below are used for connecting database
     */
    public static final String REQUEST_FIELD_FROMUSERID = "fromUserID";
    public static final String REQUEST_FIELD_FROMUSERNAME = "fromUserName";
    public static final String REQUEST_FIELD_TOUSERID = "toUserID";
    public static final String REQUEST_FIELD_TOUSERNAME = "toUserName";
    public static final String REQUEST_FIELD_ISACCEPTED = "isAccept";
    public static final String REQUEST_FIELD_ISREJECTED = "isReject";
    public static final String REQUEST_FIELD_ISFROMCHECKED = "isFromCheck";
    public static final String REQUEST_FIELD_ISTOCHECKED = "isToCheck";

    private String mFromUserID;
    private String mFromUserName;
    private String mToUserID;
    private String mToUserName;

    public RequestFriendSession() {
        mFromUserID = "";
        mFromUserName = "";
        mToUserID = "";
        mToUserName = "";
    }

    public String getFromUserID() {
        if (TextUtils.isEmpty(mFromUserID))
            mFromUserID = getString(REQUEST_FIELD_FROMUSERID);
        return mFromUserID;
    }

    public void setFromUserID(String fromUserID) {
        put(REQUEST_FIELD_FROMUSERID, fromUserID);
    }

    public String getFromUserName() {
        if (TextUtils.isEmpty(mFromUserName))
            mFromUserName = getString(REQUEST_FIELD_FROMUSERNAME);
        return mFromUserName;
    }

    public void setFromUserName(String fromUserName) {
        put(REQUEST_FIELD_FROMUSERNAME, fromUserName);
    }

    public String getToUserID() {
        if (TextUtils.isEmpty(mToUserID))
            mToUserID = getString(REQUEST_FIELD_TOUSERID);
        return mToUserID;
    }

    public void setToUserID(String toUserID) {
        put(REQUEST_FIELD_TOUSERID, toUserID);
    }

    public String getToUserName() {
        if (TextUtils.isEmpty(mToUserName))
            mToUserName = getString(REQUEST_FIELD_TOUSERNAME);
        return mToUserName;
    }

    public void setToUserName(String toUserName) {
        put(REQUEST_FIELD_TOUSERNAME, toUserName);
    }

    public boolean getIsAccepted() {
        return getBoolean(REQUEST_FIELD_ISACCEPTED);
    }

    public void setIsAccepted(boolean isAccept) {
        put(REQUEST_FIELD_ISACCEPTED, isAccept);
    }

    public boolean getIsRejected() {
        return getBoolean(REQUEST_FIELD_ISREJECTED);
    }

    public void setIsRejected(boolean isReject) {
        put(REQUEST_FIELD_ISREJECTED, isReject);
    }

    public boolean getIsFromChecked() {
        return getBoolean(REQUEST_FIELD_ISFROMCHECKED);
    }

    public void setIsFromChecked(boolean isCheck) {
        put(REQUEST_FIELD_ISFROMCHECKED, isCheck);
    }

    public boolean getIsToChecked() {
        return getBoolean(REQUEST_FIELD_ISTOCHECKED);
    }

    public void setIsToChecked(boolean isCheck) {
        put(REQUEST_FIELD_ISTOCHECKED, isCheck);
    }
}
