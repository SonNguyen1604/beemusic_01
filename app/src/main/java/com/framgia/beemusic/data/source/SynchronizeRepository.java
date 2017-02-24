package com.framgia.beemusic.data.source;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.framgia.beemusic.Application;
import com.framgia.beemusic.data.model.Album;
import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.data.model.Song;
import com.framgia.beemusic.data.source.local.DataHelper;

import java.util.concurrent.Callable;

import rx.Observable;

import static com.framgia.beemusic.data.model.Song.IS_NON_FAVORITE;
import static com.framgia.beemusic.data.model.Song.TYPE_MEDIA;

/**
 * Created by beepi on 21/02/2017.
 */
public class SynchronizeRepository extends DataHelper implements SynchronizeContract {
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
        super(Application.getContext());
        mContentResolver =
            Application.getContext().getContentResolver();
        mSongHandler = SongRepository.getInstant(Application.getContext());
        mAlbumHandler = AlbumRepository.getInstant(Application.getContext());
        mSingerHandler = SingerRepository.getInstant(Application.getContext());
        mSongAlbumHandler = SongAlbumRepository.getInstant(Application.getContext());
        mSongSingerHandler = SongSingerRepository.getInstant(Application.getContext());
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
        Song song = new Song(idSong, nameSong, linkSong, IS_NON_FAVORITE, TYPE_MEDIA,
            genre, duration);
        return song;
    }

    private Album getAlbum(Cursor cursor) {
        String nameALbum = cursor.getString(cursor.getColumnIndexOrThrow(
            MediaStore.Audio.Media.ALBUM));
        int idAlbum = cursor.getInt(cursor.getInt(cursor.getColumnIndexOrThrow(
            MediaStore.Audio.Media.ALBUM_ID)));
        Album album = new Album(idAlbum, nameALbum, null, 0);
        return album;
    }

    private Singer getSinger(Cursor cursor) {
        String nameSinger =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media
                .ARTIST));
        int idSinger = cursor.getInt(cursor.getInt(cursor.getColumnIndexOrThrow(
            MediaStore.Audio.Media.ARTIST_ID)));
        Singer singer = new Singer(idSinger, nameSinger, 0);
        singer.setId(idSinger);
        return singer;
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
}

