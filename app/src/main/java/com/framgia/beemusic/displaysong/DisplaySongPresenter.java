package com.framgia.beemusic.displaysong;

/**
 * Created by beepi on 17/03/2017.
 */
public class DisplaySongPresenter implements DisplaySongContract.Presenter {
    private DisplaySongContract.View mView;

    public DisplaySongPresenter(DisplaySongContract.View view) {
        mView = view;
    }

    @Override
    public String convertTime(long miliseconds) {
        int seconds = (int) (miliseconds / 1000) % 60;
        int minutes = (int) ((miliseconds / (1000 * 60)) % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }
}
