package day.cloudy.apps.wear.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WearableListView;
import android.text.TextUtils;
import android.view.ViewGroup;

import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter {

    private final Context sContext;
    private final SharedPreferences sPreferences;
    private final int sRdPad;
    private final int sSqPad;

    private List<SettingsItem> sItems;

    private boolean sIsRound;
    private int mTextColor;
    private int mCircleBackgroundColor;
    private int mCircleBorderColor;
    private int mCircleBorderWidth;
    private int mImageTintColor;
    private int mCircleRadius;
    private int mCirclePadding;

    protected SettingsAdapter(Context context) {
        sContext = context;
        sPreferences = PreferenceManager.getDefaultSharedPreferences(sContext);
        Resources resources = sContext.getResources();
        sRdPad = resources.getDimensionPixelSize(R.dimen.list_item_padding_rd);
        sSqPad = resources.getDimensionPixelSize(R.dimen.list_item_padding_sq);
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearableListView.ViewHolder(new SettingsItemView(sContext));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SettingsItem item = sItems.get(position);
        SettingsItemView settingsItemView = (SettingsItemView) holder.itemView;
        settingsItemView.setTextColor(mTextColor);
        settingsItemView.setImageTintColor(mImageTintColor);
        settingsItemView.setCircleBackgroundColor(mCircleBackgroundColor);
        settingsItemView.setCircleBorderColor(mCircleBorderColor);
        settingsItemView.setCircleBorderWidth(mCircleBorderWidth);
        settingsItemView.setCircleRadius(mCircleRadius);
        settingsItemView.setCirclePadding(mCirclePadding);

        settingsItemView.setImageResource(item.iconResId);
        settingsItemView.setTitle(item.title);

        if (item instanceof BoolPrefSettingsItem) {
            BoolPrefSettingsItem prefItem = (BoolPrefSettingsItem) item;
            if (!TextUtils.isEmpty(prefItem.prefKey)) {
                boolean checked = sPreferences.getBoolean(prefItem.prefKey, false);
                settingsItemView.setSummary(sContext.getString(checked ? R.string.on : R.string.off).toUpperCase());
            }
        } else {
            settingsItemView.setSummary(null);
        }

        if (sIsRound) {
            settingsItemView.setPadding(sRdPad, 0, sRdPad, 0);
        } else {
            settingsItemView.setPadding(sSqPad, 0, sSqPad, 0);
        }
    }

    @Override
    public int getItemCount() {
        return null != sItems ? sItems.size() : 0;
    }

    protected void setItems(List<SettingsItem> settingsItems) {
        sItems = settingsItems;
        notifyDataSetChanged();
    }

    protected SettingsItem getItem(int position) {
        return sItems.get(position);
    }

    protected void setRound(boolean isRound) {
        sIsRound = isRound;
        notifyDataSetChanged();
    }

    public void setTextColor(int color) {
        mTextColor = color;
        notifyDataSetChanged();
    }

    public void setCircleBackgroundColor(int color) {
        mCircleBackgroundColor = color;
        notifyDataSetChanged();
    }

    public void setCircleBorderColor(int color) {
        mCircleBorderColor = color;
        notifyDataSetChanged();
    }

    public void setCircleBorderWidth(int width) {
        mCircleBorderWidth = width;
        notifyDataSetChanged();
    }

    public void setImageTintColor(int color) {
        mImageTintColor = color;
        notifyDataSetChanged();
    }

    public void setCircleRadius(int radius) {
        mCircleRadius = radius;
        notifyDataSetChanged();
    }

    public void setCirclePadding(int padding) {
        mCirclePadding = padding;
        notifyDataSetChanged();
    }
}
