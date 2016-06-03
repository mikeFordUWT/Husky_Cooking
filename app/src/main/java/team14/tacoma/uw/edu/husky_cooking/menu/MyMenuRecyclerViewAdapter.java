/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking.menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import team14.tacoma.uw.edu.husky_cooking.R;
import team14.tacoma.uw.edu.husky_cooking.model.FoodMenu;

import java.util.List;

/**
 * Binds data to our app / view. A flexible
 * view for providing a limited window into all of our food menus.
 * The data it binds is from menu db.
 *
 * @author Ian Skyles
 * @author Mike Ford
 * @version 6/3/2016
 */
public class MyMenuRecyclerViewAdapter extends RecyclerView.Adapter<MyMenuRecyclerViewAdapter.ViewHolder> {

    /**
     * A list of menus.
     */
    private final List<FoodMenu> mValues;
    /** Listener for the view recycler containing our menus. */
    private final MenuListFragment.OnListFragmentInteractionListener mListener;

    /**
     * Creates a cook book recycler view adapter.
     * @param items menu items to display in our recycler view (list)
     * @param listener listens for interaction with recycle view (menu list)
     */
    public MyMenuRecyclerViewAdapter(List<FoodMenu> items, MenuListFragment.OnListFragmentInteractionListener listener) {
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
                .inflate(R.layout.fragment_menu, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Display the data at the specified position:
     * @param holder where to put our view
     * @param position type of view to
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getMenuName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onMenuFragmentInteraction(holder.mItem);
                }
            }
        });
    }


    /**
     * Gets number of menus.
     * @return menu count
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }


    /**
     * An item view and metadata about its place within the RecyclerView.
     * It defines what info to display on each section and the holders for them.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The view which we will create the holder based off of.
         */
        public final View mView;
        /**
         * id number
         */
        public final TextView mIdView;
        /**
         * content
         */
        public final TextView mContentView;
        /**
         * Food menu item
         */
        public FoodMenu mItem;

        /**
         * Creates a view holder based on our view.
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
