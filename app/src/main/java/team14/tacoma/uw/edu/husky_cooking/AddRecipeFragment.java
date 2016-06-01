/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * This class controls the user adding recipes to our.
 * database hosted on CSSGATE.
 *
 * @author Mike Ford
 * @author Ian Skyles
 * @version 5/4/2016
 */
public class AddRecipeFragment extends Fragment {
    /**
     * the base url to add recipes to our database.
     */
    public static final String ADD_RECIPE_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/addRecipe.php?";

    /**
     * A tag for debugging.
     */
    public static final String TAG = "AddRecipeFragment";
    /**
     * Edit Text for allowing entry of the recipe name, recipe description, servings, and cook time.
     */
    private EditText mRecipeName, mRecipeDescript, mServings, mCookTime;

    /**
     * Empty class constructor
     */
    public AddRecipeFragment() {
        // Required empty public constructor
    }


    /**
     * Creates the view that will be shown to the user.
     * Attaches listeners to the buttons defined in the XML.
     * It sets up edit text fields and a listener for add recipe button.
     * @param inflater instantiate layout XML file into its corresponding View object
     * @param container item to contain other views
     * @param savedInstanceState save state so we can resume later
     * @return The view (user interface)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        mRecipeName = (EditText) v.findViewById(R.id.recipe_name);
        mRecipeDescript = (EditText) v.findViewById(R.id.recipe_description);
        mCookTime = (EditText) v.findViewById(R.id.new_recipe_cook_time);
        mServings = (EditText) v.findViewById(R.id.new_recipe_servings);

        //Add button from xml
        Button addIngredientsButton = (Button) v.findViewById(R.id.add_ingredients_button);

        addIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks for valid user input
                if(TextUtils.isEmpty(mRecipeName.getText().toString())){
                    Toast.makeText(v.getContext(), "Please enter a recipe name.",
                            Toast.LENGTH_LONG).show();
                    mRecipeName.requestFocus();
                    return;
                }else if(mRecipeName.getText().toString().length()<2){
                    Toast.makeText(v.getContext(), "Recipe name must be at least 2 characters.",
                            Toast.LENGTH_LONG).show();
                    mRecipeName.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(mRecipeDescript.getText().toString())){
                    Toast.makeText(v.getContext(), "Please enter recipe directions.",
                            Toast.LENGTH_LONG).show();
                    mRecipeDescript.requestFocus();
                    return;
                }else if(mRecipeDescript.getText().toString().length()<=20){
                    Toast.makeText(v.getContext(), "Recipe directions must be longer than 20 " +
                            "characters.",
                            Toast.LENGTH_LONG).show();
                    mRecipeDescript.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(mServings.getText().toString())){
                    Toast.makeText(v.getContext(), "Please enter servings amount.",
                            Toast.LENGTH_LONG).show();
                    mServings.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(mCookTime.getText().toString())){
                    Toast.makeText(v.getContext(), "Please enter a cook time.",
                            Toast.LENGTH_LONG).show();
                    mCookTime.requestFocus();
                    return;
                }
                String url = buildRecipeUrl(v);

                SharedPreferences sharedPreferences =
                        getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                                Context.MODE_PRIVATE);

                sharedPreferences.edit()
                        .putString(getString(R.string.CURRENT_RECIPE),mRecipeName
                                .getText().toString()).apply();

                AddIngredientFragment ingredientFragment = new AddIngredientFragment();
                FragmentActivity act = getActivity();
                AddRecipeTask task = new AddRecipeTask();
                task.execute(new String[]{url});

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AddIngredientFragment())
                        .addToBackStack(null).commit();
//                ((RecipeActivity) getActivity()).addRecipe(url);

            }
        });

        return v;
    }

    /**
     * This builds a url for recipe. It includes details on name,
     * cook time, description, and servings.
     * @param v the view to display error toasts in
     * @return String of the url for logging in
     */
    private String buildRecipeUrl(View v){
        StringBuilder sb = new StringBuilder(ADD_RECIPE_URL);

        try {
            String name = mRecipeName.getText().toString();
            sb.append("recipe_name=");
            sb.append(URLEncoder.encode(name, "UTF-8"));

            String description = mRecipeDescript.getText().toString();
            sb.append("&description=");
            sb.append(URLEncoder.encode(description, "UTF-8"));

            String cookTime = mCookTime.getText().toString();
            sb.append("&cook_time=");
            sb.append(URLEncoder.encode(cookTime, "UTF-8"));

            String servings = mServings.getText().toString();
            sb.append("&servings=");
            sb.append(URLEncoder.encode(servings, "UTF-8"));

            Log.i(TAG, sb.toString());
        }catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }

    /**
     * Adds the recipe to our database asynchronously.
     */
    private class AddRecipeTask extends AsyncTask<String, Void, String> {

        /**
         * calls super on pre execute.
         */
        @Override
        protected void onPreExecute() {super.onPreExecute();}
        /**
         * Adds recipe to our database.
         * @param urls where to add recipe
         * @return string of response details
         */
        @Override
        protected String doInBackground(String... urls){
            String response ="";
            HttpURLConnection urlConnection = null;
            for(String url: urls){
                try{
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                }catch (Exception e) {
                    response = "Unable to add recipe, Reason: "
                            + e.getMessage();
                }finally {
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            }
            return response;
        }

        /**
         * Does appropriate actions to set/replace
         * recycler view and adapter.
         * Lets user know if it was successful or unsuccessfully added.
         * @param result result string to be be checked
         */
        @Override
        protected void onPostExecute(String result){
            try{
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if(status.equals("success")){
                    Toast.makeText(getActivity().getApplicationContext(), "Recipe successfully added!",
                            Toast.LENGTH_LONG)
                            .show();
                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to add: "
                                    + jsonObject.get("error")
                            ,Toast.LENGTH_LONG)
                            .show();
                }
            }catch(JSONException e){
                Toast.makeText(getActivity().getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
