package day.cloudy.apps.wear.settings.view;

import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.util.List;

import day.cloudy.apps.wear.settings.R;
import day.cloudy.apps.wear.settings.adapter.SettingsItemAdapter;
import day.cloudy.apps.wear.settings.item.BoolPrefSettingsItem;
import day.cloudy.apps.wear.settings.item.PendingIntentSettingsItem;
import day.cloudy.apps.wear.settings.item.SettingsItem;
import day.cloudy.apps.wear.settings.item.SimpleSettingsItem;

@SuppressWarnings("unused")
public class SettingsRecyclerView extends FrameLayout implements ItemClickSupport.OnItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    public interface OnClickListener {

        void onHeaderClick();

        void onSimpleItemClick(int position, SimpleSettingsItem item);
    }

    private static final String TAG = SettingsRecyclerView.class.getSimpleName();

    private final SettingsItemAdapter mAdapter;

    private final HeaderView mHeaderView;
    private final WearableRecyclerView mRecyclerView;
    private OnClickListener mOnClickListener;

    public SettingsRecyclerView(Context context) {
        this(context, null);
    }

    public SettingsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mAdapter = new SettingsItemAdapter(context);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SettingsRecyclerView);
        setCircleBorderColor(ta.getColor(R.styleable.SettingsRecyclerView_ws_circle_border_color, Color.WHITE));
        setCircleBackgroundColor(ta.getColor(R.styleable.SettingsRecyclerView_ws_circle_color, Color.TRANSPARENT));
        setImageTintColor(ta.getColor(R.styleable.SettingsRecyclerView_ws_image_tint, Color.WHITE));
        setTitleTextColor(ta.getColor(R.styleable.SettingsRecyclerView_ws_title_text_color, Color.WHITE));
        setSummaryTextColor(ta.getColor(R.styleable.SettingsRecyclerView_ws_summary_text_color, Color.WHITE));
        ta.recycle();

        inflate(context, R.layout.settings_recycler_view, this);

        mHeaderView = findViewById(R.id.header_view);
        mHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null)
                    mOnClickListener.onHeaderClick();
            }
        });

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEdgeItemsCenteringEnabled(true);
        mRecyclerView.setLayoutManager(new WearableLinearLayoutManager(context, new ScalingCurvingLayoutCallback(context)));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // pin the header view above the first list item
                View firstChild = mRecyclerView.getChildAt(0);
                if (firstChild != null)
                    mHeaderView.setTranslationY(Math.min(0, firstChild.getTop() - mHeaderView.getHeight()));
            }
        });

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        ItemClickSupport ics = ItemClickSupport.addTo(mRecyclerView);
        ics.setOnItemClickListener(this);

        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                View firstChild = mRecyclerView.getLayoutManager().getChildAt(0);
                if (firstChild != null) {
                    ViewGroup.LayoutParams params = mHeaderView.getLayoutParams();
                    params.height = (mRecyclerView.getHeight() - firstChild.getHeight()) / 2;
                    mHeaderView.setLayoutParams(params);
                }
                smoothScrollToPosition(mAdapter.getSelection());
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int selection = mAdapter.getSelection();
        switch (keyCode) {
            case KeyEvent.KEYCODE_NAVIGATE_NEXT:
                if (selection < mAdapter.getItemCount() - 1) {
                    mAdapter.setSelection(++selection);
                    mRecyclerView.smoothScrollToPosition(selection);
                }
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_PREVIOUS:
                if (selection > 0) {
                    mAdapter.setSelection(--selection);
                    mRecyclerView.smoothScrollToPosition(selection);
                }
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_IN:
                onItemClicked(mRecyclerView, selection, mRecyclerView.getChildAt(selection));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        mAdapter.setSelection(position);

        SettingsItem item = mAdapter.getItem(position);
        if (item instanceof BoolPrefSettingsItem) {
            String prefKey = ((BoolPrefSettingsItem) item).prefKey;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            preferences.edit().putBoolean(prefKey, !preferences.getBoolean(prefKey, false)).apply();
        } else if (item instanceof PendingIntentSettingsItem) {
            try {
                PendingIntent pendingIntent = ((PendingIntentSettingsItem) item).intent;
                getContext().startIntentSender(pendingIntent.getIntentSender(), null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "onItemClicked: Error starting item intent", e);
            }
        } else if (item instanceof SimpleSettingsItem) {
            if (mOnClickListener != null)
                mOnClickListener.onSimpleItemClick(position, (SimpleSettingsItem) item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SettingsItem item;
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            if ((item = mAdapter.getItem(i)) instanceof BoolPrefSettingsItem) {
                if (TextUtils.equals(((BoolPrefSettingsItem) item).prefKey, key)) {
                    mAdapter.notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    public void setOnClickListener(OnClickListener clickListener) {
        mOnClickListener = clickListener;
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener scrollListener) {
        mRecyclerView.addOnScrollListener(scrollListener);
    }

    public void removeOnScrollListener(RecyclerView.OnScrollListener scrollListener) {
        mRecyclerView.removeOnScrollListener(scrollListener);
    }

    public void smoothScrollToPosition(final int position) {
        post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(position);
            }
        });
    }

    public void setSettingsItems(List<SettingsItem> items, int selection, @Nullable OnClickListener listener) {
        mAdapter.setItems(items, selection);
        mOnClickListener = listener;
    }

    public int getSelection() {
        return mAdapter.getSelection();
    }

    public void setSelection(int selection) {
        mAdapter.setSelection(selection);
    }

    public void setHeaderBackgroundColor(@ColorInt int color) {
        mHeaderView.setBackgroundColor(color);
    }

    public void setHeaderIcon(@DrawableRes int resId) {
        mHeaderView.setIconResource(resId);
    }

    public void setHeaderIconVisible(boolean visible) {
        mHeaderView.setIconVisible(visible);
    }

    public void setHeaderText(@StringRes int resId) {
        mHeaderView.setTitleText(resId);
    }

    public void setHeaderText(String text) {
        mHeaderView.setTitleText(text);
    }

    public void setHeaderTextColor(@ColorInt int color) {
        mHeaderView.setTitleTextColor(color);
    }

    public void setTitleTextColor(int color) {
        mAdapter.setTitleTextColor(color);
    }

    public void setSummaryTextColor(int color) {
        mAdapter.setSummaryTextColor(color);
    }

    public void setImageTintColor(int color) {
        mAdapter.setImageTintColor(color);
    }

    public void setCircleBackgroundColor(int color) {
        mAdapter.setCircleBackgroundColor(color);
    }

    public void setCircleBorderColor(int color) {
        mAdapter.setCircleBorderColor(color);
    }
}
