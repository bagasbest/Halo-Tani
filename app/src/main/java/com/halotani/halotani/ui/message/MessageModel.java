package com.halotani.halotani.ui.message;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageModel implements Parcelable {

    private String uid;
    private String doctorUid;
    private String doctorName;
    private String customerUid;
    private String customerName;
    private String doctorDp;
    private String status;
    private String keahlian;
    private String customerDp;
    private boolean online;

    public MessageModel(){}

    protected MessageModel(Parcel in) {
        uid = in.readString();
        doctorUid = in.readString();
        doctorName = in.readString();
        customerUid = in.readString();
        customerName = in.readString();
        doctorDp = in.readString();
        status = in.readString();
        keahlian = in.readString();
        customerDp = in.readString();
        online = in.readByte() != 0;
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDoctorUid() {
        return doctorUid;
    }

    public void setDoctorUid(String doctorUid) {
        this.doctorUid = doctorUid;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getCustomerUid() {
        return customerUid;
    }

    public void setCustomerUid(String customerUid) {
        this.customerUid = customerUid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDoctorDp() {
        return doctorDp;
    }

    public void setDoctorDp(String doctorDp) {
        this.doctorDp = doctorDp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeahlian() {
        return keahlian;
    }

    public void setKeahlian(String keahlian) {
        this.keahlian = keahlian;
    }

    public String getCustomerDp() {
        return customerDp;
    }

    public void setCustomerDp(String customerDp) {
        this.customerDp = customerDp;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(doctorUid);
        parcel.writeString(doctorName);
        parcel.writeString(customerUid);
        parcel.writeString(customerName);
        parcel.writeString(doctorDp);
        parcel.writeString(status);
        parcel.writeString(keahlian);
        parcel.writeString(customerDp);
        parcel.writeByte((byte) (online ? 1 : 0));
    }
}
