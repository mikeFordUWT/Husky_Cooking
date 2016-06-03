/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking.recipe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import team14.tacoma.uw.edu.husky_cooking.R;
import team14.tacoma.uw.edu.husky_cooking.model.Ingredient;

import java.util.List;

/**
 * This class helps us display a list of ingredients to the user.
 * Binds data to our app / view. A flexible
 * view for providing a limited window into a large data set.
 * The data it binds is from a recipes ingredients on our DB.
 *
 * @author Ian Skyles
 * @author Mike Ford
 * @version 6/3/2016
 */
public class MyIngredientFromRecipeRecyclerViewAdapter extends RecyclerView.Adapter<MyIngredientFromRecipeRecyclerViewAdapter.ViewHolder> {
    /**
     * The list of ignredients to be displayed.
     */
    private final List<Ingredient> mValues;
    /**
     * Listener for the view recycler containing our ingrdients.
     */
    private final IngredientsFromRecipeListFragment.OnRecipeIngredientListFragmentInteractionListener mListener;

    /**
     * Creates a ingredient recycler view adapter.
     * @param items items for our recycle view.
     * @param listener listens for interaction with recycle view.
     */
    public MyIngredientFromRecipeRecyclerViewAdapter(List<Ingredient> items, IngredientsFromRecipeListFragment.OnRecipeIngredientListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * Creates a view holder on creation.
     * @param parent where to put our view
     * @param viewType type of view to
     * @return a view holder for recycle view
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient_from_recipe, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Display the ingredients at the specified position (in a single column in this case).
     * @param holder where to put our view
     * @param position type of view to
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getIngredientName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onIngredientListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    /**
     * Gets ingredient count
     * @return ingredient count
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * An item view and metadata about its place within the RecyclerView.
     * Will hold our view and ingredients.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Ingredient mItem;
        /**
         * instantiate the view we have built throughout this class.
         * @param view for RecyclerView
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
        /**
         * Returns string containing text from content view, in this case:
         * a list of ingredients.
         * @return String representing recipe recycler view.
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
