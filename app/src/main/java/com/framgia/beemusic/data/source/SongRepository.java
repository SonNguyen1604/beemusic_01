package com.framgia.beemusic.data.source;

import android.content.Context;
import android.database.Cursor;

import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.local.song.SongLocalDataSource;

import java.util.List;

import rx.Observable;

/**
 * Created by beepi on 17/02/2017.
 */
public class SongRepository implements DataSource<Song> {
    private static SongRepository mSongRepository;
    private DataSource<Song> mLocalHandler;

    private SongRepository(DataSource<Song> localHandler) {
        mLocalHandler = localHandler;
    }

    public static SongRepository getInstant(Context context) {
        if (mSongRepository == null) {
            mSongRepository = new SongRepository(SongLocalDataSource.getInstant(context));
        }
        return mSongRepository;
    }

    @Override
    public List<Song> getModel(String selection, String[] args) {
        return mLocalHandler.getModel(selection, args);
    }

    @Override
    public Cursor getCursor(String selection, String[] args) {
        return mLocalHandler.getCursor(selection, args);
    }

    @Override
    public int save(Song song) {
        if (checkExistModel(song.getId())) return update(song);
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
    public boolean checkExistModel(int id) {
        return mLocalHandler.checkExistModel(id);
    }

    public Observable<Song> getDataObservableByModels(List<Song> models) {
        return mLocalHandler.getDataObservableByModels(models);
    }
}
