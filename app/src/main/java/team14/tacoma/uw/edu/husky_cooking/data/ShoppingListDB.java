package team14.tacoma.uw.edu.husky_cooking.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import team14.tacoma.uw.edu.husky_cooking.model.Ingredient;

/**
 * Created by Ian Skyles on 6/1/2016.
 */
public class ShoppingListDB {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "ShoppingList.db";

    private ShoppingListDBHelper mShoppingListDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public ShoppingListDB(Context context) {
        mShoppingListDBHelper = new ShoppingListDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mShoppingListDBHelper.getWritableDatabase();
    }


    /**
     * A helper class for our shopping list database.
     * Contains behavior for creating an item and dropping an item.
     */
    class ShoppingListDBHelper extends SQLiteOpenHelper {


        private static final String CREATE_ShoppingItem_SQL =
                "CREATE TABLE IF NOT EXISTS ShoppingItem "
                        + "(id TEXT PRIMARY KEY, amount TEXT, ingredientName TEXT, measurementType TEXT)";

        private static final String DROP_ShoppingItem_SQL =
                "DROP TABLE IF EXISTS ShoppingItem";

        public ShoppingListDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_ShoppingItem_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_ShoppingItem_SQL);
            onCreate(sqLiteDatabase);
        }

    }
    /**
     * Inserts the ingredient into the shoppingList sqlite table. Returns true if successful, false otherwise.
     * @param id
     * @param amount
     * @param ingredientName
     * @param measurementType
     * @return true or false
     */
    public boolean insertIngredient(int id, String amount, String ingredientName, String measurementType) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("amount", amount);
        contentValues.put("ingredientName", ingredientName);
        contentValues.put("measurementType", measurementType);

        long rowId = mSQLiteDatabase.insert("Ingredient", null, contentValues);
        return rowId != -1;
    }

    /**
     * Closes the database.
     */
    public void closeDB() {
        mSQLiteDatabase.close();
    }

    private static final String INGREDIENT_TABLE = "Ingredient";

    /**
     * Returns the list of Ingredient from the local shopping list table.
     * @return list
     */
    public List<Ingredient> getIngredients() {

        String[] columns = {
                "id", "shortDesc", "longDesc", "prereqs"
        };

        Cursor c = mSQLiteDatabase.query(
                INGREDIENT_TABLE,  // The table to query
                columns,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        List<Ingredient> list = new ArrayList<Ingredient>();
        for (int i=0; i<c.getCount(); i++) {
            int id = c.getInt(0);
            String amount = c.getString(1);
            String ingredientName = c.getString(2);
            String measurementType = c.getString(3);
            Ingredient Ingredient = new Ingredient(id, amount, ingredientName, measurementType);
            list.add(Ingredient);
            c.moveToNext();
        }

        return list;
    }
    /**
     * Delete all the data from the shopping list
     */
    public void deleteIngredients() {
        mSQLiteDatabase.delete(INGREDIENT_TABLE, null, null);
    }



}