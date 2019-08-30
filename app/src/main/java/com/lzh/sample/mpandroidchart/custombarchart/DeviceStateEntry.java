package com.lzh.sample.mpandroidchart.custombarchart;

import android.graphics.Matrix;
import android.os.Parcel;
import android.os.Parcelable;

public class DeviceStateEntry implements Parcelable {
//    "attr":"leak_status",
//            │ 				"source":"10,,1567148263593,0.trg=0,,",
//            │ 				"subjectId":"lumi.158d00027b0e8c",
//            │ 				"timeStamp":1567148263630,
//            │ 				"value":"1"
    private String leak_status;
    private String source;
    private String subjectId;
    private long timeStamp;
    private int value;

    public DeviceStateEntry() {}

    protected DeviceStateEntry(Parcel in) {
        leak_status = in.readString();
        source = in.readString();
        subjectId = in.readString();
        timeStamp = in.readLong();
        value = in.readInt();
    }

    public static final Creator<DeviceStateEntry> CREATOR = new Creator<DeviceStateEntry>() {
        @Override
        public DeviceStateEntry createFromParcel(Parcel in) {
            return new DeviceStateEntry(in);
        }

        @Override
        public DeviceStateEntry[] newArray(int size) {
            return new DeviceStateEntry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(leak_status);
        dest.writeString(source);
        dest.writeString(subjectId);
        dest.writeLong(timeStamp);
        dest.writeInt(value);
    }

    public String getLeak_status() {
        return leak_status;
    }

    public void setLeak_status(String leak_status) {
        this.leak_status = leak_status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public float getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
