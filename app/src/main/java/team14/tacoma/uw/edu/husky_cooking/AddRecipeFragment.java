/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
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


/**
 * This class controls the user adding recipes to our.
 * database hosted on CSSGATE.
 *
 * @author Mike Ford
 * @author Ian Skyles
 * @version 5/2/2016
 */
public class AddRecipeFragment extends Fragment {
    /**
     * the base url to add recipes to our database.
     */
    public static final String ADD_RECIPE_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/addRecipe.php?";

    /**
     * A tag for debugging.
     */
    public static final String TAG = "AddRecipeFragment";
    /**
     * Edit Text for allowing entry of the recipe name, recipe description, servings, and cook time.
     */
    private EditText mRecipeName, mRecipeDescript, mServings, mCookTime;



    public AddRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * An interface which requires implementing addRecipe(String).
     * It is used for a listener.
     */
    public interface AddRecipeInteractionListener{
        public void addRecipe(String url);
    }

    /**
     * Creates the view that will be shown to the user.
     * Attaches listeners to the buttons defined in the XML.
     * It sets up edit text fields and a listener for add recipe button.
     * @param inflater instantiate layout XML file into its corresponding View object
     * @param container item to contain other views
     * @param savedInstanceState save state so we can resume later
     * @return The view (user interface)
     */
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
                String recipeName = mRecipeName.getText().toString();
                String recipeDescript = mRecipeDescript.getText().toString();
                int recipeServing = Integer.parseInt(mServings.getText().toString());
                int recipeCookTime = Integer.parseInt(mCookTime.getText().toString());
            }
        });

        return v;
    }



    /**
     * This builds a url for recipe. It includes details on name,
     * cook time, description, and servings.
     * @param v the view to display error toasts in
     * @return String of the url for logging in
     */
    private String buildRecipeUrl(View v){
        StringBuilder sb = new StringBuilder(ADD_RECIPE_URL);

        try {
            String name = mRecipeName.getText().toString();
            sb.append("recipe_name=");
            sb.append(name);

            String description = mRecipeDescript.getText().toString();
            sb.append("&description=");
            sb.append(description);

            int cookTime = Integer.parseInt(mCookTime.getText().toString());
            sb.append("&cook_time=");
            sb.append(cookTime);

            int servings = Integer.parseInt(mServings.getText().toString());
            sb.append("&servings=");
            sb.append(servings);

            Log.i(TAG, sb.toString());
        }catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }




}
