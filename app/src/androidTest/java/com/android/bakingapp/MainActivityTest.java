package com.android.bakingapp;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.android.bakingapp.ui.activity.ListActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<ListActivity> activityRule = new ActivityTestRule<>(ListActivity.class);

    @Test
    public void validateListScreen() {
        onView(withId(R.id.recyclerview_recipes))
                .check(matches(isDisplayed()));
    }

    @Test
    public void validateStepScreen() {
        onView(allOf(withId(R.id.recyclerview_recipes),
                childAtPosition(withClassName(is("android.support.constraint.ConstraintLayout")), 0)))
                .perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recyclerview_steps))
                .check(matches(isDisplayed()));
    }

    @Test
    public void validateDetailScreen() {
        onView(allOf(withId(R.id.recyclerview_recipes),
                childAtPosition(withClassName(is("android.support.constraint.ConstraintLayout")), 0)))
                .perform(actionOnItemAtPosition(0, click()));
        onView(allOf(withId(R.id.recyclerview_steps),
                childAtPosition(withClassName(is("android.support.constraint.ConstraintLayout")), 0)))
                .perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_video_player))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recipe_description_text_view))
                .check(matches(isDisplayed()));
        onView(withId(R.id.back_button))
                .check(matches(isDisplayed()));
        onView(withId(R.id.forward_button))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickNext() {
        onView(allOf(withId(R.id.recyclerview_recipes),
                childAtPosition(withClassName(is("android.support.constraint.ConstraintLayout")), 0)))
                .perform(actionOnItemAtPosition(0, click()));
        onView(allOf(withId(R.id.recyclerview_steps),
                childAtPosition(withClassName(is("android.support.constraint.ConstraintLayout")), 0)))
                .perform(actionOnItemAtPosition(0, click()));
        onView(allOf(withId(R.id.forward_button),
                withText("Next"),
                childAtPosition(allOf(withId(R.id.navigation_card_view_2), childAtPosition(withId(R.id.recipe_detail_linear_layout), 1)), 0)))
                .perform(scrollTo(), click());
    }

    @Test
    public void clickBack() {
        onView(allOf(withId(R.id.recyclerview_recipes),
                childAtPosition(withClassName(is("android.support.constraint.ConstraintLayout")), 0)))
                .perform(actionOnItemAtPosition(0, click()));
        onView(allOf(withId(R.id.recyclerview_steps),
                childAtPosition(withClassName(is("android.support.constraint.ConstraintLayout")), 0)))
                .perform(actionOnItemAtPosition(0, click()));
        onView(allOf(withId(R.id.back_button),
                withText("Back"),
                childAtPosition(allOf(withId(R.id.navigation_card_view), childAtPosition(withId(R.id.recipe_detail_linear_layout), 0)), 0)))
                .perform(scrollTo(), click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
