package com.android.memeinn;

import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by qingsongqi on 3/6/15.
 */

public class MemorizationActivityTest extends ActivityInstrumentationTestCase2<ChapterActivity> {

    public MemorizationActivityTest() {
        super(ChapterActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();
    }

    public void testButtonA() {
        onView(withId(R.id.buttonA)).perform(click());
        onView(withId(R.id.prevWordBtn)).check(matches(withText("Previous")));
        onView(withId(R.id.nextWordBtn)).check(matches(withText("Next")));
    }

    public void testButtonB() {
        onView(withId(R.id.buttonB)).perform(click());
        onView(withId(R.id.prevWordBtn)).check(matches(withText("Previous")));
        onView(withId(R.id.nextWordBtn)).check(matches(withText("Next")));
    }


    public void testButtonAIntent() {
        onView(withId(R.id.buttonA)).perform(click());
        onView(withId(R.id.wordContentView)).check(matches(isDisplayed()));
        onView(withId(R.id.wordMeaningView)).check(matches(isDisplayed()));
    }

    public void testButtonBIntent() {
        onView(withId(R.id.buttonA)).perform(click());
        onView(withId(R.id.wordContentView)).check(matches(isDisplayed()));
        onView(withId(R.id.wordMeaningView)).check(matches(isDisplayed()));
    }
}

