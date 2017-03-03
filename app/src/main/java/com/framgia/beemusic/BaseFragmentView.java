package com.framgia.beemusic;

/**
 * Created by beepi on 03/03/2017.
 */
public interface BaseFragmentView<T> {
    void setPresenter(T presenter);
    void onSearch(String keySearch);
}
