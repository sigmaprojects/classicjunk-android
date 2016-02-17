package org.sigmaprojects.ClassicJunk.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.sigmaprojects.ClassicJunk.R;
import org.sigmaprojects.ClassicJunk.api.ClassicJunkService;
import org.sigmaprojects.ClassicJunk.api.beans.Watch;
import org.sigmaprojects.ClassicJunk.api.interfaces.APICallComplete;
import org.sigmaprojects.ClassicJunk.util.CJDataHolder;
import org.sigmaprojects.ClassicJunk.util.Utils;

import java.lang.reflect.Field;

public class EditWatchFragment extends Fragment {

    private View rootView;

    private EditText txtID;
    private EditText txtLabel;
    private EditText txtYearStart;
    private EditText txtYearEnd;
    private EditText txtZipCode;

    public static CJDataHolder cjDataHolder;

    private final String TAG = WatchListFragment.class.getName();
    private static final String ARG_SECTION_NUMBER = "section_number";



    public static EditWatchFragment newInstance(int sectionNumber) {
        EditWatchFragment fragment = new EditWatchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        cjDataHolder = CJDataHolder.getInstance();

        return fragment;
    }

    public EditWatchFragment() {
    }



    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
        if( cjDataHolder.hasLocation() ) {
            hideZipcode();
        } else {
            showZipcode();
        }

        return rootView;
	}

    private void delete(View v) {
        Log.v(TAG, "delete was clicked....");
        Watch w = new Watch();
        try {
            w.setId(Integer.valueOf(txtID.getText().toString().trim()));
        } catch(Exception e) {
            w.setId( 0 );
        }

        final ProgressDialog progressDialog = Utils.createProgressDialog(getActivity());

        ClassicJunkService.getInstance().deleteWatch(
                new DeleteFinished(progressDialog),
                w.getId()
        );
    }

    private class DeleteFinished implements APICallComplete {
        ProgressDialog progressDialog;
        public DeleteFinished(ProgressDialog progressDialog) {this.progressDialog = progressDialog;}
        @Override
        public void finished(Boolean success) {
            resetForm();
            ClassicJunkService.getInstance().download(new APICallComplete() {
                @Override
                public void finished(Boolean success) {
                    try {
                        progressDialog.dismiss();
                    } catch (Exception e) {}
                    Intent intent = new Intent(MainActivity.FINISHED_SYNCING);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                }
            });
        }
    }

    public void save(View v) {
        Watch w = new Watch();
        w.setLat( cjDataHolder.getLat() );
        w.setLng( cjDataHolder.getLng() );

        try {
            w.setId(Integer.valueOf(txtID.getText().toString().trim()));
        } catch(Exception e) {
            w.setId(0);
        }

        try {
            w.setZipCode( Integer.valueOf(txtZipCode.getText().toString().trim()) );
        } catch(Exception e) {}

        try {
            w.setYear_start( Integer.valueOf(txtYearStart.getText().toString().trim()) );
        } catch(Exception e) {}

        try {
            w.setYear_end( Integer.valueOf(txtYearEnd.getText().toString().trim()) );
        } catch(Exception e) {}

        try {
            w.setLabel(String.valueOf(txtLabel.getText()));
        } catch(Exception e) {}

        final ProgressDialog progressDialog = Utils.createProgressDialog(getActivity());

        ClassicJunkService.getInstance().saveWatch(
                new SaveFinished(progressDialog),
                w
        );
    }

    private class SaveFinished implements APICallComplete {
        ProgressDialog progressDialog;
        public SaveFinished(ProgressDialog progressDialog) {this.progressDialog = progressDialog;}
        @Override
        public void finished(Boolean success) {
            if (success) {
                resetForm();
                ClassicJunkService.getInstance().download(new APICallComplete() {
                    @Override
                    public void finished(Boolean success) {
                        try {
                            progressDialog.dismiss();
                        } catch (Exception e) {}

                        Intent intent = new Intent(MainActivity.FINISHED_SYNCING);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    }
                });
            } else {
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {}
                AlertDialog alertDialog = Utils.getErrorsDialog(getActivity(), cjDataHolder.getLastErrors());
                cjDataHolder.resetLastErrors();
                alertDialog.show();
            }
        }
    }


    public void resetForm() {
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

    public void resetForm(Watch w) {
        EditText id = (EditText)rootView.findViewById(R.id.txtID);
        EditText label = (EditText)rootView.findViewById(R.id.txtLabel);
        EditText zip = (EditText)rootView.findViewById(R.id.txtZipCode);
        EditText start = (EditText)rootView.findViewById(R.id.txtYearStart);
        EditText end = (EditText)rootView.findViewById(R.id.txtYearEnd);
        id.setText(String.valueOf(w.getId()));
        label.setText(String.valueOf(w.getLabel()));
        zip.setText(String.valueOf(w.getZipCode()));
        start.setText(String.valueOf(w.getYear_start()));
        end.setText(String.valueOf(w.getYear_end()));

        Button delButton = (Button)rootView.findViewById(R.id.btnDelete);
        delButton.setVisibility(LinearLayout.VISIBLE);
    }

    private void hideZipcode() {
        try {
            LinearLayout zipLL = (LinearLayout) rootView.findViewById(R.id.watchlocationLayout);
            zipLL.setVisibility(LinearLayout.GONE);
        } catch (Exception e) {}
        Log.v(TAG, "Hiding ZipCode container layout.");
    }
    private void showZipcode() {
        try {
            LinearLayout zipLL = (LinearLayout) rootView.findViewById(R.id.watchlocationLayout);
            zipLL.setVisibility(LinearLayout.VISIBLE);
        } catch (Exception e) {}
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
