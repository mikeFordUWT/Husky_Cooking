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

import team14.tacoma.uw.edu.husky_cooking.model.Recipe;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnCookFragmentInteractionListener}
 * interface.
 */
public class CookBookListFragment extends Fragment {
    private static final String COOKBOOK_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/cookbook.php?user=";

    private int mColumnCount = 1;

    private OnCookFragmentInteractionListener mListener;
    private List<Recipe> mRecipeList;

    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CookBookListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_cook_book_list, container, false);

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
                //todo use download cookbook task to populate list
            }else{
                Toast.makeText(view.getContext(),
                        "No network connection available. Cannot display courses",
                        Toast.LENGTH_SHORT).show();
            }
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCookFragmentInteractionListener) {
            mListener = (OnCookFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
    public interface OnCookFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCookBookFragmentInteraction(Recipe recipe);
    }

    private class DownloadCookbookTask extends AsyncTask<String, Void, String>{
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
         * @param result
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

                //todo convert code from webservices lab at line 222 for on phone  database
            }
        }


    }
}
