package org.sigmaprojects.ClassicJunk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.sigmaprojects.ClassicJunk.R;
import org.sigmaprojects.ClassicJunk.beans.Watch;

import java.util.ArrayList;

/**
 * Created by don on 6/9/2014.
 */
public class WatchAdapter extends ArrayAdapter<Watch> {

    private ArrayList<Watch> watches;
    private LayoutInflater mInflater;

    public WatchAdapter(Context context, int textViewResourceId,
                           ArrayList<Watch> watches) {
        super(context, textViewResourceId, watches);

        mInflater = LayoutInflater.from(context);

        this.watches = new ArrayList<Watch>();
        this.watches.addAll(watches);
    }



    public class ViewHolder {
        private final TextView label;
        private final TextView year_start;
        private final TextView year_end;
        public Watch data = null;

        public ViewHolder(Watch watch, View convertView) {
            this.year_start = (TextView) convertView.findViewById(R.id.year_start);
            this.year_end = (TextView) convertView.findViewById(R.id.year_end);
            this.label = (TextView) convertView.findViewById(R.id.label);

            this.year_start.setText(""+watch.year_start.toString());
            this.year_end.setText(""+watch.year_end.toString());
            this.label.setText(watch.label);

            this.data = watch;

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.watch_info, null);

            holder = new ViewHolder(watches.get(position), convertView);
            assert convertView != null;

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;

    }

}
