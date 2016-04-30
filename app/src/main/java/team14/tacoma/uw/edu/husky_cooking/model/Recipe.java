package team14.tacoma.uw.edu.husky_cooking.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 4/29/16.
 */
public class Recipe implements Serializable {
    public static final String ID = "recipe_id", NAME = "recipe_name", DESC = "description",
            MINS = "cook_time", SERVINGS = "servings";

    private int mRecipeId;
    private String mRecipeName;
    private String mDesc;
    private int mCookTime;
    private int mServings;
    private ArrayList<Ingredient> mIngredientList;


    public Recipe(int recipeId, String recipeName, String desc, int cookTime, int servings){
        this.mRecipeId = recipeId;
        this.mRecipeName = recipeName;
        this.mDesc = desc;
        this.mCookTime = cookTime;
        this.mServings = servings;
        this.mIngredientList = new ArrayList<Ingredient>();
    }

    public int getRecipeId(){
        return mRecipeId;
    }

    public String getName(){
        return mRecipeName;
    }

    public String getDescription(){
        return mDesc;
    }

    public int getCookTime(){
        return mCookTime;
    }

    public int getServings(){
        return mServings;
    }

    public ArrayList<Ingredient> getIngredientList(){
        return mIngredientList;
    }

    public static String parseRecipeJSON(String recipeJSON, List<Recipe> recipeList){
        String reason = null;
        if(recipeJSON != null){
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
        return reason;
    }
}

