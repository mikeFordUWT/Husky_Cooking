/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking.shoppinglist;

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
import java.util.ArrayList;
import java.util.List;

import team14.tacoma.uw.edu.husky_cooking.R;
import team14.tacoma.uw.edu.husky_cooking.model.Ingredient;
import team14.tacoma.uw.edu.husky_cooking.recipe.RecipeActivity;

/**
 * This fragment/class will be used to represent a list of recipes
 * held in a users cookbook on our database hosted on CSSGATE.
 *
 * @author Mike Ford
 * @author Ian Skyles
 * @version 6/3/2016
 */
public class ShoppingListFragment extends Fragment {
    /**
     * A url for husky cooking users shopping list. Used to connect to our db.
     */
    private static final String SHOPPING_LIST_URL=
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/shopping_list.php?user=";

    /**
     * A url for facebook users shopping list. Used to connect to our db.
     */
    private static final String FACE_SHOPPING_LIST_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/facebook_shopping_list.php?user=";

    /** how many columns to make the list */
    private int mColumnCount = 1;

    /**Listener for shopping list*/
    private OnShoppingListFragmentInteractionListener mListener;


    /** List of ingredients in shopping list*/
    private List<Ingredient> mIngredientList;

    /** A recyclerView to view our shopping list fragment */
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShoppingListFragment() {
    }


    /**
     * Saves instance on creation of method of fragment/app.
     * @param savedInstanceState state of the saved instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RecipeActivity)getActivity()).setActionBarTitle(getString(R.string.shopping_list_title));

    }

    /**
     * Creates the view that will be shown to the user.
     * Attaches listeners to the buttons defined in the XML.
     * It sets up recycler view and displays toast if no network connection.
     * @param inflater instantiate layout XML file into its corresponding View object
     * @param container item to contain other views
     * @param savedInstanceState save state so we can resume later
     * @return The view (user interface)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
//            recyclerView.setAdapter(new MyShoppingListRecyclerViewAdapter(mIngredientList.ITEMS, mListener));
            ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()){
                SharedPreferences sharedPreferences = getActivity()
                        .getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                String user = sharedPreferences.getString(getString(R.string.LOGGED_USER), "");
                String face = sharedPreferences.getString(getString(R.string.LOGIN_METHOD),"");

                String shoppingListURL;

                if(face.equals("facebook")){
                    shoppingListURL = FACE_SHOPPING_LIST_URL + user;
                }else{
                    shoppingListURL = SHOPPING_LIST_URL + user;
                }
                DownloadShoppingListTask task = new DownloadShoppingListTask();
                task.execute(new String[]{shoppingListURL});
            }else{
                Toast.makeText(view.getContext(),
                        "No network connection available.  Please connect and try again.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        return view;
    }



    /**
     * Attaches shopping list fragment interaction listener to mlistener.
     * @param context what to attach
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShoppingListFragmentInteractionListener) {
            mListener = (OnShoppingListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCookBookIngredientListFragmentInteractionListener");
        }
    }

    /**
     * makes shopping list listener (removes listener)
     * null if onDetach() is called.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * An interface which requires implementing
     * onShopListFragmentInteraction(Ingredient).
     * It is used to ensure the cookbook listener
     * is in place.
     */
    public interface OnShoppingListFragmentInteractionListener {
        void onShopListFragmentInteraction(Ingredient item);
    }

    /**
     * Downloads ingredients for users shopping list
     * Asynchronously (in the background) from
     * our db/webservice hosted on cssgate.
     */
    private class DownloadShoppingListTask extends AsyncTask<String, Void, String>{
        /**
         * Tells it to connect and read http responses for the shopping list:
         * (Ingredients).
         * @param urls where user shopping list is stored
         * @return list of ingredients
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
                    response = "Unable to download the shopping list, Reason: " + e.getMessage();
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
         * @param result result string to execute on
         */
        @Override
        protected void onPostExecute(String result) {
            if(result.startsWith("Unable to")){
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            List<Ingredient> mIngredientList = new ArrayList<Ingredient>();
            result = Ingredient.parseIngredientJSON(result, mIngredientList);
            //Something wrong with JSON returned
            if(result != null){
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            if(!mIngredientList.isEmpty()){
                mRecyclerView.setAdapter(new MyShoppingListRecyclerViewAdapter(mIngredientList, mListener));
            }
        }
    }
}
