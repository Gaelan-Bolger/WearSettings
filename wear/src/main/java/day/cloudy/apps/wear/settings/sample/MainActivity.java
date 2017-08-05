package day.cloudy.apps.wear.settings.sample;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.widget.Toast;

import java.util.ArrayList;

import day.cloudy.apps.wear.settings.item.BoolPrefSettingsItem;
import day.cloudy.apps.wear.settings.item.PendingIntentSettingsItem;
import day.cloudy.apps.wear.settings.item.SettingsItem;
import day.cloudy.apps.wear.settings.view.SettingsRecyclerView;
import day.cloudy.apps.wear.settings.item.SimpleSettingsItem;

public class MainActivity extends WearableActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SettingsRecyclerView settingsRecyclerView = findViewById(R.id.settings_recycler_view);

        // Set up the header
        settingsRecyclerView.setHeaderIcon(R.mipmap.ic_launcher);
        settingsRecyclerView.setHeaderText(R.string.app_name);

        // Add any combination of SettingsItems (Simple, Radio, PendingIntent, BoolPref), initial
        // selection, and an optional click handler for SimpleSettingsItems and HeaderView
        settingsRecyclerView.setSettingsItems(getSettingsItems(), 0, new SettingsRecyclerView.OnClickListener() {
            @Override
            public void onHeaderClick() {
                toast("Header clicked");
            }

            @Override
            public void onSimpleItemClick(int position, SimpleSettingsItem item) {
                if (position == 2)
                    startActivity(new Intent(MainActivity.this, RadioActivity.class));
                else
                    toast(item.title);
            }
        });
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
        settingsItems.add(new SimpleSettingsItem(R.drawable.ic_action_star, "Radio Activty"));
        settingsItems.add(new PendingIntentSettingsItem(R.drawable.ic_action_star, "PI Activity",
                PendingIntent.getActivity(this, 0, new Intent(this, PIActivity.class), 0)));
        settingsItems.add(new PendingIntentSettingsItem(R.drawable.ic_action_star, "PI Broadcast",
                PendingIntent.getBroadcast(this, 0, new Intent(PIReceiver.ACTION_MAKE_TOAST), 0)));
        settingsItems.add(new BoolPrefSettingsItem(R.drawable.ic_action_star, "BP item 1", "key_1"));
        settingsItems.add(new BoolPrefSettingsItem(R.drawable.ic_action_star, "BP item 2", "key_2"));
        return settingsItems;
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
