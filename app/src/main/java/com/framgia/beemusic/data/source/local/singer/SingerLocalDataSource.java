package com.framgia.beemusic.data.source.local.singer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.framgia.beemusic.data.model.Singer;
import com.framgia.beemusic.data.source.DataSource;
import com.framgia.beemusic.data.source.local.DataHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by beepi on 19/02/2017.
 */
public class SingerLocalDataSource extends DataHelper implements DataSource<Singer> {
    private static SingerLocalDataSource sSingerLocalHandler;

    private SingerLocalDataSource(Context context) {
        super(context);
    }

    public static SingerLocalDataSource getInstant(Context context) {
        if (sSingerLocalHandler == null) {
            sSingerLocalHandler = new SingerLocalDataSource(context);
        }
        return sSingerLocalHandler;
    }

    @Override
    public List<Singer> getModel(String selection, String[] args) {
        List<Singer> singers = null;
        try {
            openDatabase();
            singers = null;
            String sortOrder = SingerSourceContract.SingerEntry.COLUMN_NAME + " ASC";
            Cursor cursor =
                mDatabase
                    .query(SingerSourceContract.SingerEntry.TABLE_SINGER_NAME, null, selection,
                        args,
                        null, null, sortOrder);
            if (cursor != null && cursor.moveToFirst()) {
                singers = new ArrayList<>();
                do {
                    singers.add(new Singer(cursor));
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
        return singers;
    }

    @Override
    public int save(Singer model) {
        int count = -1;
        try {
            openDatabase();
            ContentValues contentValues = convertFromSinger(model);
            if (contentValues == null) return -1;
            count = (int) mDatabase
                .insert(SingerSourceContract.SingerEntry.TABLE_SINGER_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
        return count;
    }

    @Override
    public int update(Singer model) {
        int count = -1;
        try {
            ContentValues contentValues = convertFromSinger(model);
            if (contentValues == null) return count;
            openDatabase();
            count =
                mDatabase.update(SingerSourceContract.SingerEntry.TABLE_SINGER_NAME, contentValues,
                    SingerSourceContract.SingerEntry.COLUMN_ID_SINGER + " = ?",
                    new String[]{String.valueOf(model.getId())});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
        return count;
    }

    @Override
    public int delete(int id) {
        int count = -1;
        try {
            openDatabase();
            count = mDatabase.delete(SingerSourceContract.SingerEntry.TABLE_SINGER_NAME,
                SingerSourceContract.SingerEntry.COLUMN_ID_SINGER + " = ?",
                new String[]{String.valueOf(id)});
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
            mDatabase.delete(SingerSourceContract.SingerEntry.TABLE_SINGER_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDatabse();
        }
    }

    @Override
    public boolean checkExistModel(int id) {
        if (id == -1) return false;
        String selection = SingerSourceContract.SingerEntry.COLUMN_ID_SINGER + " = ?";
        List<Singer> singers = getModel(selection, new String[]{String.valueOf(id)});
        return singers != null;
    }

    @Override
    public Observable<Singer> getDataObservable(final List<Singer> mediaCursor) {
        return Observable.defer(new Func0<Observable<Singer>>() {
            @Override
            public Observable<Singer> call() {
                return Observable.from(mediaCursor);
            }
        });
    }

    private ContentValues convertFromSinger(Singer singer) {
        if (singer == null) return null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SingerSourceContract.SingerEntry.COLUMN_NAME, singer.getName());
        if (singer.getId() > -1) {
            contentValues.put(SingerSourceContract.SingerEntry.COLUMN_ID_SINGER, singer.getId());
        }
        return contentValues;
    }
}
