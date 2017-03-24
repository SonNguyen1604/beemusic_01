package com.framgia.beemusic.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.framgia.beemusic.service.MusicService;

/**
 * Created by beepi on 21/03/2017.
 */
public class MusicReceiver extends BroadcastReceiver {
    private ListenerBroadcast mListener;

    public MusicReceiver(ListenerBroadcast listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || mListener == null) return;
        switch (intent.getAction()) {
            case MusicService.ACTION_NEXT:
                mListener.onUpdateDetailBottom();
                break;
            case MusicService.ACTION_PREVIOUS:
                mListener.onUpdateDetailBottom();
                break;
            case MusicService.ACTION_PAUSE:
                mListener.onPauseReceiver();
                break;
            case MusicService.ACTION_RESUME:
                mListener.onPlayReceiver();
                break;
            default:
                break;
        }
    }

    public interface ListenerBroadcast {
        void onUpdateDetailBottom();
        void onPauseReceiver();
        void onPlayReceiver();
    }
}
