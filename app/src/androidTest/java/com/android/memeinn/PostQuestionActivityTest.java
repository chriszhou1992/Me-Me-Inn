package com.android.memeinn;

/**
 * Created by qingsongqi on 4/9/15.
 */

import android.support.test.espresso.ViewInteraction;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.android.memeinn.learn.ChapterActivity;
import com.android.memeinn.user.LoginActivity;
import com.android.memeinn.user.ProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class PostQuestionActivityTest extends ActivityInstrumentationTestCase2<LoginActivity>{

    public PostQuestionActivityTest() {
        super(LoginActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();
    }

    public void testAdminCheckPost() {
        onView(withId(R.id.uname)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("admin"), closeSoftKeyboard());

        //Test if correctly typed
        onView(withId(R.id.uname)).check(matches(withText("admin")));
        onView(withId(R.id.pword)).check(matches(withText("admin")));

        //New intent to goto MainActivity
        onView(withId(R.id.login)).perform(click());

        //Check activity transition
        onView(withId(R.id.vocab)).check(matches(withText("Go To Vocabulary")));
        onView(withId(R.id.friends)).check(matches(withText("Match with Friends")));
        onView(withId(R.id.ladder)).check(matches(withText("Match in Ladder")));

        //New intent to goto ProfileActivity
        onView(withId(R.id.imageButton)).perform(click());

        //Check activity transition
        onView(withId(R.id.checknewpost)).check(matches(withText("Check Post")));

        //New intent to goto CheckPost Activity
        onView(withId(R.id.checknewpost)).perform(click());

        onView(withId(R.id.gre)).perform(click());

        onView(withId(R.id.acceptthepost)).check(matches(withText("Accept")));

    }

    public void testUserAddPost() {
        onView(withId(R.id.uname)).perform(typeText("1111"), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("1111"), closeSoftKeyboard());

        //Test if correctly typed
        onView(withId(R.id.uname)).check(matches(withText("1111")));
        onView(withId(R.id.pword)).check(matches(withText("1111")));

        //New intent to goto MainActivity
        onView(withId(R.id.login)).perform(click());

        //Check activity transition
        onView(withId(R.id.vocab)).check(matches(withText("Go To Vocabulary")));
        onView(withId(R.id.friends)).check(matches(withText("Match with Friends")));
        onView(withId(R.id.ladder)).check(matches(withText("Match in Ladder")));

        //New intent to goto ProfileActivity
        onView(withId(R.id.imageButton)).perform(click());

        //Check activity transition
        onView(withId(R.id.addnewpost)).check(matches(withText("Add Post")));

        //New intent to goto Add Post Activity
        onView(withId(R.id.addnewpost)).perform(click());

        onView(withId(R.id.gre)).perform(click());

        onView(withId(R.id.submitpost)).check(matches(withText("Submit")));

    }
}
