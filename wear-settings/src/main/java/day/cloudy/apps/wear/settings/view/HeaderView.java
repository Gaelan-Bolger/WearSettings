package day.cloudy.apps.wear.settings.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import day.cloudy.apps.wear.settings.R;

public class HeaderView extends FrameLayout {

    private final ImageView mIcon;
    private final TextView mTitle;

    public HeaderView(@NonNull Context context) {
        this(context, null);
    }

    public HeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.header_view, this);
        mIcon = findViewById(android.R.id.icon);
        mTitle = findViewById(android.R.id.title);
    }

    public boolean isIconVisible() {
        return mIcon.getVisibility() == View.VISIBLE;
    }

    public void setIconVisible(boolean visible) {
        mIcon.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setIconResource(@DrawableRes int iconResId) {
        mIcon.setImageResource(iconResId);
    }

    public void setTitleText(@StringRes int titleResId) {
        setTitleText(getContext().getString(titleResId));
    }

    public void setTitleText(String title) {
        mTitle.setText(title);
    }

    public void setTitleTextColor(int color) {
        mTitle.setTextColor(color);
    }
}
