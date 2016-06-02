package team14.tacoma.uw.edu.husky_cooking.recipe;


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
 * A simple {@link Fragment} subclass.
 */
public class IngredientDetailFromRecipeFragment extends Fragment {
    public static final String INGREDIENT_ITEM_SELECTED = "IngredientItemSelected";

    private static final String ADD_TO_SHOPPING_LIST =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/add_to_shopping_list.php?";

    private static final String FACE_ADD_TO_SHOPPING =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/add_to_face_shopping.php?";

    /** TextView for displayng ingredient name*/
    private TextView mIngredientNameTextView;

    /**TextView that displays amount of ingredient*/
    private TextView mAmountTextView;

    /** TextView that displays measurement type of ingredient*/
    private TextView mMeasurementTypeTextView;

    public IngredientDetailFromRecipeFragment() {
        // Required empty public constructor
    }


    /**Updates view with ingredient item/ Serializable on starting this fragment. */
    @Override
    public void onStart(){
        super.onStart();
        Bundle args = getArguments();
        if(args != null){
            updateView((Ingredient) args.getSerializable(INGREDIENT_ITEM_SELECTED));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ingredient_detail_from_recipe, container, false);
        mAmountTextView = (TextView) view.findViewById(R.id.ingredient_amount_from_recipe);
        mIngredientNameTextView = (TextView) view.findViewById(R.id.ingredient_name_from_recipe);
        mMeasurementTypeTextView = (TextView) view.findViewById(R.id.ingredient_measurement_type_from_recipe);

        final Button addToShoppingList = (Button) view.findViewById(R.id.add_to_shopping_list_button);

        addToShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                AddIngredientToListTask task = new AddIngredientToListTask();

                SharedPreferences sharedPreferences = getActivity()
                        .getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);

                String face = sharedPreferences.getString(getString(R.string.LOGIN_METHOD), "");

                if(face.equals("facebook")){
                    url = buildFaceAddUrl(v);
                }else{
                    url = buildAddUrl(v);
                }
                task.execute(url);

                IngredientsFromRecipeListFragment newFrag = new IngredientsFromRecipeListFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFrag).addToBackStack(null);
                transaction.commit();


            }
        });

        return view;
    }

    /**
     * Allows ingredient to update view.
     *
     * @param ingredient ingredient to add
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
     * Helper method that retuns string if user logged in via custom login.
     * @param v
     * @return
     */
    private String buildFaceAddUrl(View v){
        StringBuilder sb = new StringBuilder(FACE_ADD_TO_SHOPPING);

        try{

            SharedPreferences sharedPreferences = getActivity()
                    .getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            String ingredient = sharedPreferences.getString(getString(R.string.CURRENT_INGREDIENT),"");
            sb.append("ingredient=");
            sb.append(URLEncoder.encode(ingredient, "UTF-8"));

            String user = sharedPreferences.getString(getString(R.string.LOGGED_USER), "");
            sb.append("&user_name=");
            sb.append(URLEncoder.encode(user, "UTF-8"));


            String amount = mAmountTextView.getText().toString();
            sb.append("&amount=");
            sb.append(URLEncoder.encode(amount, "UTF-8"));

            String measure = mMeasurementTypeTextView.getText().toString();
            sb.append("&measurement_type=");
            sb.append(URLEncoder.encode(measure, "UTF-8"));

        } catch (Exception e){
            Toast.makeText(v.getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }



        return sb.toString();
    }

    private String buildAddUrl(View v){
        StringBuilder sb = new StringBuilder(ADD_TO_SHOPPING_LIST);

        try{

            SharedPreferences sharedPreferences = getActivity()
                    .getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            String ingredient = sharedPreferences.getString(getString(R.string.CURRENT_INGREDIENT),"");
            sb.append("ingredient=");
            sb.append(URLEncoder.encode(ingredient, "UTF-8"));

            String user = sharedPreferences.getString(getString(R.string.LOGGED_USER), "");
            sb.append("&user_name=");
            sb.append(URLEncoder.encode(user, "UTF-8"));


            String amount = mAmountTextView.getText().toString();
            sb.append("&amount=");
            sb.append(URLEncoder.encode(amount, "UTF-8"));

            String measure = mMeasurementTypeTextView.getText().toString();
            sb.append("&measurement_type=");
            sb.append(URLEncoder.encode(measure, "UTF-8"));

        } catch (Exception e){
            Toast.makeText(v.getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }



        return sb.toString();
    }

    private class AddIngredientToListTask extends AsyncTask<String, Void, String> {
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
                    response = "Unable to add ingredient to shopping list, Reason: "
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
                if (status.contains("success")) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            mIngredientNameTextView.getText().toString() +
                                    " successfully added to your Shopping List!",
                            Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to add: "
                                    + mIngredientNameTextView.getText().toString() +" already in your shopping list!"
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                if(e.getMessage().contains("1")){
                    Toast.makeText(getActivity().getApplicationContext(), "Item removed from your shopping List!", Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}
