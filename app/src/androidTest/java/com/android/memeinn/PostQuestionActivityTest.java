package com.android.memeinn;

import android.app.Activity;
import android.app.Instrumentation;
test.ActivityInstrumentationTestCase2;

import com.android.memeinn.user.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.endsWith;

public class PostQuestionActivityTest extends ActivityInstrumentationTestCase2<LoginActivity>{

    private Stack<Instrumentation.ActivityMonitor> activityMonitorStack;

    public PostQuestionActivityTest() {
        super(LoginActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();
        activityMonitorStack = new Stack<>();
    }

    /**
     * Adds a monitor for the launch of an activity.
     * @param className String The name of the target class we are monitoring.
     * @return ActivityMonitor The newly created monitor.
     */
    private Instrumentation.ActivityMonitor addMonitor(String className) {
        Instrumentation.ActivityMonitor m = getInstrumentation().addMonitor(
                className, null, false);
        activityMonitorStack.push(m);
        return m;
    }

    private void closeActivity(Instrumentation.ActivityMonitor m) {
        Activity act = m.getLastActivity();
        if (act != null)
            act.finish();
        getInstrumentation().removeMonitor(m);
    }

    /**
     * Private helper function that closes all opened activities.
     */
    private void closeAllOpenedActivities() {
        Instrumentation i = getInstrumentation();
        while (!activityMonitorStack.empty()) {
            Instrumentation.ActivityMonitor m = activityMonitorStack.pop();
            Activity act = m.getLastActivity();
            if (act != null)
                act.finish();
            i.removeMonitor(m);
        }
    }

    /**
     * Private helper function used to do blocked waiting until the target activity is launched.
     * @param m ActivityMonitor
     */
    private void verifyActivityLaunch(Instrumentation.ActivityMonitor m) {
        //Verify the intent is processed
        m.waitForActivityWithTimeout(5000);
        assertEquals("Monitor for MainActivity has not been called",
                1, m.getHits());
    }

    public void testAdminCheckPost() {
        onView(withId(R.id.uname)).perform(typeText("admin\n"), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("admin\n"), closeSoftKeyboard());

        //Test if correctly typed
        onView(withId(R.id.uname)).check(matches(withText("admin")));
        onView(withId(R.id.pword)).check(matches(withText("admin")));

        Instrumentation.ActivityMonitor mainActMonitor = addMonitor(MainActivity.class.getName());
        Instrumentation.ActivityMonitor profileActMonitor = addMonitor(
                ProfileActivity.class.getName());
        Instrumentation.ActivityMonitor checkPostActMonitor = addMonitor(
                CheckPostActivity.class.getName());

        //New intent to goto MainActivity
        onView(withId(R.id.login)).perform(click());
        verifyActivityLaunch(mainActMonitor);

        //Check activity transition
        onView(withId(R.id.vocab)).check(matches(withText("Go To Vocabulary")));
        onView(withId(R.id.friends)).check(matches(withText("Match with Friends")));
        onView(withId(R.id.ladder)).check(matches(withText("Match in Ladder")));

        //New intent to goto ProfileActivity
        onView(withId(R.id.imageButton)).perform(click());
        verifyActivityLaunch(profileActMonitor);

        //Check activity transition
        onView(withId(R.id.checknewpost)).check(matches(withText("Check Post")));

        //New intent to goto CheckPost Activity
        onView(withId(R.id.checknewpost)).perform(click());
        verifyActivityLaunch(checkPostActMonitor);

        //Close all opened activities to ensure the next test works correctly
        closeActivity(profileActMonitor);
        closeActivity(mainActMonitor);
        //closeAllOpenedActivities();
    }

    public void testUserAddPost() {
        onView(withId(R.id.uname)).perform(typeText("1111\n"), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("1111\n"), closeSoftKeyboard());

        //Test if correctly typed
        onView(withId(R.id.uname)).check(matches(withText("1111")));
        onView(withId(R.id.pword)).check(matches(withText("1111")));

        Instrumentation.ActivityMonitor mainActMonitor = addMonitor(MainActivity.class.getName());
        Instrumentation.ActivityMonitor profileActMonitor = addMonitor(
                ProfileActivity.class.getName());
        Instrumentation.ActivityMonitor addPostActMonitor = addMonitor(
                AddPostActivity.class.getName());
        Instrumentation.ActivityMonitor postQuestionActMonitor = addMonitor(
                PostQuestionActivity.class.getName());

        //New intent to goto MainActivity
        onView(withId(R.id.login)).perform(click());
        verifyActivityLaunch(mainActMonitor);

        //Check activity transition
        onView(withId(R.id.vocab)).check(matches(withText("Go To Vocabulary")));
        onView(withId(R.id.friends)).check(matches(withText("Match with Friends")));
        onView(withId(R.id.ladder)).check(matches(withText("Match in Ladder")));

        //New intent to goto ProfileActivity
        onView(withId(R.id.imageButton)).perform(click());
        verifyActivityLaunch(profileActMonitor);

        //Check activity transition
        onView(withId(R.id.addnewpost)).check(matches(withText("Add Post")));

        //New intent to goto Add Post Activity
        onView(withId(R.id.addnewpost)).perform(click());
        verifyActivityLaunch(addPostActMonitor);

        onView(withId(R.id.gre)).perform(click());
        verifyActivityLaunch(postQuestionActMonitor);

        onView(withId(R.id.submitpost)).check(matches(withText("Submit")));

        //Close all opened activities to ensure the next test works correctly
        closeAllOpenedActivities();
    }

    public void testUserQuiz() {
        onView(withId(R.id.uname)).perform(typeText("1111\n"), closeSoftKeyboard());
        onView(withId(R.id.pword)).perform(typeText("1111\n"), closeSoftKeyboard());

        //Test if correctly typed
        onView(withId(R.id.uname)).check(matches(withText("1111")));
        onView(withId(R.id.pword)).check(matches(withText("1111")));

        Instrumentation.ActivityMonitor mainActMonitor = addMonitor(MainActivity.class.getName());
        Instrumentation.ActivityMonitor showQuizActMonitor = addMonitor(
                ShowUserQuizActivity.class.getName());
        Instrumentation.ActivityMonitor userQuizActMonitor = addMonitor(
                UserQuizActivity.class.getName());

        //New intent to goto MainActivity
        onView(withId(R.id.login)).perform(click());
        verifyActivityLaunch(mainActMonitor);

        //Check activity transition
        onView(withId(R.id.vocab)).check(matches(withText("Go To Vocabulary")));
        onView(withId(R.id.friends)).check(matches(withText("Match with Friends")));
        onView(withId(R.id.ladder)).check(matches(withText("Match in Ladder")));
        onView(withId(R.id.userquiz)).check(matches(withText("Go To User-created quiz")));

        //New intent to goto ShowUserQuizActivity
        onView(withId(R.id.userquiz)).perform(click());
        verifyActivityLaunch(showQuizActMonitor);

        onView(withId(R.id.gre)).perform(click());
        verifyActivityLaunch(userQuizActMonitor);

        onView(withId(R.id.name)).check(matches(withText("quiz!")));

        //Close all opened activities to ensure the next test works correctly
        closeAllOpenedActivities();
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
