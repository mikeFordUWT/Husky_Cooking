/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking.mainmenu;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import team14.tacoma.uw.edu.husky_cooking.R;
import team14.tacoma.uw.edu.husky_cooking.cookbook.CookBookListFragment;
import team14.tacoma.uw.edu.husky_cooking.menu.MenuListFragment;
import team14.tacoma.uw.edu.husky_cooking.recipe.AddRecipeFragment;
import team14.tacoma.uw.edu.husky_cooking.recipe.RecipeActivity;
import team14.tacoma.uw.edu.husky_cooking.recipe.RecipeListFragment;
import team14.tacoma.uw.edu.husky_cooking.shoppinglist.ShoppingListFragment;


/**
 * This class will be the splash screen for users when they log in.
 * From here they can click a button to take them to Cookbook,
 * all recipes, Shopping List, or create a recipe.
 * @author Mike Ford
 * @author Ian Skyles
 * @version 5/4/2016
 */
public class UserHomeFragment extends Fragment {
    /** URL for facebook user check*/
    private static final String FACEBOOK_CHECK =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/facebook_user_check.php?";

    /**
     * Required empty public constructor
     */
    public UserHomeFragment() {
        // Required empty public constructor
    }


    /**
     * Creates the view that will be shown to the user.
     * Attaches listeners to the buttons defined in the XML.
     * @param inflater instantiate layout XML file into its corresponding View object
     * @param container item to contain other views
     * @param savedInstanceState save state so we can resume later
     * @return The view (user interface)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_user_home, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        String face = sharedPreferences.getString(getString(R.string.LOGIN_METHOD), "");
        if(face.equals("facebook")){
            String url  = buildFaceString(v);
            ((RecipeActivity) getActivity()).faceBookCheck(url);
            FacebookCheck task = new FacebookCheck();
            task.execute(new String[]{url});
        }
        String user = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE).getString(getString(R.string.LOGGED_USER), "");
        //Create button lists
        Button cookbook = (Button)v.findViewById(R.id.cookbook_button);
        Button shoppingList = (Button) v.findViewById(R.id.shopping_list_button);
        Button allRecipes = (Button) v.findViewById(R.id.all_recipe_button);
        Button createRecipes = (Button) v.findViewById(R.id.create_recipe_button);
        Button menus = (Button) v.findViewById(R.id.view_menu_button);


        //set on click listener for cookbook
        cookbook.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment newFragment = new RecipeListFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        //set on click listener for
        shoppingList.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingListFragment newFragment = new ShoppingListFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
//
//                Fragment newFragment = new RecipeListFragment();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//
//                transaction.replace(R.id.fragment_container, newFragment);
//                transaction.addToBackStack(null);
//
//                transaction.commit();
            }
        });
        //set on click listener for cookbook
        cookbook.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CookBookListFragment newFragment = new CookBookListFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        //set on click listener for
        allRecipes.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RecipeListFragment newFragment = new RecipeListFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        //set on click listener for
        createRecipes.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddRecipeFragment addRecipeFragment = new AddRecipeFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, addRecipeFragment)
                        .addToBackStack(null).commit();
            }
        });

        menus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuListFragment menuListFragment = new MenuListFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, menuListFragment)
                        .addToBackStack(null).commit();
            }
        });
        return v;

    }

    /**
     * Return string for checking if a user is already in the database.
     * If they aren't a new entry is inserted into appropriate table.
     *
     */
    private String buildFaceString(View v){
        StringBuilder sb = new StringBuilder(FACEBOOK_CHECK);
        try{
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS)
                    , Context.MODE_PRIVATE);
            String user = sharedPreferences.getString(getString(R.string.LOGGED_USER), "");
            sb.append("email=");
            sb.append(user);
        }catch (Exception e){
            Toast.makeText(v.getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        return sb.toString();
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
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            if(result!=null){
                Log.e("SignInActivity", result.toString());
            }
        }
    }

}
