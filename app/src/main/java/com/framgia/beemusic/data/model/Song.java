package com.framgia.beemusic.data.model;

import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;
import com.framgia.beemusic.data.source.local.song.SongSourceContract;

/**
 * Created by beepi on 17/02/2017.
 */
public class Song extends BaseObservable {
    public static boolean IS_FAVORITE = true;
    public static boolean IS_NON_FAVORITE = false;
    public static int TYPE_MEDIA = 0;
    public static int TYPE_APP = 1;
    private int mId = -1;
    private String mName;
    private String mLink;
    private boolean mIsFavorite;
    private int mType;
    private String mGenre;
    private int mDuration;

    public Song(Cursor cursor) {
        mId = cursor.getInt(cursor.getColumnIndex(SongSourceContract.SongEntry.COLUMN_ID_SONG));
        mName = cursor.getString(cursor.getColumnIndex(SongSourceContract.SongEntry.COLUMN_NAME));
        mLink = cursor.getString(cursor.getColumnIndex(SongSourceContract.SongEntry.COLUMN_LINK));
        mType = cursor.getInt(cursor.getColumnIndex(SongSourceContract.SongEntry.COLUMN_TYPE));
        mIsFavorite = cursor.getInt(cursor.getColumnIndex(SongSourceContract.SongEntry
            .COLUMN_IS_FAVORITE)) == 1;
        mGenre = cursor.getString(cursor.getColumnIndex(SongSourceContract.SongEntry.COLUMN_GENRE));
        mDuration = cursor.getInt(cursor.getColumnIndex(SongSourceContract.SongEntry
            .COLUMN_DURATION));
    }

    public Song(String name, String link, boolean isFavorite, int type,
                String genre, int duration) {
        mName = name;
        mLink = link;
        mIsFavorite = isFavorite;
        mType = type;
        mGenre = genre;
        mDuration = duration;
    }

    public Song(int id, String name, String link, boolean isFavorite, int type, String genre,
                int duration) {
        mId = id;
        mName = name;
        mLink = link;
        mIsFavorite = isFavorite;
        mType = type;
        mGenre = genre;
        mDuration = duration;
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

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    @Bindable
    public boolean getIsFavorite() {
        return mIsFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        mIsFavorite = isFavorite;
        notifyPropertyChanged(BR.isFavorite);
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }
}
