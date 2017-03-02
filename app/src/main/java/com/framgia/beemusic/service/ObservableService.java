package com.framgia.beemusic.service;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.data.source.SynchronizeRepository;

/**
 * Created by beepi on 22/02/2017.
 */
public class ObservableService extends Service {
    private final static int DELETE_SYN = 0;
    private final static int ADD_SYN = 1;
    private SynchronizeRepository mSynchronizeRepository;
    private int mMediastoreCount = 0;
    private ContentObserver mContentObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Cursor mediaCurrentCursor = mSynchronizeRepository.getCursorFromMediastore();
            int currentMediastoreCount = mediaCurrentCursor.getCount();
            int changedCase = currentMediastoreCount < mMediastoreCount ? DELETE_SYN : ADD_SYN;
            switch (changedCase) {
                case DELETE_SYN:
                    Cursor delCursor = mSynchronizeRepository.getDelCursor(mediaCurrentCursor);
                    mSynchronizeRepository.synchronizeByDelModel(delCursor);
                    break;
                case ADD_SYN:
                    Cursor addCursor = mSynchronizeRepository.getAddCursor(mediaCurrentCursor);
                    mSynchronizeRepository.synchronizeByAddModel(addCursor);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        initService();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initService() {
        mSynchronizeRepository = SynchronizeRepository.getInstant();
        mMediastoreCount = mSynchronizeRepository.getCursorFromMediastore().getCount();
        BeeApplication.getInstant()
            .getContentResolver()
            .registerContentObserver(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                true, mContentObserver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BeeApplication.getInstant().getContentResolver()
            .unregisterContentObserver(mContentObserver);
    }
}
