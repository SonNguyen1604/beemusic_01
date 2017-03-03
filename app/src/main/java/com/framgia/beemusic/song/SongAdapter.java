package com.framgia.beemusic.song;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.databinding.ItemSongAdapterBinding;

import java.util.List;

/**
 * Created by beepi on 03/03/2017.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private ObservableArrayList<Song> mSongList;
    private ObservableArrayList<String> mSingerList;
    private LayoutInflater mLayoutInflater;
    private SongContract.Presenter mPresenter;

    public SongAdapter(ObservableArrayList<Song> songs, ObservableArrayList<String> singers,
                       SongContract.Presenter presenter) {
        mSongList = songs;
        mSingerList = singers;
        mPresenter = presenter;
    }

    public List<Song> getSongList() {
        return mSongList;
    }

    public List<String> getSingerList() {
        return mSingerList;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        ItemSongAdapterBinding binding =
            ItemSongAdapterBinding.inflate(mLayoutInflater, parent, false);
        return new SongViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mSongList == null ? 0 : mSongList.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        private ItemSongAdapterBinding mBinding;
        private BindingModel mBindingModel;

        public SongViewHolder(ItemSongAdapterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(int pos) {
            Song song = mSongList.get(pos);
            if (song == null) return;
            mBindingModel = new BindingModel(song, mSingerList.get(pos), pos, mPresenter);
            mBinding.setBindingModel(mBindingModel);
            mBinding.executePendingBindings();
        }

        public class BindingModel extends BaseObservable {
            private Song mSong;
            private String mSinger;
            private int mPosition;
            private SongContract.Presenter mBindPresenter;

            public BindingModel(Song song, String singer, int position,
                                SongContract.Presenter presenter) {
                mSong = song;
                mSinger = singer;
                mPosition = position;
                mBindPresenter = presenter;
            }

            @Bindable
            public Song getSong() {
                return mSong;
            }

            public void setSong(Song song) {
                mSong = song;
                notifyPropertyChanged(BR.song);
            }

            @Bindable
            public String getSinger() {
                return mSinger;
            }

            public void setSinger(String singer) {
                mSinger = singer;
                notifyPropertyChanged(BR.singer);
            }

            @Bindable
            public int getPosition() {
                return mPosition;
            }

            public void setPosition(int position) {
                mPosition = position;
                notifyPropertyChanged(BR.position);
            }

            public SongContract.Presenter getBindPresenter() {
                return mBindPresenter;
            }

            public void setBindPresenter(SongContract.Presenter bindPresenter) {
                mBindPresenter = bindPresenter;
            }
        }
    }
}
