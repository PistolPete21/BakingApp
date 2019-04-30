package com.android.bakingapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bakingapp.R;
import com.android.bakingapp.model.objects.Step;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ItemViewHolder> {

    private final StepAdapter.StepListAdapterOnClickHandler clickHandler;
    private List<Step> steps;

    public interface StepListAdapterOnClickHandler {
        void onClick(Step step);
    }

    public StepAdapter(StepAdapter.StepListAdapterOnClickHandler stepListAdapterOnClickHandler) {
        this.clickHandler = stepListAdapterOnClickHandler;
    }

    @NonNull
    @Override
    public StepAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.adapter_list_step_item, viewGroup, false);
        return new StepAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.getItemTextView().setText(steps.get(position).getShortDescription());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView itemTextView;

        private ItemViewHolder(View view) {
            super(view);
            itemTextView = view.findViewById(R.id.step_label);
            view.setOnClickListener(this);
        }

        TextView getItemTextView() {
            return itemTextView;
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Step step = steps.get(adapterPosition);
            clickHandler.onClick(step);
        }
    }

    @Override
    public int getItemCount() {
        if (steps != null) {
            return steps.size();
        } else {
            return 0;
        }
    }

    public void setSteps(List<Step> steps) {
        if (steps != null) {
            this.steps = steps;
        }
    }
}
