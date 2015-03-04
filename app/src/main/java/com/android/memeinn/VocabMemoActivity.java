package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.GetCallback;
/**
 * Created by bchen11 on 3/1/15.
 */
public class VocabMemoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memorizationpage);
        //gotoNext();
        final ParseObject a = new ParseObject("yarn");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("GRE");
        query.getInBackground("CMXG79JFqT", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Log.d("MyApp", "Get the object");
                    //a = object;

                } else {
                    Log.d("MyApp", "Get Nothing");

                }
            }
        });


    }

    /**
     * triggers intent to quiz activity
     * @param view
     */
    public void gotoNext(View view) {


    }

}
