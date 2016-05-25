package team14.tacoma.uw.edu.husky_cooking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientDetailFromRecipeFragment extends Fragment {
    public static final String INGREDIENT_ITEM_SELECTED = "IngredientItemSelected";

    /** TextView for displayng ingredient name*/
    private TextView mIngredientNameTextView;

    /**TextView that displays amount of ingredient*/
    private TextView mAmountTextView;

    /** TextView that displays measurement type of ingredient*/
    private TextView mMeasurementTypeTextView;

    public IngredientDetailFromRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ingredient_detail_from_recipe, container, false);
    }

}
