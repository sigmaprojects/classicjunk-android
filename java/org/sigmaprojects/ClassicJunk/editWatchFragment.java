package org.sigmaprojects.ClassicJunk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.sigmaprojects.ClassicJunk.beans.Watch;

import java.lang.reflect.Field;

public class editWatchFragment extends Fragment {

    static final String TAG = "ClassicJunk";
    private static View rootView;

    private EditText txtID = null;
    private EditText txtLabel = null;
    private EditText txtYearStart = null;
    private EditText txtYearEnd = null;
    private EditText txtZipCode = null;

    public static CJDataHolder cjDataHolder = CJDataHolder.getInstance();

    public static editWatchFragment newInstance() {
        editWatchFragment fragment = new editWatchFragment();
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    public editWatchFragment() {
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
            rootView = inflater.inflate(R.layout.editwatch_frag, container, false);

            txtID = (EditText)rootView.findViewById(R.id.txtID);
            txtLabel = (EditText)rootView.findViewById(R.id.txtLabel);
            txtYearStart = (EditText)rootView.findViewById(R.id.txtYearStart);
            txtYearEnd = (EditText)rootView.findViewById(R.id.txtYearEnd);
            txtZipCode = (EditText)rootView.findViewById(R.id.txtZipCode);
        } catch (InflateException e) {
            Log.e(TAG,"InflateException: " + e.getMessage());
        }

        Button submitButton = (Button) rootView.findViewById(R.id.btnSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { save(v); }
        });

        Button cancelButton = (Button) rootView.findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { resetForm(); }
        });

        Button deleteButton = (Button) rootView.findViewById(R.id.btnDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View _v = v;
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to delete this alert?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                delete(_v);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        if( cjDataHolder.hasEditWatch() ) {
            resetForm(cjDataHolder.getEditWatch());
        } else {
            resetForm();
        }
        if( MainActivity.hasLocation() ) {
            hideZipcode();
        }

        return rootView;
	}

    private void delete(View v) {
        Watch w = new Watch();
        try {
            w.id = Integer.valueOf(txtID.getText().toString().trim());
        } catch(Exception e) {
            w.id = 0;
        }
        ((MainActivity)getActivity()).deleteWatch(w);
        Log.v(TAG, "delete was clicked....");
    }

    public void save(View v) {
        Watch w = new Watch();
        w.lat = MainActivity.lat;
        w.lng = MainActivity.lng;

        try {
            w.id = Integer.valueOf(txtID.getText().toString().trim());
        } catch(Exception e) {
            w.id = 0;
        }

        try {
            w.zipcode = Integer.valueOf(txtZipCode.getText().toString().trim());
        } catch(Exception e) {}

        try {
            w.year_start = Integer.valueOf(txtYearStart.getText().toString().trim());
        } catch(Exception e) {}

        try {
            w.year_end = Integer.valueOf(txtYearEnd.getText().toString().trim());
        } catch(Exception e) {}

        try {
            w.label = String.valueOf(txtLabel.getText());
        } catch(Exception e) {}

        ((MainActivity)getActivity()).saveWatch(w);
    }

    public static void resetForm() {
        cjDataHolder.setEditWatch();
        EditText id = (EditText)rootView.findViewById(R.id.txtID);
        EditText label = (EditText)rootView.findViewById(R.id.txtLabel);
        EditText zip = (EditText)rootView.findViewById(R.id.txtZipCode);
        EditText start = (EditText)rootView.findViewById(R.id.txtYearStart);
        EditText end = (EditText)rootView.findViewById(R.id.txtYearEnd);
        id.setText("0");
        label.setText("");
        zip.setText("");
        start.setText("");
        end.setText("");

        Button delButton = (Button)rootView.findViewById(R.id.btnDelete);
        delButton.setVisibility(LinearLayout.GONE);
    }

    public static void resetForm(Watch w) {
        EditText id = (EditText)rootView.findViewById(R.id.txtID);
        EditText label = (EditText)rootView.findViewById(R.id.txtLabel);
        EditText zip = (EditText)rootView.findViewById(R.id.txtZipCode);
        EditText start = (EditText)rootView.findViewById(R.id.txtYearStart);
        EditText end = (EditText)rootView.findViewById(R.id.txtYearEnd);
        id.setText(String.valueOf(w.id));
        label.setText(String.valueOf(w.label));
        zip.setText(String.valueOf(w.zipcode));
        start.setText(String.valueOf(w.year_start));
        end.setText(String.valueOf(w.year_end));

        Button delButton = (Button)rootView.findViewById(R.id.btnDelete);
        delButton.setVisibility(LinearLayout.VISIBLE);
    }

    private void hideZipcode() {
        LinearLayout zipLL = (LinearLayout)rootView.findViewById(R.id.watchlocationLayout);
        zipLL.setVisibility(LinearLayout.GONE);
        Log.v(TAG, "Hiding ZipCode container layout.");
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
