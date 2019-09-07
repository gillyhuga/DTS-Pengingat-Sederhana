package com.application.pengingatsederhana.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Reminder implements Parcelable {
    private int id;
    private String title;
    private String desc;
    private String clock;
    private String waktu;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getClock() {
        return clock;
    }

    public void setClock(String clock) {
        this.clock = clock;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.desc);
        parcel.writeString(this.waktu);
        parcel.writeString(this.clock);

    }

    public Reminder() {
    }

    protected Reminder(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.desc = in.readString();
        this.waktu = in.readString();
        this.clock = in.readString();
    }

    public static final Parcelable.Creator<Reminder> CREATOR = new Parcelable.Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel source) {
            return new Reminder(source);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
}
