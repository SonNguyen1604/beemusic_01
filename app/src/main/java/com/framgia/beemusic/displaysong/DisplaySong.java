package com.framgia.beemusic.displaysong;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.R;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.databinding.ActivityDisplaySongBinding;

public class DisplaySong extends AppCompatActivity {
    private static final String EXTRA_SONG = "song";
    private static final String EXTRA_SINGER = "singer";
    private ActivityDisplaySongBinding mBinding;

    public static Intent getIntent(Song song, String singer) {
        Intent intent = new Intent(BeeApplication.getInstant(), DisplaySong.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_SONG, song);
        intent.putExtra(EXTRA_SINGER, singer);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_display_song);
        mBinding.setRotate(R.anim.rotation);
        bindExtraBundle();
    }

    private void bindExtraBundle() {
        Intent intent = getIntent();
        if (intent == null) ;
        Song song = (Song) intent.getSerializableExtra(EXTRA_SONG);
        String singer = intent.getStringExtra(EXTRA_SINGER);
        mBinding.setSinger(singer);
        mBinding.setSong(song);
    }
}
