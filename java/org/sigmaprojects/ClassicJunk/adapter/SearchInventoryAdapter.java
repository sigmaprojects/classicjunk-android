package org.sigmaprojects.ClassicJunk.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.sigmaprojects.ClassicJunk.CJDataHolder;
import org.sigmaprojects.ClassicJunk.R;
import org.sigmaprojects.ClassicJunk.beans.Inventory;
import org.sigmaprojects.ClassicJunk.beans.WatchInventory;

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
        /*
        this.inventories = new ArrayList<Inventory>();
        this.inventories.addAll(inventories);
        */
        //this.inventories = cjDataHolder.getSearchInventories();
        this.searchinventories = new ArrayList<Inventory>();
        this.searchinventories.addAll(searchinventories);

    }


    public class ViewHolder {
        public TextView caryear;
        public TextView car;
        public TextView locationlabel;
        public TextView arrived;
        public Inventory data;
        /*
        public ViewHolder(Inventory inventory, View convertView) {
            this.caryear = (TextView) convertView.findViewById(R.id.caryear);
            this.car = (TextView) convertView.findViewById(R.id.car);
            this.locationlabel = (TextView) convertView.findViewById(R.id.locationlabel);
            this.arrived = (TextView) convertView.findViewById(R.id.arrived);

            this.caryear.setText(""+inventory.caryear.toString());
            this.car.setText(""+inventory.car);
            this.locationlabel.setText(""+inventory.location.label);
            this.arrived.setText(""+inventory.timeago.toString());

            this.data = inventory;
        }
        */
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.watchinventory_info, parent, false);

            //holder = new ViewHolder(searchinventories.get(position), convertView);
            holder = new ViewHolder();
            assert convertView != null;

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Inventory inventory = searchinventories.get(position);
        if( inventory != null ) {
            holder.caryear = (TextView) convertView.findViewById(R.id.caryear);
            holder.car = (TextView) convertView.findViewById(R.id.car);
            holder.locationlabel = (TextView) convertView.findViewById(R.id.locationlabel);
            holder.arrived = (TextView) convertView.findViewById(R.id.arrived);

            holder.caryear.setText("" + inventory.caryear.toString());
            holder.car.setText("" + inventory.car);
            holder.locationlabel.setText("" + inventory.location.label);
            holder.arrived.setText("" + inventory.timeago.toString());

            holder.data = inventory;
        }
        convertView.setTag(holder);

        return convertView;

    }

}
