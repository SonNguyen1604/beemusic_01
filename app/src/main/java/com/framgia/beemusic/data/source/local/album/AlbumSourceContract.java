package com.framgia.beemusic.data.source.local.album;

import com.framgia.beemusic.data.source.local.BaseColumn;

/**
 * Created by beepi on 17/02/2017.
 */
public final class AlbumSourceContract {
    private AlbumSourceContract() {
    }

    public static class AlbumEntry extends BaseColumn {
        public static final String TABLE_ALBUM_NAME = "Album";
        public static final String COLUMN_IMAGE_LINK = "image_link";
    }
}
