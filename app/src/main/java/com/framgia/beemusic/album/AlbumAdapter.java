package com.framgia.beemusic.album;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.databinding.ItemAlbumAdapterBinding;

import java.util.List;

/**
 * Created by beepi on 24/03/2017.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private List<Album> mAlbums;
    private LayoutInflater mLayoutInflater;
    private AlbumContract.Presenter mPresenter;

    public AlbumAdapter(List<Album> albums, AlbumContract.Presenter presenter) {
        mAlbums = albums;
        mPresenter = presenter;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) mLayoutInflater = LayoutInflater.from(parent.getContext());
        ItemAlbumAdapterBinding binding =
            ItemAlbumAdapterBinding.inflate(mLayoutInflater, parent, false);
        return new AlbumAdapter.AlbumViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mAlbums != null ? mAlbums.size() : 0;
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        private ItemAlbumAdapterBinding mBinding;

        public AlbumViewHolder(ItemAlbumAdapterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(int pos) {
            Album album = mAlbums.get(pos);
            if (album == null) return;
            mBinding.setAlbum(album);
            mBinding.executePendingBindings();
        }
    }
}
