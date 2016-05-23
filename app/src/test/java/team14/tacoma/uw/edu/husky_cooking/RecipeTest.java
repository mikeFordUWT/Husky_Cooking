package team14.tacoma.uw.edu.husky_cooking;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

import team14.tacoma.uw.edu.husky_cooking.model.Recipe;
import team14.tacoma.uw.edu.husky_cooking.model.User;

/**
 * Created by Ian Skyles on 5/16/2016.
 */
public class RecipeTest extends TestCase {
    private Recipe mRecipe;

    public void setUp() {
        mRecipe = new Recipe(1001, "TestPancakes", "tasty test cakes", 20, 3);
    }


    @Test
    public void testConstructor() {
        Recipe recipe = new Recipe(777, "BuzzBerries", "berries that make you buzz", 5, 50);
        assertNotNull(recipe);
    }

    public void testParseRecipeJSON() {
        String recipeJSON = "[{\"id\":\"777\",\"recipeName\":\"buzz berries\"," +
                "\"desc\":\"get a buzz\",\"cooktime\":\"5\",\"serings\":\"5\"}]";
        String message =  Recipe.parseRecipeJSON(recipeJSON
                , new ArrayList<Recipe>());
        assertTrue("JSON With Valid String", message == null);
    }

    @Test
    public void testGetCookTime() {
        assertEquals(20, mRecipe.getCookTime());
    }


    @Test
    public void testGetServings() {
        assertEquals(3, mRecipe.getServings());
    }

    @Test
    public void testGetDsecription() {
        assertEquals("tasty test cakes", mRecipe.getDescription());
    }

    @Test
    public void testGetName() {
        assertEquals("TestPancakes", mRecipe.getName());
    }

    @Test
    public void testGetRecipeID() {
        assertEquals(1001, mRecipe.getRecipeId());
    }


}
