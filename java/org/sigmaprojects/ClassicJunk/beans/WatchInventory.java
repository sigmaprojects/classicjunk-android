package org.sigmaprojects.ClassicJunk.beans;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by don on 6/8/2014.
 */
public class WatchInventory {
    @SerializedName("id")
    public Integer id;

    @SerializedName("distance")
    public Float distance;

    @SerializedName("created")
    public Date created;

    public Watch watch;

    public Inventory inventory;


}
