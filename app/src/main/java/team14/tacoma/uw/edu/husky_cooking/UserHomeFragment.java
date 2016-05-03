/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * This class will be the splash screen for users when they log in.
 * From here they can click a button to take them to Cookbook,
 * all recipes, Shopping List, or create a recipe.
 * @author Mike Ford
 * @author Ian Skyles
 * @version 5/2/2016
 */
public class UserHomeFragment extends Fragment {


    /**
     * Required empty public constructor
     */
    public UserHomeFragment() {
        // Required empty public constructor
    }


    /**
     * Creates the view that will be shown to the user.
     * Attaches listeners to the buttons defined in the XML.
     * @param inflater instantiate layout XML file into its corresponding View object
     * @param container item to contain other views
     * @param savedInstanceState save state so we can resume later
     * @return The view (user interface)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_user_home, container, false);

        //Create button lists
        Button Cookbook = (Button)v.findViewById(R.id.cookbook_button);
        Button ShoppingList = (Button) v.findViewById(R.id.shopping_list_button);
        Button AllRecipes = (Button) v.findViewById(R.id.all_recipe_button);
        Button CreateRecipes = (Button) v.findViewById(R.id.create_recipe_button);


        //TODO UPDATE WHERE BUTTONS TAKE USER AND WHAT FRAGMENTS THEY START
        //set on click listener for cookbook
        Cookbook.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment newFragment = new RecipeListFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        //set on click listener for
        ShoppingList.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment newFragment = new RecipeListFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        //set on click listener for cookbook
        Cookbook.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment newFragment = new CookBookListFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        //set on click listener for
        AllRecipes.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment newFragment = new RecipeListFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        //set on click listener for
        CreateRecipes.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddRecipeFragment addRecipeFragment = new AddRecipeFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, addRecipeFragment)
                        .addToBackStack(null).commit();
            }
        });
        return v;

    }



}
