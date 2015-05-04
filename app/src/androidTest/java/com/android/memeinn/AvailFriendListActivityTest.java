package com.android.memeinn;


import android.graphics.Color;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.memeinn.match.AvailFriendListActivity;
import com.parse.ParseUser;

public class AvailFriendListActivityTest extends
        ActivityInstrumentationTestCase2<AvailFriendListActivity> {

    private AvailFriendListActivity testActivity;

    public AvailFriendListActivityTest() {
        super(AvailFriendListActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //Login as user "a"
        ParseUser.logIn("a", "a");
        testActivity = getActivity();
    }

    public void testUserDisplay() {
        LinearLayout ll = (LinearLayout) testActivity.findViewById(R.id.buttonField);

        int totalUsers = ll.getChildCount();
        Log.d("users", "users" + totalUsers);
        System.out.println("users" + totalUsers + "\n\n");
        for (int i = 0; i < totalUsers; i++) {
            Button b = (Button) ll.getChildAt(i);
            String btnText = b.getText().toString();
            if (btnText.equals("alwaysOnline"))
                assertEquals(b.getCurrentTextColor(), Color.GREEN);
            else if (btnText.equals("alwaysInMatch"))
                assertEquals(b.getCurrentTextColor(), Color.BLUE);
        }
        //onView(matches(withText("alwaysOnline"))).check()
    }
}
