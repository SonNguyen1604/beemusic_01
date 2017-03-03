package com.framgia.beemusic.song;

import com.framgia.beemusic.BasePresenter;
import com.framgia.beemusic.BaseView;
import com.framgia.beemusic.data.model.Song;

import java.util.List;

import ru.rambler.libs.swipe_layout.SwipeLayout;

/**
 * Created by beepi on 03/03/2017.
 */
public interface SongContract {
    interface View extends BaseView<SongContract.Presenter> {
        void initRecycleview(List<Song> songs, List<String> singer);
        void notifyItemRemove(int pos);
    }

    interface Presenter extends BasePresenter {
        void onDeleteSong(Song song, int pos);
        void onAddToAlbum(Song song, SwipeLayout layout);
        void onAddToFavorite(Song song, SwipeLayout layout);
        void onRemoveFromFavorite(Song song, SwipeLayout layout);
        void onOpenPlayMusic(Song song);
    }
}
