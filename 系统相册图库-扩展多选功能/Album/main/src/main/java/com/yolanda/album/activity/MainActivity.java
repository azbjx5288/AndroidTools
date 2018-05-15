package com.yolanda.album.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.yolanda.album.R;
import com.yolanda.album.adapter.MainBannerAdapter;
import com.yolanda.album.view.AutoPlayViewPager;

import java.util.List;

public class MainActivity extends BasicActivity implements View.OnClickListener {
    /**
     * 选择照片
     */
    private static final int REQUEST_CODE_SELECT_PHOTO = 0x01;

    /**
     * ViewPager
     */
    private AutoPlayViewPager autoPlayViewPager;
    /**
     * ViewPager适配器
     */
    private MainBannerAdapter mainBannerAdapter;

    private List<String> mImagePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("主页");
        setContentView(R.layout.activity_main);
        autoPlayViewPager = (AutoPlayViewPager) findViewById(R.id.vp_main);
        mainBannerAdapter = new MainBannerAdapter(this);
        autoPlayViewPager.setAdapter(mainBannerAdapter);

        findViewById(R.id.btn_start).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start) {
            if (mImagePaths != null && mImagePaths.size() > 0) {
                autoPlayViewPager.start();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_PHOTO && resultCode == RESULT_OK) {
            // 处理图片
            handlePhotos(data);
        }
    }

    /**
     * 处理用户选择的照片
     *
     * @param intent
     */
    private void handlePhotos(Intent intent) {
        mImagePaths = intent.getStringArrayListExtra(AlbumActivity.KEY_OUTPUT_STRING);
        mainBannerAdapter.notifyDataSetChanged(mImagePaths);
    }

    @Override
    public boolean onOptionsItemSelectedCompat(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.main_select_photo) {// 选择图片
            Intent intent = new Intent(this, AlbumActivity.class);
            intent.putExtra(AlbumActivity.KEY_INPUT_COUNT, 3);
            startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTO);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
