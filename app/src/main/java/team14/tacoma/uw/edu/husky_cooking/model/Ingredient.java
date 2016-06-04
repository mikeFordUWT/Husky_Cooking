/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */

package team14.tacoma.uw.edu.husky_cooking.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * This class was designed to model an ingredient for a recipe.
 * Ingredients will consist of the name, type, and amount.
 * @author Mike Ford
 * @author Ian Skyles
 * @version 5/2/2016
 */
public class Ingredient implements Serializable{

    /** Constant strings representing ingredient, measuring type, and amount. */
    public static final String ID = "id"
            , NAME = "ingredient", MEASURE_TYPE= "measure_type", AMOUNT = "amount";


    /** An integer to represent ingredient id number.
     * These will be unique and autoincremented by the database code */
    private int mIngredientId;
    /** A String which will store the ingredients name. */
    private String mIngredientName;
    /** A String which will store the measurement type. */
    private String mMeasurementType;
    /** A String which will store the amount, as measurement by the amount.. */
    private String mAmount;

//    /**
//     * This constructor creates a new Ingredient with
//     * specified id, name, and amount.
//     *
//     * @param id The id of the ingredient.
//     * @param name The name of the ingredient.
//     * @param amount The amount of the ingredient.
//     */
//    public Ingredient(int id, String name, String amount){
//        this.mIngredientId = id;
//        this.mIngredientName = name;
//        this.mAmount = amount;
//        this.mMeasurementType = null;
//    }

    /**
     * This constructor creates a new Ingredient with
     * specified id, name, measuring type, and amount.
     *
     * @param id The id of the ingredient.
     * @param name The name of the ingredient.
     * @param measureType How the ingredient will be measured (cups, gallons, lbs).
     * @param amount The Name of the ingredient.
     */
    public Ingredient(int id, String name, String measureType, String amount){
        this.mIngredientId = id;
        this.mIngredientName = name;
        this.mMeasurementType = measureType;
        this.mAmount = amount;
    }

    /**
     * A method to get the ingredient ID.
     * @return ingredient id number
     */
    public int getIngredientId() {
        return mIngredientId;
    }

    /**
     * A method to get the ingredient name.
     * @return ingredient name
     */
    public String getIngredientName() {
        return mIngredientName;
    }

    /**
     * A method to set the ingredient name.
     * @param mIngredientName name of ingredients
     */
    public void setIngredientName(String mIngredientName) {
        this.mIngredientName = mIngredientName;
    }
    /**
     * A method to get the measurement type.
     * @return measurement type
     */
    public String getMeasurementType() {
        if(mMeasurementType == null){
            return "";
        }else{
            return mMeasurementType;
        }

    }
    /**
     * A method to set an ingredients measurement type
     * @param mMeasurementType name of ingredients
     */
    public void setMeasurementType(String mMeasurementType) {
        this.mMeasurementType = mMeasurementType;
    }
    /**
     * A method to get the amount of an ingredient needed for a recipe.
     * @return amount needed for recipe
     */
    public String getAmount() {
        return mAmount;
    }
    /**
     * A method to set the amouint of an ingredient that is necessary for a recipe.
     * @param mAmount name of ingredients
     */
    public void setAmount(String mAmount) {
        this.mAmount = mAmount;
    }

    /**
     * Works with Serializable to parse a json item.
     * @param ingredientJSON Javascript object representing ingredient
     * @param ingredientList A list of ingredients
     * @return null if successfully parsed JSON or the reason it failed as a string
     */
    public static String parseIngredientJSON(String ingredientJSON, List<Ingredient> ingredientList){
        String reason = null;
        if(ingredientJSON != null){
            if(ingredientJSON.equals("")){
                reason = "There are no ingredients in this list.";
                //reason = "You don't have any items in your shopping list, take a nap!";
            }else{
                try{
                    JSONArray arr = new JSONArray(ingredientJSON);
                    for(int i = 0; i<arr.length(); i++){
                        JSONObject obj = arr.getJSONObject(i);
                        Ingredient ingredient;

                        ingredient = new Ingredient(obj.getInt(Ingredient.ID),
                                obj.getString(Ingredient.NAME),obj.getString(Ingredient.MEASURE_TYPE),
                                obj.getString(Ingredient.AMOUNT));


                        ingredientList.add(ingredient);
                    }
                }catch(JSONException e){
                    reason = "Unable to parse ingredients, Reason: " + e.getMessage();
                }
            }

        }
        return reason;
    }

    /**
     * Turns the object into a string representation.
     * @return a string representing amount, measurement type,
     * and ingredient name (if it has one).
     */
    @Override
    public String toString() {
        String toReturn;
        if(mMeasurementType == null){
            toReturn = mAmount + " "+ mIngredientName;
        }else{
            toReturn = mAmount +" " + mMeasurementType + " " + mIngredientName;
        }
        return toReturn;
    }
}
