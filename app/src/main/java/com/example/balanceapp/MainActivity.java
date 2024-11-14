// MainActivity.java

package com.example.balanceapp;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.appUsageListView);
        UsageStatsHelper usageStatsHelper = new UsageStatsHelper(this);

        if (usageStatsHelper.hasUsageStatsPermission()) {
            displayAppUsage(usageStatsHelper);
        } else {
            usageStatsHelper.redirectToUsageSettings();
        }
    }

    private void displayAppUsage(UsageStatsHelper usageStatsHelper) {
        long startOfDay = usageStatsHelper.getStartOfDayInMillis();
        long endOfDay = System.currentTimeMillis();
        Map<String, Long> appUsageTimes = usageStatsHelper.getAppUsageTimes(startOfDay, endOfDay);

        List<AppUsageInfo> appUsageList = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        for (Map.Entry<String, Long> entry : appUsageTimes.entrySet()) {
            String packageName = entry.getKey();
            long timeSpent = entry.getValue();

            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
                String appName = packageManager.getApplicationLabel(appInfo).toString();
                Drawable appIcon = packageManager.getApplicationIcon(appInfo);

                appUsageList.add(new AppUsageInfo(appName, appIcon, timeSpent));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        AppUsageAdapter adapter = new AppUsageAdapter(this, appUsageList);
        listView.setAdapter(adapter);
    }
}
