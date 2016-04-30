package team14.tacoma.uw.edu.husky_cooking.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 4/30/16.
 */
public class Ingredient implements Serializable{
    public static final String ID = "id"
            , NAME = "ingredient", MEASURE_TYPE= "measure_type", AMOUNT = "amount";


    private int mIngredientId;
    private String mIngredientName;
    private String mMeasurementType;
    private String mAmount;

    public Ingredient(int id, String name, String amount){
        this.mIngredientId = id;
        this.mIngredientName = name;
        this.mAmount = amount;
        this.mMeasurementType = null;
    }

    public Ingredient(int id, String name, String measureType, String amount){
        this.mIngredientId = id;
        this.mIngredientName = name;
        this.mMeasurementType = measureType;
        this.mAmount = amount;
    }


    public int getmIngredientId() {
        return mIngredientId;
    }

    public void setmIngredientId(int mIngredientId) {
        this.mIngredientId = mIngredientId;
    }

    public String getmIngredientName() {
        return mIngredientName;
    }

    public void setmIngredientName(String mIngredientName) {
        this.mIngredientName = mIngredientName;
    }

    public String getmMeasurementType() {
        if(mMeasurementType == null){
            return "";
        }else{
            return mMeasurementType;
        }

    }

    public void setmMeasurementType(String mMeasurementType) {
        this.mMeasurementType = mMeasurementType;
    }

    public String getmAmount() {
        return mAmount;
    }

    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
    }

    public static String parseIngredientJSON(String ingredientJSON, List<Ingredient> ingredientList){
        String reason = null;
        if(ingredientJSON != null){
            try{
                JSONArray arr = new JSONArray(ingredientJSON);
                for(int i = 0; i<arr.length(); i++){
                    JSONObject obj = arr.getJSONObject(i);
                    Ingredient ingredient = new Ingredient(obj.getInt(Ingredient.ID),
                            obj.getString(Ingredient.NAME),obj.getString(Ingredient.MEASURE_TYPE),
                            obj.getString(Ingredient.AMOUNT));
                    ingredientList.add(ingredient);
                }
            }catch(JSONException e){
                reason = "Unable to parse ingredients, Reason: " + e.getMessage();
            }
        }
        return reason;
    }

    @Override
    public String toString() {
        String toReturn;
        if(mMeasurementType==null){
            toReturn = mAmount + " "+ mMeasurementType;
        }else{
            toReturn = mAmount +" " + mMeasurementType + " " + mIngredientName;
        }

        return toReturn;
    }
}
