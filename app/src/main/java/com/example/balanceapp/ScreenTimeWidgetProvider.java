package com.example.balanceapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;  // Correct import for AppWidgetManager
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;
import android.widget.Toast;
import android.view.View;  // Correct import for View constants

import java.util.Map;

public class ScreenTimeWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        UsageStatsHelper usageStatsHelper = new UsageStatsHelper(context);

        // Check if permission is granted
        if (!usageStatsHelper.hasUsageStatsPermission()) {
            usageStatsHelper.redirectToUsageSettings();
            return;
        }

        long startOfDay = usageStatsHelper.getStartOfDayInMillis();
        long endOfDay = usageStatsHelper.getEndOfDayInMillis();

        Map<String, Long> appUsageTimes = usageStatsHelper.getAppUsageTimes(startOfDay, endOfDay);

        // Iterate through app usages and set them to the widget
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.screen_time_widget);

            int appIndex = 1;
            for (Map.Entry<String, Long> entry : appUsageTimes.entrySet()) {
                String appPackage = entry.getKey();
                long timeSpent = entry.getValue();

                // Get the app name and icon
                String appName = usageStatsHelper.getAppName(appPackage);
                Drawable appIconDrawable = usageStatsHelper.getAppIcon(appPackage);
                Bitmap appIconBitmap = ((BitmapDrawable) appIconDrawable).getBitmap();

                // If app has usage time, set it in the widget
                if (timeSpent > 0) {
                    String appNameId = "widgetAppName" + appIndex;
                    String appIconId = "widgetAppIcon" + appIndex;
                    String appTimeId = "widgetTimeSpent" + appIndex;

                    // Set app name, icon, and time spent
                    views.setTextViewText(context.getResources().getIdentifier(appNameId, "id", context.getPackageName()), appName);
                    views.setImageViewBitmap(context.getResources().getIdentifier(appIconId, "id", context.getPackageName()), appIconBitmap);
                    views.setTextViewText(context.getResources().getIdentifier(appTimeId, "id", context.getPackageName()), usageStatsHelper.formatUsageTime(timeSpent));

                    // Set app visibility if there is usage time
                    views.setViewVisibility(context.getResources().getIdentifier("widgetApp" + appIndex, "id", context.getPackageName()), View.VISIBLE);

                    appIndex++;
                }
            }

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Code for enabling the widget (if needed)
    }

    @Override
    public void onDisabled(Context context) {
        // Code for disabling the widget (if needed)
    }
}
