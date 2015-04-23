package com.android.memeinn;

import android.support.test.espresso.ViewInteraction;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.android.memeinn.user.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class FriendActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    public FriendActivityTest() {
        super(LoginActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();
    }

    /**
     * Friend name could be listed
     */
    public void testFriendList() {
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
        onView(withId(R.id.friends)).check(matches(withText("Friends")));

        //New intent to goto FriendActivity
        onView(withId(R.id.friends)).perform(click());

        //Check activity transition
        onView(withId(R.id.name)).check(matches(withText("My Friends")));

        ViewInteraction v = onView(withId(R.id.lst_users));
        if (v == null)
            Log.d("Friend List", "Friend List is null! ");
        else
            Log.d("Friend List", "Friend List is not null! ");

        //New intent to goto FriendActivity
        onView(withId(R.id.addfriend)).perform(click());

        ViewInteraction w = onView(withId(R.id.txt_username));
        if (w == null)
            Log.d("friendRequest List", "friendRequest List is null! ");
        else
            Log.d("friendRequest List", "friendRequest List is not null! ");

    }

}