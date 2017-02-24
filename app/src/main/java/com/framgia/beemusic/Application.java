package com.framgia.beemusic;

import android.content.Context;

/**
 * Created by beepi on 22/02/2017.
 */
public class Application extends android.app.Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        if (sContext == null) {
            sContext = getApplicationContext();
        }
    }

    public static Context getContext() {
        return sContext;
    }
}
