package com.framgia.beemusic.data.source;

import com.framgia.beemusic.data.model.Album;

import java.util.List;

/**
 * Created by beepi on 24/03/2017.
 */
public interface AlbumDataSource extends DataSource<Album> {
    void updateCountForDelSong(List<Integer> idAlbums);
}
