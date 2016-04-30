package team14.tacoma.uw.edu.husky_cooking.model;

/**
 * Created by Mike on 4/30/16.
 */
public class Ingredient {
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
