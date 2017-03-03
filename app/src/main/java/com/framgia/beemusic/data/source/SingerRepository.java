package com.framgia.beemusic.data.source;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.data.source.local.singer.SingerLocalDataSource;
import com.framgia.beemusic.data.source.local.singer.SingerSourceContract;

import java.util.List;

import rx.Observable;

/**
 * Created by beepi on 19/02/2017.
 */
public class SingerRepository implements DataSource<Singer> {
    private final static String DEFAULT_SINGER = "unknown";
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
    public Cursor getCursor(String selection, String[] args) {
        return mLocalHandler.getCursor(selection, args);
    }

    @Override
    public Singer getModel(int id) {
        return mLocalHandler.getModel(id);
    }

    @Override
    public int save(Singer model) {
        return mLocalHandler.save(model);
    }

    @Override
    public int update(Singer model) {
        return mLocalHandler.update(model);
    }

    public void updateCountByDelSong(List<Integer> idSingers) {
        if (idSingers == null) return;
        for (Integer id : idSingers) {
            Singer singer = getModel(id);
            if (singer == null) continue;
            if (singer.getCount() == 1) delete(id);
            singer.setCount(singer.getCount() - 1);
            update(singer);
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

    public Observable<Singer> getDataObservableByModels(List<Singer> models) {
        return mLocalHandler.getDataObservableByModels(models);
    }

    public String getSingerNameByIds(List<Integer> idSingers) {
        if (idSingers == null || idSingers.size() == 0) return null;
        String singerIds = "";
        String result = "";
        String singerName;
        for (Integer id : idSingers) singerIds += String.valueOf(id).concat(",");
        singerIds = singerIds.substring(0, singerIds.length() - 1);
        String selection = SingerSourceContract.SingerEntry.COLUMN_ID_SINGER
            + " IN (" + singerIds + ")";
        Cursor cursor = mLocalHandler.getCursor(selection, null);
        if (cursor == null) return null;
        while (cursor.moveToNext()) {
            singerName = cursor.getString(cursor.getColumnIndex(
                SingerSourceContract.SingerEntry.COLUMN_NAME));
            if (TextUtils.isEmpty(singerName)) singerName = DEFAULT_SINGER;
            result = result.concat(singerName).concat(",");
        }
        if (TextUtils.isEmpty(result)) return DEFAULT_SINGER;
        if (result.endsWith(",")) result = result.substring(0, result.length() - 1);
        return result;
    }
}
