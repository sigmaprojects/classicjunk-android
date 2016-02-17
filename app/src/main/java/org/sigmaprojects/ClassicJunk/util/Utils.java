package org.sigmaprojects.ClassicJunk.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import org.sigmaprojects.ClassicJunk.R;

import java.util.ArrayList;

/**
 * Created by don on 2/16/2016.
 */
public class Utils {

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        // dialog.setMessage(Message);
        return dialog;
    }

    public static AlertDialog getErrorsDialog(Context context, ArrayList<String> errors) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error");

        StringBuffer text = new StringBuffer();
        for (String error: errors) {
            text.append(error).append('\n');
        }
        builder.setMessage(text);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

}
