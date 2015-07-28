package org.sigmaprojects.ClassicJunk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.sigmaprojects.ClassicJunk.R;
import org.sigmaprojects.ClassicJunk.beans.WatchInventory;

import java.util.ArrayList;

/**
 * Created by don on 6/9/2014.
 */
public class WatchInventoryAdapter extends ArrayAdapter<WatchInventory> {

    private ArrayList<WatchInventory> watchinventories;
    private LayoutInflater mInflater;

    public WatchInventoryAdapter(Context context, int textViewResourceId,
                                 ArrayList<WatchInventory> watchinventories) {
        super(context, textViewResourceId, watchinventories);

        mInflater = LayoutInflater.from(context);

        this.watchinventories = new ArrayList<WatchInventory>();
        this.watchinventories.addAll(watchinventories);
    }


    public class ViewHolder {
        public TextView caryear;
        public TextView car;
        public TextView locationlabel;
        public TextView arrived;
        public WatchInventory data = null;
        /*

        public ViewHolder(WatchInventory watchinventory, View convertView) {
            this.caryear = (TextView) convertView.findViewById(R.id.caryear);
            this.car = (TextView) convertView.findViewById(R.id.car);
            this.locationlabel = (TextView) convertView.findViewById(R.id.locationlabel);
            this.arrived = (TextView) convertView.findViewById(R.id.arrived);

            this.caryear.setText(""+watchinventory.inventory.caryear.toString());
            this.car.setText(""+watchinventory.inventory.car);
            this.locationlabel.setText(""+watchinventory.inventory.location.label);
            this.arrived.setText(""+watchinventory.inventory.timeago.toString());

            this.data = watchinventory;
        }
        */
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
            holder.caryear = (TextView) convertView.findViewById(R.id.caryear);
            holder.car = (TextView) convertView.findViewById(R.id.car);
            holder.locationlabel = (TextView) convertView.findViewById(R.id.locationlabel);
            holder.arrived = (TextView) convertView.findViewById(R.id.arrived);

            holder.caryear.setText("" + wi.inventory.caryear.toString());
            holder.car.setText("" + wi.inventory.car);
            holder.locationlabel.setText("" + wi.inventory.location.label);
            holder.arrived.setText("" + wi.inventory.timeago.toString());

            holder.data = wi;
        }
        convertView.setTag(holder);

        return convertView;

    }

}
