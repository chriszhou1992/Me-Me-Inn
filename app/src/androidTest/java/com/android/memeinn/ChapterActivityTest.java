package com.android.memeinn;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

import com.android.memeinn.learn.ChapterActivity;

import java.util.Stack;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ChapterActivityTest extends ActivityInstrumentationTestCase2<ChapterActivity> {

    private Stack<Instrumentation.ActivityMonitor> activityMonitorStack;

    public ChapterActivityTest() {
        super(ChapterActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();
    }

    public void testWithHighFrequencyClicked() {
        onView(withId(R.id.buttonA)).perform(click());
        onView(withId(R.id.chap1)).perform(click());
        onView(withId(R.id.prevWordBtn)).check(matches(withText("Previous")));
        onView(withId(R.id.nextWordBtn)).check(matches(withText("Next")));
    }

    public void testWithMediumFrequencyClicked() {
        onView(withId(R.id.buttonB)).perform(click());
        onView(withId(R.id.chap2)).perform(click());
        onView(withId(R.id.prevWordBtn)).check(matches(withText("Previous")));
        onView(withId(R.id.nextWordBtn)).check(matches(withText("Next")));
    }

    public void testWithLowFrequencyClicked() {
        onView(withId(R.id.buttonC)).perform(click());
        onView(withId(R.id.chap3)).perform(click());
        onView(withId(R.id.prevWordBtn)).check(matches(withText("Previous")));
        onView(withId(R.id.nextWordBtn)).check(matches(withText("Next")));
    }

}
