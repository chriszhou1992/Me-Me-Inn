package com.android.memeinn;

/**
 * Created by qingsongqi on 3/6/15.
 */

import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ChapterActivityTest extends ActivityInstrumentationTestCase2<ChapterActivity> {

    public ChapterActivityTest() {
        super(ChapterActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();
    }

    public void testButtons() {
        onView(withId(R.id.buttonA)).check(matches(withText("A")));
        onView(withId(R.id.buttonB)).check(matches(withText("B")));
        onView(withId(R.id.buttonC)).check(matches(withText("C")));
        onView(withId(R.id.buttonD)).check(matches(withText("D")));
    }


    public void testButtonIntent() {
        onView(withId(R.id.buttonA)).perform(click());
        onView(withId(R.id.prevWordBtn)).check(matches(withText("Previous")));
        onView(withId(R.id.nextWordBtn)).check(matches(withText("Next")));
    }
}
