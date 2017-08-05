package day.cloudy.apps.wear.settings.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.wear.widget.CurvingLayoutCallback;
import android.view.View;

import day.cloudy.apps.wear.settings.R;

class ScalingCurvingLayoutCallback extends CurvingLayoutCallback {

    private static final float MAX_CHILD_SCALE = 0.65f;

    private float mProgressToCenter;

    ScalingCurvingLayoutCallback(Context context) {
        super(context);
    }

    @Override
    public void onLayoutFinished(View child, RecyclerView parent) {
        super.onLayoutFinished(child, parent);

        // Figure out % progress from top to bottom
        float centerOffset = ((float) child.getHeight() / 2.0f) / (float) parent.getHeight();
        float yRelativeToCenterOffset = (child.getY() / parent.getHeight()) + centerOffset;

        // Normalize for center
        mProgressToCenter = Math.abs(0.5f - yRelativeToCenterOffset);

        // Adjust to the maximum scale
        mProgressToCenter = Math.min(mProgressToCenter, MAX_CHILD_SCALE);

        child.setAlpha(1 - mProgressToCenter);
        child.findViewById(R.id.setting_radio_button).setScaleX(1 - mProgressToCenter);
        child.findViewById(R.id.setting_radio_button).setScaleY(1 - mProgressToCenter);
    }
}
