package com.android.bakingapp.data;

import android.os.AsyncTask;

import com.android.bakingapp.model.objects.Recipe;
import com.android.bakingapp.model.response.BaseResponse;
import com.android.bakingapp.utils.Constants;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchRecipes extends AsyncTask<Void, Void, List<Recipe>> {

    private List<Recipe> recipes = new ArrayList<>();
    private final DataListener dataListener;

    public interface DataListener {
        void setData(List<Recipe> recipes);
    }

    public FetchRecipes(DataListener dataListener) {
        this.dataListener = dataListener;
    }

    @Override
    protected List<Recipe> doInBackground(Void... params) {
        return getRecipes();
    }

    @Override
    protected void onPostExecute(List<Recipe> results) {
        if (results != null) {
            recipes = new ArrayList<>(results);
        }
        if (dataListener != null) {
            dataListener.setData(recipes);
        }
    }

    private List<Recipe> getRecipes() {

        NetworkUtils networkUtils = new NetworkUtils();

        try {
            BaseResponse response = networkUtils.getResponseFromHttpUrl(new BaseResponse(), new URL(Constants.BASE_URL));

            if (response != null) {
                recipes = response.getRecipes();
            }

            return recipes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
