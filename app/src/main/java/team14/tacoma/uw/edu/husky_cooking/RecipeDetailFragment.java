/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import team14.tacoma.uw.edu.husky_cooking.model.Recipe;


/**
 * This fragment is used to populate the recipe item text views. Also, it is
 * used to update them.
 * @author Mike Ford
 * @author Ian Skyles
 * @version 5/2/2016
 */
public class RecipeDetailFragment extends Fragment {
    /** Used to update view on start */
    public static final String RECIPE_ITEM_SELECTED = "RecipeItemSelected";

    /** TextView that displays recipe name */
    private TextView mRecipeNameTextView;
    /** TextView that displays recipe servings */
    private TextView mServingsTextView;
    /** TextView that displays recipe cook time */
    private TextView mCookTimeTextView;
    /** TextView that displays recipe directions */
    private TextView mDirectionsTextView;


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
    public void onStart(){
        super.onStart();

        Bundle args = getArguments();
        if(args != null){
            updateView((Recipe) args.getSerializable(RECIPE_ITEM_SELECTED));
        }
    }

    /**
     * Creates the view that will be shown to the user.
     * Attaches listeners to the buttons defined in the XML.
     * Sets the TextViews with appropriate data to display to
     * @param inflater instantiate layout XML file into its corresponding View object
     * @param container item to contain other views
     * @param savedInstanceState save state so we can resume later
     * @return The view (user interface)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        mRecipeNameTextView = (TextView) view.findViewById(R.id.recipe_name);
        mServingsTextView = (TextView) view.findViewById(R.id.recipe_servings);
        mCookTimeTextView = (TextView) view.findViewById(R.id.recipe_cook_time);
        mDirectionsTextView = (TextView) view.findViewById(R.id.recipe_directions);

        return view;

    }

    /**
     * Allows the recipe to update the view.
     * @param recipe recipe to add
     */
    public void updateView(Recipe recipe){
        mRecipeNameTextView.setText(recipe.getName());
        mServingsTextView.setText(recipe.getServings()+ " servings");
        mCookTimeTextView.setText(recipe.getCookTime() + " minutes");
        mDirectionsTextView.setText("\n" + recipe.getDescription());

        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                        Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(getString(R.string.CURRENT_RECIPE), recipe.getName())
                .commit();
    }

}
