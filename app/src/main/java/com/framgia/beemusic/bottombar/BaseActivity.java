package com.framgia.beemusic.bottombar;

import android.content.IntentFilter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.framgia.beemusic.R;
import com.framgia.beemusic.broadcastreceiver.MusicReceiver;
import com.framgia.beemusic.displaysong.DisplaySongActivity;
import com.framgia.beemusic.service.MusicService;

import static com.framgia.beemusic.displaysong.DisplaySongActivity.sService;

public class BaseActivity extends AppCompatActivity implements MusicReceiver.ListenerBroadcast {
    private BindBottomModel mModel;
    private MusicReceiver mMusicReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new BindBottomModel();
        observerService();
        registerReceiver();
    }

    private void observerService() {
        if (DisplaySongActivity.sService == null) {
            mModel.isServiceRunning.set(false);
            return;
        }
        mModel.isServiceRunning.set(true);
        onUpdateDetailBottom();
    }

    private void registerReceiver() {
        mMusicReceiver = new MusicReceiver(this);
        IntentFilter intent = new IntentFilter();
        intent.addAction(MusicService.ACTION_PREVIOUS);
        intent.addAction(MusicService.ACTION_RESUME);
        intent.addAction(MusicService.ACTION_NEXT);
        intent.addAction(MusicService.ACTION_PAUSE);
        registerReceiver(mMusicReceiver, intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        observerService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sService.isPlaying()) sService.onResume();
        unregisterReceiver(mMusicReceiver);
    }

    public BindBottomModel getModel() {
        return mModel;
    }

    public void setModel(BindBottomModel model) {
        mModel = model;
    }

    public void onPrevious() {
        if (sService == null) return;
        sService.onPlayPrevious();
    }

    public void onNext() {
        if (sService == null) return;
        sService.onPlayNext();
    }

    public void onChangeState(BindBottomModel model) {
        if (!model.isStatePause.get()) {
            sService.onPause();
            return;
        }
        sService.onResume();
    }

    public void onOpenDetailMusic() {
        startActivity(DisplaySongActivity.getIntent(sService.getSong(),
            sService.getSinger()));
    }

    @Override
    public void onUpdateDetailBottom() {
        mModel.song.set(sService.getSong().getName());
        mModel.singer.set(sService.getSinger());
    }

    @Override
    public void onPauseMusic() {
        mModel.isStatePause.set(!sService.isPlaying());
    }

    @Override
    public void onPlayMusic() {
        mModel.isStatePause.set(!sService.isPlaying());
    }

    public class BindBottomModel {
        public final int rotate = R.anim.rotation;
        public final ObservableBoolean isServiceRunning = new ObservableBoolean();
        public final ObservableBoolean isStatePause = new ObservableBoolean();
        public final ObservableField<String> singer = new ObservableField<>();
        public final ObservableField<String> song = new ObservableField<>();
    }
}
