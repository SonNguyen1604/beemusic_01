package com.framgia.beemusic.displaysong;

import com.framgia.beemusic.data.model.Song;

/**
 * Created by beepi on 16/03/2017.
 */
public interface DisplaySongContract {
    interface View {
        void onPause();
        void onPlay();
        void onNext();
        void onPrevious();
        void onShuffle();
        void onRepeat();
        Song getSong();
        String getSinger();
    }

    interface Presenter {
        String convertTime(long miliseconds);
    }
}
