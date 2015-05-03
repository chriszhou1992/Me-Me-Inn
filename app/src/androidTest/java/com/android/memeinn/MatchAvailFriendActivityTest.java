package com.android.memeinn;

import android.test.ActivityInstrumentationTestCase2;

import com.android.memeinn.match.AvailFriendListActivity;

/**
 * Created by bchen11 on 5/3/15.
 */
public class MatchAvailFriendActivityTest extends ActivityInstrumentationTestCase2<AvailFriendListActivity> {
    public MatchAvailFriendActivityTest() {
        super(AvailFriendListActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Espresso will not launch our activity for us, we must launch it via getActivity().
        getActivity();

    }

    public void testWithAlwaysOnline() {

    }

}
