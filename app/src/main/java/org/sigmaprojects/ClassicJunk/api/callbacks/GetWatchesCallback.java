package org.sigmaprojects.ClassicJunk.api.callbacks;

import android.util.Log;

import org.sigmaprojects.ClassicJunk.api.beans.Watch;
import org.sigmaprojects.ClassicJunk.api.beans.WatchInventory;
import org.sigmaprojects.ClassicJunk.api.beans.WatchResponse;
import org.sigmaprojects.ClassicJunk.api.interfaces.APICallComplete;
import org.sigmaprojects.ClassicJunk.util.CJDataHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
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
    public void onResponse(Call<WatchResponse> call, Response<WatchResponse> response) {
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

        Collections.sort(watchInventories, new Comparator<WatchInventory>() {
            @Override
            public int compare(WatchInventory item1, WatchInventory item2) {
                return item2.getId().compareTo(item1.getId());
            }
        });

        cjDataHolder.setWatches(watches);
        cjDataHolder.setWatchInventories(watchInventories);

        apiCallComplete.finished(true);

    }

    @Override
    public void onFailure(Call<WatchResponse> call, Throwable t) {
        Log.e(TAG, "failure", t);
        apiCallComplete.finished(false);
    }
}
