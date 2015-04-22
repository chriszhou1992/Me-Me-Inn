package com.android.memeinn.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.android.memeinn.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.widget.GameRequestDialog;

/**
 * Created by yifan on 4/21/15.
 */
public class FriendsInvitesActivity extends Fragment {
    GameRequestDialog requestDialog;
    CallbackManager callbackManager;
    private AccessToken accessToken;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        requestDialog = new GameRequestDialog(this);
        accessToken = AccessToken.getCurrentAccessToken();

        requestDialog.registerCallback(callbackManager, new FacebookCallback<GameRequestDialog.Result>() {
            public void onSuccess(GameRequestDialog.Result result) {
                String id = result.getRequestId();
            }

            public void onCancel() {
            }

            public void onError(FacebookException error) {
            }

        });

        onClickGameRequestButton();
    }

    private void onClickGameRequestButton() {
        GameRequestContent content = new GameRequestContent.Builder()
                .setMessage("Come play this level with me")
                .build();
        requestDialog.show(content);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}