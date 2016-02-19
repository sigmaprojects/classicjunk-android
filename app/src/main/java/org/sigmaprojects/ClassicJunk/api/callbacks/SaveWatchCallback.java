package org.sigmaprojects.ClassicJunk.api.callbacks;

import android.util.Log;

import org.sigmaprojects.ClassicJunk.api.beans.WatchResponse;
import org.sigmaprojects.ClassicJunk.api.interfaces.APICallComplete;
import org.sigmaprojects.ClassicJunk.util.CJDataHolder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by don on 2/15/2016.
 */
public class SaveWatchCallback implements Callback<WatchResponse> {

    private APICallComplete apiCallComplete;
    private CJDataHolder cjDataHolder;
    private final String TAG = SaveWatchCallback.class.getName();

    public SaveWatchCallback(APICallComplete apiCallComplete) {
        this.apiCallComplete = apiCallComplete;
        this.cjDataHolder = CJDataHolder.getInstance();
    }

    @Override
    public void onResponse(Call<WatchResponse> call, Response<WatchResponse> response) {
        WatchResponse b = response.body();
        //Log.v(TAG, "onResponse completed for save watch" + " code: " + b.getCode() + " status: " + b.getStatus() + " errors: " + b.getErrorsarray());
        /*
        Log.v(TAG, "raw: " + response.raw().toString() );
        Log.v(TAG, "raw 2: " + response.raw().message());
        Log.v(TAG, "onResponse completed for save watch" + " code: " + b.getcode());
        Log.v(TAG, " status: " + b.getstatus() );
        Log.v(TAG, " errors: " + b.geterrorsarray());
        */

        cjDataHolder.resetLastErrors();

        try {
            if (b.geterrorsarray().isEmpty()) {
/*
                ClassicJunkService.getInstance().download(new APICallComplete() {
                    @Override
                    public void finished(Boolean success) {

                    }
                });
*/
                apiCallComplete.finished(true);
            } else {
                cjDataHolder.setLastErrors(b.geterrorsarray());
                apiCallComplete.finished(false);
            }
        } catch (Exception e)  {
            apiCallComplete.finished(false);
        }


        /*
        ArrayList<Watch> watches = b.getResults();
        ArrayList<WatchInventory> watchInventories = new ArrayList<>();

        for(Watch watch : watches) {
            watchInventories.addAll(watch.getWatchInventories());
        }

        cjDataHolder.setWatches(watches);
        cjDataHolder.setWatchInventories(watchInventories);
        */



    }

    @Override
    public void onFailure(Call<WatchResponse> call, Throwable t) {
        Log.e(TAG, "failure", t);
        apiCallComplete.finished(false);
    }
}
