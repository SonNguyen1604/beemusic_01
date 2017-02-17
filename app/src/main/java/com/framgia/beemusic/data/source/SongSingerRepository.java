package com.framgia.beemusic.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.framgia.beemusic.data.source.local.DataHelper;
import com.framgia.beemusic.data.source.local.singer.SingerSourceContract;
import com.framgia.beemusic.data.source.local.singersong.SongSingerSourceContract;
import com.framgia.beemusic.data.source.local.song.SongSourceContract;

/**
 * Created by beepi on 20/02/2017.
 */
public class SongSingerRepository extends DataHelper implements DataSourceRelationship {
    private static SongSingerRepository sSongSingerRepository;

    private SongSingerRepository(Context context) {
        super(context);
    }

    public static SongSingerRepository getInstant(Context context) {
        if (sSongSingerRepository == null) {
            sSongSingerRepository = new SongSingerRepository(context);
        }
        return sSongSingerRepository;
    }

    @Override
    public Cursor getModel(String selection, String[] Args) {
        SQLiteDatabase db = getReadableDatabase();
        String sortOrder = SongSourceContract.SongEntry.COLUMN_ID_SONG + " ASC";
        return db.query(SongSingerSourceContract.SongSingerEntry.TABLE_TEMP_SONG_SINGER_NAME, null,
            selection, Args,
            null, null, sortOrder);
    }

    /**
     * @param id1: id of song
     * @param id2: id of singer
     * @return number of row inserted
     */
    @Override
    public int save(int id1, int id2) {
        int count = -1;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createContentValue(id1, id2);
        if (contentValues == null) return -1;
        count = (int) db
            .insert(SongSingerSourceContract.SongSingerEntry.TABLE_TEMP_SONG_SINGER_NAME, null,
                contentValues);
        db.close();
        return count;
    }

    /**
     * @param id1: id of song
     * @param id2: id of singer
     * @return number of row deleted
     */
    @Override
    public int delete(int id1, int id2) {
        int count = -1;
        String selection = SongSourceContract.SongEntry.COLUMN_ID_SONG
            + " = ? " + " and "
            + SingerSourceContract.SingerEntry.COLUMN_ID_SINGER + " = ?";
        SQLiteDatabase db = getWritableDatabase();
        count = db.delete(SongSingerSourceContract.SongSingerEntry.TABLE_TEMP_SONG_SINGER_NAME,
            selection, new String[]{String.valueOf(id1), String.valueOf(id2)});
        db.close();
        return count;
    }

    @Override
    public void deleteAlls() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SongSingerSourceContract.SongSingerEntry.TABLE_TEMP_SONG_SINGER_NAME, null, null);
        db.close();
    }

    private ContentValues createContentValue(int idSong, int idSinger) {
        if (idSinger == -1 && idSong == -1) return null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SongSourceContract.SongEntry.COLUMN_ID_SONG, idSong);
        contentValues.put(SingerSourceContract.SingerEntry.COLUMN_ID_SINGER, idSinger);
        return contentValues;
    }
}
