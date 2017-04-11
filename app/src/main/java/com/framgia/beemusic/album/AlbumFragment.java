package com.framgia.beemusic.album;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.beemusic.R;
import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.databinding.FragmentAlbumBinding;
import com.framgia.beemusic.util.draganddrop.DragAndDrop;
import com.framgia.beemusic.util.draganddrop.ItemTouchHelperCallback;

/**
 * Created by beepi on 24/03/2017.
 */
public class AlbumFragment extends Fragment implements AlbumContract.View,
    DragAndDrop.OnDragListener {
    private AlbumContract.Presenter mPresenter;
    private FragmentAlbumBinding mBinding;
    private ObservableField<AlbumAdapter> mAdapter = new ObservableField<>();
    private int mSpanCount;
    private ItemTouchHelper mItemTouchHelper;

    public AlbumFragment() {
    }

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        mBinding = DataBindingUtil.bind(view);
        mBinding.setFragment(this);
        mSpanCount = getResources().getInteger(R.integer.span_count);
        if (mPresenter != null) mPresenter.subcribe();
        return view;
    }

    public ObservableField<AlbumAdapter> getAdapter() {
        return mAdapter;
    }

    public int getSpanCount() {
        return mSpanCount;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setPresenter(AlbumContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void initRecycleview(ObservableArrayList<Album> albums) {
        if (mPresenter == null) return;
        mAdapter.set(new AlbumAdapter(albums, mPresenter, this));
        initDragAndDrop();
    }

    @Override
    public void notifyItemRemove(int pos) {
        // todo delete album
    }

    @Override
    public void onSearch(String keySearch) {
        mPresenter.onSearch(keySearch);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter == null) return;
        mPresenter.unsubcribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter == null) return;
        mPresenter.unsubcribe();
    }

    private void initDragAndDrop() {
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mAdapter.get());
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mBinding.recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
