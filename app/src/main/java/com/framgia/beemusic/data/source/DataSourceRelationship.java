package com.framgia.beemusic.data.source;

import android.database.Cursor;

/**
 * Created by beepi on 20/02/2017.
 */
public interface DataSourceRelationship {
    Cursor getModel(String selection, String[] Args);
    int save(int id1, int id2);
    int delete(int id1, int id2);
    void deleteAlls();
}
