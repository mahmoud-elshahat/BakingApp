package pharaoh.com.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pharaoh.com.bakingapp.RecyclerView.RecyclerViewMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by MahmoudAhmed on 10/10/2017.
 */
@RunWith(AndroidJUnit4.class)
public class TestingClass {

    @Rule
    public ActivityTestRule<pharaoh.com.bakingapp.ui.Activities.HomeActivity> mActivityRule =
            new ActivityTestRule(pharaoh.com.bakingapp.ui.Activities.HomeActivity.class);



    private CountingIdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void testingRecyclerView() {

        //Checking that first item retrieved correctly
        onView(withRecyclerView(R.id.recipesList).atPositionOnView(0, R.id.recipe_steps_count))
                .check(matches(withText("7")));
        onView(withRecyclerView(R.id.recipesList).atPositionOnView(0, R.id.recipe_name))
                .check(matches(withText("Nutella Pie")));
        onView(withRecyclerView(R.id.recipesList).atPositionOnView(0, R.id.recipe_servings))
                .check(matches(withText("10")));

        //Make click on item , at example at position 1 "Brownies"
        onView(ViewMatchers.withId(R.id.recipesList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,
                        click()));

        onView(withId(R.id.ingredientList)).check(matches(isDisplayed()));
        onView(withId(R.id.stepsList)).check(matches(isDisplayed()));


        onView(ViewMatchers.withId(R.id.stepsList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView(withId(R.id.description)).check(matches(isDisplayed()));


    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }



}
