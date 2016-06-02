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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import team14.tacoma.uw.edu.husky_cooking.model.Ingredient;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnShoppingListFragmentInteractionListener}
 * interface.
 */
public class ShoppingListFragment extends Fragment {
    private static final String SHOPPING_LIST_URL=
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/shopping_list.php?user=";

    private static final String FACE_SHOPPING_LIST_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/facebook_shopping_list.php?user=";
    /** how many columns to make the list */
    private int mColumnCount = 1;

    /**Listener for shopping list*/
    private OnShoppingListFragmentInteractionListener mListener;


    /** List of ingredients*/
    private List<Ingredient> mIngredientList;

    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShoppingListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnShoppingListFragmentInteractionListener {
        void onShopListFragmentInteraction(Ingredient item);
    }

    /**
     * Downloads ingredients for user cookbook
     * Asynchronously (in the background) from
     * our db/webservice hosted on cssgate.
     */
    private class DownloadShoppingListTask extends AsyncTask<String, Void, String>{
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
