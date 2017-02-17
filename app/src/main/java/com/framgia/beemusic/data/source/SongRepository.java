package com.framgia.beemusic.data.source;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.local.song.SongLocalHander;

import java.util.List;

import rx.Observable;

/**
 * Created by beepi on 17/02/2017.
 */
public class SongRepository implements DataSource<Song> {
    private static SongRepository mSongRepository;
    private DataSource<Song> mLocalHandler;
    private ContentResolver mContentResolver;

    private SongRepository(DataSource<Song> localHandler, Context context) {
        mLocalHandler = localHandler;
        mContentResolver = context.getContentResolver();
    }

    public static SongRepository getInstant(Context context) {
        if (mSongRepository == null) {
            mSongRepository = new SongRepository(SongLocalHander.getInstant(context), context);
        }
        return mSongRepository;
    }

    @Override
    public List<Song> getModel(String selection, String[] Args) {
        return mLocalHandler.getModel(selection, Args);
    }

    @Override
    public int save(Song song) {
        return mLocalHandler.save(song);
    }

    @Override
    public int update(Song song) {
        return mLocalHandler.update(song);
    }

    @Override
    public int delete(int id) {
        return mLocalHandler.delete(id);
    }

    @Override
    public void deleteAlls() {
        mLocalHandler.deleteAlls();
    }

    @Override
    public void implementCallback(Callback<Song> callback, List<Song> models) {
        mLocalHandler.implementCallback(callback, models);
    }

    @Override
    public Song getDataFromMediaStore(Cursor cursor) {
        return mLocalHandler.getDataFromMediaStore(cursor);
    }

    @Override
    public Observable<Cursor> getDataObservable(Cursor mediaCursor) {
        return mLocalHandler.getDataObservable(mediaCursor);
    }

    public Cursor getCursorFromMediaStore() {
        String sortOrder = MediaStore.Audio.Media._ID;
        Cursor cursor = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
            MediaStore.Audio.Media.IS_MUSIC + " = ?", new String[]{"1"}, sortOrder);
        return cursor;
    }
}
