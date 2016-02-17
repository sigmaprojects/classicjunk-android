package org.sigmaprojects.ClassicJunk.util;

/**
 * Created by don on 6/15/2014.
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        } catch(Exception e) {
            Log.v("GCMDEMO", "Intent.FLAG_INCLUDE_STOPPED_PACKAGES failed");
        }

        try {
            intent.addFlags(Intent.FLAG_RECEIVER_NO_ABORT );
        } catch(Exception e) {
            Log.v("GCMDEMO", "Intent.FLAG_RECEIVER_NO_ABORT failed");
        }

        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED );
        } catch(Exception e) {
            Log.v("GCMDEMO", "Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED failed");
        }

        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);


    }
}