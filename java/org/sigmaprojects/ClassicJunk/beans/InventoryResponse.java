package org.sigmaprojects.ClassicJunk.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by don on 6/8/2014.
 */
public class InventoryResponse {

    @SerializedName("code")
    public Integer code;

    @SerializedName("status")
    public String status;

    public ArrayList<Inventory> results;

    public List errorsarray;

}
