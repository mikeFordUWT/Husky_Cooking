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
 * @version 5/4/2016
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
        Button cookbook = (Button)v.findViewById(R.id.cookbook_button);
        Button shoppingList = (Button) v.findViewById(R.id.shopping_list_button);
        Button allRecipes = (Button) v.findViewById(R.id.all_recipe_button);
        Button createRecipes = (Button) v.findViewById(R.id.create_recipe_button);
        Button menus = (Button) v.findViewById(R.id.view_menu_button);


        //set on click listener for cookbook
        cookbook.setOnClickListener( new View.OnClickListener() {
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
        shoppingList.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingListFragment newFragment = new ShoppingListFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
//
//                Fragment newFragment = new RecipeListFragment();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//
//                transaction.replace(R.id.fragment_container, newFragment);
//                transaction.addToBackStack(null);
//
//                transaction.commit();
            }
        });
        //set on click listener for cookbook
        cookbook.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CookBookListFragment newFragment = new CookBookListFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        //set on click listener for
        allRecipes.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RecipeListFragment newFragment = new RecipeListFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        //set on click listener for
        createRecipes.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddRecipeFragment addRecipeFragment = new AddRecipeFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, addRecipeFragment)
                        .addToBackStack(null).commit();
            }
        });

        menus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuListFragment menuListFragment = new MenuListFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, menuListFragment)
                        .addToBackStack(null).commit();
            }
        });
        return v;

    }



}
