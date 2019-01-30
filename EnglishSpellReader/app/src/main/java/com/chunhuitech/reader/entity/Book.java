package com.chunhuitech.reader.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String id;
    private String cnName;
    private String desc;

    public Book() {
        this.id = "";
        this.cnName = "";
        this.desc = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(cnName);
        dest.writeString(desc);
    }

    public static final Parcelable.Creator<Book> CREATOR = new Creator<Book>()
    {
        @Override
        public Book[] newArray(int size)
        {
            return new Book[size];
        }

        @Override
        public Book createFromParcel(Parcel in)
        {
            Book book = new Book();
            book.setId(in.readString());
            book.setCnName(in.readString());
            book.setDesc(in.readString());
            return book;
        }
    };
}
