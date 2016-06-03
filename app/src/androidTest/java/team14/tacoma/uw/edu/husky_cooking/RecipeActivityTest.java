package team14.tacoma.uw.edu.husky_cooking;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.Random;

import team14.tacoma.uw.edu.husky_cooking.recipe.RecipeActivity;

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
    public void testShoppingList() {
        solo.clickOnButton("View All Recipes");
        solo.clickInRecyclerView(0);
        solo.clickOnButton("View Ingredients");
        solo.clickInRecyclerView(0);
        solo.clickOnButton("Add To Shopping List");
        solo.goBack();
        solo.goBack();
        solo.goBack();

        solo.clickOnButton("Shopping List");
        solo.clickInRecyclerView(0);
        boolean fragmentLoaded = solo.searchText("ground cinnamon");
        assertTrue("Can view items in shopping list!", fragmentLoaded);
    }

    public void testCookBook() {
        solo.clickOnButton("Cookbook");
        solo.clickInRecyclerView(0);
        boolean fragmentLoaded = solo.searchText("Blueberry Pancakes");
        assertTrue("Can view cookbook recipes!", fragmentLoaded);
    }
    public void testMenu() {
        solo.clickOnButton("View Menus");
        solo.clickInRecyclerView(0);
        solo.clickInRecyclerView(1);
        boolean fragmentLoaded = solo.searchText("Hash Browns");
        assertTrue("Can view recipes from food menus!", fragmentLoaded);
    }



    public void testCreateANewRecipeAndIngredient() {
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
        solo.clickOnButton("ADD INGREDIENTS");
        solo.enterText(0, "waffle mix");
        solo.enterText(1, "5");
        solo.enterText(1, "cups");
        solo.clickOnButton("FINISH");
        solo.clickOnButton("View Ingredients");
        boolean ingredientFound = solo.searchText("waffle mix");
        solo.goBack();
        solo.goBack();
        boolean textFound = solo.searchText(RecipeName);
        assertTrue("Recipe added and ingredient added.", textFound && ingredientFound);
    }


    //Could not get menu bar click with robotium working
    /*
    public void testLogout() {
        solo.clickOnActionBarItem(R.id.action_logout);
        boolean textFound = solo.searchText("Enter your email");

        assertTrue("Login fragment loaded", textFound);
        solo.enterText(0, "test@gmail.com");
        solo.enterText(1, "testtest");
        solo.clickOnButton("LOG IN");
        boolean worked = solo.searchText("HOME");
        assertTrue("Logout and sign in back in!", worked);
    }
    */


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
