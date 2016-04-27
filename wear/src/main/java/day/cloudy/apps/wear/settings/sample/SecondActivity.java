package day.cloudy.apps.wear.settings.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import day.cloudy.apps.wear.settings.SettingsItem;
import day.cloudy.apps.wear.settings.SimpleSettingsItem;
import day.cloudy.apps.wear.settings.WearableSettingListView;

public class SecondActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WearableSettingListView settingListView = new WearableSettingListView(this);

        // Set styles in XML or Java
        settingListView.setTextColor(Color.CYAN);
        settingListView.setImageTintColor(Color.CYAN);
        settingListView.setCircleBackgroundColor(Color.MAGENTA);
        settingListView.setCircleBorderColor(Color.CYAN);
        settingListView.setCircleBorderWidth(getResources().getDimensionPixelSize(R.dimen.circle_border_width));

        settingListView.setClickListener(new WearableSettingListView.ClickListener() {
            @Override
            public void onHeaderClicked() {
            }

            @Override
            public void onSimpleItemClicked(SimpleSettingsItem simpleSettingsItem) {
                // Do something with selected item
                Toast.makeText(SecondActivity.this, simpleSettingsItem.title, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        View headerView = settingListView.inflateHeaderView(R.layout.list_header);
        headerView.findViewById(android.R.id.icon).setVisibility(View.GONE);
        ((TextView) headerView.findViewById(android.R.id.title)).setTextColor(Color.CYAN);
        ((TextView) headerView.findViewById(android.R.id.title)).setText("Option title");
        settingListView.setSettingsItems(getSettingsItems());
        setContentView(settingListView);
    }

    @NonNull
    private List<SettingsItem> getSettingsItems() {
        List<SettingsItem> items = new ArrayList<>();
        items.add(new SimpleSettingsItem(R.drawable.ic_action_star, "Item 1"));
        items.add(new SimpleSettingsItem(R.drawable.ic_action_star, "Item 2"));
        items.add(new SimpleSettingsItem(R.drawable.ic_action_star, "Item 3"));
        return items;
    }
}
