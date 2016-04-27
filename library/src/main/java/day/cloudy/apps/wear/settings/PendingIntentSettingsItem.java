package day.cloudy.apps.wear.settings;


import android.app.PendingIntent;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

public class PendingIntentSettingsItem extends SettingsItem {

    public final PendingIntent intent;

    public PendingIntentSettingsItem(@DrawableRes int iconResId, String title, @NonNull PendingIntent intent) {
        super(iconResId, title);
        this.intent = intent;
    }
}
