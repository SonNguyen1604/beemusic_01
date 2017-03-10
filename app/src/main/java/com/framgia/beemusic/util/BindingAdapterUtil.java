package com.framgia.beemusic.util;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.framgia.beemusic.R;

/**
 * Created by beepi on 27/02/2017.
 */
public class BindingAdapterUtil {
    private static int VERTICAL_ITEM_SPACE = 30;

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

    @BindingAdapter({"title", "color", "onListener", "activity"})
    public static void setSupportActionBar(Toolbar toolbar, String title, int color,
                                           SearchView.OnQueryTextListener onListener,
                                           AppCompatActivity activity) {
        toolbar.inflateMenu(R.menu.toolbar_main);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(color);
        initSearchView(toolbar.getMenu(), onListener, activity);
    }

    private static void initSearchView(Menu menu, SearchView.OnQueryTextListener onListener,
                                       AppCompatActivity activity) {
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) itemSearch.getActionView();
        SearchManager searchManager =
            (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(activity.getComponentName()));
        searchView.setOnQueryTextListener(onListener);
        ImageView searchIcon = (ImageView) searchView.findViewById(R.id.search_button);
        searchIcon.setImageResource(R.drawable.ic_action_search);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView view,
                                        LayoutManager.LayoutManagerFactory factory) {
        view.setLayoutManager(factory.create(view));
        view.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
    }

    @BindingAdapter("adapter")
    public static void setAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        if (adapter == null) return;
        view.setAdapter(adapter);
    }
}
