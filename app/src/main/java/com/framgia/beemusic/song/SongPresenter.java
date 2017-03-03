package com.framgia.beemusic.song;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.AlbumRepository;
import com.framgia.beemusic.data.source.SingerRepository;
import com.framgia.beemusic.data.source.SongAlbumRepository;
import com.framgia.beemusic.data.source.SongRepository;
import com.framgia.beemusic.data.source.SongSingerRepository;
import com.framgia.beemusic.data.source.local.singer.SingerSourceContract;
import com.framgia.beemusic.data.source.local.songsinger.SongSingerSourceContract;

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
    private final static String DEFAULT_SINGER = "unknown";

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
                    singers.add(getSinger(song.getId()));
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void unsubcribe() {
        mSubscription.clear();
    }

    @Override
    public String getSinger(int idSong) {
        String selection = SongSingerSourceContract.SongSingerEntry.COLUMN_ID_SONG + " = ?";
        int idSinger;
        List<Singer> singers;
        String singer = new String();
        Cursor cursor = mSongSingerHandler.getCursor(selection, new String[]{String.valueOf
            (idSong)});
        if (cursor == null || cursor.getCount() == 0) return null;
        while (cursor.moveToNext()) {
            idSinger = cursor.getInt(cursor.getColumnIndex(SongSingerSourceContract
                .SongSingerEntry.COLUMN_ID_SINGER));
            selection = SingerSourceContract.SingerEntry.COLUMN_ID_SINGER + " = ?";
            singers = mSingerHandler.getModel(selection, new String[]{String.valueOf
                (idSinger)});
            if (singers == null) continue;
            singer = singer.concat(singers.get(0).getName()).concat(",");
        }
        cursor.close();
        singer = singer.substring(0, singer.length() - 1);
        if (TextUtils.isEmpty(singer)) singer = DEFAULT_SINGER;
        return singer;
    }

    @Override
    public void onDeleteSong(Song song, int pos) {
        mView.notifyItemRemove(pos);
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
