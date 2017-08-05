package day.cloudy.apps.wear.settings.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import day.cloudy.apps.wear.settings.item.SettingsItem;
import day.cloudy.apps.wear.settings.view.SettingsRecyclerView;
import day.cloudy.apps.wear.settings.item.SimpleSettingsItem;

public class PIActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsRecyclerView settingsRecyclerView = new SettingsRecyclerView(this);
        settingsRecyclerView.setBackgroundColor(Color.WHITE);

        // Set up the header
        settingsRecyclerView.setHeaderBackgroundColor(Color.LTGRAY);
        settingsRecyclerView.setHeaderIcon(R.mipmap.ic_launcher);
        settingsRecyclerView.setHeaderText("PI Activity");
        settingsRecyclerView.setHeaderTextColor(Color.BLACK);

        // Set styles in XML or Java
        settingsRecyclerView.setTitleTextColor(Color.BLACK);
        settingsRecyclerView.setSummaryTextColor(Color.DKGRAY);
        settingsRecyclerView.setImageTintColor(Color.DKGRAY);
        settingsRecyclerView.setCircleBackgroundColor(Color.TRANSPARENT);
        settingsRecyclerView.setCircleBorderColor(Color.DKGRAY);

        // Add any combination of SettingsItems (Simple, Radio, PendingIntent, BoolPref), initial
        // selection, and an optional click handler for SimpleSettingsItems and HeaderView
        settingsRecyclerView.setSettingsItems(getSettingsItems(), 0, new SettingsRecyclerView.OnClickListener() {
            @Override
            public void onHeaderClick() {
                // header clicked
            }

            @Override
            public void onSimpleItemClick(int position, SimpleSettingsItem item) {
                // Do something with selected item
                Toast.makeText(PIActivity.this, item.title, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        setContentView(settingsRecyclerView);
    }

    @NonNull
    private List<SettingsItem> getSettingsItems() {
        List<SettingsItem> settingsItems = new ArrayList<>();
        settingsItems.add(new SimpleSettingsItem(R.drawable.ic_action_star, "Item 1"));
        settingsItems.add(new SimpleSettingsItem(R.drawable.ic_action_star, "Item 2"));
        settingsItems.add(new SimpleSettingsItem(R.drawable.ic_action_star, "Item 3"));
        return settingsItems;
    }
}
