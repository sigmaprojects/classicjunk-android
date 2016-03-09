package org.sigmaprojects.ClassicJunk.api.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by don on 2/15/2016.
 */
public class WatchResponse implements Parcelable {

    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("results")
    @Expose
    private ArrayList<Watch> results = new ArrayList<>();

    @SerializedName("errorsarray")
    @Expose
    private ArrayList<String> errorsarray = new ArrayList<>();

    public Integer getcode() {
        return code;
    }

    public void setcode(Integer code) {
        this.code = code;
    }

    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public ArrayList<Watch> getresults() {
        return results;
    }

    public void setresults(ArrayList<Watch> results) {
        this.results = results;
    }

    public ArrayList<String> geterrorsarray() {
        return errorsarray;
    }

    public void seterrorsarray(ArrayList<String> errorsarray) {
        this.errorsarray = errorsarray;
    }




    protected WatchResponse(Parcel in) {
        code = in.readByte() == 0x00 ? null : in.readInt();
        status = in.readString();
        if (in.readByte() == 0x01) {
            results = new ArrayList<Watch>();
            in.readList(results, Watch.class.getClassLoader());
        } else {
            results = null;
        }
        if (in.readByte() == 0x01) {
            errorsarray = new ArrayList<String>();
            in.readList(errorsarray, String.class.getClassLoader());
        } else {
            errorsarray = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (code == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(code);
        }
        dest.writeString(status);
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
        if (errorsarray == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(errorsarray);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WatchResponse> CREATOR = new Parcelable.Creator<WatchResponse>() {
        @Override
        public WatchResponse createFromParcel(Parcel in) {
            return new WatchResponse(in);
        }

        @Override
        public WatchResponse[] newArray(int size) {
            return new WatchResponse[size];
        }
    };
}