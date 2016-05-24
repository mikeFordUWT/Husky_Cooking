package team14.tacoma.uw.edu.husky_cooking;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import team14.tacoma.uw.edu.husky_cooking.model.Ingredient;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Ingredient} and makes a call to the
 * specified {@link ShoppingListFragment.OnShoppingListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyShoppingListRecyclerViewAdapter extends RecyclerView.Adapter<MyShoppingListRecyclerViewAdapter.ViewHolder> {

    private final List<Ingredient> mValues;
    private final ShoppingListFragment.OnShoppingListFragmentInteractionListener mListener;


    /**
     * Creates a shopping list recycler view adapter.
     * @param items items for our recycle view.
     * @param listener listens for interaction with recycle view.
     */
    public MyShoppingListRecyclerViewAdapter(List<Ingredient> items, ShoppingListFragment.OnShoppingListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * Creates a list of ingredients.
     * @param parent where to put our view
     * @param viewType type of view to
     * @return a view holder for recycle view
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient, parent, false);
        return new ViewHolder(view);
    }

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
                    mListener.onShopListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Ingredient mItem;

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
