package com.framgia.beemusic.data.source;

import android.content.Context;
import android.database.Cursor;

import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.data.source.local.singer.SingerLocalHandler;
import com.framgia.beemusic.data.source.local.singer.SingerSourceContract;

import java.util.List;

import rx.Observable;

/**
 * Created by beepi on 19/02/2017.
 */
public class SingerRepository implements DataSource<Singer> {
    private static SingerRepository sSingerRepository;
    private DataSource<Singer> mLocalHandler;

    private SingerRepository(DataSource<Singer> localHandler) {
        mLocalHandler = localHandler;
    }

    public static SingerRepository getInstant(Context context) {
        if (sSingerRepository == null) {
            sSingerRepository = new SingerRepository(SingerLocalHandler.getInstant(context));
        }
        return sSingerRepository;
    }

    @Override
    public List<Singer> getModel(String selection, String[] Args) {
        return mLocalHandler.getModel(selection, Args);
    }

    @Override
    public int save(Singer model) {
        int idSinger = checkExistSinger(model.getName());
        if (idSinger != -1) {
            model.setId(idSinger);
            update(model);
        }
        return mLocalHandler.save(model);
    }

    @Override
    public int update(Singer model) {
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
    public void implementCallback(Callback<Singer> callback, List<Singer> models) {
        mLocalHandler.implementCallback(callback, models);
    }

    @Override
    public Singer getDataFromMediaStore(Cursor cursor) {
        return mLocalHandler.getDataFromMediaStore(cursor);
    }

    @Override
    public Observable<Cursor> getDataObservable(Cursor mediaCursor) {
        return mLocalHandler.getDataObservable(mediaCursor);
    }

    /**
     * check exist of singer object in database
     *
     * @param name: singer 's name
     * @return id of singer
     */
    private int checkExistSinger(String name) {
        String selection = SingerSourceContract.SingerEntry.COLUMN_NAME + " = ?";
        List<Singer> singers = getModel(selection, new String[]{name});
        if (singers == null) return -1;
        return singers.get(0).getId();
    }

    private int getCountSong(int id) {
        String selection = SingerSourceContract.SingerEntry.COLUMN_ID_SINGER + " = ?";
        List<Singer> singers = getModel(selection, new String[]{String.valueOf(id)});
        if (singers == null) return -1;
        return singers.get(0).getCount();
    }
}
