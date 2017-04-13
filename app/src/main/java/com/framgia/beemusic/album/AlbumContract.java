package com.framgia.beemusic.album;

import com.framgia.beemusic.BaseFragmentPresenter;
import com.framgia.beemusic.BaseFragmentView;
import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.data.model.Song;

import java.util.List;

/**
 * Created by beepi on 24/03/2017.
 */
public interface AlbumContract {
    interface View extends BaseFragmentView<Presenter> {
        void initRecycleview(List<Album> albums);
        void notifyItemRemove(int pos);
        void showDialog(Album album, int pos);
        void openActivityCreatedAlbum();
    }

    interface Presenter extends BaseFragmentPresenter {
        void createAlbum();
        void onAddToAlbum(Song song, Singer singer);
        void onShowDialog(Album album, int pos);
        void onDeleteAlbum(Album album, int pos);
        void onOpenSongDetail(AlbumAdapter.AlbumViewHolder holder);
    }
}
