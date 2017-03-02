package com.framgia.beemusic.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.framgia.beemusic.R;
import com.framgia.beemusic.data.source.AlbumRepository;
import com.framgia.beemusic.data.source.SingerRepository;
import com.framgia.beemusic.data.source.SongRepository;
import com.framgia.beemusic.data.source.SynchronizeRepository;

import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainContract.Presenter mPresenter;
    private static final int READ_EXTERNAL_STORAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPresenter();
        checkAndRequestPermission();
    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showConfirmPermissionDialog();
            } else {
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_CODE);
            }
        } else mPresenter.subcribe();
    }

    private void showConfirmPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
            .setCancelable(true)
            .setTitle(R.string.title_permission)
            .setMessage(R.string.msg_external_storage_permision);
        builder.setNegativeButton(android.R.string.yes,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    showNotLoadOnMediastore();
                }
            });
        builder.setPositiveButton(android.R.string.no,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE_CODE);
                }
            });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
            if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.subcribe();
            }
        }
    }

    private void initPresenter() {
        mPresenter = new MainPresenter(this,
            new CompositeSubscription(),
            SongRepository.getInstant(this),
            AlbumRepository.getInstant(this),
            SingerRepository.getInstant(this),
            SynchronizeRepository.getInstant());
    }

    private void showNotLoadOnMediastore() {
        Toast.makeText(this, R.string.msg_not_load_mediastore, Toast.LENGTH_SHORT)
            .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubcribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubcribe();
    }
}
