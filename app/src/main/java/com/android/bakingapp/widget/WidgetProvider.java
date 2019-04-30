package com.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.android.bakingapp.BuildConfig;
import com.android.bakingapp.R;
import com.android.bakingapp.ui.activity.ListActivity;
import com.android.bakingapp.utils.Constants;

public class WidgetProvider extends AppWidgetProvider {

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = updateRecipes(context);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public RemoteViews updateRecipes(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipes_widget_layout);
        SharedPreferences sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        views.setTextViewText(R.id.widget_title, sharedPreferences.getString(Constants.WIDGET_TITLE, ""));
        views.setTextViewText(R.id.widget_ingredients, sharedPreferences.getString(Constants.WIDGET_INGREDIENTS, ""));

        Intent intent = new Intent(context, ListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_title, pendingIntent);
        views.setOnClickPendingIntent(R.id.widget_ingredients, pendingIntent);

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // onDeleted
    }

    @Override
    public void onEnabled(Context context) {
        // onEnabled
    }

    @Override
    public void onDisabled(Context context) {
        // onDisabled
    }
}
