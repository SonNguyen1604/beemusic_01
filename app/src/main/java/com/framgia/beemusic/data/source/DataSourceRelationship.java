package com.framgia.beemusic.data.source;

import android.database.Cursor;

import java.util.List;

/**
 * Created by beepi on 28/02/2017.
 */
public interface DataSourceRelationship {
    List<Integer> getListId(int idSong);
    Cursor getCursor(String selection, String[] args);
    int save(int id1, int id2);
    int delete(String selection, String[] args);
    int delete(int idSong);
    void deleteAlls();
}
