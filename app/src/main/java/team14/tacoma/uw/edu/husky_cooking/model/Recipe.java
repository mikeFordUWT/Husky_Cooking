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
import java.util.ArrayList;
import java.util.List;

/**
 * This class was designed to model a Recipe.
 * Recipes will consist of the (unique) id, name, description,
 * cook time, servings, and ingredient list.
 *
 * @author Mike Ford
 * @author Ian Skyles
 * @version 5/2/2016
 */
public class Recipe implements Serializable {
    /** Constant strings representing recipe id, recipe name, descripotion, cooking time, and servings.
     * Used for the JSON */
    public static final String ID = "recipe_id", NAME = "recipe_name", DESC = "description",
            MINS = "cook_time", SERVINGS = "servings";


    /** An integer to represent (unique) recipe id number.
     * These will be unique and auto incremented by the database code */
    private int mRecipeId;
    /** A String which will store the recipe's name. */
    private String mRecipeName;
    /** A String which will store a short description about the recipe
     * (what it is, taste, tips). */
    private String mDesc;
    /** An integer which will store the cook time for a recipe. */
    private int mCookTime;
    /** An integer which will store the number of servings a recipe will make. */
    private int mServings;
    /**
     * A list of ingredients necessary to make a recipe.
     */
    private ArrayList<Ingredient> mIngredientList;


    /**
     * This class was designed to model a recipe.
     * This constructor creates a recipe with the following:
     * Unique recipe id, the recipe's name,
     * the recipe's description, time to cook/make the recipe,
     * and how many servings will be produced.
     * Ingredient list is also stored, but created empty.
     *
     * @param recipeId ids should be unique (thus auto incremented)
     * @param recipeName name of the recipe (eg "ian's banana ice cream")
     * @param desc a short amout of text describing the food
     * @param cookTime how many minutes it takes to make a recipe
     * @param servings how many servings will be produced if the directions are followed
     */
    public Recipe(int recipeId, String recipeName, String desc, int cookTime, int servings){
        this.mRecipeId = recipeId;
        this.mRecipeName = recipeName;
        this.mDesc = desc;
        this.mCookTime = cookTime;
        this.mServings = servings;
        this.mIngredientList = new ArrayList<Ingredient>();
    }

    /**
     * Returns the recipes id number
     * @return (unique) id number
     */
    public int getRecipeId(){
        return mRecipeId;
    }

    /**
     * Return the recipes name.
     * @return recipe name
     */
    public String getName(){
        return mRecipeName;
    }

    /**
     * Returns a recipes description.
     * @return description of recipe
     */
    public String getDescription(){
        return mDesc;
    }

    /**
     * Returns time required to cook recipe.
     * @return minutes required to make recipe
     */
    public int getCookTime(){
        return mCookTime;
    }

    /**
     * Returns the number of servings the recipe will make.
     * @return number of servings
     */
    public int getServings(){
        return mServings;
    }

    /**
     * Returns list of ingredients required to create a recipe.
     * @return list of ingredients used for the recipe
     */
    public ArrayList<Ingredient> getIngredientList(){
        return mIngredientList;
    }

    /**
     * Works with Serializable to parse a json item.
     * @param recipeJSON Javascript object representing a recipe
     * @param recipeList A list of recipes
     * @return null if successfully parsed JSON or the reason it failed as a string
     */
    public static String parseRecipeJSON(String recipeJSON, List<Recipe> recipeList){
        String reason = null;
        if(recipeJSON != null){

            if(recipeJSON.equals("")){
                reason = "You don't have any recipes in your Cookbook yet!";
            }else{
                try{
                    JSONArray arr = new JSONArray(recipeJSON);

                    for(int i =0; i<arr.length(); i++){
                        JSONObject obj = arr.getJSONObject(i);
                        Recipe recipe = new Recipe(obj.getInt(Recipe.ID),
                                obj.getString(Recipe.NAME), obj.getString(Recipe.DESC),
                                obj.getInt(Recipe.MINS), obj.getInt(Recipe.SERVINGS));
                        recipeList.add(recipe);
                    }
                }catch (JSONException e){
                    reason = "Unable to parse data, Reason:" + e.getMessage();
                }
            }
        }
        return reason;
    }
    /**
     * Turns the recipe into a string representation.
     *  We use this later for sharing a recipe.
     *  @return a string representing recipe
     */
    @Override
    public String toString() {
        StringBuilder recipe = new StringBuilder();
        recipe.append("The name of the recipe is ");
        recipe.append(this.mRecipeName);
        recipe.append(". \n\n");
        recipe.append("It makes ");
        recipe.append(this.mServings);
        recipe.append(" servings and takes ");
        recipe.append(this.mCookTime);
        recipe.append(" minutes to cook. \n\n");
        recipe.append("You'll need these ingredients:\n");
        for (Ingredient ingr : this.mIngredientList) {
            recipe.append(ingr.getIngredientName());
            recipe.append(" ");
            recipe.append(ingr.getAmount());
            recipe.append(" ");
            recipe.append(ingr.getIngredientName());
            recipe.append("\n");
        }
        recipe.append(this.mDesc);
        recipe.append("The name of the recipe is ");
        recipe.append(this.mRecipeName);
        recipe.append("I hope you love it!");
        return recipe.toString();
    }
}

