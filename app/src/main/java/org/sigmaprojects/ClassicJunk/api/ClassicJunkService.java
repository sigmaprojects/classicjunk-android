package org.sigmaprojects.ClassicJunk.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.sigmaprojects.ClassicJunk.api.beans.InventoryResponse;
import org.sigmaprojects.ClassicJunk.api.beans.Watch;
import org.sigmaprojects.ClassicJunk.api.beans.WatchInventory;
import org.sigmaprojects.ClassicJunk.api.beans.WatchResponse;
import org.sigmaprojects.ClassicJunk.api.callbacks.DeleteWatchCallback;
import org.sigmaprojects.ClassicJunk.api.callbacks.GetWatchesCallback;
import org.sigmaprojects.ClassicJunk.api.callbacks.SaveWatchCallback;
import org.sigmaprojects.ClassicJunk.api.callbacks.SearchInventoryCallback;
import org.sigmaprojects.ClassicJunk.api.interfaces.APICallComplete;
import org.sigmaprojects.ClassicJunk.api.interfaces.ClassicJunkAPI;
import org.sigmaprojects.ClassicJunk.util.CJDataHolder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by don on 2/15/2016.
 */
public class ClassicJunkService {

    private static ClassicJunkService classicJunkService;
    private ClassicJunkAPI classicJunkAPI;
    private CJDataHolder cjDataHolder;

    private final String TAG = ClassicJunkService.class.getName();

    private ClassicJunkService() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new ClassicJunkAPI.DateDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.classicjunk.sigmaprojects.org/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        classicJunkAPI = retrofit.create(ClassicJunkAPI.class);
        cjDataHolder = CJDataHolder.getInstance();
    }

    public static ClassicJunkService getInstance() {
        if (classicJunkService == null) {
            classicJunkService = new ClassicJunkService();
        }
        return classicJunkService;
    }

    public void download(APICallComplete apiCallComplete) {
        Log.v(TAG, "downloading");
        Call<WatchResponse> call = classicJunkAPI.getWatches(
                cjDataHolder.getDeviceId()
        );
        GetWatchesCallback cb = new GetWatchesCallback(apiCallComplete);
        call.enqueue(cb);
    }

    public void search(
            APICallComplete apiCallComplete,
            String car,
            Integer yearStart,
            Integer yearEnd
    ) {
        Log.v(TAG, "search");
        Call<InventoryResponse> call = classicJunkAPI.searchInventory(
                car,
                yearStart,
                yearEnd,
                cjDataHolder.getLat().toString(),
                cjDataHolder.getLng().toString()
        );
        SearchInventoryCallback cb = new SearchInventoryCallback(apiCallComplete);
        call.enqueue(cb);
    }

    public void saveWatch(
            APICallComplete apiCallComplete,
            Watch watch
    ) {
        Log.v(TAG, "saveWatch");

        Call<WatchResponse> call;
        if( watch.getId() != null && watch.getId() != 0 ) {
            call = classicJunkAPI.updateWatch(
                    cjDataHolder.getDeviceId(),
                    watch.getId(),
                    watch.getLabel(),
                    watch.getYear_start(),
                    watch.getYear_end(),
                    cjDataHolder.getLat(),
                    cjDataHolder.getLng(),
                    watch.getZipCode()
            );
        } else {
            call = classicJunkAPI.saveWatch(
                    cjDataHolder.getDeviceId(),
                    watch.getLabel(),
                    watch.getYear_start(),
                    watch.getYear_end(),
                    cjDataHolder.getLat(),
                    cjDataHolder.getLng(),
                    watch.getZipCode()
            );
        }
        SaveWatchCallback cb = new SaveWatchCallback(apiCallComplete);
        call.enqueue(cb);
    }

    public void deleteWatch(
            APICallComplete apiCallComplete,
            Integer id
    ) {
        Log.v(TAG, "search");
        Call<WatchResponse> call = classicJunkAPI.deleteWatch(
                cjDataHolder.getDeviceId(),
                id
        );
        DeleteWatchCallback cb = new DeleteWatchCallback(apiCallComplete);
        call.enqueue(cb);
    }

}
