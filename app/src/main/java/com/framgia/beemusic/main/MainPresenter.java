package com.framgia.beemusic.main;

import android.database.Cursor;

import com.framgia.beemusic.data.source.AlbumRepository;
import com.framgia.beemusic.data.source.SingerRepository;
import com.framgia.beemusic.data.source.SongRepository;

import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by beepi on 20/02/2017.
 */
public class MainPresenter implements MainContract.Presenter {
    private SongRepository mSongRepository;
    private SingerRepository mSingerRepository;
    private AlbumRepository mAlbumRepository;
    private SynchronizeRepository mSynchronizeRepository;
    private MainContract.View mView;
    private CompositeSubscription mSubscription;

    public MainPresenter(MainContract.View view, CompositeSubscription subscription,
                         SongRepository songRepository, AlbumRepository albumRepository,
                         SingerRepository singerRepository,
                         SynchronizeRepository synchronizeRepository) {
        mView = view;
        mSubscription = subscription;
        mSongRepository = songRepository;
        mAlbumRepository = albumRepository;
        mSingerRepository = singerRepository;
        mSynchronizeRepository = synchronizeRepository;
    }

    @Override
    public void subcribe() {
        mSubscription.clear();
        Subscription subscription = mSynchronizeRepository.getCursorObservable
            (mSynchronizeRepository.getCursorFromMediastore())
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(new Subscriber<Cursor>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Cursor cursor) {
                    mSynchronizeRepository.synchronizeByAddModel(cursor);
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void unsubcribe() {
        mSubscription.clear();
    }
}
