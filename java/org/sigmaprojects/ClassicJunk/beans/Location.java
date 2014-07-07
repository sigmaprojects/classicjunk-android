package org.sigmaprojects.ClassicJunk.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by don on 6/8/2014.
 */
public class Location {

    @SerializedName("id")
    public Integer id;

    @SerializedName("label")
    public String label;

    @SerializedName("address")
    public String address;

    @SerializedName("phonenumber")
    public String phonenumber;

    @SerializedName("lat")
    public Float lat;

    @SerializedName("lng")
    public Float lng;

}
