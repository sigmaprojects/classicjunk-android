package org.sigmaprojects.ClassicJunk.beans;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by don on 6/8/2014.
 */
public class Watch {
    @SerializedName("id")
    public Integer id;

    @SerializedName("label")
    public String label;

    @SerializedName("year_start")
    public Integer year_start;

    @SerializedName("year_end")
    public Integer year_end;

    @SerializedName("lat")
    public Float lat;

    @SerializedName("lng")
    public Float lng;

    @SerializedName("zipcode")
    public Integer zipcode;

    @SerializedName("created")
    public Date created;

    public ArrayList<WatchInventory> watchinventories;

    public void echo(String Tag) {
        Log.v(Tag, "id: " + id);
        Log.v(Tag, "label: " + label);
        Log.v(Tag, "year_start: " + year_start);
        Log.v(Tag, "year_end: " + year_end);
        Log.v(Tag, "lat: " + lat);
        Log.v(Tag, "lng: " + lng);
        Log.v(Tag, "zipcode: " + zipcode);
    }
    /*
    public boolean validate() {
        if( label == null || label.length() <= 1 ) {
            return false;
        }
        if( year_start == null || year_start.toString().length() <= 1 ) {
            return false;
        }
        if( year_end == null || year_end.toString().length() <= 1 ) {
            return false;
        }
        if(
            (lat == null || lat.toString().length() <= 1) ||
            (lat == null || lat.toString().length() <= 1) &&
            ( zipcode == null || zipcode.toString().length() <= 1 )
        ) {
            return false;
        }
        return true;
    }
    */

}
