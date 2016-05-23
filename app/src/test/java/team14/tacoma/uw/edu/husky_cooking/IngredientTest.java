package team14.tacoma.uw.edu.husky_cooking;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

import team14.tacoma.uw.edu.husky_cooking.model.Ingredient;

/**
 * Created by Ian Skyles on 5/16/2016.
 */
public class IngredientTest extends TestCase {
    private Ingredient mIngredient;
    private Ingredient mIngredient2;

    public void setUp() {
        mIngredient = new Ingredient(1001, "apples","3 large whole apples");
        mIngredient2 = new Ingredient(1005, "oranges","grams", "3 whole oranges");
    }


    @Test
    public void testConstructor() {
        Ingredient ingredient = new Ingredient(444, "orange sauce","1 cup");
        assertNotNull(ingredient);
    }

    public void testParseIngredientJSON() {
        String ingredientJson = "[{\"id\":\"777\",\"ingredientName\":\"blackBerries\"," +
                "\"amount\":\"500 small berries\"}]";
        String message =  Ingredient.parseIngredientJSON(ingredientJson
                , new ArrayList<Ingredient>());
        assertTrue("JSON With Valid String", message == null);
    }

    @Test
    public void testGetMeasurementType() {
        assertEquals("grams", mIngredient2.getMeasurementType());
    }


    @Test
    public void testGetServings() {
        assertEquals("3 large whole apples", mIngredient.getAmount());
    }

    @Test
    public void getIngredientName() {
        assertEquals("apples", mIngredient.getIngredientName());
    }

    @Test
    public void testGetIngredientID() {
        assertEquals(1005, mIngredient2.getIngredientId());
    }

}
