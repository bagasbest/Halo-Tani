package com.halotani.halotani.ui.home.consultation;

import android.os.Parcel;
import android.os.Parcelable;

public class ConsultationFindModel implements Parcelable {

    private String name;
    private String description;
    private String keahlian;
    private String experience;
    private String phone;
    private String dp;
    private String like;
    private String certificate;
    private String uid;
    private String role;

    public ConsultationFindModel(){}

    protected ConsultationFindModel(Parcel in) {
        name = in.readString();
        description = in.readString();
        keahlian = in.readString();
        experience = in.readString();
        phone = in.readString();
        dp = in.readString();
        like = in.readString();
        certificate = in.readString();
        uid = in.readString();
        role = in.readString();
    }

    public static final Creator<ConsultationFindModel> CREATOR = new Creator<ConsultationFindModel>() {
        @Override
        public ConsultationFindModel createFromParcel(Parcel in) {
            return new ConsultationFindModel(in);
        }

        @Override
        public ConsultationFindModel[] newArray(int size) {
            return new ConsultationFindModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeahlian() {
        return keahlian;
    }

    public void setKeahlian(String keahlian) {
        this.keahlian = keahlian;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(keahlian);
        parcel.writeString(experience);
        parcel.writeString(phone);
        parcel.writeString(dp);
        parcel.writeString(like);
        parcel.writeString(certificate);
        parcel.writeString(uid);
        parcel.writeString(role);
    }
}
