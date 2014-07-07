package org.sigmaprojects.ClassicJunk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;

import org.sigmaprojects.ClassicJunk.beans.Watch;
import org.sigmaprojects.ClassicJunk.beans.WatchInventory;
import org.sigmaprojects.ClassicJunk.beans.WatchReponse;
import org.sigmaprojects.ClassicJunk.util.CLJSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by don on 6/11/2014.
 */
public class apiService {

    static final String TAG = "ClassicJunk";
    private String regid = "";
    private MainActivity activity;

    public apiService(MainActivity a, String registrationId) {
        regid = registrationId;
        activity = a;
    }



    public void setRegid(String _regid) { regid = _regid; }
    public String getRegid() {
        //return "APA91bGq-nqQd8zR0kB5Oj_1q-IO4386VBjjL_uTEgOwKdnXcjk9LnRduYe1JunOpoSSkIoeUILMsqncK1LauA_YKqL1OZglEdqKBQDLYv22z6rgTJkd1yh95l9-8rTTQPL4KfAaxtZ4IBG_SyZsp591Up-DZUGlYA";
        return regid;
    }






    public void download() {
        BackgroundDownloadTask task = new BackgroundDownloadTask(activity);
        task.execute();
    }
    public void download(ViewPager viewPager) {
        BackgroundDownloadTask task = new BackgroundDownloadTask(activity,viewPager);
        task.execute();
    }

    public void saveWatch(Watch w) {
        w.registration_id = getRegid();
        BackgroundSaveWatchTask task = new BackgroundSaveWatchTask(w,activity);
        task.execute();
    }

    public void deleteWatch(Watch w) {
        w.registration_id = getRegid();
        BackgroundDeleteWatchTask task = new BackgroundDeleteWatchTask(w,activity);
        task.execute();
    }



    private class BackgroundDeleteWatchTask extends AsyncTask <Void, Void, Void> {
        private ProgressDialog progressDialog;
        private Watch watch;
        private MainActivity activity;
        private AlertDialog.Builder alertDialog;
        private boolean syncError = false;

        public BackgroundDeleteWatchTask(Watch w, MainActivity a) {
            activity = a;
            watch = w;
            progressDialog = new ProgressDialog(a);
            alertDialog = new AlertDialog.Builder( a )
                    .setTitle("Error Syncing")
                    .setMessage("Could not delete alert.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) { dialog.dismiss(); }
                    });
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Deleting...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if( syncError == true ) {
                alertDialog.show();
            } else {
                download();
                activity.resetEditWatchFrag();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                CLJSON.deleteWatch(watch, getRegid());
            } catch(Exception e) {
                Log.e(TAG,"Delete error",e);
                syncError = true;
            }
            return null;
        }
    }


    private class BackgroundSaveWatchTask extends AsyncTask <Void, Void, List> {
        private ProgressDialog progressDialog;
        private AlertDialog.Builder builder;
        private Watch watch;
        private MainActivity activity;
        private AlertDialog.Builder alertDialog;
        private boolean syncError = false;

        public BackgroundSaveWatchTask(Watch w, MainActivity a) {
            activity = a;
            watch = w;
            progressDialog = new ProgressDialog(a);
            builder = new AlertDialog.Builder(activity);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int id) {
                    d.dismiss();
                }
            });
            alertDialog = new AlertDialog.Builder( a )
                    .setTitle("Error Syncing")
                    .setMessage("Could not save alert.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) { dialog.dismiss(); }
                    });
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Saving...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(List result) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if( syncError == true ) {
                alertDialog.show();
            } else {
                if (result.size() > 0) {
                    String msg = TextUtils.join("\n", result);
                    builder.setMessage(msg).setTitle("Validation errors.");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    download();
                    activity.resetEditWatchFrag();
                }
            }
        }

        @Override
        protected List doInBackground(Void... params) {
            try {
                WatchReponse response = CLJSON.saveWatch(watch, getRegid());
                return response.errorsarray;
            } catch(Exception e) {
                Log.e(TAG,"Save error",e);
                syncError = true;
                return Arrays.asList("");
            }
        }
    }



    private class BackgroundDownloadTask extends AsyncTask <Void, Void, Void> {
        private ProgressDialog progressDialog;
        private ArrayList<WatchInventory> watchinventories = new ArrayList<WatchInventory>();
        private ArrayList<Watch> watches = new ArrayList<Watch>();
        private boolean syncError = false;
        private AlertDialog.Builder alertDialog;

        public BackgroundDownloadTask(MainActivity activity) {
            progressDialog = new ProgressDialog( activity );
            alertDialog = new AlertDialog.Builder( activity )
                .setTitle("Error Syncing")
                .setMessage("Could not sync alerts")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) { dialog.dismiss(); }
                    });
        }

        public BackgroundDownloadTask(MainActivity activity, ViewPager viewPager) {
            viewPager.setCurrentItem(2);
            progressDialog = new ProgressDialog( viewPager.getContext() );
        }


        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if( syncError == true ) {
                alertDialog.show();
            } else {
                MainActivity.updateWatchInventories(watchinventories);
                MainActivity.updateWatches(watches);
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                watches = CLJSON.getWatches(getRegid());
                Log.v(TAG, watches.toString());
                watchinventories = new ArrayList<WatchInventory>();

                for (Watch watch : watches) {
                    watchinventories.addAll(watch.watchinventories);
                }
            } catch(Exception e) {
                Log.e(TAG,"Download error",e);
                syncError = true;
            }
            return null;
        }
    }




}
