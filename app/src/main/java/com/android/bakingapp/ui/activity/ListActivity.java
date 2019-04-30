package com.android.bakingapp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.android.bakingapp.model.objects.Recipe;
import com.android.bakingapp.ui.fragment.ListFragment;

import java.util.ArrayList;

import static com.android.bakingapp.utils.Constants.RECIPE_LIST;

public class ListActivity extends BaseActivity {

    private ArrayList<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            recipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        ListFragment listFragment = (ListFragment) fragmentManager.findFragmentByTag("ListFragment");

        if (listFragment == null) {
            listFragment = new ListFragment();
            fragmentManager.beginTransaction()
                    .add(fragmentContainer.getId(), listFragment, "ListFragment")
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST, recipes);
    }
}
