package org.sigmaprojects.ClassicJunk.api.interfaces;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;
import org.sigmaprojects.ClassicJunk.api.beans.InventoryResponse;
import org.sigmaprojects.ClassicJunk.api.beans.WatchResponse;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by don on 2/15/2016.
 */
public interface ClassicJunkAPI {

    public static final String[] DATE_FORMATS = new String[] {
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd"
    };

    @GET("inventory/search/format/json/")
    Call<InventoryResponse> searchInventory(
            @Query("car") String car,
            @Query("yearStart") Integer yearStart,
            @Query("yearEnd") Integer yearEnd,
            @Query("lat") String lat,
            @Query("lng") String lng
    );

    @GET("watch/getwatches/format/json/")
    Call<WatchResponse> getWatches(
            @Query("device_id") String device_id
    );

    @GET("watch/deleteWatch/format/json/")
    Call<WatchResponse> deleteWatch(
            @Query("device_id") String device_id,
            @Query("id") Integer id
    );

    @GET("watch/createwatch/")
    Call<WatchResponse> saveWatch(
            @Query("device_id") String device_id,
            @Query("label") String label,
            @Query("year_start") Integer year_start,
            @Query("year_end") Integer year_end,
            @Query("lat") Float lat,
            @Query("lng") Float lng,
            @Query("zipcode") Integer zipcode
    );

    @GET("watch/updatewatch/")
    Call<WatchResponse> updateWatch(
            @Query("device_id") String device_id,
            @Query("id") Integer id,
            @Query("label") String label,
            @Query("year_start") Integer year_start,
            @Query("year_end") Integer year_end,
            @Query("lat") Float lat,
            @Query("lng") Float lng,
            @Query("zipcode") Integer zipcode
    );


    public static class DateDeserializer implements JsonDeserializer<DateTime> {
        @Override
        public DateTime deserialize(JsonElement jsonElement, Type typeOF,JsonDeserializationContext context) throws JsonParseException {
            for (String format : DATE_FORMATS) {
                try {
                    //return DateTime.parse(jsonElement.getAsString(), ISODateTimeFormat.dateTime());
                    long dateSeconds = Long.valueOf(jsonElement.getAsString());
                    return new DateTime( new Date(dateSeconds * 1000) );
                } catch (Exception e) {
                    Log.e("DateDes", "error parsing? ", e);
                }
            }
            throw new JsonParseException("Unparseable date: " + jsonElement.getAsString() + " Supported formats: " + Arrays.toString(DATE_FORMATS));
        }
    }

}
