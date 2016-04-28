package day.cloudy.apps.wear.settings;

import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import java.util.List;

@SuppressWarnings("unused")
public class WearableSettingListView extends BoxInsetLayout implements WearableListView.ClickListener, WearableListView.OnScrollListener {

    public interface ClickListener {
        void onHeaderClicked();

        void onSimpleItemClicked(SimpleSettingsItem simpleSettingsItem);
    }

    private static final String TAG = WearableSettingListView.class.getSimpleName();

    private final SettingsAdapter mSettingsAdapter;
    private final FrameLayout mHeaderContainer;
    private final WearableListView mListView;
    private WearableListView.OnScrollListener mOnScrollListener;
    private ClickListener mClickListener;

    public WearableSettingListView(Context context) {
        this(context, null);
    }

    public WearableSettingListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableSettingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mSettingsAdapter = new SettingsAdapter(context);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WearableSettingsListView);
        setTextColor(ta.getColor(R.styleable.WearableSettingsListView_android_textColor, Color.WHITE));
        setImageTintColor(ta.getColor(R.styleable.WearableSettingsListView_image_tint, Color.WHITE));
        setCircleBackgroundColor(ta.getColor(R.styleable.WearableSettingsListView_circle_color, Color.TRANSPARENT));
        setCircleBorderColor(ta.getColor(R.styleable.WearableSettingsListView_circle_border_color, Color.WHITE));
        setCircleBorderWidth(ta.getDimensionPixelSize(R.styleable.WearableSettingsListView_circle_border_width,
                getResources().getDimensionPixelSize(R.dimen.default_circle_border_width)));
        setCircleRadius(ta.getDimensionPixelSize(R.styleable.WearableSettingsListView_circle_radius,
                getResources().getDimensionPixelSize(R.dimen.default_circle_radius)));
        setCirclePadding(ta.getDimensionPixelSize(R.styleable.WearableSettingsListView_circle_padding,
                getResources().getDimensionPixelSize(R.dimen.default_circle_padding)));
        ta.recycle();

        View.inflate(context, R.layout.settings_list_view, this);
        mHeaderContainer = (FrameLayout) findViewById(R.id.header_container);
        mListView = (WearableListView) findViewById(android.R.id.list);
        mListView.addOnScrollListener(this);
        mListView.setClickListener(this);
        mListView.setAdapter(mSettingsAdapter);

        setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                setOnApplyWindowInsetsListener(null);

                boolean isRound = insets.isRound();
                mSettingsAdapter.setRound(isRound);

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mHeaderContainer.getLayoutParams();
                if (isRound) {
                    params.topMargin = getResources().getDimensionPixelSize(R.dimen.list_header_margin_top_rd);
                } else {
                    params.topMargin = getResources().getDimensionPixelSize(R.dimen.list_header_margin_top_sq);
                }

                int systemWindowInsetBottom = insets.getSystemWindowInsetBottom();
                mHeaderContainer.setTranslationY(systemWindowInsetBottom);
                insets.replaceSystemWindowInsets(insets.getSystemWindowInsetLeft(), systemWindowInsetBottom, insets.getSystemWindowInsetRight(), systemWindowInsetBottom);
                return insets;
            }
        });
    }

    public View inflateHeaderView(@LayoutRes int headerLayoutResId) {
        View headerView = null;
        if (headerLayoutResId > 0) {
            headerView = View.inflate(getContext(), headerLayoutResId, null);
            if (null != headerView) {
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                mHeaderContainer.addView(headerView, params);
            }
        }
        return headerView;
    }

    public void setSettingsItems(List<SettingsItem> settingsItems) {
        mSettingsAdapter.setItems(settingsItems);
    }

    public void setOnScrollListener(WearableListView.OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public void onTopEmptyRegionClick() {
        if (null != mClickListener)
            mClickListener.onHeaderClicked();
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        SettingsItem item = mSettingsAdapter.getItem(position);

        if (item instanceof SimpleSettingsItem) {
            if (null != mClickListener)
                mClickListener.onSimpleItemClicked((SimpleSettingsItem) item);
        } else if (item instanceof PendingIntentSettingsItem) {
            try {
                ((PendingIntentSettingsItem) item).intent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        } else if (item instanceof BoolPrefSettingsItem) {
            String prefKey = ((BoolPrefSettingsItem) item).prefKey;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean newValue = !sharedPreferences.getBoolean(prefKey, false);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(prefKey, newValue);
            editor.apply();
            mSettingsAdapter.notifyItemChanged(position);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAbsoluteScrollChange(int i) {
        mHeaderContainer.setTranslationY(Math.min(0, -i));
        if (null != mOnScrollListener)
            mOnScrollListener.onAbsoluteScrollChange(i);
    }

    @Override
    public void onScroll(int i) {
        if (null != mOnScrollListener)
            mOnScrollListener.onScroll(i);
    }

    @Override
    public void onScrollStateChanged(int i) {
        if (null != mOnScrollListener)
            mOnScrollListener.onScrollStateChanged(i);
    }

    @Override
    public void onCentralPositionChanged(int i) {
        if (null != mOnScrollListener)
            mOnScrollListener.onCentralPositionChanged(i);
    }

    public SettingsAdapter getAdapter() {
        return mSettingsAdapter;
    }

    public void setTextColor(int color) {
        mSettingsAdapter.setTextColor(color);
    }

    public void setImageTintColor(int color) {
        mSettingsAdapter.setImageTintColor(color);
    }

    public void setCircleBackgroundColor(int color) {
        mSettingsAdapter.setCircleBackgroundColor(color);
    }

    public void setCircleBorderColor(int color) {
        mSettingsAdapter.setCircleBorderColor(color);
    }

    public void setCircleBorderWidth(int width) {
        mSettingsAdapter.setCircleBorderWidth(width);
    }

    public void setCircleRadius(int radius) {
        mSettingsAdapter.setCircleRadius(radius);
    }

    public void setCirclePadding(int padding) {
        mSettingsAdapter.setCirclePadding(padding);
    }

    public void setEnableGestureNavigation(boolean enable) {
        mListView.setEnableGestureNavigation(enable);
    }

    public void setGreedyTouchMode(boolean greedy) {
        mListView.setGreedyTouchMode(greedy);
    }

    public void scrollToPosition(int position) {
        mListView.scrollToPosition(position);
    }

    public void smoothScrollToPosition(int position) {
        mListView.smoothScrollToPosition(position);
    }

}
