package com.framgia.beemusic;

import android.app.Application;

/**
 * Created by beepi on 22/02/2017.
 */
public class BeeApplication extends Application {
    private static BeeApplication sInstant;

    public static BeeApplication getInstant() {
        return sInstant;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstant = this;
    }
}
