package day.cloudy.apps.wear.settings.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class IntentReceiver extends BroadcastReceiver {

    public static final String ACTION_MAKE_TOAST = "action.MAKE_TOAST";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_MAKE_TOAST.equals(intent.getAction())) {
            Toast.makeText(context, "Make toast", Toast.LENGTH_SHORT).show();
        }
    }
}
