package com.framgia.beemusic.data.source;

import android.database.Cursor;

import rx.Observable;

/**
 * Created by beepi on 21/02/2017.
 */
public interface SynchronizeContract {
    Cursor getCursorFromMediastore();
    Observable<Cursor> getCursorObservable(Cursor cursor);
    void synchronizeByAddModel(Cursor cursor);
    void synchronizeByDelModel(Cursor delCursor);
    Cursor getDelCursor(Cursor currentMediaCursor);
    Cursor getAddCursor(Cursor currentMediaCurrsor);
}
