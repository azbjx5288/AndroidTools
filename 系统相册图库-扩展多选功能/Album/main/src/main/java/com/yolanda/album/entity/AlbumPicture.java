/*
 * AUTHOR：YOLANDA
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.yolanda.album.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 照片
 * </br>
 * Created in Aug 27, 2015 4:56:36 PM
 *
 * @author YOLANDA
 */
public class AlbumPicture implements Parcelable {

    /**
     * 照片id
     */
    private int id;
    /**
     * 照片名称
     */
    private String name;
    /**
     * 图片路径
     */
    private String path;
    /**
     * 是否被选中
     */
    private boolean isChecked;

    public AlbumPicture() {
    }

    public AlbumPicture(int id, String name, String path, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(path);
        dest.writeInt(isChecked ? 1 : 0);
    }

    private AlbumPicture(Parcel source) {
        id = source.readInt();
        name = source.readString();
        path = source.readString();
        isChecked = source.readInt() == 1;
    }

    public static final Parcelable.Creator<AlbumPicture> CREATOR = new Parcelable.Creator<AlbumPicture>() {

        @Override
        public AlbumPicture createFromParcel(Parcel source) {
            return new AlbumPicture(source);
        }

        @Override
        public AlbumPicture[] newArray(int size) {
            return new AlbumPicture[size];
        }
    };

}
