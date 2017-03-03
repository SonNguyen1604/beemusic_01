package com.framgia.beemusic.song;

import com.framgia.beemusic.BasePresenter;
import com.framgia.beemusic.BaseView;
import com.framgia.beemusic.data.model.Song;

import java.util.List;

/**
 * Created by beepi on 03/03/2017.
 */
public interface SongContract {
    interface View extends BaseView<SongContract.Presenter> {
        void initRecycleview(List<Song> songs, List<String> singer);
    }

    interface Presenter extends BasePresenter {
        String getSinger(int idSong);
    }
}
