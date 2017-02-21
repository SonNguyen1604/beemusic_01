package com.framgia.beemusic.data.model;

import android.database.Cursor;

import com.framgia.beemusic.data.source.local.album.AlbumSourceContract;

/**
 * Created by beepi on 17/02/2017.
 */
public class Album {
    private int mId = -1;
    private String mName;
    private String mImageLink;
    private int mCount;

    public Album(String name, String imageLink, int count) {
        mName = name;
        mImageLink = imageLink;
        mCount = count;
    }

    public Album(Cursor cursor) {
        mId = cursor.getInt(cursor.getColumnIndex(AlbumSourceContract.AlbumEntry.COLUMN_ID_ALBUM));
        mName = cursor.getString(cursor.getColumnIndex(AlbumSourceContract.AlbumEntry.COLUMN_NAME));
        mImageLink = cursor
            .getString(cursor.getColumnIndex(AlbumSourceContract.AlbumEntry.COLUMN_IMAGE_LINK));
        mCount = cursor.getInt(cursor.getColumnIndex(AlbumSourceContract.AlbumEntry.COLUMN_COUNT));
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageLink() {
        return mImageLink;
    }

    public void setImageLink(String imageLink) {
        mImageLink = imageLink;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }
}
