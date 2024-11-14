// AppUsageInfo.java

package com.example.balanceapp;

import android.graphics.drawable.Drawable;

public class AppUsageInfo {
    String appName;
    Drawable appIcon;
    long timeSpent;

    public AppUsageInfo(String appName, Drawable appIcon, long timeSpent) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.timeSpent = timeSpent;
    }

    public String getFormattedTime() {
        long minutes = timeSpent / (1000 * 60);
        long hours = minutes / 60;
        minutes = minutes % 60;
        return hours + "h " + minutes + "m";
    }
}
