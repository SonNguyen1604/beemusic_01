package com.framgia.beemusic.data.source.local.songsinger;

import com.framgia.beemusic.data.source.local.BaseColumn;

/**
 * Created by beepi on 28/02/2017.
 */
public class SongSingerSourceContract {
    private SongSingerSourceContract() {
    }

    public static class SongSingerEntry extends BaseColumn {
        public static final String TABLE_SONG_SINGER_RELATIONSHIP_NAME = "Song_singer_relationship";
    }
}
