package org.sigmaprojects.ClassicJunk.util;

import org.sigmaprojects.ClassicJunk.api.beans.Inventory;
import org.sigmaprojects.ClassicJunk.api.beans.Watch;
import org.sigmaprojects.ClassicJunk.api.beans.WatchInventory;

import java.util.ArrayList;

/**
 * Created by don on 12/18/2014.
 */
public class CJDataHolder {

    // array list of watch inventories, notifications that have been raked server side.
    private ArrayList<WatchInventory> watchInventories = new ArrayList<WatchInventory>();
    public ArrayList<WatchInventory> getWatchInventories() {return watchInventories;}
    public void setWatchInventories(ArrayList<WatchInventory> watchInventories) {this.watchInventories = watchInventories;}
    public boolean hasWatchInventories() { return (watchInventories.size() > 0); }


    // array list of watches, alerts that are created by the client.
    private ArrayList<Watch> watches = new ArrayList<Watch>();
    public ArrayList<Watch> getWatches() {return watches;}
    public void setWatches(ArrayList<Watch> watches) {this.watches = watches;}
    public boolean hasWatches() { return (watches.size() > 0); }


    // the current Watch bean that is queued to be edited by the client.
    private Watch editWatch = null;
    public Watch getEditWatch() {return editWatch;}
    public void setEditWatch(Watch editWatch) {this.editWatch = editWatch;}
    public void setEditWatch() {this.editWatch = null;}
    public boolean hasEditWatch() { return (this.editWatch != null); }

    // the device id
    private String deviceId;
    public String getDeviceId() { return deviceId;}
    public void setDeviceId(String id) {this.deviceId = id;}

    // lat
    private Float lat;
    public Float getLat() { return lat;}
    public void setLat(Float lat) {this.lat = lat;}

    // lng
    private Float lng;
    public Float getLng() { return lng;}
    public void setLng(Float lng) {this.lng = lng;}

    public boolean hasLocation() {
        return lat != null && lat != 0.00 && lng != null && lng != 0.00;
    }

    // array list of search inventories
    private ArrayList<Inventory> searchInventories = new ArrayList<Inventory>();
    public ArrayList<Inventory> getSearchInventories() {return searchInventories;}
    public void setSearchInventories(ArrayList<Inventory> inventories) {this.searchInventories = inventories;}
    public boolean hasSearchInventories() { return (searchInventories.size() > 0); }

    /*
    private static final CJDataHolder holder = new CJDataHolder();
    public static CJDataHolder getInstance() {
        return holder;
    }
    */

    private ArrayList<String> lastErrors = new ArrayList<>();
    public ArrayList<String> getLastErrors() {return lastErrors;}
    public void setLastErrors(ArrayList<String> errors) {this.lastErrors = errors;}
    public void resetLastErrors() {this.lastErrors = new ArrayList<>();}

    private static CJDataHolder holder;
    public static CJDataHolder getInstance() {
        if (holder == null) {
            holder = new CJDataHolder();
        }
        return holder;
    }

}
