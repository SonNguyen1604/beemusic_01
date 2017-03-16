package com.framgia.beemusic.displaysong;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.R;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.databinding.ActivityDisplaySongBinding;
import com.framgia.beemusic.service.MusicService;

public class DisplaySongActivity extends AppCompatActivity {
    private static final String EXTRA_SONG = "EXTRA_SONG";
    private static final String EXTRA_SINGER = "EXTRA_SINGER";
    private ActivityDisplaySongBinding mBinding;
    private static MusicService mService;
    private boolean mIsBound;
    private Song mSong;
    private String mSinger;
    private Intent mIntentService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mService = binder.getService();
            mIsBound = true;
            if(mSong == null && mSinger == null) {
                mService.onResume();
                mSong = mService.getSong();
                mSinger = mService.getSinger();
            }
            mService.setSong(mSong);
            mService.setSinger(mSinger);
            mService.onPlay();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsBound = false;
            mService = null;
        }
    };

    public static Intent getIntent(Song song, String singer) {
        Intent intent = new Intent(BeeApplication.getInstant(), DisplaySongActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_SONG, song);
        intent.putExtra(EXTRA_SINGER, singer);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_display_song);
        mBinding.setRotate(R.anim.rotation);
        bindExtraBundle();
        bindService();
    }

    private void bindExtraBundle() {
        Intent intent = getIntent();
        if (intent == null) ;
        mSong = (Song) intent.getSerializableExtra(EXTRA_SONG);
        mSinger = intent.getStringExtra(EXTRA_SINGER);
        mBinding.setSinger(mSinger);
        mBinding.setSong(mSong);
    }

    private void bindService() {
        if(mIsBound) return;
        mIntentService = new Intent(this,MusicService.class);
        startService(mIntentService);
        bindService(mIntentService, mConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
