package org.sigmaprojects.ClassicJunk.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by don on 6/8/2014.
 */
public class WatchReponse {

    @SerializedName("code")
    public Integer code;

    @SerializedName("status")
    public String status;

    public ArrayList<Watch> results;

    public List errorsarray;

}
