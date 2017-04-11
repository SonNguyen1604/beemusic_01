package com.framgia.beemusic.album;

import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.databinding.ItemAlbumAdapterBinding;
import com.framgia.beemusic.util.draganddrop.DragAndDrop;
import com.framgia.beemusic.util.draganddrop.ItemTouchHelperAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by beepi on 24/03/2017.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>
    implements ItemTouchHelperAdapter {
    private List<Album> mAlbums;
    private LayoutInflater mLayoutInflater;
    private AlbumContract.Presenter mPresenter;
    private DragAndDrop.OnDragListener mOnDragListener;

    public AlbumAdapter(List<Album> albums, AlbumContract.Presenter presenter, DragAndDrop
        .OnDragListener listener) {
        mAlbums = albums;
        mPresenter = presenter;
        mOnDragListener = listener;
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

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mAlbums, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
    }

    public List<Album> getAlbums() {
        return mAlbums;
    }

    public void removeItem(int pos) {
        mAlbums.remove(pos);
        notifyItemRemoved(pos);
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        public final ObservableBoolean isTransparent = new ObservableBoolean();
        public final static float TRANSPARENT = 0.5f;
        public final static float NOT_TRANSPARENT = 1.0f;
        private ItemAlbumAdapterBinding mBinding;
        private Album mAlbum;
        private int mPos;

        public AlbumViewHolder(ItemAlbumAdapterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(int pos) {
            mAlbum = mAlbums.get(pos);
            mPos = pos;
            if (mAlbum == null) return;
            mBinding.setHolder(this);
            mBinding.setListener(mOnDragListener);
            mBinding.setPresenter(mPresenter);
            mBinding.executePendingBindings();
        }

        public Album getAlbum() {
            return mAlbum;
        }

        public int getPos() {
            return mPos;
        }
    }
}
