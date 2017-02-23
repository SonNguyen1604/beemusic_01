package com.framgia.beemusic.util;

import android.databinding.BindingAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.framgia.beemusic.R;

/**
 * Created by beepi on 27/02/2017.
 */
public class BindingAdapterUtil {
    @BindingAdapter({"toolbar", "activity"})
    public static void setUpDrawerListener(final DrawerLayout drawlayout, Toolbar toolbar,
                                           final AppCompatActivity appCompatActivity) {
        ActionBarDrawerToggle actionBarDrawerToggle =
            new ActionBarDrawerToggle(appCompatActivity,
                drawlayout, toolbar, R.string.msg_open_drawer, R.string.msg_close_drawer);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}
