package com.android.bakingapp.ui.fragment;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.bakingapp.BuildConfig;
import com.android.bakingapp.R;
import com.android.bakingapp.data.FetchRecipes;
import com.android.bakingapp.data.NetworkUtils;
import com.android.bakingapp.databinding.FragmentMainListBinding;
import com.android.bakingapp.model.objects.Ingredient;
import com.android.bakingapp.model.objects.Recipe;
import com.android.bakingapp.ui.activity.DetailActivity;
import com.android.bakingapp.ui.activity.StepActivity;
import com.android.bakingapp.ui.adapter.ListAdapter;
import com.android.bakingapp.utils.Constants;
import com.android.bakingapp.widget.WidgetProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.android.bakingapp.utils.Constants.RECIPE;
import static com.android.bakingapp.utils.Constants.RECIPE_LIST;

public class ListFragment extends Fragment implements ListAdapter.ListAdapterOnClickHandler, FetchRecipes.DataListener {

    private ListAdapter listAdapter;
    private FragmentMainListBinding binding;
    private ArrayList<Recipe> recipes = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_list, container, false);

        View view = binding.getRoot();

        if (getResources().getBoolean(R.bool.isTablet)) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
            binding.recyclerviewRecipes.setLayoutManager(gridLayoutManager);
            listAdapter = new ListAdapter(this);
            binding.recyclerviewRecipes.setAdapter(listAdapter);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            binding.recyclerviewRecipes.setLayoutManager(linearLayoutManager);
            listAdapter = new ListAdapter(this);
            binding.recyclerviewRecipes.setAdapter(listAdapter);
        }
        loadRecipes();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            recipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
            listAdapter.setData(recipes);
        }
    }

    private void loadRecipes() {
        if (new NetworkUtils().isOnline(getActivity())) {
            binding.loadingIndicator.setVisibility(View.VISIBLE);
            showRecyclerView();
            new FetchRecipes(this).execute();
        } else {
            showConnectToNetworkMessage();
        }
    }

    @Override
    public void setData(List<Recipe> recipes) {
        binding.loadingIndicator.setVisibility(View.GONE);
        this.recipes = (ArrayList<Recipe>) recipes;
        listAdapter.setData(recipes);
    }

    @Override
    public void onClick(Recipe recipe) {
        SharedPreferences sharedPreferences;
        if (getResources().getBoolean(R.bool.isTablet)) {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(RECIPE, recipe);
            if (getActivity() != null) {
                sharedPreferences = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
                boolean recipeInWidget = Objects.requireNonNull(sharedPreferences.getString(Constants.WIDGET_TITLE, "")).equalsIgnoreCase(recipe.getName());

                if (recipeInWidget) {
                    sharedPreferences.edit()
                            .remove(Constants.WIDGET_TITLE)
                            .remove(Constants.WIDGET_INGREDIENTS)
                            .apply();
                } else {
                    sharedPreferences = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
                    sharedPreferences
                            .edit()
                            .putString(Constants.WIDGET_TITLE, recipe.getName())
                            .putString(Constants.WIDGET_INGREDIENTS, getIngredients(recipe))
                            .apply();
                }

                ComponentName provider = new ComponentName(getActivity(), WidgetProvider.class);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
                int[] ids = appWidgetManager.getAppWidgetIds(provider);
                WidgetProvider widgetProvider = new WidgetProvider();
                widgetProvider.onUpdate(getActivity(), appWidgetManager, ids);
            }
            startActivityForResult(intent, 1);
        } else {
            Intent intent = new Intent(getContext(), StepActivity.class);
            intent.putExtra(RECIPE, recipe);
            if (getActivity() != null) {
                sharedPreferences = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
                boolean recipeInWidget = Objects.requireNonNull(sharedPreferences.getString(Constants.WIDGET_TITLE, "")).equalsIgnoreCase(recipe.getName());
                if (recipeInWidget) {
                    sharedPreferences.edit()
                            .remove(Constants.WIDGET_TITLE)
                            .remove(Constants.WIDGET_INGREDIENTS)
                            .apply();
                } else {
                    sharedPreferences = getActivity().getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
                    sharedPreferences
                            .edit()
                            .putString(Constants.WIDGET_TITLE, recipe.getName())
                            .putString(Constants.WIDGET_INGREDIENTS, getIngredients(recipe))
                            .apply();
                }

                ComponentName provider = new ComponentName(getActivity(), WidgetProvider.class);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
                int[] ids = appWidgetManager.getAppWidgetIds(provider);
                WidgetProvider widgetProvider = new WidgetProvider();
                widgetProvider.onUpdate(getActivity(), appWidgetManager, ids);
            }
            startActivityForResult(intent, 1);
        }
    }

    private void showRecyclerView() {
        /* First, make sure the error is invisible */
        binding.errorMessage.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
        binding.recyclerviewRecipes.setVisibility(View.VISIBLE);
    }

    private void showConnectToNetworkMessage() {
        /* First, hide the currently visible data */
        binding.recyclerviewRecipes.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        binding.errorMessage.setText(R.string.network_message);
        binding.errorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recipes != null) {
            outState.putParcelableArrayList(RECIPE_LIST, recipes);
        }
    }

    private String getIngredients(Recipe recipe) {
        StringBuilder result = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            result.append(ingredient.getIngredient()).append("\n");
        }
        return result.toString();
    }
}
