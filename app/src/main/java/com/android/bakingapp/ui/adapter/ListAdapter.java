package com.android.bakingapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bakingapp.R;
import com.android.bakingapp.model.objects.Recipe;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder> {
    private final ListAdapterOnClickHandler clickHandler;
    private List<Recipe> recipes;

    public interface ListAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public ListAdapter(ListAdapterOnClickHandler listAdapterOnClickHandler) {
        this.clickHandler = listAdapterOnClickHandler;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.adapter_list_recipe_item, viewGroup, false);
        return new ListAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.getRecipeTextView().setText(recipes.get(position).getName());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView recipeTextView;

        private ItemViewHolder(View view) {
            super(view);
            recipeTextView = view.findViewById(R.id.recipe_text_view);
            view.setOnClickListener(this);
        }

        TextView getRecipeTextView() {
            return recipeTextView;
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = recipes.get(adapterPosition);
            clickHandler.onClick(recipe);
        }
    }

    @Override
    public int getItemCount() {
        if (null == recipes) return 0;
        return recipes.size();
    }

    public void setData(List<Recipe> results) {
        recipes = results;
        notifyDataSetChanged();
    }
}
