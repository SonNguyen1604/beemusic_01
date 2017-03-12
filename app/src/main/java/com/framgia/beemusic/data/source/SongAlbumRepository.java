package com.framgia.beemusic.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.framgia.beemusic.data.source.local.DataHelper;
import com.framgia.beemusic.data.source.local.song.SongSourceContract;
import com.framgia.beemusic.data.source.local.songalbum.SongAlbumSourceContract;

import java.util.ArrayList;
import java.util.List;

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
    public List<Integer> getListId(int idSong) {
        String selection = SongAlbumSourceContract.SongAlbumEntry.COLUMN_ID_SONG + " = ?";
        List<Integer> idAlbums = new ArrayList<>();
        int idAlbum;
        Cursor cursor = getCursor(selection,
            new String[]{String.valueOf(idSong)});
        if (cursor == null || cursor.getCount() == 0) return null;
        while (cursor.moveToNext()) {
            idAlbum = cursor.getInt(cursor.getColumnIndex(
                SongAlbumSourceContract.SongAlbumEntry.COLUMN_ID_ALBUM));
            idAlbums.add(idAlbum);
        }
        closeCursor(cursor);
        closeDatabse();
        return idAlbums;
    }

    @Override
    public Cursor getCursor(String selection, String[] args) {
        Cursor cursor = null;
        try {
            openDatabase();
            String sortOrder = SongSourceContract.SongEntry.COLUMN_ID_SONG + " ASC";
            cursor = mDatabase.query(SongAlbumSourceContract.SongAlbumEntry
                    .TABLE_SONG_ALBUM_RELATIONSHIP_NAME, null,
                selection, args,
                null, null, sortOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    @Override
    public int save(int idSong, int idAlbum) {
        int count = -1;
        try {
            openDatabase();
            ContentValues contentValues = createContentValue(idSong, idAlbum);
            if (contentValues == null) return -1;
            count = (int) mDatabase
                .insert(SongAlbumSourceContract.SongAlbumEntry.TABLE_SONG_ALBUM_RELATIONSHIP_NAME,
                    null,
                    contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
        return count;
    }

    @Override
    public int delete(String selection, String[] args) {
        int count = -1;
        try {
            openDatabase();
            count =
                mDatabase.delete(
                    SongAlbumSourceContract.SongAlbumEntry.TABLE_SONG_ALBUM_RELATIONSHIP_NAME,
                    selection, args);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
        return count;
    }

    @Override
    public int delete(int idSong) {
        String selection = SongAlbumSourceContract.SongAlbumEntry.COLUMN_ID_SONG + " = ?";
        return delete(selection, new String[]{String.valueOf(idSong)});
    }

    @Override
    public void deleteAlls() {
        try {
            openDatabase();
            mDatabase
                .delete(SongAlbumSourceContract.SongAlbumEntry.TABLE_SONG_ALBUM_RELATIONSHIP_NAME,
                    null,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
    }

    private ContentValues createContentValue(int idSong, int idAlbum) {
        if (idAlbum == -1 && idSong == -1) return null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SongAlbumSourceContract.SongAlbumEntry.COLUMN_ID_SONG, idSong);
        contentValues.put(SongAlbumSourceContract.SongAlbumEntry.COLUMN_ID_ALBUM, idAlbum);
        return contentValues;
    }
}
