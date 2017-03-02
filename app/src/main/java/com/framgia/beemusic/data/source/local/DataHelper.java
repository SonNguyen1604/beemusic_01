package com.framgia.beemusic.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.framgia.beemusic.data.source.local.BaseColumn.COLUMN_COUNT;
import static com.framgia.beemusic.data.source.local.BaseColumn.COLUMN_NAME;
import static com.framgia.beemusic.data.source.local.album.AlbumSourceContract.AlbumEntry.COLUMN_ID_ALBUM;
import static com.framgia.beemusic.data.source.local.album.AlbumSourceContract.AlbumEntry.COLUMN_IMAGE_LINK;
import static com.framgia.beemusic.data.source.local.album.AlbumSourceContract.AlbumEntry.TABLE_ALBUM_NAME;
import static com.framgia.beemusic.data.source.local.singer.SingerSourceContract.SingerEntry.COLUMN_ID_SINGER;
import static com.framgia.beemusic.data.source.local.singer.SingerSourceContract.SingerEntry.TABLE_SINGER_NAME;
import static com.framgia.beemusic.data.source.local.song.SongSourceContract.SongEntry.COLUMN_DURATION;
import static com.framgia.beemusic.data.source.local.song.SongSourceContract.SongEntry.COLUMN_GENRE;
import static com.framgia.beemusic.data.source.local.song.SongSourceContract.SongEntry.COLUMN_ID_SONG;
import static com.framgia.beemusic.data.source.local.song.SongSourceContract.SongEntry.COLUMN_IS_FAVORITE;
import static com.framgia.beemusic.data.source.local.song.SongSourceContract.SongEntry.COLUMN_LINK;
import static com.framgia.beemusic.data.source.local.song.SongSourceContract.SongEntry.COLUMN_TYPE;
import static com.framgia.beemusic.data.source.local.song.SongSourceContract.SongEntry.TABLE_SONG_NAME;
import static com.framgia.beemusic.data.source.local.songalbum.SongAlbumSourceContract.SongAlbumEntry.TABLE_SONG_ALBUM_RELATIONSHIP_NAME;
import static com.framgia.beemusic.data.source.local.songsinger.SongSingerSourceContract.SongSingerEntry.TABLE_SONG_SINGER_RELATIONSHIP_NAME;

/**
 * Created by beepi on 21/03/2016.
 */
public class DataHelper extends SQLiteOpenHelper {
    private static final String DATABSE_NAME = "BeeMusic.db";
    protected SQLiteDatabase mDatabase;
    private static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTERGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String AUTO_CREMENT = " AUTOINCREMENT";
    private static final String NOT_NULL = " NOT NULL";
    private static final String COMMA_CREATE_TABLE = "CREATE TABLE ";
    private static final String DROPTABLE_IF_EXIST = "DROP TABLE IF EXISTS ";
    private static final String COMMA_CREATE_SONG_TABLE =
        COMMA_CREATE_TABLE + TABLE_SONG_NAME + " ("
            + COLUMN_ID_SONG + INTERGER_TYPE + PRIMARY_KEY + AUTO_CREMENT + NOT_NULL + COMMA_SEP
            + COLUMN_NAME + TEXT_TYPE + COMMA_SEP
            + COLUMN_LINK + TEXT_TYPE + COMMA_SEP
            + COLUMN_TYPE + INTERGER_TYPE + COMMA_SEP
            + COLUMN_GENRE + TEXT_TYPE + COMMA_SEP
            + COLUMN_DURATION + INTERGER_TYPE + COMMA_SEP
            + COLUMN_IS_FAVORITE + INTERGER_TYPE + " )";
    private static final String COMMA_CREATE_ALBUM_TABLE =
        COMMA_CREATE_TABLE + TABLE_ALBUM_NAME + " ("
            + COLUMN_ID_ALBUM + INTERGER_TYPE + PRIMARY_KEY + AUTO_CREMENT + COMMA_SEP
            + COLUMN_NAME + TEXT_TYPE + COMMA_SEP
            + COLUMN_COUNT + INTERGER_TYPE + COMMA_SEP
            + COLUMN_IMAGE_LINK + TEXT_TYPE + " )";
    private static final String COMMA_CREATE_SINGER_TABLE =
        COMMA_CREATE_TABLE + TABLE_SINGER_NAME + " ("
            + COLUMN_ID_SINGER + INTERGER_TYPE + PRIMARY_KEY + AUTO_CREMENT + COMMA_SEP
            + COLUMN_COUNT + INTERGER_TYPE + COMMA_SEP
            + COLUMN_NAME + TEXT_TYPE + " )";
    private static final String COMMA_CREATE_TEMP_SONG_ALBUM_TABLE =
        COMMA_CREATE_TABLE + TABLE_SONG_ALBUM_RELATIONSHIP_NAME + " ("
            + COLUMN_ID_SONG + INTERGER_TYPE + PRIMARY_KEY + COMMA_SEP
            + COLUMN_ID_ALBUM + INTERGER_TYPE + PRIMARY_KEY + " )";
    private static final String COMMA_CREATE_TEMP_SINGER_SONG_TABLE =
        COMMA_CREATE_TABLE + TABLE_SONG_SINGER_RELATIONSHIP_NAME + " ("
            + COLUMN_ID_SINGER + INTERGER_TYPE + PRIMARY_KEY + COMMA_SEP
            + COLUMN_ID_SONG + INTERGER_TYPE + PRIMARY_KEY + " )";
    private static final String COMMA_DROP_SONG_TABLE =
        DROPTABLE_IF_EXIST + TABLE_SONG_NAME;
    private static final String COMMA_DROP_ALBUM_TABLE =
        DROPTABLE_IF_EXIST + TABLE_ALBUM_NAME;
    private static final String COMMA_DROP_SINGER_TABLE =
        DROPTABLE_IF_EXIST + TABLE_SINGER_NAME;
    private static final String COMMA_DROP_TEMP_SONG_ALBUM_TABLE =
        DROPTABLE_IF_EXIST + TABLE_SONG_ALBUM_RELATIONSHIP_NAME;
    private static final String COMMA_DROP_TEMP_SINGER_SONG_TABLE =
        DROPTABLE_IF_EXIST + TABLE_SONG_SINGER_RELATIONSHIP_NAME;

    public DataHelper(Context context) {
        super(context, DATABSE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COMMA_CREATE_ALBUM_TABLE);
        db.execSQL(COMMA_CREATE_SINGER_TABLE);
        db.execSQL(COMMA_CREATE_SONG_TABLE);
        db.execSQL(COMMA_CREATE_TEMP_SONG_ALBUM_TABLE);
        db.execSQL(COMMA_CREATE_TEMP_SINGER_SONG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(COMMA_DROP_ALBUM_TABLE);
        db.execSQL(COMMA_DROP_SONG_TABLE);
        db.execSQL(COMMA_DROP_SINGER_TABLE);
        db.execSQL(COMMA_DROP_TEMP_SONG_ALBUM_TABLE);
        db.execSQL(COMMA_DROP_TEMP_SINGER_SONG_TABLE);
        onCreate(db);
    }

    protected void openDatabase() {
        mDatabase = getWritableDatabase();
    }

    protected void closeDatabse() {
        if (mDatabase == null) return;
        mDatabase.close();
    }
}
