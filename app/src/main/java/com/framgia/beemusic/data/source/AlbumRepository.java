package com.framgia.beemusic.data.source;

import android.content.Context;
import android.database.Cursor;

import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.source.local.album.AlbumLocalDataSource;

import java.util.List;

import rx.Observable;

/**
 * Created by beepi on 19/02/2017.
 */
public class AlbumRepository implements AlbumDataSource {
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
        return mLocalHandler.save(model);
    }

    @Override
    public int update(Album model) {
        return mLocalHandler.update(model);
    }

    @Override
    public void updateCountForDelSong(List<Integer> idAlbums) {
        if (idAlbums == null) return;
        for (Integer id : idAlbums) {
            Album album = getModel(id);
            if (album == null) continue;
            if (album.getCount() == 0) return;
            album.setCount(album.getCount() - 1);
            update(album);
        }
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
    public Observable<Album> getDataObservableByModels(List<Album> models) {
        return mLocalHandler.getDataObservableByModels(models);
    }

    @Override
    public Cursor getCursor(String selection, String[] args) {
        return mLocalHandler.getCursor(selection, args);
    }

    @Override
    public Album getModel(int id) {
        return mLocalHandler.getModel(id);
    }
}
