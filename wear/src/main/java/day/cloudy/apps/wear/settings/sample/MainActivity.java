package day.cloudy.apps.wear.settings.sample;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import day.cloudy.apps.wear.settings.BoolPrefSettingsItem;
import day.cloudy.apps.wear.settings.PendingIntentSettingsItem;
import day.cloudy.apps.wear.settings.SettingsItem;
import day.cloudy.apps.wear.settings.SimpleSettingsItem;
import day.cloudy.apps.wear.settings.WearableSettingListView;

public class MainActivity extends WearableActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WearableSettingListView settingListView = (WearableSettingListView) findViewById(R.id.settings_list);

        // Click handler for SimpleSettingsItems and HeaderView
        settingListView.setClickListener(new WearableSettingListView.ClickListener() {
            @Override
            public void onHeaderClicked() {
                toast(getString(R.string.app_name));
            }

            @Override
            public void onSimpleItemClicked(SimpleSettingsItem settingsItem) {
                toast(settingsItem.title);
            }
        });

        // Add a HeaderView to the list
        View headerView = settingListView.inflateHeaderView(R.layout.list_header);
        ImageView headerIcon = (ImageView) headerView.findViewById(android.R.id.icon);
        headerIcon.setImageResource(R.mipmap.ic_launcher);
        TextView headerText = (TextView) headerView.findViewById(android.R.id.title);
        headerText.setText(R.string.app_name);

        // Add any combination of SettingsItems (Simple, PendingIntent, BoolPref)
        settingListView.setSettingsItems(getSettingsItems());
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        toast(key + "=" + sharedPreferences.getBoolean(key, false));
    }

    @NonNull
    private ArrayList<SettingsItem> getSettingsItems() {
        ArrayList<SettingsItem> settingsItems = new ArrayList<>();
        settingsItems.add(new SimpleSettingsItem(R.drawable.ic_action_star, "Simple item 1"));
        settingsItems.add(new SimpleSettingsItem(R.drawable.ic_action_star, "Simple item 2"));
        settingsItems.add(new PendingIntentSettingsItem(R.drawable.ic_action_star, "PI Activity",
                PendingIntent.getActivity(this, 0, new Intent(this, SecondActivity.class), 0)));
        settingsItems.add(new PendingIntentSettingsItem(R.drawable.ic_action_star, "PI Broadcast",
                PendingIntent.getBroadcast(this, 0, new Intent(IntentReceiver.ACTION_MAKE_TOAST), 0)));
        settingsItems.add(new BoolPrefSettingsItem(R.drawable.ic_action_star, "BP item 1", "key_1"));
        settingsItems.add(new BoolPrefSettingsItem(R.drawable.ic_action_star, "BP item 2", "key_2"));
        return settingsItems;
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
