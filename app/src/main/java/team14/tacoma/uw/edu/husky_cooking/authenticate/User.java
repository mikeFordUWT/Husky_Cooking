/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking.authenticate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import team14.tacoma.uw.edu.husky_cooking.model.Recipe;

/**
 * This class was designed to model a User of the app.
 * Users will consist of the id, email, and cook book.
 * @author Mike Ford
 * @author Ian Skyles
 * @version 5/2/2016
 */
public class User implements Serializable{
    /** Constant strings representing user id and email.  */
    public static final String ID = "user_id", EMAIL = "email";

    /** A integer which will store the users (unique, autoincrementing) id number. */
    private int mId;
    /** A String which will store the users's email. */
    private String mEmail;
    /** An ArrayList that stores all the recipes in a users personalized cookbook. */
    private ArrayList<Recipe> mCookBook;

    /**
     * This constructor creates a new User with
     * specified id and email. It also creates an empty cookbook.
     *
     * @param id The id of the ingredient.
     * @param email The name of the ingredient.
     */
    public User(int id, String email){
        this.mId = id;
        this.mEmail = email;
        this.mCookBook = new ArrayList<Recipe>();

    }


    /**
     * Returns the users (unique) id number.
     * @return user id
     */
    public int getId() {
        return mId;
    }


    /**
     * Returns the users email address.
     * @return user email
     */
    public String getEmail() {
        return mEmail;
    }


    /**
     * Returns the users personalized list of recipes (cookbook).
     * @return lists of recipes
     */
    public ArrayList<Recipe> getCookBook() {
        return mCookBook;
    }

    /**
     * Works with Serializable to parse a json item.
     * @param userJSON Javascript object representing user
     * @param userList A list of users
     * @return null if successfully parsed JSON or the reason it failed as a string
     */
    public static String parseUserJSON(String userJSON, List<User> userList){
        String reason = null;
        if(userJSON !=null){
            try{
                JSONArray arr = new JSONArray(userJSON);
                for(int i =0; i<arr.length(); i++){
                    JSONObject obj = arr.getJSONObject(i);
                    User user = new User(obj.getInt(User.ID), obj.getString(User.EMAIL));
                    userList.add(user);
                }

            }catch (JSONException e){
                reason = "Unable to parse data, Reason: " + e.getMessage();
            }
        }
        return reason;
    }


    /**
     * Turns the user into a string representation which
     * only consists of email at this point. (We might add a lists of recipes later)
     * @return user's email
     */
    @Override
    public String toString() {
        return this.getEmail();
    }
}
