package org.sigmaprojects.ClassicJunk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.sigmaprojects.ClassicJunk.adapter.WatchAdapter;
import org.sigmaprojects.ClassicJunk.beans.Watch;

import java.util.ArrayList;

public class WatchListFragment extends Fragment {

    static final String TAG = "ClassicJunk";
    private static View rootView;

    ArrayList<Watch> watches;


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
                    RelativeLayout clickedView = (RelativeLayout) view;
                    //Toast.makeText(getActivity(), "Item with id [" + id + "] - Position [" + position + "] - Planet [" + clickedView.getText() + "]", Toast.LENGTH_SHORT).show();

                    WatchAdapter.ViewHolder tag =(WatchAdapter.ViewHolder)clickedView.getTag();

                    MainActivity a = (MainActivity)getActivity();
                    a.resetEditWatchFrag( tag.data );

                }
            });

        } catch (InflateException e) {
        }

		return rootView;
	}

    public static void updateListView(ArrayList<Watch> watches) {
        ListView lv = (ListView) rootView.findViewById(R.id.watchListView);
        WatchAdapter watchAdapter = new WatchAdapter(rootView.getContext(), R.layout.watch_info, watches);
        lv.setAdapter(watchAdapter);
    }



}
