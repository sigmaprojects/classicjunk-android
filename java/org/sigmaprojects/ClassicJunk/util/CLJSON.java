package org.sigmaprojects.ClassicJunk.util;

import android.util.Log;

import com.goebl.david.Response;
import com.goebl.david.Webb;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.json.JSONObject;
import org.sigmaprojects.ClassicJunk.CJApp;
import org.sigmaprojects.ClassicJunk.CJDataHolder;
import org.sigmaprojects.ClassicJunk.beans.Watch;
import org.sigmaprojects.ClassicJunk.beans.WatchReponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by don on 6/8/2014.
 */
public class CLJSON {

    private static final String TAG = "CLJSON";
    private static String apiBaseURL = "http://api.classicjunk.sigmaprojects.org";
    public static CJDataHolder cjDataHolder = CJDataHolder.getInstance();

    public static ArrayList<Watch> getWatches() {
        GsonBuilder builder = new GsonBuilder();

        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        Gson gson = builder.create();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id", CJApp.getDeviceUuid());
            jsonObject.put("format", "json");
        } catch (JSONException e) {
            Log.v(TAG,"JSON error:",e);
            //e.printStackTrace();
        }
        String json = jsonObject.toString();
        String data = sendRequest("/watch/getwatches", json);

        WatchReponse response;

        response = gson.fromJson(data, WatchReponse.class);

        Log.v(TAG, response.toString());

        ArrayList<Watch> results = response.results;

        return results;
    }

    public static void deleteWatch(Watch w) {
        GsonBuilder builder = new GsonBuilder();
        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        Gson gson = builder.create();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("device_id", CJApp.getDeviceUuid());
            jsonObject.put("format", "json");
            jsonObject.put("id", w.id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jsonObject.toString();

        sendRequest("/watch/deleteWatch", json);
    }

    public static WatchReponse saveWatch(Watch w) {
        GsonBuilder builder = new GsonBuilder();
        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        Gson gson = builder.create();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("device_id", CJApp.getDeviceUuid());
            jsonObject.put("format", "json");
            jsonObject.put("id", w.id);
            jsonObject.put("label", w.label);
            jsonObject.put("year_start", w.year_start);
            jsonObject.put("year_end", w.year_end);
            jsonObject.put("lat", w.lat);
            jsonObject.put("lng", w.lng);
            jsonObject.put("zipcode", w.zipcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jsonObject.toString();

        String endpoint = "/watch/create";
        if( w.id != null && w.id > 0 ) {
            endpoint = "/watch/updatewatch";
        }
        Log.v(TAG, "saving... : " + endpoint + " - " + json);
        String data = sendRequest(endpoint, json);

        WatchReponse response = gson.fromJson(data, WatchReponse.class);

        Log.v(TAG, "Found errors: " + response.errorsarray.toString());

        return response;
    }

    public static String sendRequest(String endpoint, String json) {
        Webb webb = Webb.create();
        webb.setBaseUri(apiBaseURL);

        Response<String> response = webb
                .post(endpoint)
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .header("Connection", "close")
                .body(json)
                .asString();

        String body = "";
        if( response.getStatusCode() == 200 ) {
            body = response.getBody();
        } else {
            body = response.getErrorBody().toString();
            Log.v(TAG,"error yo: "+body);
        }

        Log.v(TAG,""+body);

        return body + "";

    }


}
