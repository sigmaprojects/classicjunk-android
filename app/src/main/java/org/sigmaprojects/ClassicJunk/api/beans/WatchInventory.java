package org.sigmaprojects.ClassicJunk.api.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

/**
 * Created by don on 2/15/2016.
 */
public class WatchInventory implements Parcelable {

    private Integer id;
    private Float distance;
    private DateTime created;
    private Watch watch;
    private Inventory inventory;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public Watch getWatch() {
        return watch;
    }

    public void setWatch(Watch watch) {
        this.watch = watch;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    protected WatchInventory(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        distance = in.readByte() == 0x00 ? null : in.readFloat();
        long tmpCreated = in.readLong();
        created = tmpCreated != -1 ? new DateTime(tmpCreated) : null;
        watch = (Watch) in.readValue(Watch.class.getClassLoader());
        inventory = (Inventory) in.readValue(Inventory.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        if (distance == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(distance);
        }
        dest.writeLong(created != null ? created.getMillis() : -1L);
        dest.writeValue(watch);
        dest.writeValue(inventory);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WatchInventory> CREATOR = new Parcelable.Creator<WatchInventory>() {
        @Override
        public WatchInventory createFromParcel(Parcel in) {
            return new WatchInventory(in);
        }

        @Override
        public WatchInventory[] newArray(int size) {
            return new WatchInventory[size];
        }
    };
}