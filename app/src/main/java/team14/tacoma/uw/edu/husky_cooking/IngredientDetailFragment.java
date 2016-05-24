package team14.tacoma.uw.edu.husky_cooking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import team14.tacoma.uw.edu.husky_cooking.model.Ingredient;


/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientDetailFragment extends Fragment {

    public static final String INGREDIENT_ITEM_SELECTED = "IngredientItemSelected";





    /** TextView for displayng ingredient name*/
    private TextView mIngredientNameTextView;

    /**TextView that displays amount of ingredient*/
    private TextView mAmountTextView;

    /** TextView that displays measurement type of ingredient*/
    private TextView mMeasurementTypeTextView;

    /** Required empty constructor */
    public IngredientDetailFragment() {
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
        return inflater.inflate(R.layout.fragment_ingredient_detail, container, false);
    }

    /**
     * Allows ingredient to update view.
     *
     * @param ingredient ingredient to add
     */
    public void updateView(Ingredient ingredient) {
        mAmountTextView.setText(ingredient.getAmount());
        mIngredientNameTextView.setText(ingredient.getIngredientName());
        if(!ingredient.getMeasurementType().equals("")) {
            mMeasurementTypeTextView.setText(ingredient.getMeasurementType());
        }


    }

}
