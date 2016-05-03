package team14.tacoma.uw.edu.husky_cooking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddRecipeFragment extends Fragment {
    public static final String ADD_RECIPE_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/addRecipe.php?";

    public static final String TAG = "AddRecipeFragment";
    private EditText mRecipeName, mRecipeDescript, mServings, mCookTime;



    public AddRecipeFragment() {
        // Required empty public constructor
    }


    public interface AddRecipeInteractionListener{
        public void addRecipe(String url);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        mRecipeName = (EditText) v.findViewById(R.id.recipe_name);
        mRecipeDescript = (EditText) v.findViewById(R.id.recipe_description);
        mCookTime = (EditText) v.findViewById(R.id.new_recipe_cook_time);
        mServings = (EditText) v.findViewById(R.id.new_recipe_servings);




        Button addRecipeButton = (Button) v.findViewById(R.id.add_recipe_button);

        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String recipeName = mRecipeName.getText().toString();
//                String recipeDescript = mRecipeDescript.getText().toString();
//                int recipeServing = Integer.parseInt(mServings.getText().toString());
//                int recipeCookTime = Integer.parseInt(mCookTime.getText().toString());


                String url = buildRecipeUrl(v);
                ((RecipeActivity) getActivity()).addRecipe(url);
            }
        });

        return v;
    }




    private String buildRecipeUrl(View v){
        StringBuilder sb = new StringBuilder(ADD_RECIPE_URL);

        try {
            String name = mRecipeName.getText().toString();
            sb.append("recipe_name=");
            sb.append(URLEncoder.encode(name, "UTF-8"));

            String description = mRecipeDescript.getText().toString();
            sb.append("&description=");
            sb.append(URLEncoder.encode(description, "UTF-8"));

            String cookTime = mCookTime.getText().toString();
            sb.append("&cook_time=");
            sb.append(URLEncoder.encode(cookTime, "UTF-8"));

            String servings = mServings.getText().toString();
            sb.append("&servings=");
            sb.append(URLEncoder.encode(servings, "UTF-8"));

            Log.i(TAG, sb.toString());
        }catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }




}
