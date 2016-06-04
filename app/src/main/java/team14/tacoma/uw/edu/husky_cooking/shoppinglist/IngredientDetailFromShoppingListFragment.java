/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking.shoppinglist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import team14.tacoma.uw.edu.husky_cooking.R;
import team14.tacoma.uw.edu.husky_cooking.model.Ingredient;

/**
 * Deals with getting ingredient details from shopping list.
 * Pulls from users shopping list ingredients on our database hosted on CSSGATE.
 *
 * @author Ian Skyles
 * @author Mike Ford
 * @version 6/3/2016
 */
public class IngredientDetailFromShoppingListFragment extends Fragment {

    /**
     * Used to unserilize ingredients. Table name fromm db.
     */
    public static final String INGREDIENT_ITEM_SELECTED = "IngredientItemSelected";

    /**
     * Used to connect with our shopping list database from cookbook.
     * Removing ingredient from shopping list for husky cooking user.
     */
    private static final String REMOVE_FROM_SHOPPING_LIST_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/remove_shopping_list.php?";

    /**
     * Used to connect with our shopping list database from cookbook.
     * Removing ingredient from shopping list for facebook user.
     */
    private static final String FACE_REMOVE_FROM_SHOPPING_LIST_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/remove_face_shopping_list.php?";



    /** TextView for displayng ingredient name*/
    private TextView mIngredientNameTextView;

    /**TextView that displays amount of ingredient*/
    private TextView mAmountTextView;

    /** TextView that displays measurement type of ingredient*/
    private TextView mMeasurementTypeTextView;

    /** Required empty constructor */
    public IngredientDetailFromShoppingListFragment() {
        // Required empty public constructor
    }

    /**
     * Details the behavior for when the fragment is started.
     * This fragment is started when a user views ingredient
     * details from the shopping list.
     * Gets info from ingredient item selected.
     */
    @Override
    public void onStart(){
        super.onStart();
        Bundle args = getArguments();
        if(args != null){
            updateView((Ingredient) args.getSerializable(INGREDIENT_ITEM_SELECTED));
        }
    }

    /**
     * Creates the view from viewing an ingredient item when inside of the shopping list.
     * @param inflater instantiate layout XML file into its corresponding View object
     * @param container item to contain other views
     * @param savedInstanceState save state so we can resume later
     * @return what is to be displayed to user
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_ingredient_in_list_detail, container, false);
        mAmountTextView = (TextView) view.findViewById(R.id.ingredient_amount_in_list);
        mIngredientNameTextView = (TextView) view.findViewById(R.id.ingredient_name_in_list);
        mMeasurementTypeTextView = (TextView) view.findViewById(R.id.ingredient_measurement_type_in_list);

        final Button removeFromShoppingList = (Button) view.findViewById(R.id.delete_from_shopping_list_button);

        removeFromShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingListFragment newFrag = new ShoppingListFragment();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                String face= sharedPreferences.getString(getString(R.string.LOGIN_METHOD), "");
                String url;
                if(face.equals("facebook")){
                    url = buildFaceRemoveUrl(v);
                }else{
                    url = buildRemoveUrl(v);
                }



                RemoveIngredientFromListTask task = new RemoveIngredientFromListTask();
                task.execute(url);
//                ViewGroup layout = (ViewGroup) removeFromShoppingList.getParent();
//                if(null!= layout){
//                    layout.removeView(removeFromShoppingList);
//                }

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFrag).addToBackStack(null);
                transaction.commit();
//                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    /**
     * Adds the relevant ingredient information to the users screen
     * based on ingredient details from db.
     * @param ingredient info to populate view with
     */
    public void updateView(Ingredient ingredient) {
        mAmountTextView.setText(ingredient.getAmount());
        mIngredientNameTextView.setText(ingredient.getIngredientName());
        mMeasurementTypeTextView.setText(ingredient.getMeasurementType());


        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                        Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(getString(R.string.CURRENT_INGREDIENT), ingredient.getIngredientName())
                .apply();
        sharedPreferences.edit().putString(getString(R.string.CURRENT_AMOUNT), ingredient.getAmount())
                .apply();
        sharedPreferences.edit().putString(getString(R.string.CURRENT_MEASURE_TYPE), ingredient.getMeasurementType())
                .apply();

    }

    /**
     * Builds database access URL to remove an ingredient to shopping list.
     * For fb users
     * @param v What the url will be based on (the filled in prompts from view)
     * @return URL to remove an ingredient from shopping list.
     */
    private String buildFaceRemoveUrl(View v){
        StringBuilder sb = new StringBuilder(FACE_REMOVE_FROM_SHOPPING_LIST_URL);
        try{
            SharedPreferences sharedPreferences = getActivity()
                    .getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            String user = sharedPreferences.getString(getString(R.string.LOGGED_USER), "");
            sb.append("user=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

            String ingredient = sharedPreferences.getString(getString(R.string.CURRENT_INGREDIENT), "");
            sb.append("&ingredient=");
            sb.append(URLEncoder.encode(ingredient, "UTF-8"));

        }catch (Exception e){
            Toast.makeText(v.getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }

    /**
     * Builds database access URL to remove an ingredient from shopping list.
     * This one is for hc users
     * @param v What the url will be based on (the filled in prompts from view)
     * @return URL to remove an ingredient from shopping list.
     */
    private String buildRemoveUrl(View v){
        StringBuilder sb = new StringBuilder(REMOVE_FROM_SHOPPING_LIST_URL);
        try{
            SharedPreferences sharedPreferences = getActivity()
                    .getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            String user = sharedPreferences.getString(getString(R.string.LOGGED_USER), "");
            sb.append("user=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

            String ingredient = sharedPreferences.getString(getString(R.string.CURRENT_INGREDIENT), "");
            sb.append("&ingredient=");
            sb.append(URLEncoder.encode(ingredient, "UTF-8"));

        }catch (Exception e){
            Toast.makeText(v.getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }




    /**
     * This class is what will call the database from the background. It uses the url given
     * by the buildAddUrl for fb or husky cooking user.
     * It removes the ingredient from the users shopping list in background.
     */
    private class RemoveIngredientFromListTask extends AsyncTask<String, Void, String> {
        /**
         * Try to visit the urls given, in this case removing from
         * the users shopping list.
         * @param urls which will add ingredient to database.
         * @return details on outcome of successful remove or unsuccessful add to db
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to remove ingredient from shopping list, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }


            }

            return response;
        }

        /**
         * Lets the user know via toast whether or not the ingredient was removed from the
         * shopping list successfully.
         * @param result result string to be be checked
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.contains("success")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Recipe successfully added to your Cookbook!",
                            Toast.LENGTH_LONG)
                            .show();
                } else {
                    String error = jsonObject.get("error").toString();
                    String minusP = error.substring(0,error.length()-1);
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to add: "
                                    + minusP +" in your Cookbook"
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                if(e.getMessage().contains("1")){
                    Toast.makeText(getActivity().getApplicationContext(),
                            mIngredientNameTextView.getText().toString()+
                                    " has been removed from your shopping List!",
                            Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}
