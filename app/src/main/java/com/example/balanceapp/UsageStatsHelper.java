package com.example.balanceapp;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.provider.Settings;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UsageStatsHelper {

    private final Context context;
    private final UsageStatsManager usageStatsManager;

    public UsageStatsHelper(Context context) {
        this.context = context;
        this.usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }

    public boolean hasUsageStatsPermission() {
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    public void redirectToUsageSettings() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        context.startActivity(intent);
    }

    public long getStartOfDayInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long startOfDayInMillis = calendar.getTimeInMillis();

        if (System.currentTimeMillis() < startOfDayInMillis) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            startOfDayInMillis = calendar.getTimeInMillis();
        }

        return startOfDayInMillis;
    }

    public long getEndOfDayInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        return calendar.getTimeInMillis();
    }

    public Map<String, Long> getAppUsageTimes(long startTime, long endTime) {
        Map<String, Long> usageMap = new HashMap<>();
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        if (usageStatsList != null) {
            for (UsageStats usageStats : usageStatsList) {
                long totalTime = usageStats.getTotalTimeInForeground();
                if (totalTime > 0) {
                    usageMap.put(usageStats.getPackageName(), totalTime);
                }
            }
        }

        return usageMap;
    }

    public String formatUsageTime(long timeInMillis) {
        long hours = TimeUnit.MILLISECONDS.toHours(timeInMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis) % 60;
        return hours + " hrs " + minutes + " mins";
    }

    public String getAppName(String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, 0)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return packageName;
        }
    }

    public Drawable getAppIcon(String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getApplicationIcon(packageName);  // This returns a Drawable object
        } catch (PackageManager.NameNotFoundException e) {
            return context.getResources().getDrawable(android.R.drawable.sym_def_app_icon);  // Default icon
        }
    }

}
