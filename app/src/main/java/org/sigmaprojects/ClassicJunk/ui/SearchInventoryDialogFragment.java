package org.sigmaprojects.ClassicJunk.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.sigmaprojects.ClassicJunk.App;
import org.sigmaprojects.ClassicJunk.R;
import org.sigmaprojects.ClassicJunk.api.ClassicJunkService;
import org.sigmaprojects.ClassicJunk.api.interfaces.APICallComplete;
import org.sigmaprojects.ClassicJunk.util.Utils;

import java.lang.reflect.Field;

//import android.app.Dialog;
//import android.app.DialogFragment;


/**
 * Created by don on 1/2/2015.
 */
public class SearchInventoryDialogFragment extends DialogFragment {

    public static final String PREFS_NAME = "searchInventoryDialog";

    private ProgressDialog progressDialog;

    private static final String ARG_SECTION_NUMBER = "section_number";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflated = inflater.inflate(R.layout.search_inventory_dialog, null);

        final EditText searchCarField = (EditText) inflated.findViewById(R.id.search_car);
        final EditText searchYearStartField = (EditText) inflated.findViewById(R.id.search_yearstart);
        final EditText searchYearEndField = (EditText) inflated.findViewById(R.id.search_yearend);

        final SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, 0);

        try {
            searchCarField.setText(sp.getString("search_car", ""));
            searchYearStartField.setText(Integer.toString(sp.getInt("search_yearstart", 0)));
            searchYearEndField.setText(Integer.toString(sp.getInt("search_yearend", 0)));
        } catch (Exception e) {}

        builder.setView(inflated)
                // Add action buttons
                .setPositiveButton(R.string.search_button_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("search_car", String.valueOf(searchCarField.getText().toString().trim()));
                        editor.putInt("search_yearstart", Integer.valueOf(searchYearStartField.getText().toString().trim()));
                        editor.putInt("search_yearend", Integer.valueOf(searchYearEndField.getText().toString().trim()));
                        editor.apply();

                        progressDialog = Utils.createProgressDialog(getActivity());

                        ClassicJunkService.getInstance().search(
                                new SearchFinished(),
                                sp.getString("search_car", ""),
                                sp.getInt("search_yearstart", 1950),
                                sp.getInt("search_yearend", 2050)
                        );
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SearchInventoryDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public static SearchInventoryDialogFragment newInstance(int sectionNumber) {
        SearchInventoryDialogFragment f = new SearchInventoryDialogFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        f.setArguments(args);

        return f;
    }

    public class SearchFinished implements APICallComplete {
        @Override
        public void finished(Boolean success) {
            try {
                progressDialog.dismiss();
            }catch (Exception e) {}
            Intent intent = new Intent(MainActivity.SHOW_SEARCH_RESULTS);
            LocalBroadcastManager.getInstance(App.getAppContext()).sendBroadcast(intent);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
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
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#f37305"));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#f37305"));

        try {

        } catch (Exception e) {

        }
    }

}
