/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import team14.tacoma.uw.edu.husky_cooking.R;
import team14.tacoma.uw.edu.husky_cooking.model.Recipe;

/**
 * Deals with getting recipe list from recipes in menu.
 * Pulls from menu recipe db on our database hosted on CSSGATE.
 *
 * @author Ian Skyles
 * @author Mike Ford
 * @version 6/3/2016
 */
public class RecipeFromMenuListFragment extends Fragment {
    /**
     * The base url for accessing the recipe's ingredient list.
     */
    private static final String MENU_URL = "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/get_menu_recipes.php?menu=";

    /**
     * Number of columns to display recipe list in
     */
    private int mColumnCount = 1;

    /**
     * Listener for interacting with the recipe list inside menu section of app.
     */
    private OnListFragmentInteractionListener mListener;

    /**
     * Allows for continously flowing / recylcing the menu list if
     * it is bigger than the device screen.
     */
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment.
     */
    public RecipeFromMenuListFragment() {
    }

    /**
     * Used super to save and recover information for ingredient list.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Creates the view for the recipes list from recipes in the menu list.
     * Eventually the recycle view will be instantiated on this screen.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return what is to be displayed to user
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_from_menu_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
//            recyclerView.setAdapter(new RecipeFromMenuRecyclerViewAdapter(DummyContent.ITEMS, mListener));
            ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected()){
                SharedPreferences sharedPreferences = getActivity()
                        .getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                String toEncode = sharedPreferences.getString(getString(R.string.CURRENT_MENU),"");
                try{
                    String menu = URLEncoder.encode(toEncode, "UTF-8");
                    String menuURL = MENU_URL+ menu;
                    DownloadRecipesTask task = new DownloadRecipesTask();
                    task.execute(new String[]{menuURL});
                }catch (Exception e){
                    Toast.makeText(view.getContext(), "Something wrong with the url " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }


            }else{
                Toast.makeText(view.getContext(),
                        "No network connection available. Please connect and try again.",
                        Toast.LENGTH_SHORT).show();
            }

        }
        return view;
    }


    /**
     * Attaches menu list fragment interaction listener to mlistener.
     * @param context what to attach
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    /**
     * Set listener to null when detatching,
     * no longer want to listen for clicks on list.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Must have onRecipeFromMenuListFragmentInteraction.
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity (recycle view adapter).
     *
     */
    public interface OnListFragmentInteractionListener {

        void onRecipeFromMenuListFragmentInteraction(Recipe item);
    }


    /**
     * Downloads recipes for specific foodMenu
     * Asynchronously (in the background) from
     * our db/webservice hosted on cssgate.
     */
    private class DownloadRecipesTask extends AsyncTask<String, Void, String> {
        /**
         * Tells it to connect and read http responses for the recipes
         * from the recipes in the menu.
         * @param urls where recipes are stored
         * @return list of recipes
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection =null;
            for(String url: urls) {
                try{
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while((s = buffer.readLine())!=null){
                        response += s;
                    }
                }catch (Exception e) {
                    response = "Unable to download the cookbook, Reason: " + e.getMessage();
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
         * Gives user network toast if can't connect.
         * @param result result string to execute on
         */
        @Override
        protected void onPostExecute(String result) {
            if(result.startsWith("Unable to")){
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            List<Recipe> mRecipeList = new ArrayList<>();
            result = Recipe.parseRecipeJSON(result, mRecipeList);
            //Something wrong with JSON returned
            if(result != null){
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            if(!mRecipeList.isEmpty()){
                mRecyclerView.setAdapter(new RecipeFromMenuRecyclerViewAdapter(mRecipeList, mListener));
            }
        }
    }
}
