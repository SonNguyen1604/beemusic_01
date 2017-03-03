package com.framgia.beemusic.song;

import android.support.annotation.NonNull;

import com.framgia.beemusic.data.source.AlbumRepository;
import com.framgia.beemusic.data.source.SingerRepository;
import com.framgia.beemusic.data.source.SongAlbumRepository;
import com.framgia.beemusic.data.source.SongRepository;
import com.framgia.beemusic.data.source.SongSingerRepository;

/**
 * Created by beepi on 03/03/2017.
 */
public class SongPresenter implements SongContract.Presenter {
    private SongContract.View mView;
    private SongRepository mSongHandler;
    private AlbumRepository mAlbumHandler;
    private SingerRepository mSingerHandler;
    private SongAlbumRepository mSongAlbumHandler;
    private SongSingerRepository mSongSingerHandler;

    public SongPresenter(@NonNull SongContract.View view,
                         SongRepository songHandler,
                         AlbumRepository albumHandler,
                         SingerRepository singerHandler,
                         SongAlbumRepository songAlbumHandler,
                         SongSingerRepository songSingerHandler) {
        mView = view;
        mSongHandler = songHandler;
        mAlbumHandler = albumHandler;
        mSingerHandler = singerHandler;
        mSongAlbumHandler = songAlbumHandler;
        mSongSingerHandler = songSingerHandler;
        mView.setPresenter(this);
    }
}
