package com.android.memeinn;


import android.app.AlertDialog;
import android.content.Context;

public class Utility {

    /**
     * Public function that opens up a warning dialog on the specified Context displaying
     * the specified title and message.
     * @param c Context Dialog will be displayed.
     * @param title String The title of the dialog.
     * @param msg String The message displayed in the dialog.
     */
    public static void warningDialog(Context c, String title, String msg) {
        AlertDialog.Builder warning = new AlertDialog.Builder(c);
        warning.setTitle(title);
        warning.setMessage(msg);
        warning.setPositiveButton(android.R.string.yes, null);
        warning.setIcon(android.R.drawable.ic_dialog_alert);
        warning.create().show();
    }
}
