package org.sigmaprojects.ClassicJunk;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.sigmaprojects.ClassicJunk.adapter.WatchInventoryAdapter;
import org.sigmaprojects.ClassicJunk.beans.WatchInventory;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;

public class WatchInventoryFragment extends Fragment {

    private static final String TAG = "ClassicJunk";
    private static View rootView;
    private static ListView lv;

    private static final CJDataHolder cjDataHolder = CJDataHolder.getInstance();

    private static enum SortTypes {
        CREATED,
        ARRIVED,
        DISTANCE,
        CARYEAR
    }

    public static WatchInventoryFragment newInstance() {
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return new WatchInventoryFragment();
    }

    public WatchInventoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
                // We know the View is a TextView so we can cast it
                //RelativeLayout clickedView = (RelativeLayout) view;
                View clickedView = view;
                //Toast.makeText(getActivity(), "Item with id [" + id + "] - Position [" + position + "] - Planet [" + clickedView.getText() + "]", Toast.LENGTH_SHORT).show();

                WatchInventoryAdapter.ViewHolder tag =(WatchInventoryAdapter.ViewHolder)clickedView.getTag();

                MainActivity a = (MainActivity)getActivity();
                showDialog(a, tag.data);

            }
        });
        Log.v(TAG,"onCreateView WatchInventoryFragment ran.......");

        resetAdapter();

        setHasOptionsMenu(true);
        return rootView;
    }

    private void showDialog(MainActivity activity, WatchInventory wi) {

        final String newline = System.getProperty("line.separator");
        final String info =
                "<strong>Address: </strong>" +
                "<span>" + wi.inventory.location.address + "</span>" + "<br />" + "<br />" +
                newline +
                "<strong>Phone: </strong>" +
                //"<a href='tel:'+loc.phonenumber + '">' + formatPhone(loc.phonenumber) + '</a>"
                "<a href='tel:" + wi.inventory.location.phonenumber + "'>" +
                formatPhone(wi.inventory.location.phonenumber) +
                "</a>";

        TextView showText = new TextView(activity);
        showText.setText( Html.fromHtml(info) );
        showText.setTextIsSelectable(true);
        showText.setTextSize(20);
        showText.setPadding(10,10,10,10);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Build the Dialog
        builder.setView(showText)
                .setTitle(wi.inventory.location.label)
                .setCancelable(true)
                .create();

        AlertDialog alert = builder.create();
        alert.show();
    }


    private String formatPhone(String text) {
        Log.v(TAG, "phone length is: " + text.length());
        if( text.length() == 11 ) {
            return text.replaceFirst("(\\d)(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3-$4");
        } else if( text.length() == 10 ) {
            //ph = ph.replace(/(\d{3})(\d{3})(\d{4})/, "$1-$2-$3");
            return text.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");

        }
        Log.v(TAG, "shit didn't take");
        return text;
    }

    private static void resetAdapter() {
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

    private static void updateSort(String type) {
        final String _type = type;
        Collections.sort(cjDataHolder.getWatchInventories(), new Comparator<WatchInventory>() {
            @Override
            public int compare(WatchInventory item1, WatchInventory item2) {
                switch (SortTypes.valueOf(_type)) {
                    case CREATED: {
                        return item2.created.compareTo(item1.created);
                    }
                    case ARRIVED: {
                        return item2.inventory.arrived.compareTo(item1.inventory.arrived);
                    }
                    case DISTANCE: {
                        return -item2.distance.compareTo(item1.distance);
                    }
                    case CARYEAR: {
                        return -item2.inventory.caryear.compareTo(item1.inventory.caryear);
                    }
                    default: {
                        return item2.created.compareTo(item1.created);
                    }
                }

            }
        });
        resetAdapter();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.watchinventory_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
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

}
