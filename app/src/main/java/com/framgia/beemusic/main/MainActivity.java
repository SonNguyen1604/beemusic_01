package com.framgia.beemusic.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.framgia.beemusic.R;
import com.framgia.beemusic.data.source.AlbumRepository;
import com.framgia.beemusic.data.source.SingerRepository;
import com.framgia.beemusic.data.source.SongAlbumRepository;
import com.framgia.beemusic.data.source.SongRepository;
import com.framgia.beemusic.data.source.SongSingerRepository;
import com.framgia.beemusic.data.source.SynchronizeRepository;
import com.framgia.beemusic.databinding.ActivityMainBinding;
import com.framgia.beemusic.service.ObservableService;
import com.framgia.beemusic.song.SongFragment;
import com.framgia.beemusic.song.SongPresenter;
import com.framgia.beemusic.util.ActivityUtils;

import rx.subscriptions.CompositeSubscription;

import static com.framgia.beemusic.util.Constant.CONTENT_EMAIL;
import static com.framgia.beemusic.util.Constant.GMAIL;
import static com.framgia.beemusic.util.Constant.SUBJECT_EMAIL;

public class MainActivity extends AppCompatActivity
    implements MainContract.View, NavigationView.OnNavigationItemSelectedListener,
    SearchView.OnQueryTextListener {
    private MainContract.Presenter mPresenter;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setMainActivity(this);
        mBinding.navigationView.setNavigationItemSelectedListener(this);
        initPresenter();
        checkAndRequestPermission();
    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showConfirmPermissionDialog();
            } else {
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_CODE);
            }
            return;
        }
        if (!ActivityUtils.isInstalled(this)) {
            mPresenter.subcribe();
        }
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
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE_CODE);
                }
            });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!ActivityUtils.isInstalled(this)) {
                    mPresenter.subcribe();
                }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_song:
                initSongFragment();
                break;
            case R.id.item_album:
                // todo open fragment album
                break;
            case R.id.item_singer:
                // todo open fragment singer
                break;
            case R.id.item_favorite:
                // todo open fragment favorite
                break;
            case R.id.item_feadback:
                sendFeadback();
                break;
            default:
                break;
        }
        mBinding.drawlayout.closeDrawers();
        return true;
    }

    private void sendFeadback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT_EMAIL);
        intent.putExtra(Intent.EXTRA_TEXT, CONTENT_EMAIL);
        intent.setData(Uri.parse(GMAIL));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void initSongFragment() {
        SongFragment songFragment =
            (SongFragment) getSupportFragmentManager().findFragmentById(R.id.linear_content);
        if (songFragment != null) return;
        songFragment = SongFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
            songFragment, R.id.linear_content);
        new SongPresenter(songFragment,SongRepository.getInstant(this),
            AlbumRepository.getInstant(this),SingerRepository.getInstant(this),
            SongAlbumRepository.getInstant(this),SongSingerRepository.getInstant(this));
    }

    @Override
    public void startObserverService() {
        startService(new Intent(this, ObservableService.class));
    }
}
