package com.android.memeinn;


import android.app.AlertDialog;
import android.content.Context;

public class Utility {

    public static void warningDialog(Context c, String title, String msg) {
        AlertDialog.Builder warning = new AlertDialog.Builder(c);
        warning.setTitle(title);
        warning.setMessage(msg);
        warning.setPositiveButton(android.R.string.yes, null);
        warning.setIcon(android.R.drawable.ic_dialog_alert);
        warning.create().show();
    }
}
