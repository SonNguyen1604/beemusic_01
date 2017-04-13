package com.framgia.beemusic.album.createalbum;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.framgia.beemusic.R;
import com.framgia.beemusic.databinding.ActivityCreatedAlbumBinding;

public class CreatedAlbumActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCreatedAlbumBinding binding =
            DataBindingUtil.setContentView(this, R.layout.activity_created_album);
        binding.setActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                //todo create an album
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_created_album, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
