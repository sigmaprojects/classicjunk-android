package org.sigmaprojects.ClassicJunk.beans;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by don on 6/8/2014.
 */
public class Inventory {

    @SerializedName("id")
    public String id;

    @SerializedName("car")
    public String car;

    @SerializedName("caryear")
    public Integer caryear;

    @SerializedName("arrived")
    public Date arrived;

    @SerializedName("notes")
    public String notes;

    @SerializedName("created")
    public Date created;

    @SerializedName("timeago")
    public String timeago;

    public Location location;

}
