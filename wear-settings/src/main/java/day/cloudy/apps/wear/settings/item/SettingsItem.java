package day.cloudy.apps.wear.settings.item;

import android.support.annotation.DrawableRes;

public abstract class SettingsItem {

    @DrawableRes
    public final int iconResId;
    public final String title;

    public SettingsItem(@DrawableRes int iconResId, String title) {
        this.iconResId = iconResId;
        this.title = title;
    }

}
