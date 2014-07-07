package org.sigmaprojects.ClassicJunk.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.sigmaprojects.ClassicJunk.MainActivity;
import org.sigmaprojects.ClassicJunk.apiService;
import org.sigmaprojects.ClassicJunk.beans.Watch;
import org.sigmaprojects.ClassicJunk.beans.WatchReponse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by don on 6/14/2014.
 */
public class GCM {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static String REG_ID = "";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    String SENDER_ID = "850262227046";

    static final String TAG = "ClassicJunk";
    private GoogleCloudMessaging gcm;
    //private MainActivity activity;
    //private Context context;

    public GCM(MainActivity a, Context c) {

        //activity = a;
        //context = c;

        if( checkPlayServices(a) ) {
            gcm = GoogleCloudMessaging.getInstance(a);
            REG_ID = getRegistrationId(c);
            Log.i(TAG, "found reg ID? " + REG_ID);
            if (REG_ID.isEmpty()) {
                RegisterInBackground task = new RegisterInBackground(c);
                task.execute();
            } else {
                setupAPI(a);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

    }

    public boolean hasRegID() {
        return !REG_ID.isEmpty();
    }

    public String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        /*
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        */
        return registrationId;
    }

    public boolean checkPlayServices(MainActivity a) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(a);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, a,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                a.finish();
            }
            return false;
        }
        return true;
    }


    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but how you store the regID in your app is up to you.
        return context.getSharedPreferences(MainActivity.class.getSimpleName(),Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        int v = 1;
        return v;
    }


    /*
    private void registerInBackground() {
        new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        //gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    String regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    REG_ID = regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.

                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, REG_ID);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.i(TAG, ex.toString());
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //mDisplay.append(msg + "\n");
                Log.i(TAG, "Something? " + msg);
                setupAPI();
            }
        }.execute();
        //...
    }
    */
    private class RegisterInBackground extends AsyncTask <Void, Void, Context> {
        private Context context;

        public RegisterInBackground(Context c) {
            context = c;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Context c) {
            MainActivity activity = (MainActivity) c;
            setupAPI( activity );
        }

        @Override
        protected Context doInBackground(Void... params) {
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                String regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;
                REG_ID = regid;
                storeRegistrationId(context, REG_ID);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                Log.i(TAG, ex.toString());
            }
            return context;
        }
    }

    private void setupAPI(MainActivity activity) {
        apiService apiservice = new apiService(activity, REG_ID);
        activity.setAPIService( apiservice );
        activity.getAPIService().download();
    }



    private void sendRegistrationIdToBackend() {
        // Your implementation here.
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        Log.i(TAG, "RegId: " + regId);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }


}
