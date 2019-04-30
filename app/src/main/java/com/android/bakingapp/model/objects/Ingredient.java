package com.android.bakingapp.model.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;
import android.util.JsonToken;

import com.android.bakingapp.data.JsonObject;

public class Ingredient extends JsonObject<Ingredient> implements Parcelable {

    private float quantity;
    private String measure;
    private String ingredient;

    public Ingredient() {
    }

    public float getQuantity() {
        return quantity;
    }

    private void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    private void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient.substring(0 , 1).toUpperCase() + ingredient.substring(1);
    }

    private void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public Ingredient getObject(JsonReader jsonReader) throws Exception {
        Ingredient ingredient = new Ingredient();
        if (jsonReader.peek() != JsonToken.NULL) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.peek() != JsonToken.NULL ? jsonReader.nextName() : "";
                switch (name) {
                    case "quantity":
                        ingredient.setQuantity(Float.parseFloat(jsonReader.nextString()));
                        break;
                    case "measure":
                        ingredient.setMeasure(jsonReader.nextString());
                        break;
                    case "ingredient":
                        ingredient.setIngredient(jsonReader.nextString());
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
        return ingredient;
    }

    private Ingredient(Parcel in) {
        quantity = in.readFloat();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
