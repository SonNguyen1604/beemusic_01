package com.framgia.beemusic.main;

import com.framgia.beemusic.BasePresenter;

/**
 * Created by beepi on 20/02/2017.
 */
public interface MainContract {
    interface View {
        void startObserverService();
        void initSongFragment();
        void onSearch(String keySearch);
    }

    interface Presenter extends BasePresenter {
    }
}
