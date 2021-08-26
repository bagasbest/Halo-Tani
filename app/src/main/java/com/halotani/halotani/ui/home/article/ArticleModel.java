package com.halotani.halotani.ui.home.article;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleModel implements Parcelable {

    private String title;
    private String description;
    private String uid;
    private String dp;
    private String dateAdded;
    private boolean isFavorite;
    private String dateUpdated;

    ArticleModel(){}

    protected ArticleModel(Parcel in) {
        title = in.readString();
        description = in.readString();
        uid = in.readString();
        dp = in.readString();
        dateAdded = in.readString();
        isFavorite = in.readByte() != 0;
        dateUpdated = in.readString();
    }

    public static final Creator<ArticleModel> CREATOR = new Creator<ArticleModel>() {
        @Override
        public ArticleModel createFromParcel(Parcel in) {
            return new ArticleModel(in);
        }

        @Override
        public ArticleModel[] newArray(int size) {
            return new ArticleModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(uid);
        parcel.writeString(dp);
        parcel.writeString(dateAdded);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
        parcel.writeString(dateUpdated);
    }
}
