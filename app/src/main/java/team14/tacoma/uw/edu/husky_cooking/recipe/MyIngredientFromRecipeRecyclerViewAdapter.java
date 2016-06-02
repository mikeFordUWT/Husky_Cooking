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
 * {@link RecyclerView.Adapter} that can display a {@link Ingredient} and makes a call to the
 * specified {@link IngredientsFromRecipeListFragment.OnRecipeIngredientListFragmentInteractionListener}.
 *
 */
public class MyIngredientFromRecipeRecyclerViewAdapter extends RecyclerView.Adapter<MyIngredientFromRecipeRecyclerViewAdapter.ViewHolder> {

    private final List<Ingredient> mValues;
    private final IngredientsFromRecipeListFragment.OnRecipeIngredientListFragmentInteractionListener mListener;

    public MyIngredientFromRecipeRecyclerViewAdapter(List<Ingredient> items, IngredientsFromRecipeListFragment.OnRecipeIngredientListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient_from_recipe, parent, false);
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
                    mListener.onIngredientListFragmentInteraction(holder.mItem);
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
