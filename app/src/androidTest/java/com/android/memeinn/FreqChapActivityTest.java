package com.android.memeinn;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.android.memeinn.learn.VocabActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class FreqChapActivityTest extends ActivityInstrumentationTestCase2<VocabActivity> {

    public FreqChapActivityTest() {
        super(VocabActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();
    }


    /**
     * Test new vocabulary partitioning features
     */


    public void testButtonA() {
        onView(withId(R.id.gre)).perform(click());
        onView(withId(R.id.gre)).check(matches(withText("GRE")));
    }

    public void testButtonB() {
        onView(withId(R.id.ielts)).perform(click());
        onView(withId(R.id.ielts)).check(matches(withText("IELTS")));
    }

    public void testButtonC() {
        onView(withId(R.id.act)).perform(click());
        onView(withId(R.id.act)).check(matches(withText("ACT")));
    }

    public void testButtonD() {
        onView(withId(R.id.toefl)).perform(click());
        onView(withId(R.id.toefl)).check(matches(withText("TOEFL")));
    }

    public void testButtonE() {
        onView(withId(R.id.sat)).perform(click());
        onView(withId(R.id.sat)).check(matches(withText("SAT")));
    }

    public void testButtonF() {
        onView(withId(R.id.gmat)).perform(click());
        onView(withId(R.id.gmat)).check(matches(withText("GMAT")));
    }


}

