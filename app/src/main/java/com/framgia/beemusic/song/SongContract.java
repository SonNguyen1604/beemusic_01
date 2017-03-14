package com.framgia.beemusic.song;

import android.databinding.ObservableArrayList;

import com.framgia.beemusic.BaseFragmentPresenter;
import com.framgia.beemusic.BaseFragmentView;
import com.framgia.beemusic.data.model.Song;

import ru.rambler.libs.swipe_layout.SwipeLayout;

/**
 * Created by beepi on 03/03/2017.
 */
public interface SongContract {
    interface FragmentView extends BaseFragmentView<Presenter> {
        void initRecycleview(ObservableArrayList<Song> songs, ObservableArrayList<String> singer);
        void notifyItemRemove(int pos);
    }

    interface Presenter extends BaseFragmentPresenter {
        void onDeleteSong(Song song, int pos);
        void onAddToAlbum(Song song, SwipeLayout layout);
        void onAddToFavorite(Song song, SwipeLayout layout);
        void onRemoveFromFavorite(Song song, SwipeLayout layout);
        void onOpenPlayMusic(Song song);
        void subcribeFavorite(Song song);
    }
}
