package com.framgia.beemusic.album;

import android.databinding.ObservableArrayList;

import com.framgia.beemusic.BaseFragmentPresenter;
import com.framgia.beemusic.BaseFragmentView;
import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.data.model.Song;

/**
 * Created by beepi on 24/03/2017.
 */
public interface AlbumContract {
    interface View extends BaseFragmentView<Presenter> {
        void initRecycleview(ObservableArrayList<Album> albums);
        void notifyItemRemove(int pos);
    }

    interface Presenter extends BaseFragmentPresenter {
        void createAlbum();
        void onAddToAlbum(Song song, Singer singer);
    }
}
