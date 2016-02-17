package org.sigmaprojects.ClassicJunk.api.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by don on 2/15/2016.
 */
public class Watch implements Parcelable {

    private Integer id = 0;
    private String label;
    private Integer year_start;
    private Integer year_end;
    private Float lat;
    private Float lng;
    private Integer zipCode;
    private DateTime created;
    private ArrayList<WatchInventory> watchinventories;

    public Watch() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getYear_start() {
        return year_start;
    }

    public void setYear_start(Integer year_start) {
        this.year_start = year_start;
    }

    public Integer getYear_end() {
        return year_end;
    }

    public void setYear_end(Integer year_end) {
        this.year_end = year_end;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public ArrayList<WatchInventory> getWatchInventories() {
        return watchinventories;
    }

    public void setwatchinventories(ArrayList<WatchInventory> watchinventories) {
        this.watchinventories = watchinventories;
    }

    public void echo(String Tag) {
        Log.v(Tag, "id: " + id);
        Log.v(Tag, "label: " + label);
        Log.v(Tag, "year_start: " + year_start);
        Log.v(Tag, "year_end: " + year_end);
        Log.v(Tag, "lat: " + lat);
        Log.v(Tag, "lng: " + lng);
        Log.v(Tag, "zipcode: " + zipCode);
    }

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
                                ( zipCode == null || zipCode.toString().length() <= 1 )
                ) {
            return false;
        }
        return true;
    }



    protected Watch(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        label = in.readString();
        year_start = in.readByte() == 0x00 ? null : in.readInt();
        year_end = in.readByte() == 0x00 ? null : in.readInt();
        lat = in.readByte() == 0x00 ? null : in.readFloat();
        lng = in.readByte() == 0x00 ? null : in.readFloat();
        zipCode = in.readByte() == 0x00 ? null : in.readInt();
        long tmpCreated = in.readLong();
        created = tmpCreated != -1 ? new DateTime(tmpCreated) : null;
        if (in.readByte() == 0x01) {
            watchinventories = new ArrayList<WatchInventory>();
            in.readList(watchinventories, WatchInventory.class.getClassLoader());
        } else {
            watchinventories = null;
        }
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
        dest.writeString(label);
        if (year_start == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(year_start);
        }
        if (year_end == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(year_end);
        }
        if (lat == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(lat);
        }
        if (lng == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(lng);
        }
        if (zipCode == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(zipCode);
        }
        dest.writeLong(created != null ? created.getMillis() : -1L);
        if (watchinventories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(watchinventories);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Watch> CREATOR = new Parcelable.Creator<Watch>() {
        @Override
        public Watch createFromParcel(Parcel in) {
            return new Watch(in);
        }

        @Override
        public Watch[] newArray(int size) {
            return new Watch[size];
        }
    };
}