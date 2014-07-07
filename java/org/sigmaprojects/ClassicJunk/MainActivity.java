package org.sigmaprojects.ClassicJunk;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import org.sigmaprojects.ClassicJunk.adapter.TabsPagerAdapter;
import org.sigmaprojects.ClassicJunk.beans.Watch;
import org.sigmaprojects.ClassicJunk.beans.WatchInventory;
import org.sigmaprojects.ClassicJunk.util.GCM;
import org.sigmaprojects.ClassicJunk.util.GPSTracker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

    private static final String TAG = "ClassicJunk";

	private ViewPager viewPager;
	private static TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "New", "Alerts", "Notifications" };

    //public final static apiService apiservice = new apiService();
    private static apiService apiservice = null;

    public static Float lat = Float.valueOf("0.00");
    public static Float lng = Float.valueOf("0.00");

    private static GCM gcm;

    //static Context context;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        Context context = getApplicationContext();

        if( gcm == null || !gcm.hasRegID() ) {
            gcm = new GCM(this, context);
        }

        List<Fragment> fragments = getFragments();
        setupLocation();

    	actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(10);
        viewPager.setAdapter(mAdapter);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }

			@Override
			public void onPageScrollStateChanged(int arg0) { }
		});

        Intent intent = getIntent();
        checkIntent(intent);
	}

    @Override
    protected void onResume() {
        super.onResume();
        //gcm.checkPlayServices(this);
        Intent intent = getIntent();
        checkIntent(intent);
    }


    @Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

    private void checkIntent(Intent intent) {
        try{
            String action = intent.getAction().toLowerCase();
            Log.v(TAG, "ACTION IS: " + action);
            if(action != null && action.equalsIgnoreCase("OPEN_TAB_2") ){
                Log.v(TAG, "Notification intent");
                //viewPager.setCurrentItem(2);
                getAPIService().download(viewPager);
            } else {
                if( gcm == null || !gcm.hasRegID() ) {
                    gcm = new GCM(this, this);
                }
            }
        }catch(Exception e){
            Log.e(TAG, "Problem consuming action from intent", e);
        }
    }

    public void setAPIService(apiService api) {
        if( apiservice == null ) {
            apiservice = api;
        }
    }
    public apiService getAPIService() {
        return apiservice;
    }


    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add( new editWatchFragment() );
        fList.add( new WatchListFragment() );
        fList.add( new WatchInventoryFragment() );
        return fList;

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

    public void resetEditWatchFrag() {
        editWatchFragment.resetForm();
        viewPager.setCurrentItem(1);
    }

    public void resetEditWatchFrag(Watch w) {
        editWatchFragment.resetForm(w);
        viewPager.setCurrentItem(0);
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

    public static void updateWatchInventories(ArrayList<WatchInventory> watchInventories) {
        WatchInventoryFragment.updateListView(watchInventories);
    }

    public static void updateWatches(ArrayList<Watch> watches) {
        WatchListFragment.updateListView(watches);
    }



}
