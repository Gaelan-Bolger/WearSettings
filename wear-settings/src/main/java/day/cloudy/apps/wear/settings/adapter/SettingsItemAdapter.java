package day.cloudy.apps.wear.settings.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.wear.widget.WearableRecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import day.cloudy.apps.wear.settings.R;
import day.cloudy.apps.wear.settings.item.BoolPrefSettingsItem;
import day.cloudy.apps.wear.settings.item.SettingsItem;

public class SettingsItemAdapter extends WearableRecyclerView.Adapter<SettingsItemViewHolder> {

    private final Context mContext;
    private final SharedPreferences mPreferences;

    private List<SettingsItem> mItems;
    private int mSelection;
    private int mTitleTextColor;
    private int mSummaryTextColor;
    private int mImageTintColor;
    private int mCircleBackgroundColor;
    private int mCircleBorderColor;

    public SettingsItemAdapter(Context context) {
        mContext = context;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public SettingsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SettingsItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.settings_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SettingsItemViewHolder holder, int position) {
        SettingsItem item = getItem(position);

        holder.setTitleTextColor(mTitleTextColor);
        holder.setSummaryTextColor(mSummaryTextColor);
        holder.setImageTintColor(mImageTintColor);
        holder.setCircleBackgroundColor(mCircleBackgroundColor);
        holder.setCircleBorderColor(mCircleBorderColor);

        holder.setChecked(mSelection == position);
        holder.setImageResource(item.iconResId);
        holder.setTitle(item.title);

        if (item instanceof BoolPrefSettingsItem) {
            BoolPrefSettingsItem prefItem = (BoolPrefSettingsItem) item;
            if (!TextUtils.isEmpty(prefItem.prefKey)) {
                boolean checked = mPreferences.getBoolean(prefItem.prefKey, false);
                holder.setSummary(mContext.getString(checked ? R.string.on : R.string.off).toUpperCase());
            } else {
                holder.setSummary(null);
            }
        } else {
            holder.setSummary(null);
        }
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    public SettingsItem getItem(int position) {
        return mItems.get(position);
    }

    public void setItems(List<SettingsItem> settingsItems, int selection) {
        mItems = settingsItems;
        mSelection = selection;
        notifyDataSetChanged();
    }

    public int getSelection() {
        return mSelection;
    }

    public void setSelection(int selection) {
        mSelection = selection;
        notifyDataSetChanged();
    }

    public void setTitleTextColor(int color) {
        mTitleTextColor = color;
        notifyDataSetChanged();
    }

    public void setSummaryTextColor(int color) {
        mSummaryTextColor = color;
        notifyDataSetChanged();
    }

    public void setImageTintColor(int color) {
        mImageTintColor = color;
        notifyDataSetChanged();
    }

    public void setCircleBackgroundColor(int color) {
        mCircleBackgroundColor = color;
        notifyDataSetChanged();
    }

    public void setCircleBorderColor(int circleBorderColor) {
        mCircleBorderColor = circleBorderColor;
        notifyDataSetChanged();
    }
}