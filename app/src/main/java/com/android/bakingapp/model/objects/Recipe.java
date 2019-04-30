package com.android.bakingapp.model.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;
import android.util.JsonToken;

import com.android.bakingapp.data.JsonObject;
import com.android.bakingapp.data.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class Recipe extends JsonObject<Recipe> implements Parcelable {

    private Integer id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private Integer servings;
    private String image;

    public Recipe() {
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    private void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    private void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    private void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    private void setImage(String image) {
        this.image = image;
    }

    @Override
    public Recipe getObject(JsonReader jsonReader) throws Exception {
        Recipe recipe = new Recipe();
        if (jsonReader.peek() != JsonToken.NULL) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.peek() != JsonToken.NULL ? jsonReader.nextName() : "";
                switch (name) {
                    case "id":
                        recipe.setId(jsonReader.nextInt());
                        break;
                    case "name":
                        recipe.setName(jsonReader.nextString());
                        break;
                    case "ingredients":
                        recipe.setIngredients(JsonUtils.parseJsonObjectArray(Ingredient.class, jsonReader));
                        break;
                    case "steps":
                        recipe.setSteps(JsonUtils.parseJsonObjectArray(Step.class, jsonReader));
                        break;
                    case "servings":
                        recipe.setServings(jsonReader.nextInt());
                        break;
                    case "image":
                        recipe.setImage(jsonReader.nextString());
                        break;
                    default:
                        jsonReader.skipValue();
                        break;
                }
            }
            jsonReader.endObject();
        } else {
            jsonReader.nextNull();
        }
        return recipe;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        if (in.readByte() == 0x01) {
            ingredients = new ArrayList<>();
            in.readList(ingredients, Ingredient.class.getClassLoader());
        } else {
            ingredients = null;
        }
        if (in.readByte() == 0x01) {
            steps = new ArrayList<>();
            in.readList(steps, Step.class.getClassLoader());
        } else {
            steps = null;
        }
        servings = in.readInt();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        if (ingredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredients);
        }
        if (steps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(steps);
        }
        dest.writeInt(servings);
        dest.writeString(image);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
