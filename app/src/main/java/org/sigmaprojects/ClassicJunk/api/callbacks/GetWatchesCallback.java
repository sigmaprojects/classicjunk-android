package org.sigmaprojects.ClassicJunk.api.callbacks;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.sigmaprojects.ClassicJunk.App;
import org.sigmaprojects.ClassicJunk.api.beans.Watch;
import org.sigmaprojects.ClassicJunk.api.beans.WatchInventory;
import org.sigmaprojects.ClassicJunk.api.beans.WatchResponse;
import org.sigmaprojects.ClassicJunk.api.interfaces.APICallComplete;
import org.sigmaprojects.ClassicJunk.ui.MainActivity;
import org.sigmaprojects.ClassicJunk.util.CJDataHolder;

import java.util.ArrayList;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by don on 2/15/2016.
 */
public class GetWatchesCallback implements Callback<WatchResponse> {

    private APICallComplete apiCallComplete;
    private CJDataHolder cjDataHolder;
    private final String TAG = GetWatchesCallback.class.getName();

    public GetWatchesCallback(APICallComplete apiCallComplete) {
        this.apiCallComplete = apiCallComplete;
        this.cjDataHolder = CJDataHolder.getInstance();
    }

    @Override
    public void onResponse(Response<WatchResponse> response) {
        WatchResponse b = response.body();
        //Log.v(TAG, "onResponse completed for getWatches" + " code: " + b.getcode() + " status: " + b.getstatus() + " errors: " + b.geterrorsarray());

        ArrayList<Watch> watches = b.getresults();
        ArrayList<WatchInventory> watchInventories = new ArrayList<>();

        for(Watch watch : watches) {
            //Log.v(TAG, "watch_id: " + watch.getId());
            if( watch.getWatchInventories() != null && !watch.getWatchInventories().isEmpty() ) {
                //Log.v(TAG, "adding inventories: " + watch.getWatchInventories().size());
                watchInventories.addAll(watch.getWatchInventories());
            } else {
                //Log.v(TAG, "no watch inventory for watch_id: " + watch.getId());
            }
        }

        cjDataHolder.setWatches(watches);
        cjDataHolder.setWatchInventories(watchInventories);

        apiCallComplete.finished(true);

    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(TAG, "failure", t);
        apiCallComplete.finished(false);
    }
}
