package com.android.bakingapp.model.response;

import android.util.JsonReader;

import com.android.bakingapp.data.JsonUtils;
import com.android.bakingapp.model.objects.Recipe;

import java.util.List;

public class BaseResponse {

    private List<Recipe> recipes = null;

    public List<Recipe> getRecipes() {
        return recipes;
    }

    private void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public BaseResponse parseJson(JsonReader jsonReader) {
        BaseResponse response = new BaseResponse();

        try {
            jsonReader.setLenient(true);
            response.setRecipes(JsonUtils.parseJsonObjectArray(Recipe.class, jsonReader));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return response;
    }
}
