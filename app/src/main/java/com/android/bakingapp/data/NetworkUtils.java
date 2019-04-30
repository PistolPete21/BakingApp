package com.android.bakingapp.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.JsonReader;

import com.android.bakingapp.model.response.BaseResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    BaseResponse getResponseFromHttpUrl(BaseResponse responseClass, URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());

            JsonReader jsonReader = new JsonReader(isr);

            return responseClass.parseJson(jsonReader);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
