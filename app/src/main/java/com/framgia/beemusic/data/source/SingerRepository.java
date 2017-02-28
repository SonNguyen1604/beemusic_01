package com.framgia.beemusic.data.source;

import android.content.Context;
import android.database.Cursor;

import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.data.source.local.singer.SingerLocalDataSource;
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
            sSingerRepository = new SingerRepository(SingerLocalDataSource.getInstant(context));
        }
        return sSingerRepository;
    }

    @Override
    public List<Singer> getModel(String selection, String[] args) {
        return mLocalHandler.getModel(selection, args);
    }

    @Override
    public int save(Singer model) {
        if (checkExistModel(model.getId())) return update(model);
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
    public boolean checkExistModel(int id) {
        return mLocalHandler.checkExistModel(id);
    }

    @Override
    public Observable<Singer> getDataObservable(List<Singer> models) {
        return mLocalHandler.getDataObservable(models);
    }

    @Override
    public Cursor getCursor(String selection, String[] args) {
        return mLocalHandler.getCursor(selection, args);
    }

    public int getCountSong(int id) {
        String selection = SingerSourceContract.SingerEntry.COLUMN_ID_SINGER + " = ?";
        List<Singer> singers = getModel(selection, new String[]{String.valueOf(id)});
        if (singers == null) return -1;
        return singers.get(0).getCount();
    }
}
