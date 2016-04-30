package team14.tacoma.uw.edu.husky_cooking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import team14.tacoma.uw.edu.husky_cooking.model.Recipe;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment {
    private TextView mRecipeNameTextView;
    private TextView mServingsTextView;
    private TextView mCookTimeTextView;
    private TextView mDirectionsTextView;

    public static final String RECIPE_ITEM_SELECTED = "RecipeItemSelected";

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();

        Bundle args = getArguments();
        if(args != null){
            updateView((Recipe) args.getSerializable(RECIPE_ITEM_SELECTED));
        }
    }

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

    public void updateView(Recipe recipe){
        mRecipeNameTextView.setText(recipe.getName());
        mServingsTextView.setText(recipe.getServings()+ " servings");
        mCookTimeTextView.setText(recipe.getCookTime() + " minutes");
        mDirectionsTextView.setText("\n" + recipe.getDescription());
    }

}
