package com.framgia.beemusic.main;

import android.content.Context;
import android.database.Cursor;

import com.framgia.beemusic.data.source.AlbumRepository;
import com.framgia.beemusic.data.source.SingerRepository;
import com.framgia.beemusic.data.source.SongAlbumRepository;
import com.framgia.beemusic.data.source.SongRepository;
import com.framgia.beemusic.data.source.SongSingerRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 20/02/2017.
 */
public class MainPresenter implements MainContract.Presenter {
    private SongRepository mSongRepository;
    private SingerRepository mSingerRepository;
    private AlbumRepository mAlbumRepository;
    private SongAlbumRepository mSongAlbumRepository;
    private SongSingerRepository mSongSingerRepository;
    private MainContract.View mView;
    private CompositeSubscription mSubscriptions;

    public MainPresenter(Context context, MainContract.View view,
                         CompositeSubscription subscription) {
        mSingerRepository = SingerRepository.getInstant(context);
        mSongRepository = SongRepository.getInstant(context);
        mAlbumRepository = AlbumRepository.getInstant(context);
        mSongAlbumRepository = SongAlbumRepository.getInstant(context);
        mSongSingerRepository = SongSingerRepository.getInstant(context);
        mView = view;
        mSubscriptions = subscription;
    }

    /**
     * synchonize data from media store and beemusic app
     */
    private void synchronizeData(Cursor cursor) {
        int idSong, idSinger, idAlbum;
        if (cursor == null) return;
        idSong = mSongRepository.save(mSongRepository.getDataFromMediaStore(cursor));
        idSinger = mSingerRepository.save(mSingerRepository.getDataFromMediaStore(cursor));
        idAlbum = mAlbumRepository.save(mAlbumRepository.getDataFromMediaStore(cursor));
        mSongAlbumRepository.save(idSong, idAlbum);
        mSongSingerRepository.save(idSong, idSinger);
    }

    @Override
    public void subcribe() {
    }
}
