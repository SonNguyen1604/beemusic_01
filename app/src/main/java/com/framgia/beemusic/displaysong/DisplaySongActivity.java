package com.framgia.beemusic.displaysong;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.R;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.databinding.ActivityDisplaySongBinding;
import com.framgia.beemusic.service.MusicService;

public class DisplaySongActivity extends AppCompatActivity implements DisplaySongContract.View,
    SeekBar.OnSeekBarChangeListener, MusicService.ListenerDetailMusic {
    private static final String EXTRA_SONG = "EXTRA_SONG";
    private static final String EXTRA_SINGER = "EXTRA_SINGER";
    private static final String CURRENT_TIME = "00:00";
    private ActivityDisplaySongBinding mBinding;
    public static MusicService sService;
    private boolean mIsBound;
    private Song mSong;
    private String mSinger;
    private Intent mIntentService;
    private DisplaySongContract.Presenter mPresenter;
    private BindDisplaySong mModel;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            createService(service);
            mIsBound = true;
            mModel.isPlay.set(true);
            sService.setListenerDetailMusic(DisplaySongActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsBound = false;
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
        bindExtraBundle();
        bindService();
        bindData();
    }

    @Override
    public Song getSong() {
        return mSong;
    }

    @Override
    public String getSinger() {
        return mSinger;
    }

    private void bindExtraBundle() {
        Intent intent = getIntent();
        if (intent == null) ;
        mSong = (Song) intent.getSerializableExtra(EXTRA_SONG);
        mSinger = intent.getStringExtra(EXTRA_SINGER);
    }

    private void bindData() {
        mModel = new BindDisplaySong();
        mModel.currentTime.set(CURRENT_TIME);
        mBinding.setModel(mModel);
        mBinding.setView(this);
        mPresenter = new DisplaySongPresenter(this);
    }

    private void bindService() {
        if (mIsBound) return;
        mIntentService = new Intent(this, MusicService.class);
        startService(mIntentService);
        bindService(mIntentService, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    @Override
    public void onPauseMusic() {
        if (sService == null) return;
        sService.onPause();
        mModel.isPlay.set(false);
    }

    @Override
    public void onPlay() {
        if (sService == null) return;
        sService.onResume();
        mModel.isPlay.set(true);
    }

    @Override
    public void onNext() {
        if (sService == null) return;
        sService.onPlayNext();
    }

    @Override
    public void onPrevious() {
        if (sService == null) return;
        sService.onPlayPrevious();
    }

    @Override
    public void onShuffle() {
        if (sService == null) return;
        sService.setTypePlay(MusicService.TYPE_PLAY.SHUFFLE);
        mModel.isShuffle.set(true);
    }

    @Override
    public void onRepeat() {
        if (sService == null) return;
        sService.setTypePlay(MusicService.TYPE_PLAY.REPEAT);
        mModel.isShuffle.set(false);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int pos, boolean isFromUser) {
        if (!isFromUser) return;
        mModel.currentProgress.set(pos);
        sService.onSeek(pos);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void updateSeekBar(int pos) {
        mModel.currentProgress.set(pos);
        mModel.currentTime.set(mPresenter.convertTime(pos));
    }

    @Override
    public void updateDuration(int duration) {
        mModel.totalProgress.set(duration);
        mModel.durationTime.set(mPresenter.convertTime(duration));
    }

    @Override
    public void updateDetailMusic(Song song, String singer) {
        mSong = song;
        mSinger = singer;
        mModel.song.set(mSong.getName());
        mModel.singer.set(mSinger);
    }

    private void createService(IBinder service) {
        if (sService == null) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            sService = binder.getService();
        }
        sService.setSong(mSong);
        sService.setSinger(mSinger);
        if(sService.getCurrentPos() != 0){
            sService.onResume();
            return;
        }
        sService.onPlay();
    }

    public class BindDisplaySong {
        public int rotate = R.anim.rotation;
        public final ObservableField<String> song = new ObservableField<>();
        public final ObservableField<String> singer = new ObservableField<>();
        public final ObservableBoolean isPlay = new ObservableBoolean();
        public final ObservableField<String> durationTime = new ObservableField<>();
        public final ObservableField<String> currentTime = new ObservableField<>();
        public final ObservableInt totalProgress = new ObservableInt();
        public final ObservableInt currentProgress = new ObservableInt();
        public final ObservableBoolean isShuffle = new ObservableBoolean(true);
    }
}
