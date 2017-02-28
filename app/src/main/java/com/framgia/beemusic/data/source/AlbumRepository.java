package com.framgia.beemusic.data.source;

import android.content.Context;
import android.database.Cursor;

import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.source.local.album.AlbumLocalDataSource;
import com.framgia.beemusic.data.source.local.album.AlbumSourceContract;

import java.util.List;

import rx.Observable;

/**
 * Created by beepi on 19/02/2017.
 */
public class AlbumRepository implements DataSource<Album> {
    private static AlbumRepository sAlbumRepository;
    private DataSource<Album> mLocalHandler;

    private AlbumRepository(DataSource<Album> localHandler) {
        mLocalHandler = localHandler;
    }

    public static AlbumRepository getInstant(Context context) {
        if (sAlbumRepository == null) {
            sAlbumRepository = new AlbumRepository(AlbumLocalDataSource.getInstant(context));
        }
        return sAlbumRepository;
    }

    @Override
    public List<Album> getModel(String selection, String[] args) {
        return mLocalHandler.getModel(selection, args);
    }

    @Override
    public int save(Album model) {
        if (checkExistModel(model.getId())) return update(model);
        return mLocalHandler.save(model);
    }

    @Override
    public int update(Album model) {
        return mLocalHandler.update(model);
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

    @Override
    public Observable<Album> getDataObservable(List<Album> models) {
        return mLocalHandler.getDataObservable(models);
    }

    @Override
    public Cursor getCursor(String selection, String[] args) {
        return mLocalHandler.getCursor(selection, args);
    }

    private int getCountSong(int id) {
        String selection = AlbumSourceContract.AlbumEntry.COLUMN_ID_ALBUM + " = ?";
        List<Album> albums = getModel(selection, new String[]{String.valueOf(id)});
        if (albums == null) return -1;
        return albums.get(0).getCount();
    }
}
