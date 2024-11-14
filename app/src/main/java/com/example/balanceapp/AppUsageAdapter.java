// AppUsageAdapter.java

package com.example.balanceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppUsageAdapter extends ArrayAdapter<AppUsageInfo> {

    public AppUsageAdapter(Context context, List<AppUsageInfo> appUsageList) {
        super(context, 0, appUsageList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppUsageInfo appUsageInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_app_usage, parent, false);
        }

        TextView appNameTextView = convertView.findViewById(R.id.appNameTextView);
        ImageView appIconImageView = convertView.findViewById(R.id.appIconImageView);
        TextView timeSpentTextView = convertView.findViewById(R.id.timeSpentTextView);

        appNameTextView.setText(appUsageInfo.appName);
        appIconImageView.setImageDrawable(appUsageInfo.appIcon);
        timeSpentTextView.setText(appUsageInfo.getFormattedTime());

        return convertView;
    }
}
