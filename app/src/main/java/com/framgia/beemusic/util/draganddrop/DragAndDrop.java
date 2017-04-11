package com.framgia.beemusic.util.draganddrop;

import android.support.v7.widget.RecyclerView;

/**
 * Created by beepi on 04/04/2017.
 */
public interface DragAndDrop {
    interface OnDragListener {
        /**
         * Called when a view is requesting a start of a drag.
         *
         * @param viewHolder The holder of the view to drag.
         */
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }
}
