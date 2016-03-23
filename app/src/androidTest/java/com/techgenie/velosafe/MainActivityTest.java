package com.techgenie.velosafe;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Nina on 19/03/2016.
 * OnView(Matcher)
 *  perform(ViewAction)
 *  check(ViewAssertion)
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final ActivityRule<MainActivity> mainActivityTest = new ActivityRule<>(MainActivity.class);

    @Test
    public void shouldBeAbleToLoaunchMainScreen(){
        onView(withText("Velo Safe")).check(ViewAssertions.matches(isDisplayed()));
        onView(withText("REPORT")).check(ViewAssertions.matches(isDisplayed()));
    }
}
