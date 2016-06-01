/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import team14.tacoma.uw.edu.husky_cooking.authenticate.SignInActivity;
import team14.tacoma.uw.edu.husky_cooking.model.FoodMenu;
import team14.tacoma.uw.edu.husky_cooking.model.Ingredient;
import team14.tacoma.uw.edu.husky_cooking.model.Recipe;

/**
 * This activity controls adding a recipe.
 *
 * @author Mike Ford
 * @author Ian Skyles
 * @version 5/4/2016
 */
public class RecipeActivity extends AppCompatActivity
        implements RecipeListFragment.OnListFragmentInteractionListener,
        CookBookListFragment.OnCookFragmentInteractionListener,
        ShoppingListFragment.OnShoppingListFragmentInteractionListener,
        IngredientsFromRecipeListFragment.OnRecipeIngredientListFragmentInteractionListener,
        IngredientsFromCookBookListFragment.OnCookBookIngredientListFragmentInteractionListener,
        MenuListFragment.OnListFragmentInteractionListener,
        RecipeFromMenuListFragment.OnListFragmentInteractionListener,
        IngredientsFromMenuListFragment.OnListFragmentInteractionListener{

    /** base url to add a recipe to our database */
    public static final String ADD_RECIPE_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/addRecipe.php?";

    public static final String FACE_ADD_TO_COOK_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/facebook_cookbook_add.php?";

    /**
     * Saves instance on creation of method of fragment/app.
     * @param savedInstanceState the instance that will be saved in it's current state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //hide the default mail icon button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab!=null){
            fab.hide();
        }

        if(savedInstanceState == null
                || getSupportFragmentManager().findFragmentById(R.id.user_home) == null){
            UserHomeFragment userHomeFragment = new UserHomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, userHomeFragment)
                    .commit();
        }
    }

    /**
     * Controls what happens when interacting with adding recipe.
     * Changes fragment smoothly.
     * @param item the Recipe object to be used for the RecipeDetailFragment
     */
    @Override
    public void onListFragmentInteraction(Recipe item){
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(RecipeDetailFragment.RECIPE_ITEM_SELECTED, item);
        recipeDetailFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, recipeDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onMenuFragmentInteraction(FoodMenu item) {

        RecipeFromMenuListFragment recipeListFragment = new RecipeFromMenuListFragment();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        sharedPreferences.edit().putString(getString(R.string.CURRENT_MENU),
                item.getMenuName()).apply();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, recipeListFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRecipeFromMenuListFragmentInteraction(Recipe item) {
        RecipeDetailFromMenuFragment newFrag = new RecipeDetailFromMenuFragment();

        Bundle args = new Bundle();
        args.putSerializable(RecipeDetailFromMenuFragment.RECIPE_ITEM_SELECTED, item);
        newFrag.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, newFrag)
                .addToBackStack(null)
                .commit();
    }


    /**
     * Controls what happens when interacting with adding recipe
     * to cookbook.
     * Changes fragment smoothly.
     * @param recipe the Recipe to be used for the RecipeDetailFragment
     */
    @Override
    public void onCookBookFragmentInteraction(Recipe recipe){
        RecipeInCookBookDetailFragment recipeDetailFragment = new RecipeInCookBookDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(RecipeInCookBookDetailFragment.RECIPE_ITEM_SELECTED, recipe);
        recipeDetailFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, recipeDetailFragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onIngredientListFragmentInteraction(Ingredient ingredient){
        IngredientDetailFromRecipeFragment ingredientFrom = new IngredientDetailFromRecipeFragment();
        Bundle args = new Bundle();
        args.putSerializable(IngredientsFromRecipeListFragment.INGREDIENT_ITEM_SELECTED, ingredient);
        ingredientFrom.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ingredientFrom)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onIngredientFromMenuListFragmentInteraction(Ingredient item) {
        IngredientDetailFromMenuFragment ingredientFrom = new IngredientDetailFromMenuFragment();
        Bundle args = new Bundle();
        args.putSerializable(IngredientsFromMenuListFragment.INGREDIENT_ITEM_SELECTED, item);
        ingredientFrom.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ingredientFrom)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onIngredientCookBookListFragmentInteraction(Ingredient ingredient){
        IngredientDetailFromCookBookFragment ingredientFrom = new IngredientDetailFromCookBookFragment();
        Bundle args = new Bundle();
        args.putSerializable(IngredientsFromCookBookListFragment.INGREDIENT_ITEM_SELECTED, ingredient);
        ingredientFrom.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ingredientFrom)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onShopListFragmentInteraction(Ingredient ingredient){
        IngredientDetailFromShoppingListFragment ingredientDetailFromShoppingListFragment = new IngredientDetailFromShoppingListFragment();
        Bundle args = new Bundle();
        args.putSerializable(IngredientDetailFromShoppingListFragment.INGREDIENT_ITEM_SELECTED, ingredient);
        ingredientDetailFromShoppingListFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ingredientDetailFromShoppingListFragment)
                .commit();
    }

    /**
     * Updates (menu view) to show options bar..
     * @param menu menu to be inflated
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here.
     * @param item from action bar
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout){
            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.LOGIN_PREFS),
                            Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                    .apply();
            sharedPreferences.edit().putString(getString(R.string.LOGGED_USER), "")
                    .apply();
            sharedPreferences.edit().putString(getString(R.string.CURRENT_RECIPE), "")
                    .apply();

            FacebookSdk.sdkInitialize(getApplicationContext());
            facebookLogout();
            Intent i  = new Intent(this, SignInActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        if(id == R.id.action_home){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new UserHomeFragment())
                    .addToBackStack(null).commit();
        }

        if(id == R.id.action_cookbook){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CookBookListFragment())
                    .addToBackStack(null).commit();
        }

        if(id == R.id.action_shopping_list){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ShoppingListFragment())
                    .addToBackStack(null).commit();
        }



        return super.onOptionsItemSelected(item);
    }

    public void faceBookCheck(String url){
        FacebookCheck task = new FacebookCheck();
        task.execute(new String[]{url.toString()});

    }
    private void facebookLogout(){
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }
    /**
     * Handles the actions of the prebuilt back button overrides behavior.
     * Allows user to access correct fragments from a specific fragment.
     */
    @Override
    public void onBackPressed(){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(f instanceof IngredientDetailFromShoppingListFragment){
            Log.d("IngredientInList", "NOO!!!");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ShoppingListFragment())
                    .addToBackStack(null).commit();
        }else if(f instanceof ShoppingListFragment){
            Log.d("ShoppingList", "YAY!!");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new UserHomeFragment())
                    .addToBackStack(null).commit();
        }else if(f instanceof UserHomeFragment){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        } else if (f instanceof IngredientsFromRecipeListFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RecipeDetailFragment())
                    .addToBackStack(null).commit();

        }else if(f instanceof RecipeDetailFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RecipeListFragment())
                    .addToBackStack(null).commit();
        }else if(f instanceof RecipeListFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new UserHomeFragment())
                    .addToBackStack(null).commit();
        }else if(f instanceof RecipeInCookBookDetailFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CookBookListFragment())
                    .addToBackStack(null).commit();
        }else if(f instanceof CookBookListFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new UserHomeFragment())
                    .addToBackStack(null).commit();
        } else if(f instanceof IngredientsFromCookBookListFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RecipeInCookBookDetailFragment())
                    .addToBackStack(null).commit();
        }else if(f instanceof IngredientDetailFromMenuFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new IngredientsFromMenuListFragment())
                    .addToBackStack(null).commit();
        }else if(f instanceof IngredientsFromMenuListFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RecipeDetailFromMenuFragment())
                    .addToBackStack(null).commit();
        }else if(f instanceof RecipeDetailFromMenuFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RecipeFromMenuListFragment())
                    .addToBackStack(null).commit();
        }else if(f instanceof RecipeFromMenuListFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MenuListFragment())
                    .addToBackStack(null).commit();
        }else if(f instanceof MenuListFragment){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new UserHomeFragment())
                    .addToBackStack(null).commit();
        }
        else{
            super.onBackPressed();
        }
    }

    private class FacebookCheck extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        /**
         * Finds out if the user is in the database
         * @param urls A url to run in the background
         * @return repsonse string
         */
        @Override
        protected String doInBackground(String... urls){
            String response = "";
            HttpURLConnection urlConnection = null;
            for(String url:urls){
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s ="";
                    while((s=buffer.readLine())!=null){
                        response +=s;
                    }
                }catch (Exception e){
                    response = "Unable to login, Reason: " + e.getMessage();
                }finally{
                    if(urlConnection !=null){
                        urlConnection.disconnect();
                    }
                }
            }
            return response;
        }

        /**
         * Checks the String returned from doInBackground to see if the log in was successful.
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {

            if(result.startsWith("Unable to")){
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            if(result!=null){
                Log.e("SignInActivity", result.toString());
            }
        }
    }

}
