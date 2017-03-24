package com.framgia.beemusic.album;

import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;

import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.AlbumRepository;
import com.framgia.beemusic.data.source.SingerRepository;
import com.framgia.beemusic.data.source.SongAlbumRepository;
import com.framgia.beemusic.data.source.SongRepository;
import com.framgia.beemusic.data.source.SongSingerRepository;

import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 24/03/2017.
 */
public class AlbumPresenter implements AlbumContract.Presenter {
    private AlbumContract.View mView;
    private SongRepository mSongHandler;
    private AlbumRepository mAlbumHandler;
    private SingerRepository mSingerHandler;
    private SongAlbumRepository mSongAlbumHandler;
    private SongSingerRepository mSongSingerHandler;
    private CompositeSubscription mSubscription;

    public AlbumPresenter(@NonNull AlbumContract.View view,
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
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void createAlbum() {
    }

    @Override
    public void onAddToAlbum(Song song, Singer singer) {
    }

    @Override
    public void onSearch(String keySearch) {
    }

    @Override
    public void subcribe() {
        mSubscription.clear();
        final ObservableArrayList<Album> albums = new ObservableArrayList<>();
        Subscription subscription = mAlbumHandler.getDataObservableByModels(
            mAlbumHandler.getModel(null, null))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(new Subscriber<Album>() {
                @Override
                public void onCompleted() {
                    mView.initRecycleview(albums);
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Album song) {
                    albums.add(song);
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void unsubcribe() {
        mSubscription.clear();
    }
}
