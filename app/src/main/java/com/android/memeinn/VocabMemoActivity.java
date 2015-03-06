package com.android.memeinn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.GetCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bchen11 on 3/1/15.
 */
public class VocabMemoActivity extends Activity {
    public ArrayList<ParseObject> wordList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memorizationpage);

        //getWords(getWindow().getDecorView().findViewById(android.R.id.content));
    }

    /**
     * triggers intent to quiz activity
     * @param view
     */
//    public void getWords(View view) {
//        Log.d("MyApp", "enter getwords");
//
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("GRE");
//        query.whereStartsWith("Word", "a");
//        query.findInBackground(new FindCallback<ParseObject>(){
//            @Override
//            public void done(List<ParseObject> capList, ParseException e) {
//                Log.d("MyApp", "enter done");
//
//                if (e == null && capList.size() >= 4) {
//                            TextView meaning = (TextView)findViewById(R.id.wordmeaning);
//                            TextView word = (TextView)findViewById(R.id.name);
//                            meaning.setText(capList.get(0).getString("Definition"));
//                            word.setText(capList.get(0).getString("Word"));
//                }
//                else{
//                    Log.d("MyApp", "cap size is null " + capList.size());
//                }
//            }
//        });
//    };

}
