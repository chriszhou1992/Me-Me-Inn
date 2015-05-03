package com.android.memeinn;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.android.memeinn.learn.MemorizationActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MemorizationActivityTest extends ActivityInstrumentationTestCase2<MemorizationActivity> {

    public MemorizationActivityTest() {
        super(MemorizationActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();
    }

    
    public void testButtonA() {
        onView(withId(R.id.gre)).perform(click());
        onView(withId(R.id.buttonA)).perform(click());
        onView(withId(R.id.prevWordBtn)).check(matches(withText("Previous")));
        onView(withId(R.id.nextWordBtn)).check(matches(withText("Next")));
    }

    public void testButtonB() {
        onView(withId(R.id.gre)).perform(click());
        onView(withId(R.id.buttonB)).perform(click());
        onView(withId(R.id.prevWordBtn)).check(matches(withText("Previous")));
        onView(withId(R.id.nextWordBtn)).check(matches(withText("Next")));
    }


    public void testButtonAIntent() {
        onView(withId(R.id.gre)).perform(click());
        onView(withId(R.id.buttonA)).perform(click());
        onView(withId(R.id.wordContentView)).check(matches(isDisplayed()));
        onView(withId(R.id.wordMeaningView)).check(matches(isDisplayed()));
    }

    public void testButtonBIntent() {
        onView(withId(R.id.gre)).perform(click());
        onView(withId(R.id.buttonA)).perform(click());
        onView(withId(R.id.wordContentView)).check(matches(isDisplayed()));
        onView(withId(R.id.wordMeaningView)).check(matches(isDisplayed()));
    }

    //newly added test cases

    /**
     * Test new frequency partitioning features
     */


    public void testWithHighFrequencyClicked() {
        Log.d("Myapp", "testWithHighFrequencyClicked");
        onView(withId(R.id.gre)).perform(click());
        onView(withId(R.id.buttonA)).perform(click());

        onView(withId(R.id.prevWordBtn)).check(matches(withText("Previous")));
        onView(withId(R.id.nextWordBtn)).check(matches(withText("Next")));
    }

    public void testWithMediumFrequencyClicked() {
        Log.d("Myapp", "testWithMediumFrequencyClicked");
        onView(withId(R.id.gre)).perform(click());
        onView(withId(R.id.buttonB)).perform(click());

        onView(withId(R.id.prevWordBtn)).check(matches(withText("Previous")));
        onView(withId(R.id.nextWordBtn)).check(matches(withText("Next")));
    }


    public void testWithLowFrequencyClicked() {
        Log.d("Myapp", "testWithLowFrequencyClicked");
        onView(withId(R.id.gre)).perform(click());
        onView(withId(R.id.buttonC)).perform(click());

        onView(withId(R.id.prevWordBtn)).check(matches(withText("Previous")));
        onView(withId(R.id.nextWordBtn)).check(matches(withText("Next")));
    }


    public void testHighFrequencyWordMatch() throws InterruptedException{
        Log.d("Myapp", "testWithHighFrequencyClicked");

        onView(withId(R.id.gre)).perform(click());
        Thread.sleep(100);
        onView(withId(R.id.buttonA)).perform(click());
        //onView(withId(R.id.nextWordBtn)).perform(click());
        //needs sometime for database to respond
        Thread.sleep(5000);
        onView(withId(R.id.wordContentView)).check(matches(withText("abate")));
    }


    public void testMeidumFrequencyWordMatch() throws InterruptedException{
        Log.d("Myapp", "testWithHighFrequencyClicked");

        onView(withId(R.id.gre)).perform(click());
        Thread.sleep(100);
        onView(withId(R.id.buttonB)).perform(click());
        //onView(withId(R.id.nextWordBtn)).perform(click());
        //needs sometime for database to respond
        Thread.sleep(5000);
        onView(withId(R.id.wordContentView)).check(matches(withText("abhor")));
    }



    public void testLowFrequencyWordMatch() throws InterruptedException{
        Log.d("Myapp", "testWithHighFrequencyClicked");

        onView(withId(R.id.gre)).perform(click());
        Thread.sleep(100);
        onView(withId(R.id.buttonC)).perform(click());
        //onView(withId(R.id.nextWordBtn)).perform(click());
        //needs sometime for database to respond
        Thread.sleep(5000);
        onView(withId(R.id.wordContentView)).check(matches(withText("abacus")));
    }



        //all the way from sign in
        /*
        onView(withId(R.id.uname)).perform(typeText("my name"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("my pass"), ViewActions.closeSoftKeyboard());

        //Test if correctly typed
        onView(withId(R.id.uname)).check(matches(withText("my name")));
        onView(withId(R.id.pword)).check(matches(withText("my pass")));

        //New intent to goto MainActivity
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(100);
        onView(withId(R.id.vocab)).perform(click());
        Thread.sleep(100);*/
}

