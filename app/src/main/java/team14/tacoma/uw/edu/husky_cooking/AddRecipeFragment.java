package team14.tacoma.uw.edu.husky_cooking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddRecipeFragment extends Fragment {
    private static final ArrayList<Integer> COOK_TIMES
            = new ArrayList<Integer>(Arrays.asList(5,10,15,20,25,30,35,40, 45,50,55,60,65,70,75,80
                ,85,90,95,100,105,110,115,120));
    private static final ArrayList<Integer> SERVINGS
            = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));

    private EditText mRecipeName, mRecipeDescript, mCookingTime;
    private Spinner mCookTimeSpin, mServingsSpin;



    public AddRecipeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        mRecipeName = (EditText) v.findViewById(R.id.recipe_name);
        mRecipeDescript = (EditText) v.findViewById(R.id.recipe_description);
        mCookTimeSpin = (Spinner) v.findViewById(R.id.cook_time_spinner);
        mServingsSpin = (Spinner) v.findViewById(R.id.servings_spinner);

        Button addRecipeButton = (Button) v.findViewById(R.id.add_recipe_button);

        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipeName = mRecipeName.getText().toString();
                String recipeDescript = mRecipeDescript.getText().toString();
            }
        });
    }

    private void populateCooktimeSpinner(){

    }

    private void populateServingSpinner(){

    }


}
