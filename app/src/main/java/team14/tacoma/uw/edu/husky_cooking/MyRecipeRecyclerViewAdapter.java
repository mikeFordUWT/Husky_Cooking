/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import team14.tacoma.uw.edu.husky_cooking.model.Recipe;

/**
 * Binds data to our app / view for the view all recipes page
 * .A flexible view for providing a limited window into a
 * large data set.
 * The data it binds is from all recipes on our DB.
 *
 * @author Mike Ford
 * @author Ian Skyles
 * @version 5/4/2016
 */
public class MyRecipeRecyclerViewAdapter extends RecyclerView.Adapter<MyRecipeRecyclerViewAdapter.ViewHolder> {
    /** A list of recipes (which have values, in cookbook or not). */
    private final List<Recipe> mValues;
    /** Listener for the view recycler containing all recipes. */
    private final RecipeListFragment.OnListFragmentInteractionListener mListener;

    /**
     * Creates a recycler view adapter for all of our recipes.
     * @param items items for our recycle view.
     * @param listener listens for interaction with recycle view.
     */
    public MyRecipeRecyclerViewAdapter(List<Recipe> items, RecipeListFragment.OnListFragmentInteractionListener listener) {
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
                .inflate(R.layout.fragment_recipe, parent, false);
        return new ViewHolder(view);
    }
    /**
     * Display the recipe data at the specified position.
     * @param holder where to put our view
     * @param position type of view to
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getName());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }
    /**
     * Gets item (total recipe display) count
     * @return item count
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * An item view and metadata about its place within the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Recipe mItem;

        /**
         * instantiate the view holder with a given view.
         * @param view for RecyclerView
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
