package com.android.memeinn;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.android.memeinn.definitionAPI.OnlineDictionaryActivity;
import com.android.memeinn.learn.ChapterActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class OnlineDictionaryActivityTest extends ActivityInstrumentationTestCase2<OnlineDictionaryActivity> {

    public OnlineDictionaryActivityTest() {
        super(OnlineDictionaryActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();
    }


    public void testWordDefinition1() {
        try{
            onView(withId(R.id.etWord)).perform(typeText(""), closeSoftKeyboard());
            Thread.sleep(2000);
            onView(withId(R.id.etResponse)).check(matches(withText("")));
        }catch(InterruptedException| NullPointerException e){
            e.printStackTrace();
        }
    }


    public void testWordDefinition2() {
        try{
            onView(withId(R.id.etWord)).perform(typeText("dissident"), closeSoftKeyboard());
            Thread.sleep(2000);
            onView(withId(R.id.etResponse)).check(matches(withText("Disagreeing, as in opinion or belief.")));
        }catch(InterruptedException| NullPointerException e){
            e.printStackTrace();
        }
    }

    public void testWordDefinition3() {
        try{
            onView(withId(R.id.etWord)).perform(typeText("balm"), closeSoftKeyboard());
            Thread.sleep(2000);
            onView(withId(R.id.etResponse)).check(matches(withText("A chiefly Mediterranean perennial herb (Melissa officinalis) in the mint family, grown for its lemon-scented foliage, which is used as a seasoning or for tea. Also called lemon balm.")));
        }catch(InterruptedException| NullPointerException e){
            e.printStackTrace();
        }
    }

    public void testWordDefinition4() {
        try{
            onView(withId(R.id.etWord)).perform(typeText("corroborate"), closeSoftKeyboard());
            Thread.sleep(2000);
            onView(withId(R.id.etResponse)).check(matches(withText("To strengthen or support with other evidence; make more certain. See Synonyms at confirm.")));
        }catch(InterruptedException| NullPointerException e){
            e.printStackTrace();
        }
    }
}
