/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking;

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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import team14.tacoma.uw.edu.husky_cooking.model.Recipe;

/**
 * This fragment/class will be used to represent a list of recipes
 * held in a users cookbook on our database hosted on CSSGATE.
 *
 * @author Mike Ford
 * @author Ian Skyles
 * @version 5/4/2016
 */
public class CookBookListFragment extends Fragment {
    private static final String COOKBOOK_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/cookbook.php?user=";

    /** how many columns to make the list */
    private int mColumnCount = 1;

    /** Listener for cookbook */
    private OnCookFragmentInteractionListener mListener;
    /** List of recipes in cookbook.*/
    private List<Recipe> mRecipeList;

    /** A recyclerView to view our cookbook */
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CookBookListFragment() {
    }

    /**
     * Saves instance on creation of method of fragment/app.
     * @param savedInstanceState state of the saved instance
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        View view = inflater.inflate(R.layout.fragment_item_cook_book_list, container, false);
        TextView header = (TextView) view.findViewById(R.id.cook_book_header);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
//            mRecyclerView.setAdapter(new MyCookBookRecyclerViewAdapter(mRecipeList.ITEMS, mListener));
            ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if(networkInfo !=null && networkInfo.isConnected()){
                SharedPreferences sharedPreferences = getActivity()
                        .getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                String user = sharedPreferences.getString(getString(R.string.LOGGED_USER), "");
                String cookURL = COOKBOOK_URL + user;
                DownloadCookbookTask task = new DownloadCookbookTask();
                task.execute(new String[]{cookURL});
            }else{
                Toast.makeText(view.getContext(),
                        "No network connection available. Cannot display courses",
                        Toast.LENGTH_SHORT).show();
            }
        }
        return view;
    }

    /**
     * Attaches cookbook list fragment interaction listener to mlistener.
     * @param context what to attach
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCookFragmentInteractionListener) {
            mListener = (OnCookFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCookBookIngredientListFragmentInteractionListener");
        }
    }
    /**
     * makes cookbook listener (removes listener)
     * null if onDetach() is called.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * An interface which requires implementing
     * onCookBookFragmentInteraction(Recipe).
     * It is used to ensure the cookbook listener
     * is in place.
     */
    public interface OnCookFragmentInteractionListener {
        void onCookBookFragmentInteraction(Recipe recipe);
    }

    /**
     * Downloads recipes for user cookbook
     * Asynchronously (in the background) from
     * our db/webservice hosted on cssgate.
     */
    private class DownloadCookbookTask extends AsyncTask<String, Void, String>{
        /**
         * Tells it to connect and read http responses for the cookbook.
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
         * @param result result string to execute on
         */
        @Override
        protected void onPostExecute(String result) {
            if(result.startsWith("Unable to")){
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            List<Recipe> mRecipeList = new ArrayList<Recipe>();
            result = Recipe.parseRecipeJSON(result, mRecipeList);
            //Something wrong with JSON returned
            if(result != null){
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            if(!mRecipeList.isEmpty()){
                mRecyclerView.setAdapter(new MyCookBookRecyclerViewAdapter(mRecipeList, mListener));
            }
        }
    }
}
