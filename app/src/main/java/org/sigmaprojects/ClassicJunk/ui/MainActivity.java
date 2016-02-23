package org.sigmaprojects.ClassicJunk.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.sigmaprojects.ClassicJunk.R;
import org.sigmaprojects.ClassicJunk.api.ClassicJunkService;
import org.sigmaprojects.ClassicJunk.api.interfaces.APICallComplete;
import org.sigmaprojects.ClassicJunk.util.CJDataHolder;
import org.sigmaprojects.ClassicJunk.util.GCM;
import org.sigmaprojects.ClassicJunk.util.GPSTracker;
import org.sigmaprojects.ClassicJunk.util.Utils;


public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String SHOW_SEARCH_RESULTS = "org.sigmaprojects.ClassicJunk.SHOW_SEARCH_RESULTS";
    public static final String FINISHED_SYNCING = "org.sigmaprojects.ClassicJunk.finished_syncing";
    public static final String RECEIVED_NOTIFICATION = "OPEN_TAB_2";



    private final String TAG = MainActivity.class.getName();
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    public CJDataHolder cjDataHolder = CJDataHolder.getInstance();
    private GCM gcm;
    //public apiService apiservice;

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


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FINISHED_SYNCING);
        intentFilter.addAction(SHOW_SEARCH_RESULTS);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, intentFilter);

        Intent intent = getIntent();
        checkIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupLocation();

        if( gcm == null ) {
            gcm = new GCM(this);
        }

        gcm.checkPlayServices(this);

        ClassicJunkService.getInstance().download(new APICallComplete() {
            @Override
            public void finished(Boolean success) {
            }
        });
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Log.v(TAG, "onNavigationDrawerItemSelected pressed: " + position);
        Fragment fragment = new Fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:
                fragment = WatchListFragment.newInstance(position);
                break;
            case 1:
                reSync();
                break;
            case 2:
                SearchInventoryDialogFragment dialog = SearchInventoryDialogFragment.newInstance(position);
                dialog.show(this.getSupportFragmentManager(), "searchInventoryDialogFragment");
                return;
            case 3:
                fragment = SearchInventoryFragment.newInstance(position);
                break;
            case 4:
                fragment = WatchInventoryFragment.newInstance(1);
                break;
            case 5:
                fragment = EditWatchFragment.newInstance(position);
                break;
            default:
                fragment = SearchInventoryFragment.newInstance(position);
                break;
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container, fragment)
                //.commit();
                .commitAllowingStateLoss();
        Log.e("TESTING", "onNavigationDrawerItemSelected was clicked: " + position);
    }


    public void onSectionAttached(int number) {
        Log.v(TAG, "onSectionAttached was clicked: " + number);
        switch (number) {
            case 0:
                mTitle = getString(R.string.section_title_2);
                break;
            case 1:
                mTitle = getString(R.string.section_title_3);
                break;
            case 2:
                mTitle = getString(R.string.section_title_4);
                break;
            case 3:
                mTitle = getString(R.string.section_title_5);
                break;
            case 4:
                mTitle = getString(R.string.section_title_1);
                break;
            default:
                mTitle = getString(R.string.app_name);;
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(mTitle);

        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.resync) {
            reSync();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reSync() {
        final ProgressDialog progressDialog = Utils.createProgressDialog(this);
        ClassicJunkService.getInstance().download(new APICallComplete() {
            @Override
            public void finished(Boolean success) {
                onNavigationDrawerItemSelected(4);
                try {
                    progressDialog.dismiss();
                } catch(Exception e) {}
            }
        });
    }


    private void checkIntent(Intent intent) {
        try{
            String action = intent.getAction().toLowerCase();
            Log.v(TAG, "ACTION IS: " + action);


            if(action != null && action.equalsIgnoreCase("OPEN_TAB_2")) {
                Log.v(TAG, "Notification intent");
                reSync();
            } else {
                /*
                if( gcm == null ) {
                    gcm = new GCM(this);
                    Log.v(TAG,"checkIntent new GCM assigned.");
                }
                */
            }
        }catch(Exception e){
            Log.e(TAG, "Problem consuming action from intent", e);
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Get extra data included in the Intent
            //String message = intent.getStringExtra("message");
            Log.d(TAG, "Got: " + intent.getAction());
            switch(intent.getAction()) {
                case FINISHED_SYNCING:
                    onNavigationDrawerItemSelected(0);
                    break;
                case RECEIVED_NOTIFICATION:
                    onNavigationDrawerItemSelected(1);
                    break;
                case SHOW_SEARCH_RESULTS:
                    onNavigationDrawerItemSelected(3);
                    break;
            }
        }
    };

    private void setupLocation() {
        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()) {
            Double _lat = gps.getLatitude();
            Double _lng = gps.getLongitude();
            Float lat = Float.valueOf("" + _lat);
            Float lng = Float.valueOf("" + _lng);
            cjDataHolder.setLat(Float.valueOf("" + _lat));
            cjDataHolder.setLng(Float.valueOf("" + _lng));
            Log.v(TAG, "Latitude: " + lat + "    Lng: " + lng);
            gps.stopUsingGPS();
        } else {
            mNavigationDrawerFragment.removeSearchNavigationItem();
            gps.showSettingsAlert();
        }
    }


}
