package com.android.memeinn;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.memeinn.learn.QuizActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.hamcrest.Matcher;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

public class QuizActivityTest extends ActivityInstrumentationTestCase2<QuizActivity> {
    private String lastWord;
    private QuizActivity currAct;

    public QuizActivityTest() {
        super(QuizActivity.class);
        lastWord = new String();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        currAct = getActivity();
        if (currAct != null)
            Log.d("Myapp", "GetActivity");
    }

    /**
     * test the title of the quiz page
     */
    public void testLogoText() {
        onView(withId(R.id.name))
                .check(matches(withText("quiz!")));
    }

    /**
     * different word can be generated
     */
    public void testDifferentWord() {

        Log.d("Myapp", "testDifferentWord");

        onView(withId(R.id.testWord))
                .check(matches(withText(not(containsString("quiz!")))));
       Log.d("quizTest", "find success test word ");
        ViewInteraction v = onView(withId(R.id.testWord));
        if (v == null)
            Log.d("quizTest", "testWord is null! ");
        else
            Log.d("quizTest", "testWord is not null! ");

        v.perform(new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "Retrieve vocab from View";
            }

            @Override
             public void perform(UiController uiController, View view) {
                if (view == null)
                    Log.d("NOOO", "NULL");
                else {
                    System.out.println("called by");
                    System.out.print(view.getClass());
                    lastWord  = ((TextView)view).getText().toString(); //Save, because of check in getConstraints()

                }
            }
         });
        System.out.println("after text");

        onView(withId(R.id.testWord)).check(matches(withText(containsString(lastWord))));
        onView(withId(R.id.option0)).perform(click());
        onView(withId(R.id.testWord)).check(matches(withText(not(containsString(lastWord)))));

        onView(withId(R.id.name))
                .check(matches(withText("quiz!")));

    }

    public void testScoreAccumulation(){
        if (currAct != null)
            Log.d("Myapp", "testScoreAccumulation");

        onView(withId(R.id.testWord))
                .check(matches(withText(not(containsString("quiz!")))));

        // do checking each iteration
        ViewInteraction v = onView(withId(R.id.testWord));
        v.perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "Retrieve vocab from View";
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view == null)
                    Log.d("NOOO", "NULL");
                else {
                    Log.d("NOOO", "lastword is " + lastWord);
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("GRE");
                    query.whereEqualTo("word", lastWord);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> wordList, ParseException e) {
                            if (e == null ) {
                                Log.d("MyApp", "get the object with size " + wordList.size());
                                if(currAct == null){
                                    Log.d("MyApp", "currAct is null!");
                                }
                                else{
                                    Log.d("MyApp", "currAct has score " + currAct.getScore());
                                }
                                Log.d("MyApp", "currAct options are " + currAct.getNUM_OF_OPTIONS());
                            } else {
                                Log.d("MyApp", "Error: " + e.getMessage());
                            }
                        }
                    });
                }
            }
        });
        View questionField = currAct.findViewById(R.id.testWord);
        lastWord = "sample";
        while(lastWord.equals("sample")){
            lastWord = ((TextView) questionField).getText().toString();
        }
        int rawScore = 0;
        for(int i = 0; i < currAct.getNUM_OF_OPTIONS(); i++){
            rawScore+=getSelectionResult(questionField);
        }
        int finalScore = currAct.getScore();
        if(finalScore == rawScore){
            onView(withId(R.id.testWord))
                    .check(matches(withText(not(containsString("quiz!")))));
        }
        else
            onView(withId(R.id.testWord))
                    .check(matches(withText(containsString("quiz!"))));
    }

    public int getSelectionResult(View questionField) {

            int selButtonId = Utility.randomInt(0, currAct.getNUM_OF_OPTIONS()-1);
            Button chosen = currAct.getButton(selButtonId);
            boolean right = (boolean)chosen.getTag();

            lastWord = chosen.getText().toString();

            onView((withId(currAct.getButtonId(selButtonId)))).perform(click());
            do{}
            while(lastWord.equals(((TextView) questionField).getText().toString()));

            return right ? 1:0;
    }

}