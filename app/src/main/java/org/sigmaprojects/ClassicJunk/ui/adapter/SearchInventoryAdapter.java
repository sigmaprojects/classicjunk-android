package org.sigmaprojects.ClassicJunk.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.sigmaprojects.ClassicJunk.R;
import org.sigmaprojects.ClassicJunk.api.beans.Inventory;
import org.sigmaprojects.ClassicJunk.util.CJDataHolder;

import java.util.ArrayList;

/**
 * Created by don on 6/9/2014.
 */
public class SearchInventoryAdapter extends ArrayAdapter<Inventory> {

    private ArrayList<Inventory> searchinventories;
    private LayoutInflater mInflater;

    public static CJDataHolder cjDataHolder = CJDataHolder.getInstance();

    public SearchInventoryAdapter(Context context, int textViewResourceId, ArrayList<Inventory> searchinventories) {
        super(context, textViewResourceId, searchinventories);

        mInflater = LayoutInflater.from(context);
        this.searchinventories = new ArrayList<Inventory>();
        this.searchinventories.addAll(searchinventories);

    }


    public class ViewHolder {
        public TextView caryear;
        public TextView car;
        public TextView locationlabel;
        public TextView arrived;
        public Inventory data;
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

        Inventory inventory = searchinventories.get(position);
        if( inventory != null && inventory.getLocation() != null ) {
            holder.caryear = (TextView) convertView.findViewById(R.id.caryear);
            holder.car = (TextView) convertView.findViewById(R.id.car);
            holder.locationlabel = (TextView) convertView.findViewById(R.id.locationlabel);
            holder.arrived = (TextView) convertView.findViewById(R.id.arrived);

            holder.caryear.setText("" + inventory.getCaryear());
            holder.car.setText("" + inventory.getCar());
            holder.locationlabel.setText(inventory.getLocation().getLabel());
            holder.arrived.setText(inventory.getTimeago());

            holder.data = inventory;
        }
        convertView.setTag(holder);

        return convertView;

    }

}
