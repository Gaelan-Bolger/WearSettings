package day.cloudy.apps.wear.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableListView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class SettingsItemView extends LinearLayout implements WearableListView.OnCenterProximityListener {

    final CircledImageView image;
    final TextView title;
    final TextView summary;

    protected SettingsItemView(Context context) {
        this(context, null);
    }

    protected SettingsItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    protected SettingsItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
        Drawable selectableItemBackground = ta.getDrawable(0);
        ta.recycle();

        if (null != selectableItemBackground)
            setBackground(selectableItemBackground);
        setOrientation(LinearLayout.HORIZONTAL);

        View.inflate(context, R.layout.settings_item, this);
        image = (CircledImageView) findViewById(android.R.id.icon);
        title = (TextView) findViewById(android.R.id.title);
        summary = (TextView) findViewById(android.R.id.summary);
    }

    @Override
    public void onCenterPosition(boolean animate) {
        if (animate) {
            image.animate().alpha(1.0f);
            title.animate().alpha(1.0f);
            summary.animate().alpha(1.0f);
        } else {
            image.setAlpha(1.0f);
            title.setAlpha(1.0f);
            summary.setAlpha(1.0f);
        }
    }

    @Override
    public void onNonCenterPosition(boolean animate) {
        if (animate) {
            image.animate().alpha(0.5f);
            title.animate().alpha(0.5f);
            summary.animate().alpha(0.5f);
        } else {
            image.setAlpha(0.5f);
            title.setAlpha(0.5f);
            summary.setAlpha(0.5f);
        }
    }

    public void setImageResource(@DrawableRes int iconResId) {
        image.setImageResource(iconResId);
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setSummary(String text) {
        summary.setText(text);
        summary.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    public void setTextColor(int color) {
        title.setTextColor(color);
        summary.setTextColor(color);
    }

    public void setCircleBackgroundColor(int color) {
        image.setCircleColor(color);
    }

    public void setCircleBorderColor(int color) {
        image.setCircleBorderColor(color);
    }

    public void setCircleBorderWidth(int width) {
        image.setCircleBorderWidth(width);
    }

    public void setImageTintColor(int color) {
        image.setImageTint(color);
    }

    public void setCircleRadius(int radius) {
        image.setCircleRadius(radius);
    }

    public void setCirclePadding(int padding) {
        image.setPadding(padding, padding, padding, padding);
    }
}
