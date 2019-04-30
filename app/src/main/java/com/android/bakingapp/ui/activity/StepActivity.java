package com.android.bakingapp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.android.bakingapp.model.objects.Recipe;
import com.android.bakingapp.model.objects.Step;
import com.android.bakingapp.ui.fragment.StepFragment;

import java.util.ArrayList;

import static com.android.bakingapp.utils.Constants.RECIPE;
import static com.android.bakingapp.utils.Constants.STEP_LIST;

public class StepActivity extends BaseActivity {

    private Recipe recipe;
    private ArrayList<Step> steps = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(STEP_LIST);
        }

        if (getIntent() != null) {
            recipe = getIntent().getParcelableExtra(RECIPE);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        StepFragment stepFragment = (StepFragment) fragmentManager.findFragmentByTag("StepFragment");

        if (stepFragment == null) {
            stepFragment = new StepFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable(RECIPE, recipe);
            stepFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .add(fragmentContainer.getId(), stepFragment, "StepFragment")
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEP_LIST, steps);
    }
}
