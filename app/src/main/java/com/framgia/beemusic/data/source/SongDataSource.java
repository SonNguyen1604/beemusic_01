package com.framgia.beemusic.data.source;

import android.database.Cursor;

import com.framgia.beemusic.data.model.Song;

import java.util.List;

import rx.Observable;

/**
 * Created by beepi on 24/03/2017.
 */
public interface SongDataSource extends DataSource<Song> {
    List<Song> getModel(Cursor cursor);
    Observable<Song> getDataObservableByModels(List<Song> models);
}
