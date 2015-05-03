package com.android.memeinn;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

import com.android.memeinn.user.LoginActivity;
import com.android.memeinn.user.SignUpActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.endsWith;

public class SignUpActivityTest extends ActivityInstrumentationTestCase2<SignUpActivity> {

    public SignUpActivityTest() {
        super(SignUpActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //Espresso will not launch our activity for us, we must launch it via getActivity().
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

    public void testSignUpCanceled() {
        //Monitor the intent is processed
        Instrumentation.ActivityMonitor loginActivityMonitor = getInstrumentation()
                .addMonitor(LoginActivity.class.getName(), null, false);

        //Click Cancel Button
        onView(withId(R.id.cancel)).perform(click());
        //Verify intent is processed
        loginActivityMonitor.waitForActivityWithTimeout(5000);
        assertEquals("Monitor for LoginActivity has not been called",
                1, loginActivityMonitor.getHits());

        //Remove the loginActivityMonitor
        getInstrumentation().removeMonitor(loginActivityMonitor);

        //Check went back to LoginActivity
        onView(withId(R.id.uname)).check(matches(withHint("Username")));
        onView(withId(R.id.pword)).check(matches(withHint("Password")));
    }

    public void testInvalidSignUpDuplicateUsername() {
        onView(withId(R.id.uname)).perform(typeText("my name\n"), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("hello world\n"), closeSoftKeyboard());
        onView(withId(R.id.pword2)).perform(typeText("hello world\n"), closeSoftKeyboard());

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
        closePopUpDialog();
    }

    public void testInvalidSignUpUnmatchedPassword() {
        onView(withId(R.id.uname)).perform(typeText("abcd\n"), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("hello world\n"), closeSoftKeyboard());
        onView(withId(R.id.pword2)).perform(typeText("hello w\n"), closeSoftKeyboard());

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
        closePopUpDialog();
    }


    public void testInvalidSignUpEmptyUsername() {
        onView(withId(R.id.pword)).perform(typeText("hello world\n"), closeSoftKeyboard());
        onView(withId(R.id.pword2)).perform(typeText("hello world\n"), closeSoftKeyboard());

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
        closePopUpDialog();
    }

    public void testInvalidSignUpEmptyPassword() {
        onView(withId(R.id.uname)).perform(typeText("abcd\n"), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("\n"), closeSoftKeyboard());
        onView(withId(R.id.pword2)).perform(typeText("hello world\n"), closeSoftKeyboard());

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
        closePopUpDialog();
    }

    /**
     * Helper function that closes the pop up dialog and verify the close.
     */
    private void closePopUpDialog() {
        //close dialog
        onView(withId(android.R.id.button1)).perform(click());
        //check dialog disappeared
        onView(withClassName(endsWith("DialogTitle"))).check(doesNotExist());
    }
}
