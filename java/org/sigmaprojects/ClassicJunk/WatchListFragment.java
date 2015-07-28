package org.sigmaprojects.ClassicJunk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.sigmaprojects.ClassicJunk.adapter.WatchAdapter;
import org.sigmaprojects.ClassicJunk.beans.Watch;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class WatchListFragment extends Fragment {

    static final String TAG = "ClassicJunk";
    private static View rootView;

    ArrayList<Watch> watches;

    public static CJDataHolder cjDataHolder = CJDataHolder.getInstance();

    public static WatchListFragment newInstance() {
        WatchListFragment fragment = new WatchListFragment();
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    public WatchListFragment() {
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
            rootView = inflater.inflate(R.layout.fragment_watch_list, container, false);
            final ListView lv = (ListView) rootView.findViewById(R.id.watchListView);
            // React to user clicks on item
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                    // We know the View is a TextView so we can cast it
                    View clickedView = view;
                    //Toast.makeText(getActivity(), "Item with id [" + id + "] - Position [" + position + "] - Planet [" + clickedView.getText() + "]", Toast.LENGTH_SHORT).show();

                    WatchAdapter.ViewHolder tag =(WatchAdapter.ViewHolder)clickedView.getTag();

                    MainActivity a = (MainActivity)getActivity();
                    //a.resetEditWatchFrag( tag.data );
                    cjDataHolder.setEditWatch(tag.data);
                    a.onNavigationDrawerItemSelected(0);

                }
            });

        } catch (InflateException e) {
            Log.v(TAG,e.getMessage());
        }

        Log.v(TAG,"onCreateView WatchListFragment ran.......");

        ListView lv = (ListView) rootView.findViewById(R.id.watchListView);
        WatchAdapter watchAdapter = new WatchAdapter(rootView.getContext(), R.layout.watch_info, cjDataHolder.getWatches());
        lv.setAdapter(watchAdapter);

        // hide the No Watches Found message
        if( cjDataHolder.hasWatches() ) {
            TextView noWatches = (TextView) rootView.findViewById(R.id.no_watches);
            noWatches.setVisibility(TextView.GONE);
        }

		return rootView;
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


}
