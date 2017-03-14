package com.framgia.beemusic.data.source;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.framgia.beemusic.BeeApplication;
import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.local.song.SongSourceContract;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;

import static com.framgia.beemusic.data.model.Song.IS_NON_FAVORITE;
import static com.framgia.beemusic.data.model.Song.TYPE_MEDIA;

/**
 * Created by beepi on 21/02/2017.
 */
public class SynchronizeRepository implements SynchronizeContract {
    private static SynchronizeRepository sSynchronizeRepository;
    private static String[] sGenresProjection = {
        MediaStore.Audio.Genres.NAME,
        MediaStore.Audio.Genres._ID
    };
    private ContentResolver mContentResolver;
    private SongRepository mSongHandler;
    private AlbumRepository mAlbumHandler;
    private SingerRepository mSingerHandler;
    private SongAlbumRepository mSongAlbumHandler;
    private SongSingerRepository mSongSingerHandler;

    private SynchronizeRepository() {
        mContentResolver =
            BeeApplication.getInstant().getContentResolver();
        mSongHandler = SongRepository.getInstant(BeeApplication.getInstant());
        mAlbumHandler = AlbumRepository.getInstant(BeeApplication.getInstant());
        mSingerHandler = SingerRepository.getInstant(BeeApplication.getInstant());
        mSongAlbumHandler = SongAlbumRepository.getInstant(BeeApplication.getInstant());
        mSongSingerHandler = SongSingerRepository.getInstant(BeeApplication.getInstant());
    }

    public static SynchronizeRepository getInstant() {
        if (sSynchronizeRepository == null) {
            sSynchronizeRepository = new SynchronizeRepository();
        }
        return sSynchronizeRepository;
    }

    @Override
    public Cursor getCursorFromMediastore() {
        String sortOrder = MediaStore.Audio.Media._ID;
        Cursor cursor = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
            MediaStore.Audio.Media.IS_MUSIC + " = ?", new String[]{"1"}, sortOrder);
        return cursor;
    }

    @Override
    public Observable<Cursor> getCursorObservable(final Cursor cursor) {
        if (cursor == null) return null;
        return Observable.fromCallable(new Callable<Cursor>() {
            @Override
            public Cursor call() throws Exception {
                return cursor;
            }
        });
    }

    private String getGenreFromMediaStore(int idSong) {
        if (idSong < 0) return null;
        String genre = null;
        Uri uri = MediaStore.Audio.Genres.getContentUriForAudioId("external", idSong);
        Cursor genreCursor = mContentResolver.query(uri, sGenresProjection, null, null, null);
        if (genreCursor == null || genreCursor.getCount() == 0) return null;
        while (genreCursor.moveToNext()) {
            genre = genreCursor.getString(genreCursor.getColumnIndexOrThrow(MediaStore.Audio
                .Genres.NAME));
        }
        return genre;
    }

    private Song getSong(Cursor cursor) {
        String nameSong, linkSong, genre;
        int idSong, duration = 0;
        idSong =
            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
        nameSong = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media
            .TITLE));
        duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media
            .DURATION));
        linkSong = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media
            .DATA));
        genre = getGenreFromMediaStore(idSong);
        return new Song(idSong, nameSong, linkSong, IS_NON_FAVORITE, TYPE_MEDIA,
            genre, duration);
    }

    private Album getAlbum(Cursor cursor) {
        String nameALbum = cursor.getString(cursor.getColumnIndexOrThrow(
            MediaStore.Audio.Media.ALBUM));
        int idAlbum = cursor.getInt(cursor.getInt(cursor.getColumnIndexOrThrow(
            MediaStore.Audio.Media.ALBUM_ID)));
        return new Album(idAlbum, nameALbum, null, 1);
    }

    private Singer getSinger(Cursor cursor) {
        String nameSinger =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media
                .ARTIST));
        return new Singer(nameSinger, 1);
    }

    @Override
    public void synchronizeByAddModel(Cursor cursor) {
        int idSong, idAlbum, idSinger;
        if (cursor == null || cursor.getCount() == 0) return;
        while (cursor.moveToNext()) {
            idSong = mSongHandler.save(getSong(cursor));
            idAlbum = mAlbumHandler.save(getAlbum(cursor));
            idSinger = mSingerHandler.save(getSinger(cursor));
            mSongAlbumHandler.save(idSong, idAlbum);
            mSongSingerHandler.save(idSong, idSinger);
        }
        cursor.close();
    }

    @Override
    public void synchronizeByDelModel(Cursor delCursor) {
        int idSong;
        if (delCursor == null || delCursor.getCount() == 0) return;
        List<Song> delSongs = mSongHandler.getModel(delCursor);
        if (delSongs == null) return;
        for (Song song : delSongs) {
            idSong = song.getId();
            mSongHandler.delete(idSong);
            mAlbumHandler.updateCountForDelSong(mSongAlbumHandler.getListId(idSong));
            mSingerHandler.updateCountByDelSong(mSongSingerHandler.getListId(idSong));
            mSongAlbumHandler.delete(idSong);
            mSongSingerHandler.delete(idSong);
        }
        delCursor.close();
    }

    @Override
    public Cursor getDelCursor(Cursor currentMediaCursor) {
        String allSong = new String();
        if (currentMediaCursor == null || currentMediaCursor.getCount() == 0) return null;
        while (currentMediaCursor.moveToNext()) {
            allSong += currentMediaCursor
                .getString(currentMediaCursor.getColumnIndex(MediaStore.Audio.Media._ID)) + ",";
        }
        allSong = allSong.substring(0, allSong.length() - 1);
        currentMediaCursor.close();
        String selection = SongSourceContract.SongEntry.COLUMN_ID_SONG + " NOT IN ("
            + allSong + ") and " + SongSourceContract.SongEntry.COLUMN_TYPE + " = ?";
        return mSongHandler.getCursor(selection, new String[]{String.valueOf
            (TYPE_MEDIA)});
    }

    @Override
    public Cursor getAddCursor(Cursor currentMediaCurrsor) {
        String allSong = new String();
        String sortOrder = MediaStore.Audio.Media._ID;
        String selectionApp = SongSourceContract.SongEntry.COLUMN_TYPE + " = ?";
        Cursor appCursor =
            mSongHandler.getCursor(selectionApp, new String[]{String.valueOf(TYPE_MEDIA)});
        if (appCursor == null || appCursor.getCount() == 0) return null;
        while (appCursor.moveToNext()) {
            allSong += appCursor
                .getString(appCursor.getColumnIndex(SongSourceContract.SongEntry.COLUMN_ID_SONG)) +
                ",";
        }
        allSong = allSong.substring(0, allSong.length() - 1);
        appCursor.close();
        String selection = MediaStore.Audio.Media._ID + " NOT IN ("
            + allSong + ") and " + MediaStore.Audio.Media.IS_MUSIC + " = ?";
        return mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, selection,
            new String[]{String.valueOf(1)}, sortOrder);
    }
}

