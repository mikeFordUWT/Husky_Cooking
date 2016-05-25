package team14.tacoma.uw.edu.husky_cooking;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import team14.tacoma.uw.edu.husky_cooking.model.Recipe;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeInCookBookDetailFragment extends Fragment {

    /**
     * Used to update view on start
     */
    public static final String RECIPE_ITEM_SELECTED = "RecipeItemSelected";

    /**
     * TextView that displays recipe name
     */
    private TextView mRecipeNameTextView;
    /**
     * TextView that displays recipe servings
     */
    private TextView mServingsTextView;
    /**
     * TextView that displays recipe cook time
     */
    private TextView mCookTimeTextView;
    /**
     * TextView that displays recipe directions
     */
    private TextView mDirectionsTextView;
    /**
     * ListView that will display ingredients
     */
    private ListView mIngredientsListView;

    public RecipeInCookBookDetailFragment() {
        // Required empty public constructor
    }


    /**
     * Updates view with recipe item/ Serializable on starting this fragment.
     */
    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            updateView((Recipe) args.getSerializable(RECIPE_ITEM_SELECTED));
        }else {
            SharedPreferences sharedPreferences =
                    getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                            Context.MODE_PRIVATE);

            int id = sharedPreferences.getInt(getString(R.string.CURRENT_RECIPE_ID),0);
            String name = sharedPreferences.getString(getString(R.string.CURRENT_RECIPE), "");
            String descript = sharedPreferences.getString(getString(R.string.CURRENT_DESCRIPTION), "");
            int cookTime = sharedPreferences.getInt(getString(R.string.CURRENT_COOK_TIME), 0);
            int servings = sharedPreferences.getInt(getString(R.string.CURRENT_SERVINGS), 0);
            Recipe r = new Recipe(id, name, descript, cookTime, servings);
            updateView(r);

            Log.d(RECIPE_ITEM_SELECTED, r.toString());
        }
    }

    /**
     * Creates the view that will be shown to the user.
     * Attaches listeners to the buttons defined in the XML.
     * Sets the TextViews with appropriate data to display to
     *
     * @param inflater           instantiate layout XML file into its corresponding View object
     * @param container          item to contain other views
     * @param savedInstanceState save state so we can resume later
     * @return The view (user interface)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_in_cook_book_detail, container, false);

        mRecipeNameTextView = (TextView) view.findViewById(R.id.recipe_name_in_cook);
        mServingsTextView = (TextView) view.findViewById(R.id.recipe_servings_in_cook);
        mCookTimeTextView = (TextView) view.findViewById(R.id.recipe_cook_time_in_cook);
        mDirectionsTextView = (TextView) view.findViewById(R.id.recipe_directions_in_cook);

        Button viewIngredients = (Button) view.findViewById(R.id.view_ingredients_button_in_cook);
        viewIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Ingredien
//                TODO
            }
        });
        return view;
    }

    /**
     * Allows the recipe to update the view.
     *
     * @param recipe recipe to add
     */
    public void updateView(Recipe recipe) {
        mRecipeNameTextView.setText(recipe.getName());
        mServingsTextView.setText(recipe.getServings() + " servings");
        mCookTimeTextView.setText(recipe.getCookTime() + " minutes");
        mDirectionsTextView.setText("\n" + recipe.getDescription());

        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                        Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(getString(R.string.CURRENT_RECIPE_ID),recipe.getRecipeId())
                .commit();
        sharedPreferences.edit().putString(getString(R.string.CURRENT_RECIPE), recipe.getName())
                .commit();
        sharedPreferences.edit().putString(getString(R.string.CURRENT_DESCRIPTION), recipe.getDescription())
                .commit();
        sharedPreferences.edit().putInt(getString(R.string.CURRENT_COOK_TIME), recipe.getCookTime())
                .commit();
        sharedPreferences.edit().putInt(getString(R.string.CURRENT_SERVINGS), recipe.getServings())
                .commit();
    }

}
