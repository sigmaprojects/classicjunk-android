package org.sigmaprojects.ClassicJunk.api.callbacks;

import android.util.Log;

import org.sigmaprojects.ClassicJunk.api.beans.Inventory;
import org.sigmaprojects.ClassicJunk.api.beans.InventoryResponse;
import org.sigmaprojects.ClassicJunk.api.interfaces.APICallComplete;
import org.sigmaprojects.ClassicJunk.util.CJDataHolder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by don on 2/15/2016.
 */
public class SearchInventoryCallback implements Callback<InventoryResponse> {

    private APICallComplete apiCallComplete;
    private CJDataHolder cjDataHolder;
    private final String TAG = SearchInventoryCallback.class.getName();

    public SearchInventoryCallback(APICallComplete apiCallComplete) {
        this.apiCallComplete = apiCallComplete;
        this.cjDataHolder = CJDataHolder.getInstance();
    }

    @Override
    public void onResponse(Call<InventoryResponse> call, Response<InventoryResponse> response) {
        InventoryResponse b = response.body();
        Log.v(TAG, "onResponse completed for search inventory" + " code: " + b.getCode() + " status: " + b.getStatus() + " errors: " + b.getErrorsarray());

        ArrayList<Inventory> inventories = b.getResults();

        cjDataHolder.setSearchInventories(inventories);

        apiCallComplete.finished(true);

    }

    @Override
    public void onFailure(Call<InventoryResponse> call, Throwable t) {
        Log.e(TAG, "failure", t);
        apiCallComplete.finished(false);
    }
}
