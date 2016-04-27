package day.cloudy.apps.wear.settings;


import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

public class BoolPrefSettingsItem extends SettingsItem {

    public final String prefKey;

    public BoolPrefSettingsItem(@DrawableRes int iconResId, String title, @NonNull String prefKey) {
        super(iconResId, title);
        this.prefKey = prefKey;
    }
}
