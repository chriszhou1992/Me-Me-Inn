package com.android.memeinn;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * Created by lindsey on 15-03-20.
 */
public class ReviewActivityTest  extends ActivityInstrumentationTestCase2<ReviewActivity> {
    private String lastWord;
    private String lastProgress;
    private ReviewActivity currAct;

    public ReviewActivityTest() {
        super(ReviewActivity.class);
        lastWord = new String();
        lastProgress = new String();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        currAct = getActivity();
        if (currAct != null)
            Log.d("Myapp", "GetActivity");
    }

    /**
     * test if word is successfully removed from database relation after user clicks "I know"
     */
    public void testRemoval() {
        Log.d("Myapp", "Review Test: testRemoval");
        //get current word object
        int currPos = currAct.getCurrPos();
        ArrayList<ParseObject> wordList = currAct.getWordList();
        final ParseObject word = wordList.get(currPos);
        //assert word object in database
        ParseUser u = ParseUser.getCurrentUser();
        String relationName = "UserReviewList"+currAct.getVocabType();
        ParseRelation relation = u.getRelation(relationName);
        ParseQuery query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if ((e == null) && (!list.isEmpty())) {
                    assertTrue(list.contains(word));
                }
            }
        });
        //remove word object by clicking "I know"
        onView(withId(R.id.ik))
                    .perform(click());
        //assert word object not in database
        query = relation.getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if ((e == null) && (!list.isEmpty())) {
                    assertTrue(!list.contains(word));
                }
            }
        });
    }

    /**
     * different word can be generated when go to next by clicking "I know"
     */
    public void testNextWordGeneratedWithIK() {

        Log.d("Myapp", "Review Test: testNextWordGenerated");
        ViewInteraction v = onView(withId(R.id.wordContentView));
        if (v == null)
            Log.d("Review Test", "testWord is null! ");
        else
            Log.d("Review Test", "testWord is not null! ");

        //check word updated after "I know"

        v.perform(new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "Retrieve vocab from View";
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view == null)
                    Log.d("NOOO", "NULL");
                else {
                    System.out.println("called by");
                    System.out.print(view.getClass());
                    lastWord  = ((TextView)view).getText().toString(); //Save, because of check in getConstraints()

                }
            }
        });
        System.out.println("after text");
        onView(withId(R.id.testWord)).check(matches(withText(containsString(lastWord))));
        onView(withId(R.id.ik)).perform(click());
        onView(withId(R.id.testWord)).check(matches(withText(not(containsString(lastWord)))));

    }

    /**
     * different word can be generated when go to next by clicking "I don't know"
     */
    public void testNextWordGeneratedWithIDK() {

        Log.d("Myapp", "Review Test: testNextWordGenerated");
        ViewInteraction v = onView(withId(R.id.wordContentView));
        if (v == null)
            Log.d("Review Test", "testWord is null! ");
        else
            Log.d("Review Test", "testWord is not null! ");
        //check word updated after "I don't know"
        v.perform(new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "Retrieve vocab from View";
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view == null)
                    Log.d("NOOO", "NULL");
                else {
                    System.out.println("called by");
                    System.out.print(view.getClass());
                    lastWord  = ((TextView)view).getText().toString(); //Save, because of check in getConstraints()

                }
            }
        });
        System.out.println("after text");
        onView(withId(R.id.testWord)).check(matches(withText(containsString(lastWord))));
        onView(withId(R.id.idk)).perform(click());
        onView(withId(R.id.testWord)).check(matches(withText(not(containsString(lastWord)))));
    }

    public void testProgressBarUpdateWithIK(){

        Log.d("Myapp", "Review Test: testProgressBarUpdated");
        ViewInteraction v = onView(withId(R.id.reviewProgress));
        if (v == null)
            Log.d("Review Test", "progress bar is null! ");
        else
            Log.d("Review Test", "progress bar is not null! ");

        //check progress bar updated after "I know"
        v.perform(new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "Retrieve vocab from View";
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view == null)
                    Log.d("NOOO", "NULL");
                else {
                    System.out.println("called by");
                    System.out.print(view.getClass());
                    lastProgress  = ((TextView)view).getText().toString(); //Save, because of check in getConstraints()

                }
            }
        });
        System.out.println("after text");
        onView(withId(R.id.reviewProgress)).check(matches(withText(containsString(lastProgress))));
        onView(withId(R.id.ik)).perform(click());
        onView(withId(R.id.reviewProgress)).check(matches(withText(not(containsString(lastProgress)))));


    }

    public void testProgressBarUpdateWithIDK() {

        Log.d("Myapp", "Review Test: testProgressBarUpdated");
        ViewInteraction v = onView(withId(R.id.reviewProgress));
        if (v == null)
            Log.d("Review Test", "progress bar is null! ");
        else
            Log.d("Review Test", "progress bar is not null! ");

        //check progress bar updated after "I don't know"
        v.perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "Retrieve vocab from View";
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view == null)
                    Log.d("NOOO", "NULL");
                else {
                    System.out.println("called by");
                    System.out.print(view.getClass());
                    lastProgress = ((TextView) view).getText().toString(); //Save, because of check in getConstraints()

                }
            }
        });
        System.out.println("after text");
        onView(withId(R.id.reviewProgress)).check(matches(withText(containsString(lastProgress))));
        onView(withId(R.id.idk)).perform(click());
        onView(withId(R.id.reviewProgress)).check(matches(withText(not(containsString(lastProgress)))));
    }

}