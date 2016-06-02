/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking.recipe;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
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
import team14.tacoma.uw.edu.husky_cooking.model.Recipe;
import team14.tacoma.uw.edu.husky_cooking.recipe.IngredientsFromRecipeListFragment;


/**
 * This fragment is used to populate the recipe item text views. Also, it is
 * used to update them.
 *
 * @author Mike Ford
 * @author Ian Skyles
 * @version 5/4/2016
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * Used to update view on start
     */
    public static final String RECIPE_ITEM_SELECTED = "RecipeItemSelected";

    private static final String ADD_TO_COOK_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/cookbook_add.php?";

    private static final String FACE_ADD_TO_COOK_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/facebook_cookbook_add.php?";
    private String mParent;

    /**
     * TextView that displays recipe name
     */
    private TextView mRecipeNameTextView;
    /**
     * TextView that displays recipe servings
     */
    private TextView mServingsTextView;
    /**
     * TextView that displays recipe cook time
     */
    private TextView mCookTimeTextView;
    /**
     * TextView that displays recipe directions
     */
    private TextView mDirectionsTextView;
    /**
     * ListView that will display ingredients
     */
    private ListView mIngredientsListView;

    /**
     * Required empty constructor.
     */
    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Updates view with recipe item/ Serializable on starting this fragment.
     */
    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        mParent = "";

        if (args != null) {
            Recipe current = (Recipe) args.getSerializable(RECIPE_ITEM_SELECTED);
            updateView(current);
        }else{
            SharedPreferences sharedPreferences =
                    getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                            Context.MODE_PRIVATE);

            int id = sharedPreferences.getInt(getString(R.string.CURRENT_RECIPE_ID),0);
            String name = sharedPreferences.getString(getString(R.string.CURRENT_RECIPE), "");
            String descript = sharedPreferences.getString(getString(R.string.CURRENT_DESCRIPTION), "");
            int cookTime = sharedPreferences.getInt(getString(R.string.CURRENT_COOK_TIME), 0);
            int servings = sharedPreferences.getInt(getString(R.string.CURRENT_SERVINGS), 0);
            Recipe r = new Recipe(id, name, descript, cookTime, servings);
            updateView(r);

            Log.d(RECIPE_ITEM_SELECTED, r.toString());
        }
    }

    /**
     * Creates the view that will be shown to the user.
     * Attaches listeners to the buttons defined in the XML.
     * Sets the TextViews with appropriate data to display to
     *
     * @param inflater           instantiate layout XML file into its corresponding View object
     * @param container          item to contain other views
     * @param savedInstanceState save state so we can resume later
     * @return The view (user interface)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        final SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                        Context.MODE_PRIVATE);
        mRecipeNameTextView = (TextView) view.findViewById(R.id.recipe_name);
        mServingsTextView = (TextView) view.findViewById(R.id.recipe_servings);
        mCookTimeTextView = (TextView) view.findViewById(R.id.recipe_cook_time);
        mDirectionsTextView = (TextView) view.findViewById(R.id.recipe_directions);
//        mIngredientsListView = (ListView) view.findViewById(R.id.ingredients_detail_list_view);

        Button addToCookBook = (Button) view.findViewById(R.id.add_to_cookbook_button);

        addToCookBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                String face = sharedPreferences.getString(getString(R.string.LOGIN_METHOD), "");
                if(face.equals("facebook")){
                    url = buildFaceAddUrl(v);
                }else{
                    url = buildAddToUrl(v);
                }

                AddToCookTask task = new AddToCookTask();
                task.execute(url);
            }
        });

        Button viewIngredients = (Button) view.findViewById(R.id.view_ingredients_button);
        viewIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IngredientsFromRecipeListFragment ingredients = new IngredientsFromRecipeListFragment();


                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, ingredients)
                        .addToBackStack(null).commit();
            }
        });


        Button shareRecipe = (Button) view.findViewById(R.id.share_recipe_button);
        shareRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences =
                        getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                                Context.MODE_PRIVATE);
                int id = sharedPreferences.getInt(getString(R.string.CURRENT_RECIPE_ID),0);
                String name = sharedPreferences.getString(getString(R.string.CURRENT_RECIPE), "");
                String descript = sharedPreferences.getString(getString(R.string.CURRENT_DESCRIPTION), "");
                int cookTime = sharedPreferences.getInt(getString(R.string.CURRENT_COOK_TIME), 0);
                int servings = sharedPreferences.getInt(getString(R.string.CURRENT_SERVINGS), 0);
                Recipe r = new Recipe(id, name, descript, cookTime, servings);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, r.toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        return view;

    }

    /**
     * Allows the recipe to update the view.
     *
     * @param recipe recipe to add
     */
    public void updateView(Recipe recipe) {
        mRecipeNameTextView.setText(recipe.getName());
        mServingsTextView.setText(recipe.getServings() + " servings");
        mCookTimeTextView.setText(recipe.getCookTime() + " minutes");
        mDirectionsTextView.setText("\n" + recipe.getDescription());

        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                        Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(getString(R.string.CURRENT_RECIPE_ID),recipe.getRecipeId())
                .apply();
        sharedPreferences.edit().putString(getString(R.string.CURRENT_RECIPE), recipe.getName())
                .apply();
        sharedPreferences.edit().putString(getString(R.string.CURRENT_DESCRIPTION), recipe.getDescription())
                .apply();
        sharedPreferences.edit().putInt(getString(R.string.CURRENT_COOK_TIME), recipe.getCookTime())
                .apply();
        sharedPreferences.edit().putInt(getString(R.string.CURRENT_SERVINGS), recipe.getServings())
                .apply();


    }

    private String buildFaceAddUrl(View v){
        StringBuilder sb = new StringBuilder(FACE_ADD_TO_COOK_URL);
        try{
            SharedPreferences sharedPreferences = getActivity()
                    .getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            String user = sharedPreferences.getString(getString(R.string.LOGGED_USER), "");

            sb.append("user_name=");
            sb.append(URLEncoder.encode(user, "UTF-8"));


            String recipe = sharedPreferences.getString(getString(R.string.CURRENT_RECIPE), "");
            sb.append("&recipe_name=");
            sb.append(URLEncoder.encode(recipe, "UTF-8"));

        }catch(Exception e){
            Toast.makeText(v.getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }
    private String buildAddToUrl(View v){
        StringBuilder sb = new StringBuilder(ADD_TO_COOK_URL);
        try{
            SharedPreferences sharedPreferences = getActivity()
                    .getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

            String user = sharedPreferences.getString(getString(R.string.LOGGED_USER), "");

            sb.append("user_name=");
            sb.append(URLEncoder.encode(user, "UTF-8"));


            String recipe = sharedPreferences.getString(getString(R.string.CURRENT_RECIPE), "");
            sb.append("&recipe_name=");
            sb.append(URLEncoder.encode(recipe, "UTF-8"));

        }catch(Exception e){
            Toast.makeText(v.getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }

    /**
     * A class for adding a recipe to the User's Cookbook.
     */
    private class AddToCookTask extends AsyncTask<String, Void, String> {
        /**
         * Tells it to connect and read http responses for the cookbook.
         * @param urls where to download recipe details
         * @return string of recipe details
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
                    response = "Unable to add recipe to Cookbook, Reason: "
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
         * Does appropriate actions to set/replace
         * recycler view and adapter.
         * @param result result string to be be checked
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
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
                Toast.makeText(getActivity().getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * A class for retrieving ingredients for recipe.
     */
    private class GetIngredients extends AsyncTask<String, Void, String> {
        /**
         * Tells it to connect and read http responses for the cookbook.
         * @param urls where to download recipe details
         * @return string of recipe details
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
                    response = "Unable to add recipe to Cookbook, Reason: "
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
         * Does appropriate actions to set/replace
         * recycler view and adapter.
         * @param result result string to be be checked
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {

                } else {
                    String error = jsonObject.get("error").toString();
                    String minusP = error.substring(0,error.length()-1);
                    Toast.makeText(getActivity().getApplicationContext(), "Could not retrieve ingredients: "
                                    + minusP +" in your Cookbook"
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
