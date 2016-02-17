package org.sigmaprojects.ClassicJunk.api.callbacks;

import android.util.Log;

import org.sigmaprojects.ClassicJunk.api.ClassicJunkService;
import org.sigmaprojects.ClassicJunk.api.beans.WatchResponse;
import org.sigmaprojects.ClassicJunk.api.interfaces.APICallComplete;
import org.sigmaprojects.ClassicJunk.util.CJDataHolder;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by don on 2/15/2016.
 */
public class DeleteWatchCallback implements Callback<WatchResponse> {

    private APICallComplete apiCallComplete;
    private CJDataHolder cjDataHolder;
    private final String TAG = DeleteWatchCallback.class.getName();

    public DeleteWatchCallback(APICallComplete apiCallComplete) {
        this.apiCallComplete = apiCallComplete;
        this.cjDataHolder = CJDataHolder.getInstance();
    }

    @Override
    public void onResponse(Response<WatchResponse> response) {
        //WatchResponse b = response.body();
        //Log.v(TAG, "onResponse completed for save watch" + " code: " + b.getCode() + " status: " + b.getStatus() + " errors: " + b.getErrorsarray());
        /*
        Log.v(TAG, "raw: " + response.raw().toString() );
        Log.v(TAG, "raw 2: " + response.raw().message());
        Log.v(TAG, "onResponse completed for save watch" + " code: " + b.getcode());
        Log.v(TAG, " status: " + b.getstatus() );
        Log.v(TAG, " errors: " + b.geterrorsarray());
        */

        ClassicJunkService.getInstance().download(new APICallComplete() {
            @Override
            public void finished(Boolean success) {}
        });

        apiCallComplete.finished(true);

    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(TAG, "failure", t);
        apiCallComplete.finished(false);
    }
}
