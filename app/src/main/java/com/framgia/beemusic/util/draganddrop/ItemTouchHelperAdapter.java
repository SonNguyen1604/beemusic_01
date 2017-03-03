package com.framgia.beemusic.util.draganddrop;

/**
 * Created by beepi on 07/03/2017.
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
