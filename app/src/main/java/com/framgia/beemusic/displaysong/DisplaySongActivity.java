package com.framgia.beemusic.displaysong;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.framgia.beemusic.broadcastreceiver.MusicReceiver;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.databinding.ActivityDisplaySongBinding;
import com.framgia.beemusic.service.MusicService;

public class DisplaySongActivity extends AppCompatActivity implements DisplaySongContract.View,
    SeekBar.OnSeekBarChangeListener, MusicService.ListenerDetailMusic,
    MusicReceiver.ListenerBroadcast {
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
    private MusicReceiver mReceiver;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            createService(service);
            mIsBound = true;
            mModel.isPlay.set(true);
            mModel.isRepeat.set(sService.getTypeRepeat());
            mModel.isShuffle.set(sService.getTypeShuffle());
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
        registerReceiver();
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

    private void registerReceiver() {
        mReceiver = new MusicReceiver(this);
        IntentFilter intent = new IntentFilter();
        intent.addAction(MusicService.ACTION_PREVIOUS);
        intent.addAction(MusicService.ACTION_RESUME);
        intent.addAction(MusicService.ACTION_NEXT);
        intent.addAction(MusicService.ACTION_PAUSE);
        registerReceiver(mReceiver, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onUpdateDetailBottom() {
    }

    @Override
    public void onPauseReceiver() {
        mModel.isPlay.set(false);
    }

    @Override
    public void onPlayReceiver() {
        mModel.isPlay.set(true);
    }

    @Override
    public void onPauseMusic() {
        if (sService == null) return;
        sService.onPause();
    }

    @Override
    public void onPlay() {
        if (sService == null) return;
        sService.onResume();
    }

    @Override
    public void onNext() {
        if (sService == null) return;
        sService.onPlayNextRepeat();
    }

    @Override
    public void onPrevious() {
        if (sService == null) return;
        sService.onPlayPreviousRepeat();
    }

    @Override
    public void onShuffle() {
        if (sService == null) return;
        mModel.isShuffle.set(mModel.isShuffle.get() == MusicService.ShuffleType.OFF ?
            MusicService.ShuffleType.ON : MusicService.ShuffleType.OFF);
        initTypePlay();
    }

    @Override
    public void onRepeat() {
        if (sService == null) return;
        mModel.isRepeat.set(mModel.isRepeat.get() == MusicService.RepeatType.ALL
            ? MusicService.RepeatType.ONE : mModel.isRepeat.get() == MusicService.RepeatType.ONE
            ? MusicService.RepeatType.OFF : MusicService.RepeatType.ALL);
        initTypePlay();
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
    public void updateDetailMusic() {
        mSong = sService.getSong();
        mSinger = sService.getSinger();
        mModel.song.set(sService.getSong().getName());
        mModel.singer.set(sService.getSinger());
    }

    private void createService(IBinder service) {
        if (sService == null) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            sService = binder.getService();
            sService.setSong(mSong);
            sService.setSinger(mSinger);
            sService.onPlay();
            return;
        }
        if (sService.getSong().getId() == mSong.getId()) {
            updateDuration(mSong.getDuration());
            updateDetailMusic();
            sService.onResume();
            return;
        }
        sService.setSong(mSong);
        sService.setSinger(mSinger);
        sService.onPlay();
    }

    private void initTypePlay() {
        sService.setTypeRepeat(mModel.isRepeat.get());
        if (mModel.isShuffle.get() == MusicService.ShuffleType.ON) sService.shuffleListSong();
        sService.setTypeShuffle(mModel.isShuffle.get());
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
        public final ObservableField<MusicService.ShuffleType> isShuffle = new ObservableField<>();
        public final ObservableField<MusicService.RepeatType> isRepeat = new ObservableField<>();
    }
}
