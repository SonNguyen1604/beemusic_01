package com.framgia.beemusic.data.source.local.song;

import com.framgia.beemusic.data.source.local.BaseColumn;

/**
 * Created by beepi on 17/02/2017.
 */
public final class SongSourceContract {
    private SongSourceContract() {
    }

    public static class SongEntry extends BaseColumn {
        public static final String TABLE_SONG_NAME = "Song";
        public static final String COLUMN_ID_SONG = "_id_song";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_GENRE = "genre";
        public static final String COLUMN_DURATION = "duration";
    }
}
