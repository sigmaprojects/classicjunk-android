package org.sigmaprojects.ClassicJunk.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.sigmaprojects.ClassicJunk.R;
import org.sigmaprojects.ClassicJunk.api.beans.WatchInventory;

import java.util.ArrayList;

/**
 * Created by don on 6/9/2014.
 */
public class WatchInventoryAdapter extends ArrayAdapter<WatchInventory> {

    private ArrayList<WatchInventory> watchinventories;
    private LayoutInflater mInflater;

    private int highestSeenWatchInventoryId;

    public WatchInventoryAdapter(
            Context context,
            int textViewResourceId,
            ArrayList<WatchInventory> watchinventories,
            int highestSeenWatchInventoryId
    ) {
        super(context, textViewResourceId, watchinventories);

        mInflater = LayoutInflater.from(context);

        this.highestSeenWatchInventoryId = highestSeenWatchInventoryId;

        this.watchinventories = new ArrayList<WatchInventory>();
        this.watchinventories.addAll(watchinventories);
    }


    public class ViewHolder {
        public TextView caryear;
        public TextView car;
        public TextView locationlabel;
        public TextView arrived;
        public WatchInventory data = null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.watchinventory_info, parent, false);

            holder = new ViewHolder();
            assert convertView != null;

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WatchInventory wi = watchinventories.get(position);
        if( wi != null ) {
            try {
                holder.caryear = (TextView) convertView.findViewById(R.id.caryear);
            } catch (Exception e) {}
            try {
                holder.car = (TextView) convertView.findViewById(R.id.car);
            } catch (Exception e) {}
            try {
                holder.locationlabel = (TextView) convertView.findViewById(R.id.locationlabel);
            } catch (Exception e) {}
            try {
                holder.arrived = (TextView) convertView.findViewById(R.id.arrived);
                if( wi.getId() > highestSeenWatchInventoryId ) {
                    holder.arrived.setTextColor(Color.parseColor("#4CBB17"));
                }
            } catch (Exception e) {}
            try {
                holder.caryear.setText(""+wi.getInventory().getCaryear());
            } catch (Exception e) {}
            try {
                holder.car.setText(""+wi.getInventory().getCar());
            } catch (Exception e) {}
            try {
                holder.locationlabel.setText(""+wi.getInventory().getLocation().getLabel());
            } catch (Exception e) {}
            try {
                holder.arrived.setText(""+wi.getInventory().getTimeago());
            } catch (Exception e) {}

            holder.data = wi;
        }
        convertView.setTag(holder);

        return convertView;

    }

}
