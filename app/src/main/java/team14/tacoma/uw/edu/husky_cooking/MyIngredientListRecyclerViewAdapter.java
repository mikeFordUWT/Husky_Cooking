package team14.tacoma.uw.edu.husky_cooking;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import team14.tacoma.uw.edu.husky_cooking.ItemFragment.OnListFragmentInteractionListener;
import team14.tacoma.uw.edu.husky_cooking.dummy.DummyContent.DummyItem;
import team14.tacoma.uw.edu.husky_cooking.model.Ingredient;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link IngredientListFragment.OnShoppingListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyIngredientListRecyclerViewAdapter extends RecyclerView.Adapter<MyIngredientListRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final IngredientListFragment.OnShoppingListFragmentInteractionListener mListener;

    public MyIngredientListRecyclerViewAdapter(List<Ingredient> items, IngredientListFragment.OnShoppingListFragmentInteractionListener listener) {
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
        holder.mIdView.setText(mValues.get(position).);
        holder.mContentView.setText(mValues.get(position).content);

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

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyItem mItem;

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
