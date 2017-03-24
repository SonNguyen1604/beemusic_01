package com.framgia.beemusic.data.source;

import com.framgia.beemusic.data.model.Singer;

import java.util.List;

import rx.Observable;

/**
 * Created by beepi on 24/03/2017.
 */
public interface SingerDataSource extends DataSource<Singer> {
    void updateCountByDelSong(List<Integer> idSingers);
    Observable<Singer> getDataObservableByModels(List<Singer> models);
    String getSingerNameByIds(List<Integer> idSingers);
}
