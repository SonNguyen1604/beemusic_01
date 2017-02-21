package com.framgia.beemusic.data.source;

import android.database.Cursor;

import java.util.List;

import rx.Observable;

/**
 * Created by beepi on 17/02/2017.
 */
public interface DataSource<T> {
    interface Callback<T> {
        void onLoad(List<T> models);
    }
    List<T> getModel(String selection, String[] Args);
    int save(T model);
    int update(T model);
    int delete(int id);
    void deleteAlls();
    void implementCallback(Callback<T> callback, List<T> models);
    T getDataFromMediaStore(Cursor cursor);
    Observable<Cursor> getDataObservable(Cursor mediaCursor);
}
