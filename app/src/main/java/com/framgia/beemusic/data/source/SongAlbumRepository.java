package com.framgia.beemusic.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.framgia.beemusic.data.source.local.DataHelper;
import com.framgia.beemusic.data.source.local.album.AlbumSourceContract;
import com.framgia.beemusic.data.source.local.song.SongSourceContract;
import com.framgia.beemusic.data.source.local.songalbum.SongAlbumSourceContract;

/**
 * Created by beepi on 20/02/2017.
 */
public class SongAlbumRepository extends DataHelper implements DataSourceRelationship {
    private static SongAlbumRepository sSongAlbumRepository;

    private SongAlbumRepository(Context context) {
        super(context);
    }

    public static SongAlbumRepository getInstant(Context context) {
        if (sSongAlbumRepository == null) {
            sSongAlbumRepository = new SongAlbumRepository(context);
        }
        return sSongAlbumRepository;
    }

    @Override
    public Cursor getModel(String selection, String[] Args) {
        SQLiteDatabase db = getReadableDatabase();
        String sortOrder = SongSourceContract.SongEntry.COLUMN_ID_SONG + " ASC";
        return db.query(SongAlbumSourceContract.SongAlbumEntry.TABLE_TEMP_SONG_ALBUM_NAME, null,
            selection, Args,
            null, null, sortOrder);
    }

    /**
     * @param id1: id of song
     * @param id2: id of album
     * @return number of row inserted
     */
    @Override
    public int save(int id1, int id2) {
        int count = -1;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createContentValue(id1, id2);
        if (contentValues == null) return -1;
        count = (int) db
            .insert(SongAlbumSourceContract.SongAlbumEntry.TABLE_TEMP_SONG_ALBUM_NAME, null,
                contentValues);
        db.close();
        return count;
    }

    /**
     * @param id1: id of song
     * @param id2: id of album
     * @return number of row deleted
     */
    @Override
    public int delete(int id1, int id2) {
        int count = -1;
        String selection = SongSourceContract.SongEntry.COLUMN_ID_SONG
            + " = ? " + " and "
            + AlbumSourceContract.AlbumEntry.COLUMN_ID_ALBUM + " = ?";
        SQLiteDatabase db = getWritableDatabase();
        count = db.delete(SongAlbumSourceContract.SongAlbumEntry.TABLE_TEMP_SONG_ALBUM_NAME,
            selection, new String[]{String.valueOf(id1), String.valueOf(id2)});
        db.close();
        return count;
    }

    @Override
    public void deleteAlls() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SongAlbumSourceContract.SongAlbumEntry.TABLE_TEMP_SONG_ALBUM_NAME, null, null);
        db.close();
    }

    private ContentValues createContentValue(int idSong, int idAlbum) {
        if (idAlbum == -1 && idSong == -1) return null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SongSourceContract.SongEntry.COLUMN_ID_SONG, idSong);
        contentValues.put(AlbumSourceContract.AlbumEntry.COLUMN_ID_ALBUM, idAlbum);
        return contentValues;
    }
}
