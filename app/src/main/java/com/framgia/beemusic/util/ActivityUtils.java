package com.framgia.beemusic.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by beepi on 03/03/2017.
 */
public class ActivityUtils {
    private final static String PREF_INSTALL = "is_installed";
    private final static String KEY_INSTALL = "installed";

    public static void replaceFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment, int frameId) {
        fragmentManager.beginTransaction().replace(frameId, fragment).commit();
    }

    public static boolean isInstalled(Context context) {
        SharedPreferences shared = context.getSharedPreferences(PREF_INSTALL, MODE_PRIVATE);
        boolean isInstalled = shared.getBoolean(KEY_INSTALL, false);
        if (isInstalled) return true;
        shared.edit().putBoolean(KEY_INSTALL, true).apply();
        return false;
    }
}
