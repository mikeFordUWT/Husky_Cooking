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

import org.w3c.dom.Text;

import team14.tacoma.uw.edu.husky_cooking.model.Recipe;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFromMenuFragment extends Fragment {

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

    public RecipeDetailFromMenuFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_recipe_detail_from_menu, container, false);

        mRecipeNameTextView = (TextView) view.findViewById(R.id.recipe_name_from_menu);
        mServingsTextView = (TextView) view.findViewById(R.id.recipe_servings_from_menu);
        mCookTimeTextView = (TextView) view.findViewById(R.id.recipe_cook_time_from_menu);
        mDirectionsTextView = (TextView) view.findViewById(R.id.recipe_directions_from_menu);

        Button addToCook = (Button) view.findViewById(R.id.add_to_cookbook_button_from_menu);

        Button viewIngredients = (Button) view.findViewById(R.id.view_ingredients_from_menu_button);

        Button share = (Button) view.findViewById(R.id.share_recipe_button_from_menu);

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
