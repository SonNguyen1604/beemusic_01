package com.framgia.beemusic.song;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.beemusic.R;
import com.framgia.beemusic.databinding.FragmentSongBinding;

public class SongFragment extends Fragment implements SongContract.View {
    private SongContract.Presenter mPresenter;
    private FragmentSongBinding mBinding;

    public SongFragment() {
    }

    public static SongFragment newInstance() {
        return new SongFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);
        mBinding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setPresenter(mPresenter);
    }

    @Override
    public void setPresenter(SongContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
