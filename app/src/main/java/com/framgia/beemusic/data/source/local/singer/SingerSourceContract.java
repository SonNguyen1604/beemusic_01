package com.framgia.beemusic.data.source.local.singer;

import com.framgia.beemusic.data.source.local.BaseColumn;

/**
 * Created by beepi on 17/02/2017.
 */
public final class SingerSourceContract {
    private SingerSourceContract() {
    }

    public static class SingerEntry extends BaseColumn {
        public static final String TABLE_SINGER_NAME = "Singer";
    }
}
