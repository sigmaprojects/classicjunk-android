package org.sigmaprojects.ClassicJunk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.sigmaprojects.ClassicJunk.util.apiService;

/**
 * Created by don on 1/2/2015.
 */
public class searchInventoryDialogFragment extends DialogFragment {

    public static final String PREFS_NAME = "searchInventoryDialog";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View inflated = inflater.inflate(R.layout.search_inventory_dialog, null);
        final EditText searchCarField = (EditText) inflated.findViewById(R.id.search_car);
        final EditText searchYearStartField = (EditText) inflated.findViewById(R.id.search_yearstart);
        final EditText searchYearEndField = (EditText) inflated.findViewById(R.id.search_yearend);

        final SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, 0);
        searchCarField.setText(sp.getString("search_car", ""));
        if(sp.getInt("search_yearstart", 0) != 0) {
            try {
                searchYearStartField.setText(Integer.toString(sp.getInt("search_yearstart", 0)));
            } catch(Exception e) {}
        }
        if(sp.getInt("search_yearend", 0) != 0) {
            try {
                searchYearEndField.setText(Integer.toString(sp.getInt("search_yearend", 0)));
            } catch(Exception e) {}
        }

        builder.setView(inflated)
                // Add action buttons
                .setPositiveButton(R.string.search_button_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        /*
                        Dialog f = (Dialog)dialog;
                        EditText searchCarField = (EditText) f.findViewById(R.id.search_car);
                        EditText searchYearStartField = (EditText) f.findViewById(R.id.search_yearstart);
                        EditText searchYearEndField = (EditText) f.findViewById(R.id.search_yearend);
                        */
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("search_car", String.valueOf(searchCarField.getText().toString().trim()) );
                        editor.putInt("search_yearstart", Integer.valueOf(searchYearStartField.getText().toString().trim()) );
                        editor.putInt("search_yearend", Integer.valueOf(searchYearEndField.getText().toString().trim()) );
                        editor.apply();

                        ((MainActivity) getActivity()).apiservice.searchInventory(
                                //(EditText)rootView.findViewById(R.id.txtID)
                                sp.getString("search_car", ""),
                                sp.getInt("search_yearstart", 1950),
                                sp.getInt("search_yearend", 2050)
                        );
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        searchInventoryDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public static searchInventoryDialogFragment newInstance() {
        searchInventoryDialogFragment f = new searchInventoryDialogFragment();
        return f;
    }

}
