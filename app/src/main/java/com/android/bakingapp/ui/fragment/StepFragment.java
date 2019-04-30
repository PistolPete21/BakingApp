package com.android.bakingapp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.bakingapp.R;
import com.android.bakingapp.model.objects.Recipe;
import com.android.bakingapp.model.objects.Step;
import com.android.bakingapp.ui.activity.DetailActivity;
import com.android.bakingapp.ui.adapter.StepAdapter;

import java.util.ArrayList;

import static com.android.bakingapp.utils.Constants.RECIPE;
import static com.android.bakingapp.utils.Constants.STEP;
import static com.android.bakingapp.utils.Constants.STEP_LIST;

public class StepFragment extends Fragment implements StepAdapter.StepListAdapterOnClickHandler {

    private Recipe recipe;
    private StepAdapter stepAdapter;
    private ArrayList<Step> steps =  new ArrayList<>();

    private OnSelectedListener callback;

    public void setOnSelectedListener(OnSelectedListener callback) {
        this.callback = callback;
    }

    public interface OnSelectedListener {
        void onItemSelected(Step step, DetailFragment fragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            recipe = getArguments().getParcelable(RECIPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_steps);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        stepAdapter = new StepAdapter(this);
        stepAdapter.setSteps(recipe.getSteps());
        recyclerView.setAdapter(stepAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(STEP_LIST);
            stepAdapter.setSteps(steps);
        }
    }

    @Override
    public void onClick(Step step) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            callback.onItemSelected(step, null);
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(RECIPE, recipe);
            intent.putExtra(STEP, step);
        } else {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(RECIPE, recipe);
            intent.putExtra(STEP, step);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (steps != null) {
            outState.putParcelableArrayList(STEP_LIST, (ArrayList<? extends Parcelable>) recipe.getSteps());
        }
    }
}
