package com.android.memeinn;

import android.test.ActivityInstrumentationTestCase2;
import com.android.memeinn.definitionAPI.OnlineDictionaryActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class OnlineDictionaryActivityTest extends ActivityInstrumentationTestCase2<OnlineDictionaryActivity> {

    public OnlineDictionaryActivityTest() {
        super(OnlineDictionaryActivity.class);
    }

    /**
     * Set up tests for OnlineDictionaryActivity class by calling the super setup method.
     * Espresso will not launch our activity for us, we must launch it via getActivity().
     * @throws Exception
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    /**
     * Testing whether an empty work input resturns empty definition
     * null Test
     */
    public void testWordDefinition1() {
        try{
            onView(withId(R.id.etWord)).perform(typeText(""), closeSoftKeyboard());
            Thread.sleep(2000);
            onView(withId(R.id.etResponse)).check(matches(withText("")));
        }catch(InterruptedException| NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Testing whether the word dissident returns the correct definition
     * Correct Value Test
     */
    public void testWordDefinition2() {
        try{
            onView(withId(R.id.etWord)).perform(typeText("dissident"), closeSoftKeyboard());
            Thread.sleep(2000);
            onView(withId(R.id.etResponse)).check(matches(withText("Disagreeing, as in opinion or belief.")));
        }catch(InterruptedException| NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Testing whether the word balm returns the correct definition
     * Correct input word test
     */
    public void testWordDefinition3() {
        try{
            onView(withId(R.id.etWord)).perform(typeText("balm"), closeSoftKeyboard());
            Thread.sleep(2000);
            onView(withId(R.id.etResponse)).check(matches(withText("A chiefly Mediterranean perennial herb (Melissa officinalis) in the mint family, grown for its lemon-scented foliage, which is used as a seasoning or for tea. Also called lemon balm.")));
        }catch(InterruptedException| NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Testing whether the word corroborate returns the correct definition
     * Correct input word test
     */
    public void testWordDefinition4() {
        try{
            onView(withId(R.id.etWord)).perform(typeText("corroborate"), closeSoftKeyboard());
            Thread.sleep(2000);
            onView(withId(R.id.etResponse)).check(matches(withText("To strengthen or support with other evidence; make more certain. See Synonyms at confirm.")));
        }catch(InterruptedException| NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Testing whether the incorrectly spelled word defination returns the expectect output
     * Incorrect input word test
     */
    public void testWordDefinition5() {
        try{
            onView(withId(R.id.etWord)).perform(typeText("defination"), closeSoftKeyboard());
            Thread.sleep(2000);
            onView(withId(R.id.etResponse)).check(matches(withText("Common misspelling of definition.")));
        }catch(InterruptedException| NullPointerException e){
            e.printStackTrace();
        }
    }
}
