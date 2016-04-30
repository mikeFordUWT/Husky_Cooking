package team14.tacoma.uw.edu.husky_cooking.authenticate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import team14.tacoma.uw.edu.husky_cooking.model.Recipe;

/**
 * Created by michaelford on 4/30/16.
 */
public class User implements Serializable{
    public static final String ID = "user_id", EMAIL = "email";

    private int mId;
    private String mEmail;
    private ArrayList<Recipe> mCookBook;

    public User(int id, String email){
        this.mId = id;
        this.mEmail = email;
        this.mCookBook = new ArrayList<Recipe>();

    }


    public int getId() {
        return mId;
    }



    public String getEmail() {
        return mEmail;
    }



    public ArrayList<Recipe> getCookBook() {
        return mCookBook;
    }

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



    @Override
    public String toString() {
        return this.getEmail();
    }
}
