package com.framgia.beemusic.song;

import android.support.annotation.NonNull;

import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.AlbumRepository;
import com.framgia.beemusic.data.source.SingerRepository;
import com.framgia.beemusic.data.source.SongAlbumRepository;
import com.framgia.beemusic.data.source.SongRepository;
import com.framgia.beemusic.data.source.SongSingerRepository;

import java.util.ArrayList;
import java.util.List;

import ru.rambler.libs.swipe_layout.SwipeLayout;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

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
    private CompositeSubscription mSubscription;

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
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void subcribe() {
        mSubscription.clear();
        final List<Song> songs = new ArrayList<>();
        final List<String> singers = new ArrayList<>();
        Subscription subscription = mSongHandler.getDataObservableByModels(
            mSongHandler.getModel(null, null))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(new Subscriber<Song>() {
                @Override
                public void onCompleted() {
                    mView.initRecycleview(songs, singers);
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Song song) {
                    songs.add(song);
                    singers.add(mSingerHandler.getSingerNameByIds(
                        mSongSingerHandler.getListId(song.getId())));
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void unsubcribe() {
        mSubscription.clear();
    }

    @Override
    public void onDeleteSong(Song song, int pos) {
        mView.notifyItemRemove(pos);
        if (song == null) return;
        int idSong = song.getId();
        mSongHandler.delete(idSong);
        mSongAlbumHandler.delete(idSong);
        mSongSingerHandler.delete(idSong);
        mAlbumHandler.updateCountForDelSong(mSongAlbumHandler.getListId(idSong));
        mSingerHandler.updateCountByDelSong(mSongSingerHandler.getListId(idSong));
    }

    @Override
    public void onAddToAlbum(Song song, SwipeLayout layout) {
        layout.animateReset();
    }

    @Override
    public void onAddToFavorite(Song song, SwipeLayout layout) {
        layout.animateReset();
    }

    @Override
    public void onRemoveFromFavorite(Song song, SwipeLayout layout) {
        layout.animateReset();
    }

    @Override
    public void onOpenPlayMusic(Song song) {
        // todo open play music
    }
}
