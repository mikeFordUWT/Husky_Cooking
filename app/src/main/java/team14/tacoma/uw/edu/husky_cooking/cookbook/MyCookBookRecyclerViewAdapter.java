/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking.cookbook;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import team14.tacoma.uw.edu.husky_cooking.R;
import team14.tacoma.uw.edu.husky_cooking.model.Recipe;

/**
 * Binds data to our app / view. A flexible
 * view for providing a limited window into a large data set.
 * The data it binds is from recipes/cookbook on our DB.
 *
 * @author Mike Ford
 * @author Ian Skyles
 * @version 5/4/2016
 */
public class MyCookBookRecyclerViewAdapter extends RecyclerView.Adapter<MyCookBookRecyclerViewAdapter.ViewHolder> {

    /**
     * A list of recipes (which have values, in cookbook or not).
     */
    private final List<Recipe> mValues;
    /** Listener for the view recycler containing our recipes. */
    private final CookBookListFragment.OnCookFragmentInteractionListener mListener;

    /**
     * Creates a cook book recycler view adapter.
     * @param items items for our recycle view.
     * @param listener listens for interaction with recycle view.
     */
    public MyCookBookRecyclerViewAdapter(List<Recipe> items, CookBookListFragment.OnCookFragmentInteractionListener listener) {
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
     * Display the data at the specified position.
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
                    mListener.onCookBookFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    /**
     * Gets item (recipe) count
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

        /**
         * Returns string containing text from content view.
         * @return String representing recipe recycler view.
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
