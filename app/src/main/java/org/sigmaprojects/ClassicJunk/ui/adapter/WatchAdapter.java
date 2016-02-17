package org.sigmaprojects.ClassicJunk.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.sigmaprojects.ClassicJunk.R;
import org.sigmaprojects.ClassicJunk.api.beans.Watch;

import java.util.ArrayList;

/**
 * Created by don on 6/9/2014.
 */
public class WatchAdapter extends ArrayAdapter<Watch> {

    private ArrayList<Watch> watches;
    private LayoutInflater mInflater;

    public WatchAdapter(Context context, int textViewResourceId, ArrayList<Watch> watches) {
        super(context, textViewResourceId, watches);

        mInflater = LayoutInflater.from(context);

        this.watches = new ArrayList<Watch>();
        this.watches.addAll(watches);
    }



    public class ViewHolder {
        private TextView label;
        private TextView year_start;
        private TextView year_end;
        public Watch data = null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.watch_info, parent, false);

            holder = new ViewHolder();
            assert convertView != null;

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Watch watch = watches.get(position);
        if( watch != null ) {
            holder.year_start = (TextView) convertView.findViewById(R.id.year_start);
            holder.year_end = (TextView) convertView.findViewById(R.id.year_end);
            holder.label = (TextView) convertView.findViewById(R.id.label);

            holder.year_start.setText(""+watch.getYear_start());
            holder.year_end.setText(""+watch.getYear_end());
            holder.label.setText(""+watch.getLabel());

            holder.data = watch;
        }
        convertView.setTag(holder);

        return convertView;

    }

}
