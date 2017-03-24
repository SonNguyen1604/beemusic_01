package com.framgia.beemusic.album;

import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;

import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.AlbumDataSource;
import com.framgia.beemusic.data.source.DataSourceRelationship;
import com.framgia.beemusic.data.source.SingerDataSource;
import com.framgia.beemusic.data.source.SongDataSource;

import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 24/03/2017.
 */
public class AlbumPresenter implements AlbumContract.Presenter {
    private AlbumContract.View mView;
    private SongDataSource mSongHandler;
    private AlbumDataSource mAlbumHandler;
    private SingerDataSource mSingerHandler;
    private DataSourceRelationship mSongAlbumHandler;
    private DataSourceRelationship mSongSingerHandler;
    private CompositeSubscription mSubscription;

    public AlbumPresenter(@NonNull AlbumContract.View view,
                          SongDataSource songHandler,
                          AlbumDataSource albumHandler,
                          SingerDataSource singerHandler,
                          DataSourceRelationship songAlbumHandler,
                          DataSourceRelationship songSingerHandler) {
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
                public void onNext(Album album) {
                    albums.add(album);
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void unsubcribe() {
        mSubscription.clear();
    }
}
