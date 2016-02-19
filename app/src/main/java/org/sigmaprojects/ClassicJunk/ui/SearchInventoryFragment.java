package org.sigmaprojects.ClassicJunk.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
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
import org.sigmaprojects.ClassicJunk.api.ClassicJunkService;
import org.sigmaprojects.ClassicJunk.api.beans.Inventory;
import org.sigmaprojects.ClassicJunk.api.interfaces.APICallComplete;
import org.sigmaprojects.ClassicJunk.ui.adapter.SearchInventoryAdapter;
import org.sigmaprojects.ClassicJunk.util.CJDataHolder;
import org.sigmaprojects.ClassicJunk.util.Utils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;

public class SearchInventoryFragment extends Fragment {

    private static SearchInventoryAdapter searchinventoryAdapter;
    private static final String TAG = SearchInventoryFragment.class.getName();
    private View rootView;
    private ListView lv;

    public static CJDataHolder cjDataHolder = CJDataHolder.getInstance();

    private static final String ARG_SECTION_NUMBER = "section_number";

    private int lastClickedPosition = -1;

    private enum SortTypes {
        CREATED,
        ARRIVED,
        DISTANCE,
        CARYEAR
    };

    public static SearchInventoryFragment newInstance(int sectionNumber) {
        SearchInventoryFragment fragment = new SearchInventoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchInventoryFragment() {
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
                View clickedView = view;

                lastClickedPosition = position;

                SearchInventoryAdapter.ViewHolder tag =(SearchInventoryAdapter.ViewHolder)clickedView.getTag();

                showDialog(tag.data);

            }
        });
        //Log.v(TAG,"onCreateView WatchInventoryFragment ran.......");

        resetAdapter();

        setHasOptionsMenu(true);
        return rootView;
    }

    private void showDialog(Inventory inventory) {
        Intent intent = new Intent(getActivity(), InventoryInfo.class);

        Bundle args = new Bundle();
        args.putParcelable(InventoryInfo.ARG_INVENTORY, inventory);
        args.putInt(InventoryInfo.ARG_POSITION, lastClickedPosition);
        intent.putExtras(args);

        startActivity(intent);
    }


    private void resetAdapter() {
        lastClickedPosition = -1;
        searchinventoryAdapter = new SearchInventoryAdapter(rootView.getContext(), R.layout.watchinventory_info, cjDataHolder.getSearchInventories());
        lv.setAdapter(searchinventoryAdapter);
        searchinventoryAdapter.notifyDataSetChanged();

        TextView noInventory = (TextView) rootView.findViewById(R.id.no_inventory);
        noInventory.setText(R.string.search_none_found);
        // hide the No Inventory Found message
        if( cjDataHolder.hasSearchInventories() ) {
            noInventory.setVisibility(TextView.GONE);
        } else {
            noInventory.setVisibility(TextView.VISIBLE);
        }
    }

    private void updateSort(final SortTypes type) {
        //Log.v(TAG, "update sort: " + type + "watch inventories size: " + cjDataHolder.getWatchInventories().size());
        Collections.sort(cjDataHolder.getSearchInventories(), new Comparator<Inventory>() {
            @Override
            public int compare(Inventory item1, Inventory item2) {
                switch (type) {
                    case CREATED: {
                        return item1.getCreated().compareTo(item2.getCreated());
                    }
                    case ARRIVED: {
                        return -item1.getArrived().compareTo(item2.getArrived());
                    }
                    case DISTANCE: {
                        Float thereDistance1 = getLocationDistance(item1.getLocation());
                        Float thereDistance2 = getLocationDistance(item2.getLocation());
                        return thereDistance1.compareTo(thereDistance2);
                    }
                    case CARYEAR: {
                        return -item1.getCaryear().compareTo(item2.getCaryear());
                    }
                    default: {
                        return item1.getCreated().compareTo(item2.getCreated());
                    }
                }

            }
        });


        resetAdapter();
    }


    private Float getLocationDistance(org.sigmaprojects.ClassicJunk.api.beans.Location inventoryLocation) {
        try {
            Location there = new Location("there");
            there.setLatitude(inventoryLocation.getLat());
            there.setLongitude(inventoryLocation.getLng());
            Location here = new Location("here");
            here.setLatitude(cjDataHolder.getLat());
            here.setLongitude(cjDataHolder.getLng());
            return here.distanceTo(there);
        } catch (Exception e) {
            return new Float(1.0000000);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.watchinventory_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Log.v(TAG, "onOptionsItemSelected: " + item.getItemId() + " card year id: " + R.id.sortByYear);
        switch (item.getItemId()) {
            case R.id.sortByDate:
                updateSort(SortTypes.CREATED);
                break;

            case R.id.sortByArrived:
                updateSort(SortTypes.ARRIVED);
                break;

            case R.id.sortByDistance:
                updateSort(SortTypes.DISTANCE);
                break;

            case R.id.sortByYear:
                updateSort(SortTypes.CARYEAR);
                break;

            case R.id.resync:
                final ProgressDialog progressDialog = Utils.createProgressDialog(getActivity());
                ClassicJunkService.getInstance().download(new APICallComplete() {
                    @Override
                    public void finished(Boolean success) {
                        try {
                            progressDialog.dismiss();
                        } catch (Exception e) {}
                    }
                });
                break;
            default:
                updateSort(SortTypes.CREATED);
                break;
        }
        return true;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
