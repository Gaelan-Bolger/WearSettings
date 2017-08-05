package day.cloudy.apps.wear.settings.sample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import day.cloudy.apps.wear.settings.item.RadioSettingsItem;
import day.cloudy.apps.wear.settings.item.SettingsItem;
import day.cloudy.apps.wear.settings.view.SettingsRecyclerView;
import day.cloudy.apps.wear.settings.item.SimpleSettingsItem;

public class RadioActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RadioActivity.this);

        SettingsRecyclerView settingsRecyclerView = new SettingsRecyclerView(this);

        // Set up the header
        settingsRecyclerView.setHeaderIcon(R.mipmap.ic_launcher);
        settingsRecyclerView.setHeaderText("Radio Activity");

        // Add any combination of SettingsItems (Simple, Radio, PendingIntent, BoolPref), initial
        // selection, and an optional click handler for SimpleSettingsItems and HeaderView
        settingsRecyclerView.setSettingsItems(getSettingsItems(), prefs.getInt("radio_sel", 0), new SettingsRecyclerView.OnClickListener() {
            @Override
            public void onHeaderClick() {
                // header clicked
            }

            @Override
            public void onSimpleItemClick(int position, SimpleSettingsItem item) {
                prefs.edit().putInt("radio_sel", position).apply();
                Toast.makeText(RadioActivity.this, "Selection saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        setContentView(settingsRecyclerView);
    }

    @NonNull
    private List<SettingsItem> getSettingsItems() {
        List<SettingsItem> settingsItems = new ArrayList<>();
        settingsItems.add(new RadioSettingsItem("Radio Item 1"));
        settingsItems.add(new RadioSettingsItem("Radio Item 2"));
        settingsItems.add(new RadioSettingsItem("Radio Item 3"));
        return settingsItems;
    }
}
