package com.framgia.beemusic.data.source;

import android.content.Context;
import android.database.Cursor;

import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.source.local.album.AlbumLocalHandler;
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
            sAlbumRepository = new AlbumRepository(AlbumLocalHandler.getInstant(context));
        }
        return sAlbumRepository;
    }

    @Override
    public List<Album> getModel(String selection, String[] Args) {
        return mLocalHandler.getModel(selection, Args);
    }

    @Override
    public int save(Album model) {
        int idAlbum = checkExistAlbum(model.getName());
        if (idAlbum != -1) {
            model.setId(idAlbum);
            update(model);
        }
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
    public void implementCallback(Callback<Album> callback, List<Album> models) {
        mLocalHandler.implementCallback(callback, models);
    }

    @Override
    public Album getDataFromMediaStore(Cursor cursor) {
        return mLocalHandler.getDataFromMediaStore(cursor);
    }

    @Override
    public Observable<Cursor> getDataObservable(Cursor mediaCursor) {
        return mLocalHandler.getDataObservable(mediaCursor);
    }

    /**
     * check exist of album
     *
     * @param name: album 's name
     * @return id of album
     */
    private int checkExistAlbum(String name) {
        String selection = AlbumSourceContract.AlbumEntry.COLUMN_NAME + " = ?";
        List<Album> albums = getModel(selection, new String[]{name});
        if (albums == null) return -1;
        return albums.get(0).getId();
    }

    private int getCountSong(int id) {
        String selection = AlbumSourceContract.AlbumEntry.COLUMN_ID_ALBUM + " = ?";
        List<Album> albums = getModel(selection, new String[]{String.valueOf(id)});
        if (albums == null) return -1;
        return albums.get(0).getCount();
    }
}
