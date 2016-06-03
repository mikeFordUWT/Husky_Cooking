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
 * This class was designed to model a food menu (multiple recipes).
 *
 * @author Mike Ford
 * @author Ian Skyles
 * @version 6/3/2016
 */
public class FoodMenu implements Serializable{

    /** Constant strings representing menu id, menu name.
     * Used for the JSON
     * */
    public static final String ID = "menu_id", NAME = "menu_name";
    /** An integer to represent (unique) menu id number. */
    private int mMenuId;

    /** A String which will store the recipe's name. */
    private String mMenuName;

    /**
     * A list of ingredients necessary to make a recipe.
     */
    private ArrayList<Recipe> mRecipeList;


    /**
     *
     * This class was designed to model a food menu.
     * Food menus house recipes that taste good together!
     * @param id the id of the menu
     * @param name the name of the menu
     */
    public FoodMenu(int id, String name){
        this.mMenuId = id;
        this.mMenuName = name;
        this.mRecipeList = new ArrayList<Recipe>();
    }

    /**
     * Gets the menu item's ID.
     * @return menu ID
     */
    public int getMenuId() {
        return mMenuId;
    }

    /**
     * Gets the menu item's name
     * @return menu item name
     */
    public String getMenuName() {
        return mMenuName;
    }

    /**
     * Gets the menu item's recipes
     * @return list of recipes in menu item
     */
    public ArrayList<Recipe> getRecipeList() {
        return mRecipeList;
    }

    /**
     * Works with Serializable to parse JSON object.
     * @param menuJSON JavaScript object representing a menu.
     * @param foodMenuList A list of menus.
     * @return reason for failure or null if JSON parsed successfully.
     */
    public static String parseMenuJSON(String menuJSON, List<FoodMenu> foodMenuList){
        String reason = null;

        if(menuJSON != null){

            if(menuJSON.equals("")){
                reason = "You don't have any recipes in your Cookbook yet!";
            }else{
                try{
                    JSONArray arr = new JSONArray(menuJSON);

                    for(int i =0; i<arr.length(); i++){
                        JSONObject obj = arr.getJSONObject(i);
                        FoodMenu foodMenu = new FoodMenu(obj.getInt(FoodMenu.ID),
                                obj.getString(FoodMenu.NAME));

                        foodMenuList.add(foodMenu);
                    }
                }catch (JSONException e){
                    reason = "Unable to parse data, Reason:" + e.getMessage();
                }
            }
        }
        return reason;
    }
}
