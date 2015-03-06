package com.android.memeinn;

import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.regex.Pattern.matches;

/**
 * Created by bchen11 on 3/4/15.
 */
public class QuizActivityTest extends ActivityInstrumentationTestCase2<QuizActivity> {
    public QuizActivityTest() {
        super(QuizActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();
    }

    public void testLogoText() {
        onView(withId(R.id.name))
                .check(matches(withText("Quiz!")));
    }
}
