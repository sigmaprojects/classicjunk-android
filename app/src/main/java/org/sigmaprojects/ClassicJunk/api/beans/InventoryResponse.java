package org.sigmaprojects.ClassicJunk.api.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by don on 6/8/2014.
 */
public class InventoryResponse implements Parcelable {

    private Integer code;
    private String status;
    private ArrayList<Inventory> results;
    private ArrayList<String> errorsarray;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Inventory> getResults() {
        return results;
    }

    public void setResults(ArrayList<Inventory> results) {
        this.results = results;
    }

    public ArrayList<String> getErrorsarray() {
        return errorsarray;
    }

    public void setErrorsarray(ArrayList<String> errorsarray) {
        this.errorsarray = errorsarray;
    }

    protected InventoryResponse(Parcel in) {
        code = in.readByte() == 0x00 ? null : in.readInt();
        status = in.readString();
        if (in.readByte() == 0x01) {
            results = new ArrayList<Inventory>();
            in.readList(results, Inventory.class.getClassLoader());
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
    public static final Parcelable.Creator<InventoryResponse> CREATOR = new Parcelable.Creator<InventoryResponse>() {
        @Override
        public InventoryResponse createFromParcel(Parcel in) {
            return new InventoryResponse(in);
        }

        @Override
        public InventoryResponse[] newArray(int size) {
            return new InventoryResponse[size];
        }
    };
}