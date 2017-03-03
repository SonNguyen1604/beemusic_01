package com.framgia.beemusic.song;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.databinding.ItemSongAdapterBinding;

import java.util.List;

/**
 * Created by beepi on 03/03/2017.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private List<Song> mSongList;
    private List<String> mSingerList;
    private LayoutInflater mLayoutInflater;

    public SongAdapter(List<Song> songs, List<String> singers) {
        mSongList = songs;
        mSingerList = singers;
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
        holder.bind(mSongList.get(position), mSingerList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSongList == null ? 0 : mSongList.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        private ItemSongAdapterBinding mBinding;

        public SongViewHolder(ItemSongAdapterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Song song, String singer) {
            if (song != null) mBinding.setSong(song);
            mBinding.setSinger(singer);
            mBinding.executePendingBindings();
        }
    }
}
