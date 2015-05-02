package com.android.memeinn;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.android.memeinn.user.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.endsWith;

@LargeTest
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();
    }

    public void testLogoText() {
        onView(withId(R.id.name))
                .check(matches(withText("ME ME INN")));
    }

    public void testHints() {
        onView(withId(R.id.uname)).check(matches(withHint("Username")));
        onView(withId(R.id.pword)).check(matches(withHint("Password")));
    }

    public void testSuccessLogin() {
        onView(withId(R.id.uname)).perform(typeText("my name\n"), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("my pass\n"), closeSoftKeyboard());

        //Test if correctly typed
        onView(withId(R.id.uname)).check(matches(withText("my name")));
        onView(withId(R.id.pword)).check(matches(withText("my pass")));

        //New intent to goto MainActivity
        onView(withId(R.id.login)).perform(click());

        //Check activity transition
        onView(withId(R.id.vocab)).check(matches(withText("Go To Vocabulary")));
        onView(withId(R.id.friends)).check(matches(withText("Match with Friends")));
        onView(withId(R.id.ladder)).check(matches(withText("Match in Ladder")));
    }

    public void testInvalidLogin() {
        onView(withId(R.id.uname)).perform(typeText("my name\n"), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("my name\n"), closeSoftKeyboard());

        //Test if correctly typed
        onView(withId(R.id.uname)).check(matches(withText("my name")));
        onView(withId(R.id.pword)).check(matches(withText("my name")));

        //Click Login
        onView(withId(R.id.login)).perform(click());

        intended(toPackage("com.android.memeinn.MainActivity"));

        //check if warning dialog appears
        onView(withClassName(endsWith("DialogTitle"))).check(matches(withText("Login Failed")));
    }
}
