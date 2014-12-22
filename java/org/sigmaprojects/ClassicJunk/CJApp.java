package org.sigmaprojects.ClassicJunk;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * Created by don on 12/16/2014.
 */
public class CJApp extends Application {

    private static Context context;

    private static String deviceuuid;

    public void onCreate(){
        super.onCreate();
        CJApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return CJApp.context;
    }

    // basically used as a unique identifier now.  Used to use the GCM RegID but that caused all kinds of problems.
    // this should be anonymous enough, but if you don't think so contact me don(at)sigmaprojects.org and suggest ideas.
    public static String getDeviceUuid() {
        if( deviceuuid == null || deviceuuid.isEmpty() ) {
            final TelephonyManager tm = (TelephonyManager) getAppContext().getSystemService(Context.TELEPHONY_SERVICE);

            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(getAppContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            String deviceId = deviceUuid.toString();
            deviceuuid = deviceId;
        }
        return deviceuuid;
    }

}
