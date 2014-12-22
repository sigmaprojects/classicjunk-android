package org.sigmaprojects.ClassicJunk;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import org.sigmaprojects.ClassicJunk.beans.Watch;
import org.sigmaprojects.ClassicJunk.beans.WatchInventory;
import org.sigmaprojects.ClassicJunk.util.GCM;
import org.sigmaprojects.ClassicJunk.util.GPSTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private static final String TAG = "ClassicJunk";

    private static apiService apiservice = null;

    public static Float lat = Float.valueOf("0.00");
    public static Float lng = Float.valueOf("0.00");

    private static GCM gcm;

    public static CJDataHolder cjDataHolder = CJDataHolder.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //Context context = getApplicationContext();

        if( gcm == null ) {
            gcm = new GCM(this);
        }
        verifyApiService();

        setupLocation();


        Intent intent = getIntent();
        checkIntent(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        gcm.checkPlayServices(this);
        verifyApiService();

        Intent intent = getIntent();
        checkIntent(intent);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        switch (position) {
            case 0:
                fragment = editWatchFragment.newInstance();
                break;
            case 1:
                fragment = WatchListFragment.newInstance();
                break;
            case 2:
                fragment = WatchInventoryFragment.newInstance();
                break;
            default:
                //fragment = PlaceholderFragment.newInstance(position + 1);
                fragment = editWatchFragment.newInstance();
            break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                //.commit();
                .commitAllowingStateLoss();
        Log.e("TESTING","onNavigationDrawerItemSelected was clicked: " + position);
    }


    public void onSectionAttached(int number) {
        Log.d("TESTING","onSectionAttached was clicked: " + number);
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);

        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.resync) {
            apiservice.download(2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void verifyApiService() {
        if( apiservice == null ) {
            apiservice = new apiService(this);
            apiservice.download();
        }
    }

    private void checkIntent(Intent intent) {
        try{
            String action = intent.getAction().toLowerCase();
            Log.v(TAG, "ACTION IS: " + action);
            if(action != null && action.equalsIgnoreCase("OPEN_TAB_2") ){
                Log.v(TAG, "Notification intent");
                //viewPager.setCurrentItem(2);
                //onNavigationDrawerItemSelected(2);
                apiservice.download(2);
            } else {
                if( gcm == null ) {
                    gcm = new GCM(this);
                    Log.v(TAG,"checkIntent new GCM assigned.");
                }
            }
        }catch(Exception e){
            Log.e(TAG, "Problem consuming action from intent", e);
        }
    }

    private void setupLocation() {
        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()) {
            Double _lat = gps.getLatitude();
            Double _lng = gps.getLongitude();
            lat = Float.valueOf(""+_lat);
            lng = Float.valueOf(""+_lng);
            Log.v(TAG, "Latitude: " + lat + "    Lng: " + lng);
            gps.stopUsingGPS();
        } else {
            gps.showSettingsAlert();
        }
    }
    public static boolean hasLocation() {
        if( lat != 0.00 && lng != 0.00 ) {
            return true;
        }
        return false;
    }

    public void saveWatch(Watch w) {
        try {
            apiservice.saveWatch(w);
        } catch(NullPointerException npe) {
            Log.v(TAG, "apiservice.saveWatch(w) not ready yet (NPE).");
        }
    }

    public void deleteWatch(Watch w) {
        try {
            apiservice.deleteWatch(w);
        } catch(NullPointerException npe) {
            Log.v(TAG, "apiservice.deleteWatch(w) not ready yet (NPE).");
        }
    }


}
