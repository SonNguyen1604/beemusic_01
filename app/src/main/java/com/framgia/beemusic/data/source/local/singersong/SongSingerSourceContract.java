package com.framgia.beemusic.data.source.local.singersong;

import com.framgia.beemusic.data.source.local.BaseColumn;

/**
 * Created by beepi on 17/02/2017.
 */
public final class SongSingerSourceContract {
    private SongSingerSourceContract() {
    }

    public static class SongSingerEntry extends BaseColumn {
        public static final String TABLE_SONG_SINGER_RELATIONSHIP_NAME = "Song_singer_relationship";
    }
}
