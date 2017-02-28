package com.framgia.beemusic.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

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
    public Cursor getModel(String selection, String[] args) {
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
        } finally {
            closeDatabse();
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
    public int delete(int idSong, int idAlbum) {
        int count = -1;
        try {
            String selection = SongSourceContract.SongEntry.COLUMN_ID_SONG
                + " = ? " + " and "
                + AlbumSourceContract.AlbumEntry.COLUMN_ID_ALBUM + " = ?";
            openDatabase();
            count =
                mDatabase.delete(
                    SongAlbumSourceContract.SongAlbumEntry.TABLE_SONG_ALBUM_RELATIONSHIP_NAME,
                    selection, new String[]{String.valueOf(idSong), String.valueOf(idAlbum)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
        return count;
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
        contentValues.put(SongSourceContract.SongEntry.COLUMN_ID_SONG, idSong);
        contentValues.put(AlbumSourceContract.AlbumEntry.COLUMN_ID_ALBUM, idAlbum);
        return contentValues;
    }
}
