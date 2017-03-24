package com.framgia.beemusic.album;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.beemusic.R;
import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.databinding.FragmentAlbumBinding;

/**
 * Created by beepi on 24/03/2017.
 */
public class AlbumFragment extends Fragment implements AlbumContract.View {
    private AlbumContract.Presenter mPresenter;
    private FragmentAlbumBinding mBinding;
    private ObservableField<AlbumAdapter> mAdapter;
    private int mSpanCount;

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
        // todo init adapter
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
}
