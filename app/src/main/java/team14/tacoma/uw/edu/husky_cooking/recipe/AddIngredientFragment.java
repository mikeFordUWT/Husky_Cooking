/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking.recipe;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import team14.tacoma.uw.edu.husky_cooking.R;

/**
 * This class controls the user adding ingredients to our
 * database hosted on CSSGATE.
 *
 * @author Mike Ford
 * @author Ian Skyles
 * @version 6/3/2016
 */
public class AddIngredientFragment extends Fragment {

    /** the base url for adding an ingredient */
    private static final String ADD_INGREDIENT_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/ingredient_add.php?";
    /**
     * Edit Texts for allowing entry of the ingredient info: name,amount, measure type.
     */
    private EditText mIngredientName, mIngredientAmount, mIngredientMeasureType;

    /**
     * Empty class constructor
     */
    public AddIngredientFragment() {
        // Required empty public constructor
    }

    /**
     * Creates the view that will be shown to the user.
     * Attaches listeners to the buttons defined in the XML.
     * It sets up edit text fields and a listener for add recipe button.
     * View includes fields for user to input ingredient info
     * and a button to finish or add more.
     * @param inflater instantiate layout XML file into its corresponding View object
     * @param container item to contain other views
     * @param savedInstanceState save state so we can resume later
     * @return The view (user interface)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_ingredient, container, false);
        mIngredientName = (EditText) v.findViewById(R.id.ingredient_name_from_add);
        mIngredientAmount = (EditText) v.findViewById(R.id.ingredient_amount_from_add);
        mIngredientMeasureType = (EditText) v.findViewById(R.id.ingredient_measure_type_from_add);

        Button addMore = (Button) v.findViewById(R.id.ingredient_add_more_button);
        Button finish = (Button) v.findViewById(R.id.ingredient_finish_button);

        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate user input
                if(TextUtils.isEmpty(mIngredientName.getText().toString())){
                    Toast.makeText(v.getContext(), "Please enter an Ingredient name.",
                            Toast.LENGTH_LONG).show();
                    mIngredientName.requestFocus();
                    return;
                }else if(TextUtils.isEmpty(mIngredientAmount.getText().toString())){
                    Toast.makeText(v.getContext(), "Please enter an amount.",
                            Toast.LENGTH_LONG).show();
                    mIngredientAmount.requestFocus();
                    return;
                }

                String url = buildAddIngredientURL(v);
                AddIngredientTask  task = new AddIngredientTask();
                task.execute(new String[]{url});

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new AddIngredientFragment())
                        .addToBackStack(null).commit();

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate user input
                if(TextUtils.isEmpty(mIngredientName.getText().toString())){
                    Toast.makeText(v.getContext(), "Please enter an Ingredient name.",
                            Toast.LENGTH_LONG).show();
                    mIngredientName.requestFocus();
                    return;
                }else if(TextUtils.isEmpty(mIngredientAmount.getText().toString())){
                    Toast.makeText(v.getContext(), "Please enter an amount.",
                            Toast.LENGTH_LONG).show();
                    mIngredientAmount.requestFocus();
                    return;
                }

                String url = buildAddIngredientURL(v);
                AddIngredientTask task = new AddIngredientTask();
                task.execute(new String[]{url});

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new RecipeDetailFragment())
                        .addToBackStack(null).commit();
            }
        });
        return v;
    }

    /**
     * This builds a url for a new ingredient. It includes details on name,
     * amount, measuring type.
     * @param v the view to display error toasts in
     * @return String of the url for logging in
     */
    private String buildAddIngredientURL(View v){
        StringBuilder sb = new StringBuilder(ADD_INGREDIENT_URL);
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        try{
            String name = mIngredientName.getText().toString();
            sb.append("ingredient=");
            sb.append(URLEncoder.encode(name, "UTF-8"));

            String recipe = sharedPreferences.getString(getString(R.string.CURRENT_RECIPE), "");
            sb.append("&recipe=");
            sb.append(URLEncoder.encode(recipe, "UTF-8"));

            String amount = mIngredientAmount.getText().toString();
            sb.append("&amount=");
            sb.append(URLEncoder.encode(amount, "UTF-8"));

            String measure = mIngredientMeasureType.getText().toString();
            sb.append("&measure=");
            sb.append(URLEncoder.encode(measure, "UTF-8"));

        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }

    /**
     * Adds the ingredient to our database asynchronously.
     */
    private class AddIngredientTask extends AsyncTask<String, Void, String> {

        /**
         * calls super on pre execute.
         */
        @Override
        protected void onPreExecute() {super.onPreExecute();}
        /**
         * Adds ingredient to our database.
         * @param urls where to add recipe
         * @return string of response details about status of
         * adding it to db (eg error, success)
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
                    response = "Unable to add ingredient, Reason: "
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
         * Lets user know if ingredient was successful or unsuccessfully added via toast.
         * @param result result string to be be checked
         */
        @Override
        protected void onPostExecute(String result){
            try{
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if(status.equals("success")){
                    Toast.makeText(getActivity().getApplicationContext(), "Ingredient successfully added!",
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
