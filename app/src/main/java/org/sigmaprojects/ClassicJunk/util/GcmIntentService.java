package org.sigmaprojects.ClassicJunk.util;

/**
 * Created by don on 6/15/2014.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.sigmaprojects.ClassicJunk.R;
import org.sigmaprojects.ClassicJunk.ui.MainActivity;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "ClassicJunk";

    @Override
    protected void onHandleIntent(Intent intent) {

        //intent.setAction("OPEN_TAB_2");
        //intent.putExtra("OPEN_TAB_2",true);

        Log.v("GCMDEMO", "got intent: " + intent.getAction());
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


        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            String title = "Cars found.";
            String message = "Your car alerts have been triggered.";
            try {
                title = extras.get("title").toString();
            } catch(Exception e) {}
            try {
                message = extras.get("message").toString();
            } catch(Exception e) {}

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
                sendNotification(title,message);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " + extras.toString());
                sendNotification(title,message);
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.

                // Post notification of received message.
                //sendNotification("Received: " + extras.toString());
                sendNotification(title,message);
                Log.i(TAG, "Received: " + extras.toString());
            }

        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    public void sendNotification(String message, String title) {

        mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(MainActivity.RECEIVED_NOTIFICATION);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);


        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // for older API calls
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(message))
                            .setContentText(message);
            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mBuilder.setContentIntent(contentIntent);
            mBuilder.setAutoCancel(true);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        } else {
            // new API calls
            Notification.Builder mBuilder = new Notification.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(new Notification.BigTextStyle().bigText(message))
                    .setSmallIcon(R.mipmap.ic_notification)
                    .setColor(Color.parseColor("#c94545"));
                    mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }


    }


}

