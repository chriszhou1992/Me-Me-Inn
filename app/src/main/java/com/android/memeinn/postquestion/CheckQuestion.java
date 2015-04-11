package com.android.memeinn.postquestion;

/**
 * Created by qingsongqi on 4/9/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.memeinn.learn.ChapterActivity;
import com.android.memeinn.R;
import com.android.memeinn.learn.MemorizationActivity;
import com.android.memeinn.learn.QuizActivity;
import com.android.memeinn.review.ReviewActivity;

public class CheckQuestion extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkquestion);
        Intent intent = getIntent();

    }
}
