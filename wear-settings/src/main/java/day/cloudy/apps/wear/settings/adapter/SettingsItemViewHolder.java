package day.cloudy.apps.wear.settings.adapter;

import android.content.res.ColorStateList;
import android.support.annotation.DrawableRes;
import android.support.wear.widget.WearableRecyclerView;
import android.support.wearable.view.CircledImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import day.cloudy.apps.wear.settings.R;

class SettingsItemViewHolder extends WearableRecyclerView.ViewHolder {

    private CircledImageView image;
    private RadioButton radio;
    private TextView title;
    private TextView summary;

    SettingsItemViewHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.setting_icon);
        radio = itemView.findViewById(R.id.setting_radio_button);
        title = itemView.findViewById(R.id.setting_title);
        summary = itemView.findViewById(R.id.setting_summary);
    }

    void setImageResource(@DrawableRes int iconResId) {
        if (iconResId > 0) {
            image.setImageResource(iconResId);
            image.setVisibility(View.VISIBLE);
            radio.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.GONE);
            radio.setVisibility(View.VISIBLE);
        }
    }

    void setChecked(boolean checked) {
        radio.setChecked(checked);
    }

    void setTitle(String text) {
        title.setText(text);
    }

    void setSummary(String text) {
        summary.setText(text);
        summary.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    void setTitleTextColor(int color) {
        title.setTextColor(color);
    }

    void setSummaryTextColor(int color) {
        summary.setTextColor(color);
    }

    void setImageTintColor(int color) {
        image.setImageTint(color);
        radio.setButtonTintList(ColorStateList.valueOf(color));
    }

    void setCircleBackgroundColor(int color) {
        image.setCircleColor(color);
    }

    void setCircleBorderColor(int color) {
        image.setCircleBorderColor(color);
    }
}
