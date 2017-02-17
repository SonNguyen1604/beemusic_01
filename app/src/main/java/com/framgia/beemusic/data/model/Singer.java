package com.framgia.beemusic.data.model;

import android.database.Cursor;

import com.framgia.beemusic.data.source.local.singer.SingerSourceContract;

/**
 * Created by beepi on 17/02/2017.
 */
public class Singer {
    private int mId = -1;
    private String mName;
    private int mCount;

    public Singer(Cursor cursor) {
        mId = cursor.getInt(cursor.getColumnIndex(SingerSourceContract.SingerEntry
            .COLUMN_ID_SINGER));
        mName = cursor.getString(cursor.getColumnIndex(SingerSourceContract.SingerEntry
            .COLUMN_NAME));
        mCount = cursor.getInt(cursor.getColumnIndex(SingerSourceContract.SingerEntry
            .COLUMN_COUNT));
    }

    public Singer(String name, int count) {
        mName = name;
        mCount = count;
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

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        this.mCount = count;
    }
}
