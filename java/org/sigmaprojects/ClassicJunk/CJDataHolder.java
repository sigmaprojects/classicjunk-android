package org.sigmaprojects.ClassicJunk;

import org.sigmaprojects.ClassicJunk.beans.Watch;
import org.sigmaprojects.ClassicJunk.beans.WatchInventory;

import java.util.ArrayList;

/**
 * Created by don on 12/18/2014.
 */
public class CJDataHolder {

    // array list of watch inventories, notifications that have been raked server side.
    private ArrayList<WatchInventory> watchinventories = new ArrayList<WatchInventory>();
    public ArrayList<WatchInventory> getWatchInventories() {return watchinventories;}
    public void setWatchInventories(ArrayList<WatchInventory> watchinventories) {this.watchinventories = watchinventories;}
    public boolean hasWatchInventories() { return (watchinventories.size() > 0); }


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


    private static final CJDataHolder holder = new CJDataHolder();
    public static CJDataHolder getInstance() {return holder;}

}
