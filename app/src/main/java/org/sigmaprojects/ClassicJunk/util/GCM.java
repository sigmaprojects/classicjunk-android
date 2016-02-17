package org.sigmaprojects.ClassicJunk.util;


/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * Created by don on 12/18/2014.
 * Original from https://code.google.com/p/gcm/source/browse/gcm-client/GcmClient/src/main/java/com/google/android/gcm/demo/app/DemoActivity.java
 * Modified to be a simple util
 */
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.sigmaprojects.ClassicJunk.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main UI for the demo app.
 */
public class GCM {

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static CJDataHolder cjDataHolder = CJDataHolder.getInstance();

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "850262227046";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "ClassicJunk";

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context c;

    String regid;

    //public GCM(MainActivity a, Context c) {
    public GCM(Activity a) {
        c = App.getAppContext();

        if( checkPlayServices(a) ) {
            gcm = GoogleCloudMessaging.getInstance(c);
            regid = getRegistrationId(c);
            Log.i(TAG, "found reg ID? " + regid);
            if (regid.isEmpty()) {
                RegisterInBackground task = new RegisterInBackground(c);
                task.execute();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }
    /*
    public boolean hasRegID() {
        return !regid.isEmpty();
    }
    */

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public boolean checkPlayServices(Activity a) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(a);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, a,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                a.finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private class RegisterInBackground extends AsyncTask <Void, Void, Context> {
        private Context context;

        public RegisterInBackground(Context c) {
            context = c;
        }

        @Override
        protected void onPreExecute() {

        }
        /*
        @Override
        protected void onPostExecute(Context c) {
            //MainActivity activity = (MainActivity) c;
            MainActivity activity = (MainActivity) c;
            setupAPI( activity );
        }
        */

        @Override
        protected Context doInBackground(Void... params) {
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                String regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;
                regid = regid;
                storeRegistrationId(context, regid);
                //MainActivity activity = (MainActivity)context;
                sendRegistrationIdToBackend( regid );
                //setupAPI( activity );
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                Log.i(TAG, ex.toString());
            }
            return context;
        }
    }


    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.

    public void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }
     */

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences("gcmPreferences",Context.MODE_PRIVATE);
    }
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend(String foundregid) {
        // Your implementation here.
        try {
            URL registerDeviceURL = new URL("http://api.classicjunk.sigmaprojects.org/device/register/?device_id="+App.getDeviceUuid()+"&registration_id="+foundregid);
            URLConnection c = registerDeviceURL.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                //System.out.println(inputLine);
                Log.v(TAG,"RegisterDevice: "+inputLine);
            in.close();
        } catch(Exception e) {
            Log.e(TAG, "Error sending reg id", e);
        }
    }

    /*
    private void setupAPI(MainActivity activity) {
        apiService apiservice = new apiService(activity, regid);
        activity.setAPIService( apiservice );
        activity.getAPIService().download();
    }
    */

}

