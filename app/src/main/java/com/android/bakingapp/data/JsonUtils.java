package com.android.bakingapp.data;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static  <T extends JsonObject> List<T> parseJsonObjectArray(Class<T> objectClass, JsonReader jsonReader) throws IOException {
        List<T> list = new ArrayList<>();

        if(jsonReader.peek() != JsonToken.NULL) {
            jsonReader.beginArray();
            if (jsonReader.peek() != JsonToken.NULL) {
                while (jsonReader.hasNext()) {
                    try {
                        //noinspection unchecked
                        list.add((T) (objectClass.newInstance()).getObject(jsonReader));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            jsonReader.endArray();
        } else {
            jsonReader.nextNull();
        }

        return list;
    }
}
