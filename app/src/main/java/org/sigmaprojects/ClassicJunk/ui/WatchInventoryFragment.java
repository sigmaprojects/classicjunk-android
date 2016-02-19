package org.sigmaprojects.ClassicJunk.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.sigmaprojects.ClassicJunk.R;
import org.sigmaprojects.ClassicJunk.api.beans.WatchInventory;
import org.sigmaprojects.ClassicJunk.ui.adapter.WatchInventoryAdapter;
import org.sigmaprojects.ClassicJunk.util.CJDataHolder;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;

public class WatchInventoryFragment extends Fragment {

    private final String TAG = WatchInventoryFragment.class.getName();
    private static final String ARG_SECTION_NUMBER = "section_number";

    private View rootView;
    private ListView lv;

    private static CJDataHolder cjDataHolder;

    private int lastClickedPosition = -1;


    private enum SortTypes {
        CREATED,
        ARRIVED,
        DISTANCE,
        CARYEAR
    }

    public static WatchInventoryFragment newInstance(int sectionNumber) {
        WatchInventoryFragment fragment = new WatchInventoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        cjDataHolder = CJDataHolder.getInstance();

        return fragment;
    }

    public WatchInventoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_watchinventory, container, false);
        } catch (InflateException e) {
            Log.e(TAG,e.getMessage());
        }

        lv = (ListView) rootView.findViewById(R.id.watchInventoryListView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {

                lastClickedPosition = position;

                WatchInventoryAdapter.ViewHolder tag = (WatchInventoryAdapter.ViewHolder) view.getTag();

                showDialog(tag.data);

            }
        });

        //Log.v(TAG, "onCreateView WatchInventoryFragment ran.......");

        resetAdapter();

        setHasOptionsMenu(true);
        return rootView;
    }


    private void showDialog(WatchInventory wi) {
        Intent intent = new Intent(getActivity(), InventoryInfo.class);

        Bundle args = new Bundle();
        args.putParcelable(InventoryInfo.ARG_INVENTORY, wi.getInventory());
        args.putInt(InventoryInfo.ARG_POSITION, lastClickedPosition);
        intent.putExtras(args);

        startActivity(intent);
    }


    private void resetAdapter() {
        lastClickedPosition = -1;
        WatchInventoryAdapter watchinventoryAdapter = new WatchInventoryAdapter(rootView.getContext(), R.layout.watchinventory_info, cjDataHolder.getWatchInventories());
        lv.setAdapter(watchinventoryAdapter);
        watchinventoryAdapter.notifyDataSetChanged();

        // hide the No Inventory Found message
        if( cjDataHolder.hasWatchInventories() ) {
            TextView noInventory = (TextView) rootView.findViewById(R.id.no_inventory);
            noInventory.setText( R.string.no_inventory_found );
            //noInventory.setVisibility(RelativeLayout.GONE);
            noInventory.setVisibility(TextView.GONE);
        }
    }

    private void updateSort(String type) {
        final String _type = type;
        //Log.v(TAG, "watch inventory size: " + cjDataHolder.getWatchInventories().size());
        Collections.sort(cjDataHolder.getWatchInventories(), new Comparator<WatchInventory>() {
            @Override
            public int compare(WatchInventory item1, WatchInventory item2) {
                switch (SortTypes.valueOf(_type)) {
                    case CREATED: {
                        return item2.getCreated().compareTo(item1.getCreated());
                    }
                    case ARRIVED: {
                        return item2.getInventory().getArrived().compareTo(item1.getInventory().getArrived());
                    }
                    case DISTANCE: {
                        return -item2.getDistance().compareTo(item1.getDistance());
                    }
                    case CARYEAR: {
                        return -item2.getInventory().getCaryear().compareTo(item1.getInventory().getCaryear());
                    }
                    default: {
                        return item2.getCreated().compareTo(item1.getCreated());
                    }
                }

            }
        });
        resetAdapter();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.watchinventory_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortByDate:
                updateSort("CREATED");
                return true;

            case R.id.sortByArrived:
                updateSort("ARRIVED");
                return true;

            case R.id.sortByDistance:
                updateSort("DISTANCE");
                return true;

            case R.id.sortByYear:
                updateSort("CARYEAR");
                return true;

            default:
                updateSort("CREATED");
                return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.v(TAG, "onResume");

        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        } catch(Exception e) {}

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(InventoryInfo.INVENTORY_INFO_SWIPE_NEXT);
        intentFilter.addAction(InventoryInfo.INVENTORY_INFO_SWIPE_PREVIOUS);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, intentFilter);

    }

    @Override
    public void onStop() {
        //Log.v(TAG, "onStop");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }


    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch(intent.getAction()) {
                case InventoryInfo.INVENTORY_INFO_SWIPE_NEXT:
                    clickNextListViewItem(1);
                    break;
                case InventoryInfo.INVENTORY_INFO_SWIPE_PREVIOUS:
                    clickNextListViewItem(-1);
                    break;
            }
        }
    };

    private void clickNextListViewItem(int plusIndex) {
        int clickPosition = lastClickedPosition+plusIndex;

        try {
            lv.performItemClick(
                    lv.getAdapter().getView(clickPosition, null, null),
                    clickPosition,
                    lv.getAdapter().getItemId(clickPosition)
            );
        } catch (ArrayIndexOutOfBoundsException oob) {
            // dont care
        } catch (Exception e) {
            Log.e(TAG, "error triggering onclick", e);
        }
    }

}
