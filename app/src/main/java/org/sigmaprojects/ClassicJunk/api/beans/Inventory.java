package org.sigmaprojects.ClassicJunk.api.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

/**
 * Created by don on 6/8/2014.
 */
public class Inventory implements Parcelable {

    private String id;
    private String car;
    private Integer caryear;
    private DateTime arrived;
    private String notes;
    private DateTime created;
    private String timeago;
    private Location location;
    private String imageurl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public Integer getCaryear() {
        return caryear;
    }

    public void setCaryear(Integer caryear) {
        this.caryear = caryear;
    }

    public DateTime getArrived() {
        return arrived;
    }

    public void setArrived(DateTime arrived) {
        this.arrived = arrived;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public String getTimeago() {
        return timeago;
    }

    public void setTimeago(String timeago) {
        this.timeago = timeago;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    protected Inventory(Parcel in) {
        id = in.readString();
        car = in.readString();
        caryear = in.readByte() == 0x00 ? null : in.readInt();
        long tmpArrived = in.readLong();
        arrived = tmpArrived != -1 ? new DateTime(tmpArrived) : null;
        notes = in.readString();
        long tmpCreated = in.readLong();
        created = tmpCreated != -1 ? new DateTime(tmpCreated) : null;
        timeago = in.readString();
        imageurl = in.readString();
        location = (Location) in.readValue(Location.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(car);
        if (caryear == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(caryear);
        }
        dest.writeLong(arrived != null ? arrived.getMillis() : -1L);
        dest.writeString(notes);
        dest.writeLong(created != null ? created.getMillis() : -1L);
        dest.writeString(timeago);
        dest.writeString(imageurl);
        dest.writeValue(location);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Inventory> CREATOR = new Parcelable.Creator<Inventory>() {
        @Override
        public Inventory createFromParcel(Parcel in) {
            return new Inventory(in);
        }

        @Override
        public Inventory[] newArray(int size) {
            return new Inventory[size];
        }
    };
}