package team14.tacoma.uw.edu.husky_cooking;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.Random;

import team14.tacoma.uw.edu.husky_cooking.authenticate.SignInActivity;

/**
 * Created by Ian Skyles on 5/25/2016.
 * This class tests our recipe activity (as well as recipe detail fragment).
 */
public class RecipeActivityTest extends
        ActivityInstrumentationTestCase2<RecipeActivity> {
    private Solo solo;

    public RecipeActivityTest() {
        super(RecipeActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testRecipeList() {
        solo.clickOnButton("View All Recipes");
        solo.clickInRecyclerView(0);
        boolean fragmentLoaded = solo.searchText("French Toast");
        assertTrue("Recipe List fragment loaded", fragmentLoaded);
    }

    public void testCookBook() {
        solo.clickOnButton("Cookbook");
        solo.clickInRecyclerView(0);
        boolean fragmentLoaded = solo.searchText("Greek Pasta");
        assertTrue("Can view cookbook recipes!", fragmentLoaded);
    }



    public void testCreateANewRecipe() {
        solo.clickOnButton("Create a new Recipe");

        solo.clickOnView(getActivity().findViewById(R.id.fab));
        Random random = new Random();
        //Generate a Recipe number
        String RecipeName = "Waffle " + (random.nextInt(4) + 1)
                + (random.nextInt(4) + 1) + (random.nextInt(4) + 1 + random.nextInt(100) + 1);
        solo.enterText(0, RecipeName);
        solo.enterText(1, "Place the batter into the waffle iron. Close the waffle iron." +
                "Flip the iron. Wait until light turns green." +
                " Flip iron. Open lid and remove waffle.");
        solo.enterText(2, "10");
        solo.enterText(3, "50");
        solo.clickOnButton("Add Recipe");
        solo.clickOnButton("View All Recipes");
        boolean textFound = solo.searchText(RecipeName);
        assertTrue("Recipe added.", textFound);
    }



    public void testLogout() {
        solo.clickOnView(getActivity().findViewById(R.id.action_logout));
        boolean textFound = solo.searchText("Enter your email");
        assertTrue("Login fragment loaded", textFound);
        solo.enterText(0, "test@gmail.com");
        solo.enterText(1, "testtest");
        solo.clickOnButton("LOG IN");
        boolean worked = solo.searchText("HOME");
        assertTrue("Sign in worked!", worked);

    }


    public void testRecipeDetailFragment() {
        solo.clickOnButton("View All Recipes");
        solo.clickInRecyclerView(0);
        boolean foundRecipeDetail = solo.searchText("Fry slices");
        assertTrue("Recipe Detail fragment loaded", foundRecipeDetail);
        solo.goBack();
        boolean foundRecipeList = solo.searchText("Blueberry Pancakes");
        assertTrue("Back to List works!", foundRecipeList);
    }


    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();

    }


}
