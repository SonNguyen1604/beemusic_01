package com.framgia.beemusic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.R;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.SingerRepository;
import com.framgia.beemusic.data.source.SongAlbumRepository;
import com.framgia.beemusic.data.source.SongRepository;
import com.framgia.beemusic.data.source.SongSingerRepository;
import com.framgia.beemusic.data.source.local.song.SongSourceContract;
import com.framgia.beemusic.main.MainActivity;

import java.util.Collections;
import java.util.List;

import static com.framgia.beemusic.service.MusicService.RepeatType.OFF;
import static com.framgia.beemusic.service.MusicService.RepeatType.ONE;

/**
 * Created by beepi on 15/03/2017.
 */
public class MusicService extends Service
    implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {
    public static final String ACTION_RESUME = "com.framgia.action.RESUME";
    public static final String ACTION_PAUSE = "com.framgia.action.PAUSE";
    public static final String ACTION_NEXT = "com.framgia.action.NEXT";
    public static final String ACTION_PREVIOUS = "com.framgia.action.PREVIOUS";
    private static final int NOTIFICATION_ID = 1;
    private static final int RUNTIME_DELAY = 1000;

    public enum RepeatType {
        ALL,
        ONE,
        OFF
    }

    public enum ShuffleType {
        ON,
        OFF
    }

    private ShuffleType mTypeShuffle;
    private RepeatType mTypeRepeat;
    private final IBinder mBinder = new MusicBinder();
    private Song mSong;
    private int mIdAlbum = -1;
    private int mIdSinger = -1;
    private String mSinger;
    private List<Song> mSongs;
    private int mPosSong;
    private NotificationManager mNotificationManager;
    private MediaPlayer mMediaPlayer;
    private int mBufferPosition;
    private SongRepository mSongRepository;
    private SongAlbumRepository mSongAlbumRepository;
    private SongSingerRepository mSongSingerRepository;
    private SingerRepository mSingerRepository;
    private boolean mIsPlaying;

    public void setListenerDetailMusic(ListenerDetailMusic listenerDetailMusic) {
        mListenerDetailMusic = listenerDetailMusic;
    }

    private ListenerDetailMusic mListenerDetailMusic;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_STICKY;
        String action = intent.getAction();
        if (action == null) return START_STICKY;
        switch (action) {
            case ACTION_RESUME:
                onResume();
                break;
            case ACTION_PAUSE:
                onPause();
                break;
            case ACTION_NEXT:
                onPlayNextRepeat();
                break;
            case ACTION_PREVIOUS:
                onPlayPreviousRepeat();
                break;
            default:
                break;
        }
        return START_STICKY;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        mediaPlayer.reset();
        return false;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mMediaPlayer.start();
        if ((mPosSong == mSongs.size()) && mTypeRepeat == OFF) {
            mPosSong--;
            onPause();
            mMediaPlayer.seekTo(mSong.getDuration());
        }
        setUpAsForeground();
        updateSeekbar();
        mIsPlaying = true;
        mListenerDetailMusic.updateDuration(mSong.getDuration());
        mListenerDetailMusic.updateDetailMusic();
    }

    public void updateSeekbar() {
        Handler handler = new Handler();
        handler.postDelayed(mUpdateSeekbarRunable, RUNTIME_DELAY);
    }

    private Runnable mUpdateSeekbarRunable = new Runnable() {
        @Override
        public void run() {
            mListenerDetailMusic.updateSeekBar(mMediaPlayer.getCurrentPosition());
            updateSeekbar();
        }
    };

    public ShuffleType getTypeShuffle() {
        return mTypeShuffle;
    }

    public void setTypeShuffle(ShuffleType shuffle) {
        mTypeShuffle = shuffle;
    }

    public RepeatType getTypeRepeat() {
        return mTypeRepeat;
    }

    public void setTypeRepeat(RepeatType typeRepeat) {
        mTypeRepeat = typeRepeat;
    }

    private void initModel() {
        String selection;
        if (mIdAlbum != -1) {
            selection = creatSelection(mSongAlbumRepository.getListIdSong(mIdAlbum));
            if (selection == null) return;
            setSongs(mSongRepository.getModel(selection, null));
            return;
        }
        if (mIdSinger != -1) {
            selection = creatSelection(mSongSingerRepository.getListIdSong(mIdSinger));
            if (selection == null) return;
            setSongs(mSongRepository.getModel(selection, null));
            return;
        }
        setSongs(mSongRepository.getModel(null, null));
    }

    private String creatSelection(List<Integer> idSongs) {
        if (idSongs == null) return null;
        String selection = null, allIds = new String();
        for (Integer id : idSongs) {
            allIds.concat(String.valueOf(id)).concat(",");
        }
        if (allIds.length() == 0) return null;
        allIds = allIds.substring(0, allIds.length() - 1);
        selection = SongSourceContract.SongEntry.COLUMN_ID_SONG + " IN (" + allIds + ")";
        return selection;
    }

    private void initService() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mSongRepository = SongRepository.getInstant(BeeApplication.getInstant());
        mSongAlbumRepository = SongAlbumRepository.getInstant(BeeApplication.getInstant());
        mSongSingerRepository = SongSingerRepository.getInstant(BeeApplication.getInstant());
        mSingerRepository = SingerRepository.getInstant(BeeApplication.getInstant());
        mTypeRepeat = OFF;
        mTypeShuffle = ShuffleType.OFF;
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setWakeMode(getApplicationContext(),
            PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (mediaPlayer.getCurrentPosition() <= 0) return;
        mediaPlayer.reset();
        playRepeatType(mTypeRepeat);
    }

    public void shuffleListSong() {
        if (mSongs == null) return;
        Collections.shuffle(mSongs);
    }

    private void playRepeatType(RepeatType type) {
        switch (type) {
            case ALL:
                onPlayNextRepeat();
                break;
            case ONE:
                onPlay();
                break;
            case OFF:
                onPlayNextNonRepeat();
                break;
            default:
                break;
        }
    }

    public void onPlayNextNonRepeat() {
        if (mSongs == null) initModel();
        mPosSong++;
        if (mPosSong == mSongs.size()) {
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(mSong.getLink());
                mMediaPlayer.prepareAsync();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        mSong = mSongs.get(mPosSong);
        if (mIdSinger != -1) {
            onPlay();
            return;
        }
        List<Integer> ids = mSongSingerRepository.getListId(mSong.getId());
        if (ids == null) return;
        mSinger = mSingerRepository.getModel(ids.get(0)).getName();
        onPlay();
        sendBroadcast(new Intent(ACTION_NEXT));
    }

    @Override
    public boolean stopService(Intent name) {
        if (mMediaPlayer == null) return false;
        mMediaPlayer.stop();
        mMediaPlayer.release();
        return false;
    }

    public void onPlay() {
        if (mTypeRepeat != ONE) initModel();
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(mSong.getLink());
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() == mSong.getDuration() ?
            0 : mMediaPlayer.getCurrentPosition());
        mMediaPlayer.start();
        mIsPlaying = true;
        sendBroadcast(new Intent(ACTION_RESUME));
        mNotificationManager.notify(NOTIFICATION_ID,
            notifyNotification(R.drawable.ic_pause, ACTION_PAUSE));
    }

    public void onPause() {
        mMediaPlayer.pause();
        mIsPlaying = false;
        sendBroadcast(new Intent(ACTION_PAUSE));
        mNotificationManager.notify(NOTIFICATION_ID,
            notifyNotification(R.drawable.ic_play, ACTION_RESUME));
        stopForeground(false);
    }

    public void onPlayNextRepeat() {
        if (mSongs == null) initModel();
        mPosSong++;
        if (mPosSong == mSongs.size()) mPosSong = 0;
        mSong = mSongs.get(mPosSong);
        if (mIdSinger != -1) {
            onPlay();
            return;
        }
        List<Integer> ids = mSongSingerRepository.getListId(mSong.getId());
        if (ids == null) return;
        mSinger = mSingerRepository.getModel(ids.get(0)).getName();
        onPlay();
        sendBroadcast(new Intent(ACTION_NEXT));
    }

    public void onPlayPreviousRepeat() {
        if (mSongs == null) initModel();
        mPosSong--;
        if (mPosSong < 0) mPosSong = mSongs.size() - 1;
        mSong = mSongs.get(mPosSong);
        if (mIdSinger != -1) {
            onPlay();
            return;
        }
        List<Integer> ids = mSongSingerRepository.getListId(mSong.getId());
        if (ids == null) return;
        mSinger = mSingerRepository.getModel(ids.get(0)).getName();
        onPlay();
        sendBroadcast(new Intent(ACTION_PREVIOUS));
    }

    public void onSeek(int pos) {
        mMediaPlayer.seekTo(pos);
    }

    private void setUpAsForeground() {
        startForeground(NOTIFICATION_ID, notifyNotification(R.drawable.ic_pause, ACTION_PAUSE));
    }

    public boolean isPlaying() {
        return mIsPlaying;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    private PendingIntent createPendingIntentService(String action) {
        Intent intent = new Intent(BeeApplication.getInstant(), MusicService.class);
        intent.setAction(action);
        return PendingIntent.getService(BeeApplication.getInstant(), 0, intent, 0);
    }

    private PendingIntent createPendingIntentAcivity() {
        Intent intent = new Intent(BeeApplication.getInstant(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(BeeApplication.getInstant(), 0, intent, 0);
    }

    private Notification notifyNotification(int drawable, String action) {
        PendingIntent notifyPendingIntent, nextPendingIntent,
            previousPendingIntent;
        notifyPendingIntent = createPendingIntentService(action);
        nextPendingIntent = createPendingIntentService(ACTION_NEXT);
        previousPendingIntent = createPendingIntentService(ACTION_PREVIOUS);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bee);
        Notification notification = new NotificationCompat.Builder(this)
            .setContentIntent(createPendingIntentAcivity())
            .setContentText(mSinger)
            .setContentTitle(mSong.getName())
            .setStyle(new NotificationCompat.MediaStyle())
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(icon)
            .addAction(R.drawable.ic_previous, ACTION_PREVIOUS, previousPendingIntent)
            .addAction(drawable, ACTION_RESUME, notifyPendingIntent)
            .addAction(R.drawable.ic_next, ACTION_NEXT, nextPendingIntent)
            .setAutoCancel(action.equals(ACTION_RESUME))
            .build();
        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        return notification;
    }

    public int getPosSong() {
        return mPosSong;
    }

    public void setPosSong(int posSong) {
        mPosSong = posSong;
    }

    public String getSinger() {
        return mSinger;
    }

    public void setSinger(String singer) {
        mSinger = singer;
    }

    public Song getSong() {
        return mSong;
    }

    public void setSong(Song song) {
        mSong = song;
    }

    public int getIdAlbum() {
        return mIdAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        mIdAlbum = idAlbum;
    }

    public int getIdSinger() {
        return mIdSinger;
    }

    public void setIdSinger(int idSinger) {
        mIdSinger = idSinger;
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public void setSongs(List<Song> songs) {
        mSongs = songs;
    }

    public interface ListenerDetailMusic {
        void updateSeekBar(int pos);
        void updateDuration(int duration);
        void updateDetailMusic();
    }
}
