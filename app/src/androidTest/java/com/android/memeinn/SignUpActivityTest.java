package com.android.memeinn;

import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.endsWith;

/**
 * Created by bchen11 on 2/20/15.
 */
public class SignUpActivityTest extends ActivityInstrumentationTestCase2<SignUpActivity> {

    public SignUpActivityTest() {
        super(SignUpActivity.class);
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
        onView(withId(R.id.uname)).check(matches(withHint("Enter your username")));
        onView(withId(R.id.pword)).check(matches(withHint("Enter your password")));
        onView(withId(R.id.pword2)).check(matches(withHint("Confirm your password")));
    }

    /*
    public void testSuccessSignUp() {

        onView(withId(R.id.uname)).perform(typeText("my name"), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("my pass"), closeSoftKeyboard());

        //Test if correctly typed
        onView(withId(R.id.uname)).check(matches(withText("my name")));
        onView(withId(R.id.pword)).check(matches(withText("my pass")));

        //New intent to goto MainActivity
        onView(withId(R.id.login)).perform(click());

        //Check activity transition
        onView(withId(R.id.vocab)).check(matches(withText("Go To Vocabulary")));
        onView(withId(R.id.friends)).check(matches(withText("Match with Friends")));
        onView(withId(R.id.ladder)).check(matches(withText("Match in Ladder")));
    }*/

    public void testInvalidSignUpDuplicateUsername() {
        onView(withId(R.id.uname)).perform(typeText("my name"), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("hello world"), closeSoftKeyboard());
        onView(withId(R.id.pword2)).perform(typeText("hello world"), closeSoftKeyboard());

        //Test if correctly typed
        onView(withId(R.id.uname)).check(matches(withText("my name")));
        onView(withId(R.id.pword)).check(matches(withText("hello world")));
        onView(withId(R.id.pword2)).check(matches(withText("hello world")));

        //Click SignUp
        onView(withId(R.id.signup)).perform(click());

        //check if warning dialog appears
        onView(withClassName(endsWith("DialogTitle"))).check(matches(withText("SignUp Failed")));
        onView(withClassName(endsWith("TextView")))
                .check(matches(withText("username my name already taken")));
    }

    public void testInvalidSignUpUnmatchedPassword() {
        onView(withId(R.id.uname)).perform(typeText("abcd"), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("hello world"), closeSoftKeyboard());
        onView(withId(R.id.pword2)).perform(typeText("hello w"), closeSoftKeyboard());

        //Test if correctly typed
        onView(withId(R.id.uname)).check(matches(withText("abcd")));
        onView(withId(R.id.pword)).check(matches(withText("hello world")));
        onView(withId(R.id.pword2)).check(matches(withText("hello w")));

        //Click SignUp
        onView(withId(R.id.signup)).perform(click());

        //check if warning dialog appears
        onView(withClassName(endsWith("DialogTitle"))).check(matches(withText("Bad Input")));
        onView(withClassName(endsWith("TextView")))
                .check(matches(withText("Username/Password is not in correct format.")));
    }


    public void testInvalidSignUpEmptyUsername() {
        onView(withId(R.id.uname)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("hello world"), closeSoftKeyboard());
        onView(withId(R.id.pword2)).perform(typeText("hello world"), closeSoftKeyboard());

        //Test if correctly typed
        onView(withId(R.id.uname)).check(matches(withText("")));
        onView(withId(R.id.pword)).check(matches(withText("hello world")));
        onView(withId(R.id.pword2)).check(matches(withText("hello world")));

        //Click SignUp
        onView(withId(R.id.signup)).perform(click());

        //check if warning dialog appears
        onView(withClassName(endsWith("DialogTitle"))).check(matches(withText("Bad Input")));
        onView(withClassName(endsWith("TextView")))
                .check(matches(withText("Username/Password is not in correct format.")));
    }

    public void testInvalidSignUpEmptyPassword() {
        onView(withId(R.id.uname)).perform(typeText("abcd"), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.pword2)).perform(typeText("hello world"), closeSoftKeyboard());

        //Test if correctly typed
        onView(withId(R.id.uname)).check(matches(withText("abcd")));
        onView(withId(R.id.pword)).check(matches(withText("")));
        onView(withId(R.id.pword2)).check(matches(withText("hello world")));

        //Click SignUp
        onView(withId(R.id.signup)).perform(click());

        //check if warning dialog appears
        onView(withClassName(endsWith("DialogTitle"))).check(matches(withText("Bad Input")));
        onView(withClassName(endsWith("TextView")))
                .check(matches(withText("Username/Password is not in correct format.")));
    }
}
