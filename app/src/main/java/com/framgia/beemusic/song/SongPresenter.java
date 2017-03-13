package com.framgia.beemusic.song;

import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;

import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.AlbumRepository;
import com.framgia.beemusic.data.source.SingerRepository;
import com.framgia.beemusic.data.source.SongAlbumRepository;
import com.framgia.beemusic.data.source.SongRepository;
import com.framgia.beemusic.data.source.SongSingerRepository;
import com.framgia.beemusic.data.source.local.song.SongSourceContract;
import com.framgia.beemusic.displaysong.DisplaySong;

import ru.rambler.libs.swipe_layout.SwipeLayout;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 03/03/2017.
 */
public class SongPresenter implements SongContract.Presenter {
    private SongContract.FragmentView mView;
    private SongRepository mSongHandler;
    private AlbumRepository mAlbumHandler;
    private SingerRepository mSingerHandler;
    private SongAlbumRepository mSongAlbumHandler;
    private SongSingerRepository mSongSingerHandler;
    private CompositeSubscription mSubscription;

    public SongPresenter(@NonNull SongContract.FragmentView view,
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
        final ObservableArrayList<Song> songs = new ObservableArrayList<>();
        final ObservableArrayList<String> singers = new ObservableArrayList<>();
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
        // todo open fragment album for adding song
    }

    @Override
    public void onAddToFavorite(Song song, SwipeLayout layout) {
        layout.animateReset();
        song.setIsFavorite(Song.IS_FAVORITE);
        subcribeFavorite(song);
    }

    @Override
    public void onRemoveFromFavorite(Song song, SwipeLayout layout) {
        layout.animateReset();
        song.setIsFavorite(Song.IS_NON_FAVORITE);
        subcribeFavorite(song);
    }

    @Override
    public void onOpenPlayMusic(Song song, String singer) {
        BeeApplication.getInstant().startActivity(DisplaySong.getIntent(song, singer));
    }

    @Override
    public void subcribeFavorite(Song song) {
        Subscription subscription = Observable.just(song)
            .observeOn(Schedulers.newThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Subscriber<Song>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Song song) {
                    mSongHandler.update(song);
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void onSearch(String keySearch) {
        final ObservableArrayList<Song> songs = new ObservableArrayList<>();
        final ObservableArrayList<String> singers = new ObservableArrayList<>();
        String selection = SongSourceContract.SongEntry.COLUMN_NAME + " like '%" + keySearch + "%'";
        Subscription subscription = mSongHandler.getDataObservableByModels(
            mSongHandler.getModel(selection, null))
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
}
